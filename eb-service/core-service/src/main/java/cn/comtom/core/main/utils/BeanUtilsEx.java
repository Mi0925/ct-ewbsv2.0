package cn.comtom.core.main.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @Title 扩展BeanUtils.copyProperties支持data类型
 * @Description
 * @author
 * @date
 */
@Slf4j
public class BeanUtilsEx extends org.apache.commons.beanutils.BeanUtils {

    static {
        ConvertUtils.register(new DateConvert(), java.util.Date.class);
        ConvertUtils.register(new DateConvert(), String.class);
    }

    public static void copyProperties(Object target, Object source) {
        // 支持对日期copy
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("扩展BeanUtils.copyProperties支持data类型:" + e);
        }
    }
}

