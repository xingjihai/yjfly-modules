package cn.ejfy.web.tools.excel.xmlpoi;

import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象Excel2003读取器，通过实现HSSFListener监听器，采用事件驱动模式解析excel2003
 * 中的内容，遇到特定事件才会触发，大大减少了内存的使用。
 */
public class Excel2003Reader implements HSSFListener {
    private POIFSFileSystem fs;

    /** Should we output the formula, or the value it has? */
    private boolean outputFormulaValues = true;

    /** For parsing Formulas */
    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;
    //excel2003工作薄
    private HSSFWorkbook stubWorkbook;

    // Records we pick up as we process
    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;

    private BoundSheetRecord[] orderedBSRs;
    private ArrayList boundSheetRecords = new ArrayList();

    // For handling formulas with string results
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;

    private String lastContents;//上一次的内容
    private int sheetIndex = -1;//当前sheet(从0开始)
    private String sheetName;
    private int curRow = 0;//当前行
    private int curCol = 0;//当前列
    private Map<Integer, List<List<String>>> sheetDatas = new HashMap<>();//sheet解析后的数据

    public Map<Integer, List<String[]>> getSheetDatas() {
        Map<Integer, List<String[]>> sheetDataArrays = new HashMap<>();
        List<List<String>> rowDatas = null;
        List<String[]> rowDataArrays = null;
        String[] rowDataArray = null;
        for(Integer key : sheetDatas.keySet()) {
            rowDatas = sheetDatas.get(key);
            rowDataArrays = new ArrayList<>();
            sheetDataArrays.put(key, rowDataArrays);
            for(List<String> rowData : rowDatas) {
                rowDataArray  = new String[rowData.size()];
                rowData.toArray(rowDataArray);
                rowDataArrays.add(rowDataArray);
            }
        }
        return sheetDataArrays;
    }

    /**
     * 遍历excel下所有的sheet
     * @throws IOException
     */
    public Map<Integer, List<List<String>>> readAllSheets(String fileName) throws IOException {
        this.fs = new POIFSFileSystem(new FileInputStream(fileName));
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
                this);
        formatListener = new FormatTrackingHSSFListener(listener);
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(
                    formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }
        factory.processWorkbookEvents(request, fs);
        return sheetDatas;
    }

    /**
     * HSSFListener 监听方法，处理 Record
     */
    @Override
    public void processRecord(Record record) {
        switch (record.getSid()) {
            case BoundSheetRecord.sid: //BoundSheetRecord记录了sheetName
                boundSheetRecords.add(record);
                break;
            case BOFRecord.sid://记录了Workbook或一个sheet的开始
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    // 如果有需要，则建立子工作薄
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }

                    curRow = 0;
                    sheetIndex++;
                    sheetDatas.put(sheetIndex, new ArrayList<List<String>>());

                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    sheetName = orderedBSRs[sheetIndex].getSheetname();
                }
                break;
            case DimensionsRecord.sid://记录了每个Sheet的有效起始结束行列索引
                DimensionsRecord dr = (DimensionsRecord)record;
                break;
            case SSTRecord.sid: //记录了所有Sheet的文本单元格的文本
                sstRecord = (SSTRecord) record;
                break;
            case RowRecord.sid://记录了Sheet中行信息，如行索引，行是否隐藏
                RowRecord rr = (RowRecord)record;
                break;
            case BlankRecord.sid://Sheet中空单元格，存在单元格样式
                BlankRecord brec = (BlankRecord) record;
//                curRow = brec.getRow();
//                curCol = brec.getColumn();
//                lastContents = "";
//                setRowData();
                break;
            case BoolErrRecord.sid: //单元格为布尔类型或错误
                BoolErrRecord berec = (BoolErrRecord) record;
                curRow = berec.getRow();
                curCol = berec.getColumn();
                lastContents = berec.getBooleanValue()+"";
                setRowData();
                break;
            case FormulaRecord.sid: //单元格为公式类型
                FormulaRecord frec = (FormulaRecord) record;
                curRow = frec.getRow();
                curCol = frec.getColumn();
                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        // Formula result is a string
                        // This is stored in the next record
                        outputNextStringRecord = true;
                        nextRow = frec.getRow();
                        nextColumn = frec.getColumn();
                    } else {
                        lastContents = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    lastContents = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
                }
                setRowData();
                break;
            case StringRecord.sid://单元格中公式的字符串
                if (outputNextStringRecord) {
                    // String for formula
                    StringRecord srec = (StringRecord) record;
                    curRow = nextRow;
                    curCol = nextColumn;
                    lastContents = srec.getString();
                    outputNextStringRecord = false;
                }
                break;
            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;
                curRow = lrec.getRow();
                curCol = lrec.getColumn();
                lastContents = lrec.getValue().trim();
                setRowData();
                break;
            case LabelSSTRecord.sid:  //单元格为字符串类型
                LabelSSTRecord lsrec = (LabelSSTRecord) record;
                curRow = lsrec.getRow();
                curCol = lsrec.getColumn();
                lastContents = sstRecord == null?"":sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                setRowData();
                break;
            case NumberRecord.sid:  //单元格为数字类型
                NumberRecord numrec = (NumberRecord) record;
                curRow = numrec.getRow();
                curCol = numrec.getColumn();
                lastContents = formatListener.formatNumberDateCell(numrec).trim();
                setRowData();
                break;
            default:
                break;
        }

        // 空值的操作
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
//            curRow = mc.getRow();
//            curCol = mc.getColumn();
//            lastContents = " ";
//            setRowData();
        }
    }

    /**
     *
     */
    private void setRowData() {
        List<String> rowData = handleCurRowData();
        handleCurColData(rowData);
    }

    /**
     *
     * @param rowData
     */
    private void handleCurColData(List<String> rowData) {
        if(rowData.size() <= curCol) {
            for(int idx = rowData.size(); idx < curCol; idx ++) {
                rowData.add(idx, null);
            }
        }

        rowData.add(curCol, lastContents);
    }

    /**
     *
     * @return
     */
    private List<String> handleCurRowData() {
        List<List<String>> rowDatas = sheetDatas.get(sheetIndex);

        //数据读取结束后，保存单元格数据
        List<String> rowData = null;
        if(rowDatas.size() <= curRow) {
            for(int idx = rowDatas.size(); idx <= curRow; idx ++) {
                rowData = new ArrayList<>();
                rowDatas.add(idx, rowData);
            }
        }
        return rowDatas.get(curRow);
    }
}
