package cn.ejfy.web.tools.io.path;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class PathTool {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");  
    //public static final String FILE_SEPARATOR = File.separator;
    
    
    // ==============   与 项目无关的路径  =================  //
    /**
     * 转换为磁盘路径（"/"或"\"的转换），解决操作系统兼容问题
     */
    public static String getDiskPath(String path) {  
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);  
    } 
    
    
    // ==============   与 项目有关的路径  =================  //
    
    /**
     * 获取项目在磁盘中的基本绝对路径：（项目的根路径,即WebRoot下的路径地址）
     */
    public static String getContextDiskPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }
    /**
     * 返回项目在磁盘上的绝对路径<br>
     * 调用情况：1、new File(<b>path</b>) 获取path
     * @param path  项目所在的相对路径, 参数为空或"/"时代表<b>根路径</b>   (项目的根路径,即WebRoot下的路径地址）
     * @return  E:\stsWorkspace\loginTest\WebRoot
     */
    public static String getContextDiskPath(HttpServletRequest request, String path){
        if (StringUtils.isBlank(path)){
            path="/";
        }
        return request.getSession().getServletContext().getRealPath(path);
    }
    /**
     * 项目在磁盘中的父路径  （存放项目的路径）
     */
    public static String getContextParentDiskPath(HttpServletRequest request){
        String fpath = new File(request.getSession().getServletContext().getRealPath("/")).getParent();
        return fpath;
    }
    
    
    /**
     * 返回项目的绝对访问路径（不足处：只能返回ip不能返回域名）<br>
     * 相对基本路径：直接通过request.getContextPath()方法获取
     * @param path  项目所在的相对路径, 参数为空或"/"时代表根路径
     */
    public static String getBaseUrl(HttpServletRequest request, String path) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort()+ request.getContextPath() ;
        if (StringUtils.isBlank(path)){
            path="/";
        }
        baseUrl += path;
        return baseUrl;
    }
    
    
    /**
     *  获取请求路径 
     *  @return <b>url</b> <br> 
     *  post请求url不带参,如：http://192.168.0.34:8036/loginTest/pathTest.do<br> 
     *  get请求url带参,如：http://192.168.0.34:8036/loginTest/pathTest.do?a=1&b=6
     */
    public static String getFullURL(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }
        return url.toString();
    }
    
    /**
     * 遍历所有参数
     */
    public static  void showAllParameter(HttpServletRequest request){
        Enumeration<String> enu=request.getParameterNames();
        while(enu.hasMoreElements()){  
            String paraName=enu.nextElement();  
            System.out.println(paraName+": "+request.getParameter(paraName));  
        }
    }

    
    public static void main(String[] args) {
        
        
        System.out.println(  getDiskPath("/ad/asdfas")  );
        //磁盘中的分割符：
        System.out.println( File.separator);
        System.out.println( File.separatorChar);
        System.out.println( File.pathSeparator);
        System.out.println( File.pathSeparatorChar);
        System.out.println(  System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")  );
        
    }
    
}
