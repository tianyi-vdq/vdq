package com.tianyi.yw.common.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @Description: 
 * @ClassName: com.ali.openim.util.JsonUtils
 * @author: Omar(OmarZhang)
 * @date: 2015年12月3日 上午1:17:50 
 *
 */
public final class JsonUtils {

	/** ObjectMapper */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 不可实例化
	 */
	private JsonUtils() {
	}

	/**
	 * 将对象转换为JSON字符串
	 * 
	 * @param value
	 *            对象
	 * @return JSOn字符串
	 */
	public static String toJson(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON字符串转换为对象
	 * 
	 * @param json
	 *            JSON字符串
	 * @param valueType
	 *            对象类型
	 * @return 对象
	 */
	public static <T> T toObject(String json, Class<T> valueType) {
		try {
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON字符串转换为对象
	 * 
	 * @param json
	 *            JSON字符串
	 * @param typeReference
	 *            对象类型
	 * @return 对象
	 */
	public static <T> T toObject(String json, TypeReference<?> typeReference) {
		try {
			return mapper.readValue(json, typeReference);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将JSON字符串转换为对象
	 * 
	 * @param json
	 *            JSON字符串
	 * @param javaType
	 *            对象类型
	 * @return 对象
	 */
	public static <T> T toObject(String json, JavaType javaType) {
		try {
			return mapper.readValue(json, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将对象转换为JSON流
	 * 
	 * @param writer
	 *            writer
	 * @param value
	 *            对象
	 */
	public static void writeValue(Writer writer, Object value) {
		try {
			mapper.writeValue(writer, value);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	    * @Title: convertList2Json 
	    * @Description: 将List对象转换为Json字符串(支持泛型)
	    * @param objects
	    * @param clazz
	    * @throws IOException  
	    * @return String   
	    */ 
	    public static <T> String convertList2Json(List<T> objects, Class<?> clazz) throws IOException {
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES ,false);
	    	mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
	    	mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	        return mapper.writeValueAsString(objects);
	    }

	 /**
	  * 字符串转 对象集合
	 * @param <T>
	  * @Title: toObjectList 
	  * @param storeGroupIds
	  * @param class1
	  * @return
	  * 
	  */
	public static <T> List<T> toObjectList(String json,Class<T> clazz) {
		List<T> lists = new ArrayList<T>();
		if(StringUtils.isBlank(json)) {
			return lists;
		}
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> tempList = toObject(json, new ArrayList<Map<String,Object>>().getClass());
		for(Map<String,Object> tempMap : tempList ) {
			try {
				lists.add(mapper.readValue(toJson(tempMap),clazz));
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lists;
	}

}