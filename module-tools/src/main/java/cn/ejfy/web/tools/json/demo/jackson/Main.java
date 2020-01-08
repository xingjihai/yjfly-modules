package cn.ejfy.web.tools.json.demo.jackson;

import cn.ejfy.web.tools.json.demo.jackson.bean.Json;
import cn.ejfy.web.tools.json.util.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author duwei
 * @date 2019/8/26
 */
public class Main {
    public static void main(String[] args) {
//        WriteJsonFile();
//        ReadFromJsonFile();
//        JsonToObject();
        objectToJson();
    }

    public static void ReadFromJsonFile() {
        Json jsonObj = new Json();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Read JSON from file, convert JSON back to object");
        try {
            jsonObj = mapper.readValue(new File("d:\\Data\\jackson.json"), Json.class);
            System.out.println(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void WriteJsonFile() {
        Json jsonObj = new Json();

        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Convert Java object to JSON format and save to file");

        try {
            mapper.writeValue(new File("d:\\Data\\jackson.json"), jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void JsonToObject() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr;
        try {
            jsonStr = TestUtils.readJsonData("d:\\Data\\jackson.json");

            System.out.println(jsonStr);
//            JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, Json.class);
//            Json jsonObj = mapper.readValue(jsonStr, javaType);
            //如果是Map类型  mapper.getTypeFactory().constructParametricType(HashMap.class,String.class, Bean.class);
            Json jsonObj = mapper.readValue(jsonStr, new TypeReference<Json>() {});
            System.out.println(jsonObj);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * object 转字符串
     */
    public static void objectToJson() {
        Json jsonObj = new Json();

        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Convert Java object to JSON format and save to file");

        try {
            String str = mapper.writeValueAsString(jsonObj);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
