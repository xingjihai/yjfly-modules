package cn.ejfy.web.tools.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;

import cn.ejfy.web.tools.io.path.PathTool;
import cn.ejfy.web.tools.properties.Settings;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;





public class FileTool {
	
	
	
	
	/**
	 * spring 框架下的文件上传  （注意：上传的文件最好不要放到项目中，不利于维护）
	 * @param multipart_file  文件
	 * @param request  
	 * @param relative_folder  相对磁盘基本保存路径的相对路径（文件夹）
	 * 如磁盘基本保存路径为：E:\\stsWorkspace\\loginTest\\WebRoot\\upload， 对应文件要放upload下image文件夹， 则 relative_path="/image"
	 * @param file_name 文件名
	 * @throws Exception 
	 */
	public static void springFileUpload(MultipartFile multipart_file,HttpServletRequest request,String relative_folder,String file_name) throws  Exception{
	    String folder_path= Settings.getUpload_disk()+ PathTool.getDiskPath(relative_folder);
	    
	    if(multipart_file == null){
	        return ;
	    }
        File folder = new File(folder_path);
        if(!folder.exists()){     //要先确认有没有目录，否则会报错。
            folder.mkdirs();
        }
        String file_path=folder_path+File.separator+ file_name;
        File target_file=new File(file_path);
        multipart_file.transferTo(target_file);
	    
	}
	
	
	/**
     * 删除文件或文件夹
     * @param filePath
     */
    public static void delete(String filePath) {
        if(filePath.startsWith("http")){   //跳过网络文件
            return;
        }
        try {
            File file = new File(filePath);
            if (file.exists()){
                if(file.isDirectory()){
                    FileUtils.deleteDirectory(file);
                }else{
                    file.delete();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	

	/**
	 * 获取上传文件的访问路径
	 * @param path  一般为数据库中的路径  --  基本存储路径下的相对路径（包括文件名），如 /image/a.png
	 */
	public static String getFileVisitPath(String path){
	    if(path.startsWith("http")){   //跳过网络文件
            return path;
        }
	    return Settings.getStatics()+path;
	}
	
	/**
     * 获取上传文件的磁盘路径
     * @param path  一般为数据库中的路径  --  基本存储路径下的相对路径（包括文件名），如 /image/a.png
     */
    public static String getFileDiskPath(String path){
        if(path.startsWith("http")){   //跳过网络文件
            return path;
        }
        String disk_path=Settings.getUpload_disk()+PathTool.getDiskPath(path);
        return disk_path;
    }
	
	
	
	/**
	 * MultipartFile 转 File（不建议使用,推荐使用流） <br>
	 * 转换后最好删除临时文件。
	 * @return
	 */
    public static File multipartToFile(MultipartFile multfile) {
//        CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
//        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
//        File file = fi.getStoreLocation(); // 1 --创建临时文件（在项目的缓存文件夹中）
        File tmpFile = new File( // 2 -- 创建临时文件（在电脑的默认缓存文件夹中）
                System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + multfile.getOriginalFilename());
        try {
            multfile.transferTo(tmpFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpFile;
    }
	
	
	
	/**
     * 项目中的文件上传到第三方
    * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
    * @param url 请求地址 form表单url地址
    * @param filePath 文件在服务器保存路径
    * @return String 正确上传返回media_id
    * @throws IOException
    */
    
    /** 微信上传文件接口  */
    public static String localFileUpload(String url,File file,String type) throws Exception {
         String result = null;
            if (!file.exists() || !file.isFile()) {
                return "文件路径错误";
            }
            /**
             * 第一部分
             */
            if(StringUtils.isNotBlank(type)){
                url = url+"&type="+type;
            }
            URL urlObj = new URL(url);
            HttpURLConnection con = null;
            
            //解决HTTPS
            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                                       + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            con=(HttpURLConnection) urlObj.openConnection();
            
            /**
             * 设置关键值
             */
            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false); // post方式不能使用缓存
            // 设置请求头信息
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
    
            // 设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("content-type", "multipart/form-data; boundary=" + BOUNDARY);
            
            //con.setRequestProperty("Content-Type", "multipart/mixed; boundary=" + BOUNDARY);
            //con.setRequestProperty("content-type", "text/html");
            // 请求正文信息
    
            // 第一部分：
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // ////////必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                    + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(head);
            
            // 文件正文部分
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            // 结尾部分
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            out.close();
            /**
             * 读取服务器响应，必须读取,否则提交不成功
             */
           // con.getResponseCode();

            /**
             * 下面的方式读取也是可以的
             */

             try {

             // 定义BufferedReader输入流来读取URL的响应
                 StringBuffer buffer = new StringBuffer();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(
                 con.getInputStream(),"UTF-8"));
                 String line = null;
                 while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    buffer.append(line);
                 }
                 if(result==null){
                    result = buffer.toString();
                }
                 return buffer.toString();
             } catch (Exception e) {
                 System.out.println("发送POST请求出现异常！" + e);
                 e.printStackTrace();
             }
             return result;
    }
    
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }
    
    static class miTM implements javax.net.ssl.TrustManager,
        javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }
        
        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }
        
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
        
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
    
    public static void main(String[] args) throws Exception {
        String filePath = "d:/mv1.jpg";
        String token="Jdr_B5dQzbWlmmTAlMxbpOZiUfe100laWKeNjRgqfYAJ2GkgCdbQCQO4gAA6e0qd7uYM8fhhzx9ehQBCHlQvKQ";
        String url="https://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token="+token;
        String result = null;
        File file=new File(filePath);
        result = FileTool.localFileUpload(url, file, "image");
        System.out.println(result);
    }
}
