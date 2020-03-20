package cn.comtom.ebs.front.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 *
 * @author comtom
 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return getSubject().getSession();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return getSubject().getPrincipal() != null;
    }

    public static void logout() {
        getSubject().logout();
    }

//    public static String getCaptcha(String key) {
//        Object kaptcha = getSessionAttribute(key);
//        if (kaptcha == null) {
//            throw new RRException("验证码已失效");
//        }
//        getSession().removeAttribute(key);
//        return kaptcha.toString();
//    }

}
