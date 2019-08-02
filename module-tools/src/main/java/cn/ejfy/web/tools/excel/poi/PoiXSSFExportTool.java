package cn.ejfy.web.tools.excel.poi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel  导出数据 工具类(导出xlsx格式)
 * @author wyj
 */
public class PoiXSSFExportTool {
    
    //导出无格式的 xlsx
    private static void writeXlsx(OutputStream outputStream,Map<String,List<String[]>> map) {
        XSSFWorkbook wb = new XSSFWorkbook();
        for (String key : map.keySet()) {
            XSSFSheet sheet = wb.createSheet(key);
            List<String[]> list = map.get(key);
            for(int i=0;i<list.size();i++){
                XSSFRow row = sheet.createRow(i);
                String[] str = list.get(i);
                for(int j=0;j<str.length;j++){
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(str[j]);
                }
            }
        }
        try {
            wb.write(outputStream);
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * web项目--导出excel
     * @param fileName   文件名
     * @param response  
     * @param map  导入的数据： 标签map-->行list-->列String[]的形式
     */
    public static void  exportExcel(String fileName,HttpServletResponse response,Map<String,List<String[]>> map){
        try {
            fileName=java.net.URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        response.reset();          
        response.setContentType("application/vnd.ms-excel");        //改成输出excel文件
        response.setHeader("Content-disposition","attachment; filename="+fileName+".xlsx" );
        try {
            OutputStream outputStream=response.getOutputStream();
            writeXlsx(outputStream, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        //构造数据
        Map<String,List<String[]>> excelMap =new HashMap<>();  //Workbook
        List<String[]> sheetList=new ArrayList<>();  //Sheet
        
        for (int row_id = 0; row_id < 10; row_id++) {  
            String[] row=new String[10];  //Row
            for (int chell_id = 0; chell_id < 10; chell_id++) {
                row[chell_id]="chell"+chell_id;
            }
            sheetList.add(row);
        }
        excelMap.put("第一个sheet", sheetList);
        
        /** 1、web项目中 调用 exportExcel导出无格式xlsx文件 **/
//        exportExcel(fileName, response, excelMap);
        File file=new File("D:\\test\\PoiExcelImportTool.xlsx");
        try {
            writeXlsx(new FileOutputStream(file), excelMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
