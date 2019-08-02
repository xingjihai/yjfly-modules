package cn.ejfy.web.tools.excel.xmlpoi;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 读取excel所有工作表内容
     * 优点：可以处理大数据量，内存不溢出
     * 暂时发现的问题： 带有格式的excel无法读取。（如日期,数字格式）   相比PoiExcelImportTool工具类
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Map<Integer, List<String[]>> readAllSheets(String fileName) throws Exception {
        InputStream in = new FileInputStream(fileName);
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            in.close();
            Excel2003Reader reader = new Excel2003Reader();
            reader.readAllSheets(fileName);
            return reader.getSheetDatas();            //HSSFWorkbook支持"xls"
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            in.close();
            Excel2007Reader reader = new Excel2007Reader();
            reader.readAllSheets(fileName);
            return reader.getSheetDatas();//XSSFWorkbook支持"xlsx"
        }
        try{
            in.close();
            Excel2003Reader reader = new Excel2003Reader();
            reader.readAllSheets(fileName);
            return reader.getSheetDatas();
        }catch (Exception e){
            throw new IllegalArgumentException("你的excel版本目前poi解析不了");
        }
    }

    /**
     * 写入excel工作表内容
     * @param fileName
     * @param sheetName
     * @param headerValues
     * @param headers
     * @param resultList
     * @param res
     */
    public static void writeOneSheet(String fileName, String sheetName, String[] headerValues, String[] headers, List<Map> resultList, HttpServletResponse res) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        // 第一步，创建一个webbook，对应一个Excel文件
        SXSSFWorkbook wb = new SXSSFWorkbook(xssfWorkbook);
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        SXSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        SXSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
//        style.setWrapText(true);//换行

        //创建表头
        row = sheet.createRow(0);
        for (int i = 0; i < headerValues.length; i++) {
            SXSSFCell cel = row.createCell(i);
            cel.setCellStyle(style);
            XSSFRichTextString text = new XSSFRichTextString(headerValues[i]);
            cel.setCellValue(text);
        }

        for (int i = 0; i < resultList.size(); i++) {
            row = sheet.createRow((int) i + 1);
            Map map = resultList.get(i);
            for (Integer j = 0; j < headers.length; j++) {
                if (map.containsKey(headers[j])) {
                    SXSSFCell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    if (map.get(headers[j]) != null) {
                        if (map.get(headers[j]) instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
                            String text = "";
                            if ("registerDate".equals(headers[j]) || "preTrialDate".equals(headers[j]) ||"mailTime".equals(headers[j])||"visitTime".equals(headers[j])) {
                                text = sdf1.format(map.get(headers[j]));
                            } else {
                                text = sdf.format(map.get(headers[j]));
                            }
                            XSSFRichTextString richText = new XSSFRichTextString(text);
                            cell.setCellValue(richText);
                        } else {
                            XSSFRichTextString richText = new XSSFRichTextString(map.get(headers[j]).toString());
                            cell.setCellValue(richText);
                        }
                    } else {
                        XSSFRichTextString richText = new XSSFRichTextString("无");
                        cell.setCellValue(richText);
                    }
                }
            }
        }

        writeExcel(wb, fileName, res);
    }

    /**
     *
     * @param wb
     * @param fileName
     * @param res
     */
    private static void writeExcel(SXSSFWorkbook wb, String fileName, HttpServletResponse res) {
        // 第六步，将文件存到指定位置
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            res.reset();
            res.setContentType("application/vnd.ms-excel;charset=utf-8");
            String fileNameURLEncode = URLEncoder.encode(fileName + ".xls", "UTF-8");
            res.setHeader("Content-Disposition", "attachment;filename=" + fileNameURLEncode);
            ServletOutputStream out = res.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //读取xlsx
        try {
            Map<Integer, List<String[]>> map = readAllSheets("C:\\Users\\Administrator\\Desktop\\新建文件夹 (2)\\import_sstf_model (1).xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
