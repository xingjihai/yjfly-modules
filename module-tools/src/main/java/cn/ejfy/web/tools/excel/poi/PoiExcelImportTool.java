package cn.ejfy.web.tools.excel.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel  导入数据 工具类（支持xlsx或xls）
 * @author wyj
 */
public class PoiExcelImportTool {
    /**
     * 读取excel（xlsx或xls），返回Map
     */
    public static Map<Integer, List<String[]>> readXlsxByPath(String fileName) throws Exception{
        try {
            InputStream is = new FileInputStream(fileName);
            return readXlsxByInputStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**读取excel（xlsx或xls）,返回Map
     */
    public static Map<Integer, List<String[]>> readXlsxByInputStream(InputStream is) throws Exception {
         Map<Integer, List<String[]>> map = new HashMap<Integer, List<String[]>>();
        try {
            Workbook workbook = create(is);
            // 循环工作表Sheet  
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                List<String[]> list = new ArrayList<String[]>();
                
                for (int row_i=0;row_i<=sheet.getLastRowNum();row_i++){
                    Row row = sheet.getRow(row_i);
                    if (row == null) {
                        continue;
                    }
                    
                    String[] singleRow = new String[row.getLastCellNum()];
                    for(int column=0;column<row.getLastCellNum();column++){
                        Cell cell = row.getCell(column,Row.CREATE_NULL_AS_BLANK);
                        switch(cell.getCellType()){
                            case Cell.CELL_TYPE_BLANK:
                                singleRow[column] = "";
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                singleRow[column] = Boolean.toString(cell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                singleRow[column] = "";
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                singleRow[column] = cell.getStringCellValue();
                                if (singleRow[column] != null) {
                                    singleRow[column] = singleRow[column].replaceAll("#N/A", "").trim();
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    singleRow[column] = String.valueOf(cell.getDateCellValue());
                                } else {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    String temp = cell.getStringCellValue();
                                    // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                                    if (temp.indexOf(".") > -1) {
                                        singleRow[column] = String.valueOf(new Double(temp)).trim();
                                    } else {
                                        singleRow[column] = temp.trim();
                                    }
                                }
                                
                                break;
                            case Cell.CELL_TYPE_STRING:
                                singleRow[column] = cell.getStringCellValue().trim();
                                break;
                            default:
                                singleRow[column] = "";
                                break;
                        }
                    }
                    // ===== 跳过空行
                    boolean is_empty=true;
                    for (String str : singleRow) {
                        if(!"".equals(str) ){
                            is_empty=false;
                        }
                    }
                    if(is_empty){
                        continue;
                    }
                    // ===== 跳过空行
                    list.add(singleRow);
                }
                map.put(numSheet, list);
            }
        } catch (Exception e) {
            throw new Exception(e.getCause().getMessage());
        } 
        return map;
    }
    
    /**
     * 根据输入流判断excel格式，创建不同的Workbook实现
     */
    private static Workbook create(InputStream in) throws IOException,InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);            //HSSFWorkbook支持"xls"
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));  //XSSFWorkbook支持"xlsx"
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }
    
    
    
    
    public static void main(String[] args) throws Exception{
        File file=new File("C:\\Users\\Administrator\\Desktop\\easyUI\\courtinfo.xlsx");
        /**1、通过文件位置读取*/
        readXlsxByPath(file.getPath());
        /**2、通过流读取*/
        readXlsxByInputStream(new FileInputStream(file));
        
        
        
        //读取xlsx
        Map<Integer, List<String[]>> map = readXlsxByPath("C:\\Users\\Administrator\\Desktop\\easyUI\\courtinfo.xlsx");
        for(int n=0;n<map.size();n++){
            List<String[]> list = map.get(n);
            System.out.println("-------------------------sheet"+n+"--------------------------------");
            for(int i=1;i<list.size();i++){
                String[] arr = (String[]) list.get(i);
               
                for(int j=0;j<arr.length;j++){
                    if(j==arr.length-1)
                        System.out.print(arr[j]);
                    else
                        System.out.print(arr[j]+"|");
                }
                System.out.println();
           
                if(arr[9]!=null && !"".equals(arr[9])){
                      SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                      Date date = null;
                    date = sdf1.parse(arr[9].toString());
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpleDateFormat sdf_2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sDate=sdf.format(date);
                    String sDate2 = sdf.format(date);
//                    Date date_of_hearing = sdf_2.parse(sDate2.toString());
                    Date start = (Date)sdf.parseObject(sDate);
                    System.out.println("日期格式为:"+start);
                    
//                    System.out.println(date_of_hearing);
                    System.out.println(sDate);
                    System.out.println(sDate2);
                    
                }
                
                System.out.println();
                System.out.println("行数为:"+list.size());
                System.out.println("列数为:"+arr.length);
            }
           
        }
    }
}
