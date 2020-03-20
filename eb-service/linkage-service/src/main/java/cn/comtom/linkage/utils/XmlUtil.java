package cn.comtom.linkage.utils;

import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.commom.Coverage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

	public static String xmlPre = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";

	public static String toXml(Object object) {
		XStream xstream = new XStream();
		if(object != null && getFieldValueByName("EBDType",object).toString().equals(EBDType.EBMStateResponse.name())){
			SortableFieldKeySorter sorter = new SortableFieldKeySorter();
			sorter.registerFieldOrder(Coverage.class, new String[] { "coverageRate", "areaCode", "coveragePercent","resBrdStat" });
			FieldDictionary fieldDictionary = new FieldDictionary(sorter);
			Sun14ReflectionProvider reflectionProvider = new Sun14ReflectionProvider(fieldDictionary);
			xstream = new XStream(reflectionProvider);
		}
		Class<?>[] classes = ClassUtil.ebdClasses;
		for (Class<?> class1 : classes) {
			xstream.alias(class1.getSimpleName(), class1);
		}
		for (Class<?> class1 : classes) {
			List<String> fields = getClassField(class1, xstream);
			setFieldesAlias(xstream, class1, fields, true);
		}
		String xml = xstream.toXML(object);
		return xml;
	}
	
	public static String toSignXml(Object object) {
		XStream xstream = new XStream();
		Class<?>[] classes = ClassUtil.sigClasses;
		for (Class<?> class1 : classes) {
			xstream.alias(class1.getSimpleName(), class1);
		}
		for (Class<?> class1 : classes) {
			List<String> fields = getClassField(class1, xstream);
			setFieldesAlias(xstream, class1, fields, true);
		}
		String xml = xstream.toXML(object);
		return xml;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml, Class<T> clazz) {
		XStream xstream = new XStream();
		Class<?>[] classes = ClassUtil.ebdClasses;
		for (Class<?> class1 : classes) {
			xstream.alias(class1.getSimpleName(), class1);
		}
		for (Class<?> class1 : classes) {
			List<String> fields = getClassField(class1, xstream);
			setFieldesAlias(xstream, class1, fields, true);
		}
		return (T) xstream.fromXML(xml);
	}

	public static cn.comtom.linkage.main.access.model.signature.Signature fromXml(String xml) {
		XStream xstream = new XStream();
		Class<?>[] classes = ClassUtil.sigClasses;
		for (Class<?> class1 : classes) {
			xstream.alias(class1.getSimpleName(), class1);
		}
		for (Class<?> class1 : classes) {
			List<String> fields = getClassField(class1, xstream);
			setFieldesAlias(xstream, class1, fields, true);
		}
		return (cn.comtom.linkage.main.access.model.signature.Signature) xstream.fromXML(xml);
	}

	/**
	 * @param xstream
	 * @param c
	 * @param
	 * @param initcap
	 *            是否首字母大写
	 */
	private static void setFieldesAlias(XStream xstream, Class<?> c, List<String> fields,
                                        boolean initcap) {
		if (fields == null || fields.isEmpty()) {
			return;
		}
		if (c == null) {
			return;
		}
		for (String fieldName : fields) {
			if (StringUtils.isBlank(fieldName)) {
				continue;
			}
			String alias = fieldName;
			if (initcap) {
				alias = StringUtils.capitalize(alias);
			}
			xstream.aliasField(alias, c, fieldName);
		}
	}

	/**
	 * 获取类的属性名称结合
	 * 
	 * @param c
	 * @return
	 */
	private static List<String> getClassField(Class<?> c, XStream xstream) {
		List<String> result = innerGetClassField(c, xstream);
		return result;
	}

	private static List<String> innerGetClassField(Class<?> c, XStream xstream) {
		Field[] fields = c.getDeclaredFields();
		List<String> result = new ArrayList<String>();
		if (ArrayUtils.isEmpty(fields)) {
			return result;
		}
		for (int i = 0; i < fields.length; i++) {
			result.add(fields[i].getName());
			Class<?> type = fields[i].getType();
			if (type.isAssignableFrom(List.class)) {
				xstream.addImplicitCollection(c, fields[i].getName());
			}
		}
		Class<?> superClass = c.getSuperclass();
		if (!superClass.equals(Object.class)) {
			result.addAll(innerGetClassField(superClass, xstream));
		}
		return result;
	}
	
	
	   /**
	    * 根据属性名获取属性值 
	    * @param fieldName
	    * @param o
	    * @return
	    */
   private static Object getFieldValueByName(String fieldName, Object o) {  
       try {    
           String firstLetter = fieldName.substring(0, 1).toUpperCase();    
           String getter = "get" + firstLetter + fieldName.substring(1);    
           Method method = o.getClass().getMethod(getter, new Class[] {});    
           Object value = method.invoke(o, new Object[] {});    
           return value;    
       } catch (Exception e) {    
    	   e.printStackTrace();
           return null;    
       }    
   }   

/*	public static void main(String[] args) {
		EBD ebd = new EBD();
		ebd.setEBDID("11111111111111");
		ebd.setEBDVersion("1");
		ebd.setEBDType(EBDType.EBMStateResponse.name());
		String xml = toXml(ebd);
		System.err.println(xml);
	//	EBD ebd2 = fromXml(xml, EBD.class);
	//	System.err.println(ebd2);
	}*/
}
