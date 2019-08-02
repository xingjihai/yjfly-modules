package cn.ejfy.web.tools.jsonreturn;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


import cn.ejfy.web.tools.context.ThreadContextHolder;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * 移动端接口工具
 * @author wyj
 */
public class JsonResultUtil {
    
    /**
     * 成功
     * @return code  0<br/>
     *  message  客户端提示语（可为null,代表不提示）
     */
    public static void showSuccessJson(String message,Object data) {
        JsonResult mobileJsonResult;
        mobileJsonResult=new JsonResult(JsonResult.CODE_SUCCESS, message,data);
        renderMobileJsonResult(mobileJsonResult);
    }
    
    /**
     * 失败
     * @return code  1<br/>
     *  message  提示
     */
    public static void showFailJson(String message) {
        JsonResult mobileJsonResult=new JsonResult(JsonResult.CODE_Fail, message);
        renderMobileJsonResult(mobileJsonResult);
    }
    /**
     * 自定义json
     */
    public static void showCustomJson(Integer code,String message,Object data) {
        JsonResult mobileJsonResult=new JsonResult(code, message,data);
        renderMobileJsonResult(mobileJsonResult);
    }
    
    
    public static void renderMobileJsonResult(JsonResult mobileJsonResult) {
        String result=JSONObject.fromObject(mobileJsonResult,getConfig()).toString();
        renderJson(result);
    }
    /**
     * 直接输出json.
     */
    private static void renderJson(String text) {
        HttpServletResponse response = ThreadContextHolder.getHttpResponse();
        OutputStream os = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            os = response.getOutputStream();
            if (null != text) {
                os.write(text.getBytes("UTF-8"));
                os.flush();
            }
            os.close();
            response.flushBuffer();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
        }
    }
    
    private static JsonConfig getConfig(){
        JsonConfig jsonConfig = new JsonConfig();
        PropertyFilter filter = new PropertyFilter() {
                public boolean apply(Object object, String fieldName, Object fieldValue) {
//                if(fieldValue instanceof List){
//                    List<Object> list = (List<Object>) fieldValue;
//                    if (list.size()==0) {
//                        return true;
//                    }
//                }
//                    return null == fieldValue || "".equals(fieldValue);
                    return null == fieldValue ;
                }
        };
        jsonConfig.setJsonPropertyFilter(filter);
        return jsonConfig;
    }
}
