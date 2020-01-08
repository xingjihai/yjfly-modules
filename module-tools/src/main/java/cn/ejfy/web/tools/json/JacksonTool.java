package cn.ejfy.web.tools.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author duwei
 * @date 2019/8/26
 */
public class JacksonTool implements JsonTool{

    private TypeReference typeReference;

    /**
     * @param typeReference  new TypeReference<类>() {}
     */
    JacksonTool(TypeReference typeReference){
        this.typeReference = typeReference;
    }

    @Override
    public <T> T jsonToObject(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        T jsonObj = null;
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // json中存在新增多余字段时不抛出异常
            jsonObj = mapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            logger.error("[Jackson]", e);
        }
        return jsonObj;
    }

    @Override
    public String objectToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("[Jackson]", e);
        }
        return str;
    }

    public static void main(String[] args) {
//        JsonTool jsonTool = new JacksonTool(new TypeReference<Json>() {});
//        String jsonStr = TestUtils.readJsonData("d:\\Data\\jackson.json");
//        System.out.println(jsonStr);
//        Json json = jsonTool.jsonToObject(jsonStr);  // json字符串转对象
//        System.out.println(jsonTool.objectToJson(json));  //对象转json字符串
    }
}
