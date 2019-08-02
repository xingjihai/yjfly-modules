package cn.ejfy.web.tools.excel.xmlpoi;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.*;

/**
 * 抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 * xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低
 * 内存的耗费，特别使用于大数据量的文件。
 */
public class Excel2007Reader extends DefaultHandler {
    private SharedStringsTable sst;//共享字符串表
    private String lastContents;//上一次的内容
    private boolean nextIsString;

    private int sheetIndex = -1;//当前sheet(从0开始)
    private int curRow = 0;//当前行
    private int curCol = -1;//当前列
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
     * 读取2007版及以上excel(xlsx)内容
     * @param filename
     * @param sheetId
     * @throws Exception
     */
    public List<List<String>> readOneSheet(String filename, int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        // 根据 rId# 或 rSheet# 查找sheet
        ArrayList<List<String>> rowDatas = new ArrayList<>();
        sheetIndex = sheetId--;
        sheetDatas.put(sheetIndex, rowDatas);

        InputStream sheet = r.getSheet("rId"+sheetId);
        InputSource sheetSource = new InputSource(sheet);
        parser.parse(sheetSource);
        sheet.close();
        return rowDatas;
    }

    /**
     * 读取2007版及以上excel(xlsx)内容
     * @param filename
     * @return
     * @throws Exception
     */
    public Map<Integer, List<List<String>>> readAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        Iterator<InputStream> sheets = r.getSheetsData();
        while(sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            sheetDatas.put(sheetIndex, new ArrayList<List<String>>());

            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
        return sheetDatas;
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst)
            throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    /**
     * 解析一个element的开始时触发事件
     */
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => cell 代表单元格
        if(name.equals("c")) {
            // 获取单元格的位置，如A1,B1
            System.out.print(attributes.getValue("r") + " - ");
//            curCol=Integer.parseInt(attributes.getValue("r"));
            // Figure out if the value is an index in the SST 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            // 单元格类型
            String cellType = attributes.getValue("t");
            //cellType值 s:字符串 b:布尔 e:错误处理
            if(cellType != null && cellType.equals("s")) {
                //标识为true 交给后续endElement处理
                nextIsString = true;
            } else {
                nextIsString = false;
            }

            curCol++;
        }
        // Clear contents cache
        lastContents = "";
//        rowDatas.clear();
    }

    /**
     * 解析一个element元素结束时触发事件
     */
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        // Process the last contents as required.
        // Do now, as characters() may be called more than once
        if(nextIsString) {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            nextIsString = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if(name.equals("v")) {
            List<String> rowData = handleCurRowData();
            handleCurColData(rowData);
        }else{
            //如果标签名称为 row , 已到行尾
            if(name.equals("row")){
                handleTailColData();
                this.curRow++;
                this.curCol = -1;
            }
        }
    }

    /**
     * 得到单元格对应的索引值或是内容值
     * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
     * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        lastContents += new String(ch, start, length);
    }

    private void handleTailColData() {
        //默认长度为标题行的长度，允许超出，不足补空值
        if(curRow == 0) {
            return;
        }

        List<List<String>> rowDatas = sheetDatas.get(sheetIndex);
        if(rowDatas.size() <= curRow) {
            return;
        }
        List<String> firstRowData = rowDatas.get(0);
        List<String> rowData = rowDatas.get(curRow);
        for(int idx = rowData.size(); idx < firstRowData.size(); idx ++) {
            rowData.add(idx, null);
        }
    }

    private void handleCurColData(List<String> rowData) {
        if(rowData.size() <= curCol) {
            for(int idx = rowData.size(); idx < curCol; idx ++) {
                rowData.add(idx, null);
            }
        }

        rowData.add(curCol, lastContents.trim());
    }

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
