package cn.ejfy.web.tools.excel.poi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

/**
 * excel  导出数据 工具类（导出xls格式）
 * @author wyj
 */
public class PoiHSSFExportTool {
    /**
     * 根据数组填充整行
     * @param sheet
     * @param style   行样式
     * @param rowColumns   行数组数据
     * @param rowNum    起始行,从0开始计算
     */
    public static int fillSingleRow(HSSFSheet sheet, HSSFCellStyle style, String[] rowColumns, Integer rowNum) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < rowColumns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(rowColumns[i]);
            cell.setCellStyle(style);
        }
        rowNum++;
        return rowNum;
    }
    
    
    /**根据对象,单元格号 填充行 -- 应用于循环中填充一部分数据
     * @param obj   类
     * @param fieldColumns  填充的字段,对应类的属性数组
     * @param style    单元格样式
     * @param row      传入Row
     * @param chellstart  开始填充单元格号，从0开始计算
     */
    @SuppressWarnings({ "rawtypes" })
    public static void fillCell(Object obj, String[] fieldColumns,CellStyle style, Row row  ,int chellstart) {
        //日期格式
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        
        Map map = null;
        try {
            map = BeanUtils.describe(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        row.setHeightInPoints(15);
        int chellcount=chellstart;
        for (int i = 0; i < fieldColumns.length; i++) {
            String fieldName = fieldColumns[i];
            Object value = map.get(fieldName);
            String cellValue = "";
            if (value instanceof Date) {
                Date date = (Date) value;
                cellValue = sd.format(date);
            } else {
                cellValue = null != value ? value.toString() : "";
            }
            Cell cell = row.createCell(chellcount);
            cell.setCellValue(cellValue);
            cell.setCellStyle(style);
            chellcount++;
        }
    }
    
}
