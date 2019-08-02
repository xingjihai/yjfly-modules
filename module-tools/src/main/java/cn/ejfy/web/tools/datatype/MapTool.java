package cn.ejfy.web.tools.datatype;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapTool {
    /**
     * bean 转map
     * @throws Exception 
     */
    public static Map<String,Object> objectToMap(Object obj) throws Exception{
        if(obj==null){
            return null;
        }
        Map<String,Object> map=new HashMap<>();
        Class<?> clazz=obj.getClass();
        Field[]  declaredFields=clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}
