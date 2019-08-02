package cn.ejfy.web.tools.xml;

import cn.ejfy.web.tools.xml.test.Version;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名：XMLUtils
 * 依赖包：
 * jackson-dataformat-xml
 *
 * 缺陷：无法转换map中的list
 */
public class XMLUtils {

    private static XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        xmlMapper.setDefaultUseWrapper(false);
    }

    public static XmlMapper getXmlMapper(){
        return xmlMapper;
    }

    @SuppressWarnings("unchecked")
	public static <T> T convert(String xml, Class<?> clazz){
        try{
            return (T) xmlMapper.readValue(xml, clazz);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(InputStream xml, Class<?> clazz){
        try{
            return (T) xmlMapper.readValue(xml, clazz);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String convert(Object object){
        try {
            return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convert(String rootName, Object object){
        try {
            ObjectWriter writer = xmlMapper.writer().withRootName(rootName);
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception{


        Map aa=new HashMap();
        aa.put("a","b" );
        aa.put("c","d" );


        String str = convert("Request", aa );
        System.out.println(str);
        Map<String, Object> map = convert(str, Map.class);
        System.out.println(map);

        map = convert("<s><c>666</c><a>777</a></s>", Map.class);
        System.out.println(map);


        File file=new File("C:\\develop\\workspace_idea\\ejfyweb\\module-tools\\src\\main\\java\\cn\\ejfy\\web\\tools\\xml\\test\\test.xml");
        InputStream inputStream=new FileInputStream(file);

        List<Map<String, Object>> list =convert(inputStream,List.class);
        System.out.println(list);

        File file2=new File("C:\\develop\\workspace_idea\\ejfyweb\\module-tools\\src\\main\\java\\cn\\ejfy\\web\\tools\\xml\\test\\test2.xml");
        InputStream inputStream2=new FileInputStream(file2);

        Map<String, List> list2 =convert(inputStream2,Map.class);   //缺陷：无法转换map中的list
        System.out.println(list2);
    }
}
