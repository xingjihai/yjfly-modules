//package utils;
//
//import com.aspose.words.Document;
//import com.aspose.words.ImportFormatMode;
//import com.aspose.words.License;
//import com.aspose.words.SaveFormat;
//import com.weitu.framework.core.annotation.FileType;
//import com.weitu.framework.core.support.ExtFile;
//import com.weitu.framework.web.core.HttpService;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Administrator on 2018/4/17.
// */
//public class AsposeUtils {
//    public static boolean getLicense(){
//        boolean result=false;
//
//        try {
//            InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("license/license.xml");
//            License license=new License();
//            license.setLicense(is);
//            result=true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//    public static String mergeDoc(HttpService service,List<String> urlList,String destPath) throws Exception {
//
//        ExtFile file = service.newFile(FileType.TEMPORARY, destPath, new String[]{""});
//        String ftlPath = service.getRealPath() + File.separator + "doc";
//        //String ftl = ftlPath + File.separator + folder + ".doc";
//
//        if(getLicense()){
//            Document outDoc=new Document();
//            outDoc.removeAllChildren();
//            for(String url:urlList){
//                InputStream is=new FileInputStream(url);
//                Document document=new Document(is);
//                outDoc.appendDocument(document, ImportFormatMode.USE_DESTINATION_STYLES);
//                is.close();
//            }
//            outDoc.save(file.getUrl(), SaveFormat.DOC);
//
//        }
//
//        return file.getUrl();
//    }
//
//    public static String createDoc(HttpService service, String folder, String fileName, Map<String, String> map){
//        getLicense();
//        ExtFile file = service.newFile(FileType.TEMPORARY, fileName+".doc", folder);
//        String ftlPath = service.getRealPath() + File.separator + "doc";
//        String ftl = ftlPath + File.separator + folder + ".doc";
//        String url = null;
//        try {
//            replaceByKeyword(ftl, file.getFile().getCanonicalPath(), map, SaveFormat.DOC);
//            url = file.getUrl();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return url;
//    }
//
//    //源文件路径 目标文件路径 关键词的map 使用saveformat.XXX传参
//    public static void replaceByKeyword(String srcPath,String destPath, Map<String, String> map,int type){
//        try {
//            Document document=new Document(srcPath);
//            for(Map.Entry entry:map.entrySet()){
//                document.getRange().replace((String) entry.getKey(), (String) entry.getValue(), true, false);
//            }
//            document.save(destPath,type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
