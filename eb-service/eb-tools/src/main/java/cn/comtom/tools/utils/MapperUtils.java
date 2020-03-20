package cn.comtom.tools.utils;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 3:45
 */
public class MapperUtils {
	
    public static boolean isNotEmpty(Object object){
        if(Objects.isNull(object)){
        	return false;
        }else {
        	String str = object.toString();
            if(StringUtils.isNotEmpty(str)){
                return true;
            }
        }
        return false;
    }
}
