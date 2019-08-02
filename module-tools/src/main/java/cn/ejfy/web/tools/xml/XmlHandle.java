package cn.ejfy.web.tools.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml处理类
 * 导包：xstream-1.4.7.jar
 *       dom4j-1.6.1.jar
 * @author wyj
 */
public  class XmlHandle {
    
    /**
     * 将xml数据包转换为map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getMap(HttpServletRequest request) {
        Map<String, String> requestMap = new HashMap<String, String>();
        try {
            InputStream inputStream = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            for (Element e : (List<Element>) root.elements()) {
                requestMap.put(e.getName(), e.getText());
            }
            inputStream.close();
            inputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestMap;
    }
    
    /**将xml数据包转换为InputMessage对象
     */
    public static InputMessage getInputMessage(HttpServletRequest request){
        ServletInputStream in;
        InputMessage inputMsg = null;
        try {
            in = request.getInputStream();
            //将POST流转换为XStream对象  
            XStream xs = new XStream(new DomDriver());
            xs.alias("xml", InputMessage.class);  
            String xmlMsg = IOUtils.toString(in,"UTF-8");
            inputMsg = (InputMessage) xs.fromXML(xmlMsg); 
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return inputMsg;
    }
    
    /**将InputMessage对象转换为xml数据包
     */
    public static String getXmlString(InputMessage inputMessage){
        XStream xstream = new XStream(new DomDriver()); 
        xstream.alias("xml", InputMessage.class);
        String xml = xstream.toXML(inputMessage); 
        return xml;
    }
    
    public void test(){
        InputMessage inputMessage=new InputMessage();
        inputMessage.setContent("阿斯顿发顺丰");
        System.out.println(getXmlString(inputMessage));
    }
    
    /**
     * 使用示例
     */
    public static void main(HttpServletRequest request) {
        //假设request中有xml输出流
        
        //--1、直接转为map
        Map map= XmlHandle.getMap(request);
        //--2、转化为对象
        Object obj= XmlHandle.getInputMessage(request);
        
    }
}
