package cn.comtom.tools.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author baichun  2018-12-06
 */
public class EntityUtil {

    /**
     * 八种基本类型
     */
    private static List<String> typeList = Arrays.asList("double", "int", "long", "byte", "short", "float", "boolean", "char");

    /**
     * 判断对象是否为空对象，
     * 对象属性是String类型，如果不为null并且不是空字符串 返回false
     * 对象属性是其他类型，只要不为null都返回false
     *
     * @param entity
     * @return
     */
    public static boolean isEmpty(Object entity) {
        if (entity == null) {
            return true;
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        return Arrays.stream(fields).allMatch(field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (typeList.contains(field.getGenericType().getTypeName())) {
                    return false;
                }
                if (value instanceof String) {
                    return StringUtils.isBlank(value.toString());
                }
                return field.get(entity) == null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        });

    }

    /**
     * 判断对象是否为空对象，
     * 对象属性是String类型，如果不为null并且不是空字符串 返回false
     * 对象属性是集合类型，如果不为null并且不是空集合，返回false
     * 对象属性是其他对象类型，递归检查是否为空对象
     *
     * @param entity
     * @return
     */
    public static boolean isReallyEmpty(Object entity) {
        if (entity == null) {
            return true;
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        return Arrays.stream(fields).allMatch(field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value == null) {
                    return true;
                }
                if (typeList.contains(field.getGenericType().getTypeName())) {
                    return false;
                }
                if (value instanceof String) {
                    return StringUtils.isBlank(value.toString());
                }
                if (value instanceof Collection) {
                    return ((Collection) value).isEmpty();
                }
                return isReallyEmpty(value);


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        });

    }


}
