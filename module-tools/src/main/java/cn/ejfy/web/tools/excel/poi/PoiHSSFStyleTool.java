package cn.ejfy.web.tools.excel.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * HSSF 支持输出
 * @author wyj
 */
public class PoiHSSFStyleTool {
    /**
     * 设置单元格格式
     * @param workbook
     * @param isHeader 是否头部标题
     * @param fontSize 字号
     * @return
     */
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook, boolean isHeader, Short fontSize) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setLocked(true);
        if (isHeader) {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeightInPoints(fontSize == null ? (short) 12 : fontSize);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }
        return style;
    }
    
    /**
     * 设置单元格格式，控制是否加粗
     * @param workbook
     * @param bold
     * @return
     */
    public static HSSFCellStyle getBoldStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setLocked(true);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }
    
    
    /**
     * 常用样式
     * @param isBorder  是否有边框
     * @param isBold  是否加粗
     * @param isCenter  是否居中
     * @param isWrap  是否自动换行
     * @return
     */
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook,boolean isBorder,boolean isBold,boolean isCenter,boolean isWrap){
        HSSFCellStyle style = workbook.createCellStyle();
        
        if(isBold){
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }
        
        if(isCenter){
            // 设置这些样式
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        }

        if(isBorder){
            // 设置边框
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        }
        if(isWrap){
            // 自动换行  
            style.setWrapText(true);  
        }
        return style;
    }
    public static void main(String[] args) {
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

         // 背景色
        style.setFillForegroundColor(HSSFColor.YELLOW.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        style.setFillBackgroundColor(HSSFColor.YELLOW.index); 

        // 设置边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        // 自动换行  
        style.setWrapText(true);  

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(HSSFColor.RED.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");

        // 把字体 应用到当前样式
        style.setFont(font);

    }
    
}
