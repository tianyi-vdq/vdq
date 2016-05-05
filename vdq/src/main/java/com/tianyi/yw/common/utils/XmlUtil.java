package com.tianyi.yw.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;

/**
 * xml工具�?依赖于dom4j解析
 * 
 * @author dengbin
 * 
 */
public class XmlUtil {

	/** 以子节点方式添加 */
	public static final int NODE_SHEME = 1;
	/** 以属性方式添�?*/
	public static final int ATTRIBUTE_SHEME = 2;
	/**node节点名称为类型首字母小写*/
	public static final int SIMPLE_NODE=1;
	/**node节点名称为包名加类名*/
	public static final int PACKAGE_NODE=2;

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * 字符串转换成Document对象
	 * 
	 * @param xmlStr
	 *            xml字符�?
	 * @return Document
	 */
	public Document StringToXml(String xmlStr) {
		try {
			if (xmlStr == null || "".equals(xmlStr))
				return null;
			else {
				return DocumentHelper.parseText(xmlStr);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * Document对象转换成字符串
	 * 
	 * @param doc
	 *            Document对象
	 * @return xml字符�?
	 */
	public String XmlToString(Document doc) {
		if (doc == null)
			return null;
		else
			return doc.asXML();
	}

	/**
	 * 对象转换成Document,spring已包含类似功�?
	 * 
	 * @param object
	 *            对象
	 * @param scheme
	 *            模式
	 * @param nodeType
	 *            节点名称类型
	 * @return Document
	 */
	public Document ObjectToXml(String rootName, Object object, int scheme,int nodeType) {
		if (object == null)
			return null;
		else {
			Document doc = new DOMDocument();
			Element root = new DOMElement(rootName);
			doc.add(root);

			if (object instanceof List) {
				for (Object obj : (List<?>) object) {
					findObject(root, obj, scheme,nodeType);
				}
			} else if (object instanceof Set) {
				for (Object obj : (Set<?>) object) {
					findObject(root, obj, scheme,nodeType);
				}
			} else {
				findObject(root, object, scheme,nodeType);
			}
			return doc;
		}
	}

	protected void findObject(Element root, Object object, int scheme,int nodeType) {
		Class<?> clazz = object.getClass();

		Field[] fields = clazz.getDeclaredFields();
		Element element=null;
		if(nodeType==XmlUtil.SIMPLE_NODE){
			element= new DOMElement(
					firstUpperToLetter(clazz.getSimpleName()));
		}else{
			element = new DOMElement(clazz.getName());
		}
		if (scheme == XmlUtil.ATTRIBUTE_SHEME) {
			for (Field field : fields) {
				parseChildren(field, element, object,nodeType);
			}

		} else {
			for (Field field : fields) {
				Element felement = new DOMElement(field.getName());
				felement.setText(toString(getter(object,
						firstLetterToUpper(field.getName()))));
				element.add(felement);
			}
		}
		root.add(element);
	}

	@SuppressWarnings("deprecation")
	protected void parseChildren(Field field, Element element, Object object,int nodeType) {
		Class<?> type = field.getType();
		if (isSimpleType(type)) {
			element.setAttributeValue(
					field.getName(),
					toString(getter(object, firstLetterToUpper(field.getName()))));
		} else {
			try {
				Class<?> clazz = Class.forName(field.getType()
						.getCanonicalName());
				Object child = getter(object,
						firstLetterToUpper(field.getName()));
				if (child != null) {
					Element chidElelment =null;
					if(nodeType==XmlUtil.SIMPLE_NODE){
						chidElelment= new DOMElement(
								firstUpperToLetter(clazz.getSimpleName()));
					}else{
						chidElelment = new DOMElement(clazz.getName());
					}
					element.add(chidElelment);
					Field[] chidFields = clazz.getDeclaredFields();
					for (Field chidField : chidFields)
						parseChildren(chidField, chidElelment, child,nodeType);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * Document转换成对�?
	 * 
	 * @param doc
	 * @return
	 */
	public Object XmlToObject(Document doc, int scheme) {
		Object object = null;
		try {
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			Iterator<Element> iters = root.elementIterator();
			object = findObject(iters);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return object;
	}

	protected Object findObject(Iterator<Element> iters) {
		try {
			Object object = null;
			while (iters.hasNext()) {
				Element item = (Element) iters.next();
				if (item.getName().indexOf(".") != -1) {
					Class<?> clazz = Class.forName(item.getName());
					object = clazz.newInstance();
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						if(isSimpleType(field.getType())){
							setter(object, firstLetterToUpper(field.getName()),
									item.attribute(field.getName()).getValue(),
									field.getType());
						}else{
							@SuppressWarnings("unchecked")
							Iterator<Element> children = item.elementIterator();
							Object o =findObject(children);
							if(o.getClass().getName().equals(field.getType().getCanonicalName())){
								setter(object, firstLetterToUpper(field.getName()),o,field.getType());
							}
						}
					}
				}
				return object;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * �?��数据类型判断
	 * @param type 数据类型
	 * @return
	 */
	protected boolean isSimpleType(Class<?> type){
		if (type == String.class 
				|| type == int.class || type == Integer.class 
				|| type == double.class || type == Double.class
				|| type == char.class || type == Character.class
				|| type == long.class || type == Long.class
				|| type == float.class || type == Float.class
				|| type == byte.class || type == Byte.class
				|| type == boolean.class || type == Boolean.class
				|| type == short.class || type == Short.class) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 调用对象的属性get方法
	 * 
	 * @param obj
	 * @param att
	 * @return
	 */
	protected Object getter(Object obj, String att) {
		try {
			Method method = obj.getClass().getMethod("get" + att);
			return method.invoke(obj);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 调用对象属�?的set方法
	 * 
	 * @param obj
	 * @param att
	 * @param value
	 * @param type
	 */
	protected void setter(Object obj, String att, Object value, Class<?> type) {
		try {
			Method method = obj.getClass().getMethod("set" + att, type);
			if (type == String.class)
				method.invoke(obj, toString(value));
			else if (type == Integer.class || type == int.class)
				method.invoke(obj, toInteger(value));
			else if (type == double.class || type == Double.class)
				method.invoke(obj, toDouble(value));
			else if(type == char.class || type == Character.class)
				method.invoke(obj,toCharacter(value));
			else if(type == long.class || type == Long.class)
				method.invoke(obj,toLong(value));
			else if(type == float.class || type == Float.class)
				method.invoke(obj,toFloat(value));
			else if(type == byte.class || type == Byte.class)
				method.invoke(obj,toByte(value));
			else if(type == boolean.class || type == Boolean.class)
				method.invoke(obj,toBoolean(value));
			else if(type == short.class || type == Short.class)
				method.invoke(obj,toShort(value));
			else
				method.invoke(obj,value);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}


	
	/**
	 * 首字母大�?
	 * 
	 * @param str
	 * @return
	 */
	protected String firstLetterToUpper(String str) {
		char[] array = str.toCharArray();
		array[0] -= 32;
		return String.valueOf(array);
	}

	/**
	 * 首字母小�?
	 * 
	 * @param str
	 * @return
	 */
	protected String firstUpperToLetter(String str) {
		char[] array = str.toCharArray();
		array[0] += 32;
		return String.valueOf(array);
	}

	/**
	 * 对象转换成字符串
	 * 
	 * @param object
	 * @return
	 */
	protected String toString(Object object) {
		if (object == null)
			return "";
		else
			return object.toString();
	}

	/**
	 * 对象转换成整�?
	 * 
	 * @param object
	 * @return
	 */
	protected Integer toInteger(Object object) {
		String str = toString(object);
		if ("".equals(str))
			return 0;
		else
			return Integer.parseInt(str);
	}
	
	/**
	 * 对象转换成double
	 * @param object
	 * @return
	 */
	protected Double toDouble(Object object){
		String str = toString(object);
		if("".equals(str))
			return 0.0;
		else
			return Double.parseDouble(str);
	}
	
	/**
	 * 对象转换成float
	 * @param object
	 * @return
	 */
	protected Float toFloat(Object object){
		String str = toString(object);
		if("".equals(str))
			return 0.0f;
		else
			return Float.parseFloat(str);
	}
	
	/**
	 * 对象转换成long
	 * @param object
	 * @return
	 */
	protected Long toLong(Object object){
		String str = toString(object);
		if("".equals(str))
			return 0l;
		else
			return Long.parseLong(str);
	}
	
	/**
	 * 对象转换成booean
	 * @param object
	 * @return
	 */
	protected Boolean toBoolean(Object object){
		String str = toString(object);
		if("".equals(str))
			return true;
		else
			return Boolean.parseBoolean(str);
	}
	
	/**
	 * 对象转换成short
	 * @param object
	 * @return
	 */
	protected Short toShort (Object object){
		String str = toString(object);
		if("".equals(str))
			return 0;
		else
			return Short.parseShort(str);
	}
	
	/**
	 * 对象转换成byte
	 * @param object
	 * @return
	 */
	protected Byte toByte(Object object){
		String str = toString(object);
		if("".equals(str))
			return 0;
		else
			return Byte.parseByte(str);
	}
	
	/**
	 * 对象转换成char
	 * @param object
	 * @return
	 */
	protected Character toCharacter(Object object){
		if(object==null)
			return '\u0beb';
		else
			return (Character) object;
	}

}
