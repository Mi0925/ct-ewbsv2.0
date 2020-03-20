package cn.comtom.tools.utils;

import org.springframework.format.annotation.DateTimeFormat;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

    public static Map<String, Object> convertBean2Map(Object bean) {
        HashMap<String, Object> map = new HashMap<>();
        if (null == bean) {
            return map;
        }
        Class<?> clazz = bean.getClass();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return map;
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method method = descriptor.getReadMethod();

                Object result;
                try {
                    result = method.invoke(bean);
                    if (null != result) {
                        if (result instanceof Date) {
                            DateTimeFormat dt = null;
                            Field field = clazz.getDeclaredField(propertyName);
                            dt = field.getAnnotation(DateTimeFormat.class);
                            if (dt != null) {
                                SimpleDateFormat sd = new SimpleDateFormat(dt.pattern());
                                result = sd.format((Date) result);
                            }
                        }
                        map.put(propertyName, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }
}
