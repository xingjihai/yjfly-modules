package cn.ejfy.web.tools.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/28.
 */
public class ObjectMapperUtils {
    /**
     * json格式转object
     *
     * @param <T>
     * @param jsonString
     * @param objectClass
     * @return
     */
    public static <T> T json2Object(String jsonString, Class<T> objectClass) {
        if (jsonString == null || jsonString.length() <= 0 || objectClass == null) {
            return null;
        }

        T object = null;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            object = objectMapper.readValue(jsonString, objectClass);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * object转json字符串
     *
     * @param object
     * @return
     */
    public static String object2Json(Object object) {
        if (object == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(object);
        return jsonNode.toString();
    }

    public static void main(String[] args) {
        //json转对象（当对象中有对象属性时）
//        Map<String, Class> classMap = new HashMap<String, Class>();
//        classMap.put("holiday", HolidayJuhe.class);
//        classMap.put("list", HolidayDate.class);
//        JsonConfig jsonConfig = new JsonConfig();
//        jsonConfig.setCollectionType(List.class);
//        jsonConfig.setClassMap(classMap);
//        jsonConfig.setRootClass(HolidayJuhe.class);
//        List<HolidayJuhe> jsonList = (List<HolidayJuhe>) JSONArray.toCollection(jarray, jsonConfig);
    }
}
