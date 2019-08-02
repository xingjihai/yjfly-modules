package cn.ejfy.web.tools.word;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * word 文档生成工具类
 * 原理：
 * 模板word
 * 替换模板word中的关键字，然后生成新的word。
 */

public class WordPOI {


    private static String getUrl(HttpServletRequest request){
        StringBuffer url = new StringBuffer(request.getRequestURL().toString().replace(request.getRequestURI(), ""));
        if(!"".equals(request.getContextPath()) || !"/".equals(request.getContextPath())){
            url.append(request.getContextPath() + "/");
        }else{
            url.append("/");
        }
        return url.toString();
    }



    // 替换word中需要替换的特殊字符
    public static boolean replaceAndGenerateWord(String srcPath,
                                                 String destPath, Map<String, String> map) {
        String[] sp = srcPath.split("\\.");
        String[] dp = destPath.split("\\.");
        if ((sp.length > 0) && (dp.length > 0)) {// 判断文件有无扩展名
            // 比较文件扩展名
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
                try {
                    XWPFDocument document = new XWPFDocument(
                            POIXMLDocument.openPackage(srcPath));
                    // 替换段落中的指定文字
                    Iterator<XWPFParagraph> itPara = document
                            .getParagraphsIterator();
                    while (itPara.hasNext()) {
                        XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (int i = 0; i < runs.size(); i++) {
                            String oneparaString = runs.get(i).getText(
                                    runs.get(i).getTextPosition());
                            for (Entry<String, String> entry : map
                                    .entrySet()) {
                                oneparaString = oneparaString.replace(
                                        entry.getKey(), entry.getValue());
                            }
                            runs.get(i).setText(oneparaString, 0);
                        }
                    }

                    // 替换表格中的指定文字
                    Iterator<XWPFTable> itTable = document.getTablesIterator();
                    while (itTable.hasNext()) {
                        XWPFTable table = (XWPFTable) itTable.next();
                        int rcount = table.getNumberOfRows();
                        for (int i = 0; i < rcount; i++) {
                            XWPFTableRow row = table.getRow(i);
                            List<XWPFTableCell> cells = row.getTableCells();
                            for (XWPFTableCell cell : cells) {
                                String cellTextString = cell.getText();
                                for (Entry<String, String> e : map.entrySet()) {
                                    if (cellTextString.contains(e.getKey()))
                                        cellTextString = cellTextString
                                                .replace(e.getKey(),
                                                        e.getValue());
                                }
                                cell.removeParagraph(0);
                                cell.setText(cellTextString);
                            }
                        }
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(destPath);
                    document.write(outStream);
                    outStream.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            } else
                // doc只能生成doc，如果生成docx会出错
                if ((sp[sp.length - 1].equalsIgnoreCase("doc"))
                        && (dp[dp.length - 1].equalsIgnoreCase("doc"))) {
                    HWPFDocument document = null;
                    FileOutputStream outStream = null;
                    FileInputStream inStream = null;
                    try {
                        File file = new File(srcPath);
                        file.setReadOnly();//文件只读模式
                        inStream = new FileInputStream(file);
                        document = new HWPFDocument(inStream);
                        Range range = document.getRange();
                        HeaderStories headerStories =  new HeaderStories(document);
                        Range range2 =  headerStories.getRange();
                        String str = document.getDocumentText();
                        if (str.contains("f_cra")) {
                            String  addr = map.get("foot_courtRoomAddr");
                            if (addr.length()<5){
                                addr = "     ";
                            }
                            range2.replaceText("f_cra", addr);
                        }
//
                        for (Entry<String, String> entry : map.entrySet()) {
                            range.replaceText(entry.getKey(), entry.getValue());
                        }

                        outStream = new FileOutputStream(destPath);
                        document.write(outStream);
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    } finally {
                        try {
                            if(outStream != null){
                                outStream.close();
                            }
                            if(inStream != null){
                                inStream.close();
                            }
                        }catch (Exception e){

                        }
                    }
                } else {
                    return false;
                }
        } else {
            return false;
        }
    }

    /**替换word中需要替换的特殊字符
     * docx支持换行
     * @param lineFeedChar 换行分割符
     */
    public static boolean replaceAndGenerateWord2(String srcPath,
                                                 String destPath, Map<String, String> map,String lineFeedChar) {
        String[] sp = srcPath.split("\\.");
        String[] dp = destPath.split("\\.");
        if ((sp.length > 0) && (dp.length > 0)) {// 判断文件有无扩展名
            // 比较文件扩展名
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
                try {
                    XWPFDocument document = new XWPFDocument(
                            POIXMLDocument.openPackage(srcPath));
                    // 替换段落中的指定文字
                    Iterator<XWPFParagraph> itPara = document
                            .getParagraphsIterator();
                    while (itPara.hasNext()) {
                        XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                        List<XWPFRun> runs = paragraph.getRuns();
                        // --
                        List<Map> addRuns=new ArrayList();
                        // --
                        for (int i = 0; i < runs.size(); i++) {
                            String oneparaString = runs.get(i).getText(
                                    runs.get(i).getTextPosition());
                            for (Map.Entry<String, String> entry : map.entrySet()) {
//                                oneparaString = oneparaString.replace(entry.getKey(), entry.getValue());
                                // --
                                if(oneparaString.contains(entry.getKey())&&entry.getValue().contains( lineFeedChar )){
                                    String[] strArr=entry.getValue().split( lineFeedChar );

                                    for (String str : strArr) {
                                        Map runMap=new HashMap();
                                        runMap.put("content",str);
                                        runMap.put("run",runs.get(i));
                                        addRuns.add(runMap);
                                    }
                                    oneparaString = oneparaString.replace(entry.getKey(),"");
                                }else{
                                    oneparaString = oneparaString.replace(entry.getKey(), entry.getValue());
                                }
                                // --
                            }
                            runs.get(i).setText(oneparaString, 0);
                        }
                        // --
                        for (Map runMap : addRuns) {
                            String content=(String)runMap.get("content");
                            XWPFRun run=(XWPFRun)runMap.get("run");

                            XWPFRun newRun=paragraph.createRun();
                            newRun.setText(content);
                            newRun.setFontFamily(run.getFontFamily());
                            newRun.setFontSize(run.getFontSize());
                            newRun.addCarriageReturn();
                        }
                        // --
                    }

                    // 替换表格中的指定文字
                    Iterator<XWPFTable> itTable = document.getTablesIterator();
                    while (itTable.hasNext()) {
                        XWPFTable table = (XWPFTable) itTable.next();
                        int rcount = table.getNumberOfRows();
                        for (int i = 0; i < rcount; i++) {
                            XWPFTableRow row = table.getRow(i);
                            List<XWPFTableCell> cells = row.getTableCells();
                            for (XWPFTableCell cell : cells) {
                                String cellTextString = cell.getText();
                                for (Map.Entry<String, String> e : map.entrySet()) {
                                    if (cellTextString.contains(e.getKey()))
                                        cellTextString = cellTextString
                                                .replace(e.getKey(),
                                                        e.getValue());
                                }
                                cell.removeParagraph(0);
                                cell.setText(cellTextString);
                            }
                        }
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(destPath);
                    document.write(outStream);
                    outStream.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            } else
                // doc只能生成doc，如果生成docx会出错
                if ((sp[sp.length - 1].equalsIgnoreCase("doc"))
                        && (dp[dp.length - 1].equalsIgnoreCase("doc"))) {
                    HWPFDocument document = null;
                    FileOutputStream outStream = null;
                    FileInputStream inStream = null;
                    try {
                        File file = new File(srcPath);
                        file.setReadOnly();//文件只读模式
                        inStream = new FileInputStream(file);
                        document = new HWPFDocument(inStream);
                        Range range = document.getRange();
                        HeaderStories headerStories =  new HeaderStories(document);
                        Range range2 =  headerStories.getRange();
                        String str = document.getDocumentText();
                        if (str.contains("f_cra")) {
                            String  addr = map.get("foot_courtRoomAddr");
                            if (addr.length()<5){
                                addr = "     ";
                            }
                            range2.replaceText("f_cra", addr);
                        }
//
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            range.replaceText(entry.getKey(), entry.getValue());
                        }

                        outStream = new FileOutputStream(destPath);
                        document.write(outStream);
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    } finally {
                        try {
                            if(outStream != null){
                                outStream.close();
                            }
                            if(inStream != null){
                                inStream.close();
                            }
                        }catch (Exception e){

                        }
                    }
                } else {
                    return false;
                }
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String filepathString = "E:\\stsWorkspace\\wtfr\\weitu-dispuse-admin\\src\\main\\java\\com\\weitu\\dispuse\\admin\\survey\\record_mediate.docx";
        String destpathString = "E:\\stsWorkspace\\wtfr\\weitu-dispuse-admin\\src\\main\\java\\com\\weitu\\dispuse\\admin\\survey\\record_mediate_2.docx";
        Map replaceMap=new HashMap();
        replaceMap.put("{preCaseCode}","" );
        replaceMap.put("{caseLitigantReason}","" );
        replaceMap.put("{judgeAssistant}","法官助理"  );
        replaceMap.put("{courtClerk}","书记员");
        replaceMap.put("{litigantString}", "" );
        replaceMap.put("{recordContent}", "是地方给第三方给第三方个\n士大夫多个第三方公司的广告\n发到公司的风格地方 " );
        System.out.println(replaceAndGenerateWord2(filepathString,destpathString, replaceMap,"\n"));
    }


    /**
     * 根据指定的参数值、模板，生成 word 文档
     * @param param 需要替换的变量
     * @param template 模板
     */
    public static XWPFDocument generateWord(Map<String, Object> param, String template) {
        XWPFDocument doc = null;
        try {
            OPCPackage pack = POIXMLDocument.openPackage(template);
            doc = new XWPFDocument(pack);
            if (param != null && param.size() > 0) {

                //处理段落
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc);

                //处理表格
                Iterator<XWPFTable> it = doc.getTablesIterator();
                while (it.hasNext()) {
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
                            processParagraphs(paragraphListTable, param, doc);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    /**
     * 处理段落
     * @param paragraphList
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,XWPFDocument doc) throws InvalidFormatException, FileNotFoundException{
        if(paragraphList != null && paragraphList.size() > 0){
            for(XWPFParagraph paragraph:paragraphList){
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    System.out.println("text=="+text);
                    if(text != null){
                        boolean isSetText = false;
                        for (Entry<String, Object> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if(text.indexOf(key) != -1){
                                isSetText = true;
                                Object value = entry.getValue();
                                if (value instanceof Map) {  //图片替换
                                    text = text.replace(key, "");
                                    Map pic = (Map)value;
                                    int width = Integer.parseInt(pic.get("width").toString());
                                    int height = Integer.parseInt(pic.get("height").toString());
                                    int picType = getPictureType(pic.get("type").toString());
                                    String byteArray = (String) pic.get("content");


                                    System.out.println("=="+byteArray);
                                    //ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);

                                    //int ind = doc.getAllPictures().size() - 1;
                                    //doc.createPicture(ind, width , height,paragraph);
                                    CTInline inline = run.getCTR().addNewDrawing().addNewInline();
                                    insertPicture(doc,byteArray,inline, width, height);

                                }else if (value instanceof String) {//文本替换
                                    System.out.println("key=="+key);
                                    text = text.replace(key, value.toString());
                                }
                            }
                        }
                        if(isSetText){
                            run.setText(text,0);
                        }
                    }
                }
            }
        }
    }
    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = XWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = XWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = XWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }
    /**
     * 将输入流中的数据写入字节数组
     * @param in
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in,boolean isClose){
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(isClose){
                try {
                    in.close();
                } catch (Exception e2) {
                    System.out.println("关闭流失败");
                }
            }
        }
        return byteArray;
    }

    /**
     * insert Picture
     * @param document
     * @param filePath
     * @param inline
     * @param width
     * @param height
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     */
    private static void insertPicture(XWPFDocument document, String filePath,
                                      CTInline inline, int width,
                                      int height) throws InvalidFormatException,
            FileNotFoundException {
        document.addPictureData(new FileInputStream(filePath),XWPFDocument.PICTURE_TYPE_PNG);
        int id = document.getAllPictures().size() - 1;
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        POIXMLDocumentPart part = new POIXMLDocumentPart();
//        String blipId = document.getAllPictures().get(id).getRelationParts().get(id).getRelationship().getId();
        String blipId = document.getAllPictures().get(id).getPackageRelationship().getId();
        String picXml = getPicXml(blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);
        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("IMG_" + id);
        docPr.setDescr("IMG_" + id);
    }
    /**
     * get the xml of the picture
     * @param blipId
     * @param width
     * @param height
     * @return
     */
    private static String getPicXml(String blipId, int width, int height) {
        String picXml =
                "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                        "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                        "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                        "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + 0 +
                        "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" +
                        "         </pic:nvPicPr>" + "         <pic:blipFill>" +
                        "            <a:blip r:embed=\"" + blipId +
                        "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                        "            <a:stretch>" + "               <a:fillRect/>" +
                        "            </a:stretch>" + "         </pic:blipFill>" +
                        "         <pic:spPr>" + "            <a:xfrm>" +
                        "               <a:off x=\"0\" y=\"0\"/>" +
                        "               <a:ext cx=\"" + width + "\" cy=\"" + height +
                        "\"/>" + "            </a:xfrm>" +
                        "            <a:prstGeom prst=\"rect\">" +
                        "               <a:avLst/>" + "            </a:prstGeom>" +
                        "         </pic:spPr>" + "      </pic:pic>" +
                        "   </a:graphicData>" + "</a:graphic>";
        return picXml;
    }
}