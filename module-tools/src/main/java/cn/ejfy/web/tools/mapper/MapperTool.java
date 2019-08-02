package cn.ejfy.web.tools.mapper;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 将json字符串参数转换为对象形式
 */
public class MapperTool {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2map(Object obj) {
		return mapper.convertValue(obj, Map.class);
	}

	public static <T> T fromJson(Object obj, Class<T> cls) {
		try {
			return mapper.readValue(obj.toString(), cls);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static SimpleDateFormat default_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static ObjectMapper mapper = new ObjectMapper().setDateFormat(default_format);
}