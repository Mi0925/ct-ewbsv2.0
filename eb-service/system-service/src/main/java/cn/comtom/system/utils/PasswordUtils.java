package cn.comtom.system.utils;

import org.apache.shiro.crypto.hash.Sha256Hash;

public class PasswordUtils {

    /**
     * 获取加密后的密码
     * @param password
     * @param salt
     * @return
     */
    public static String getEncryptedPassword(String password,String salt){
        return new Sha256Hash(password, salt).toHex();
    }

    /**
     * 密码是否匹配
     * @param matchString
     * @param salt
     * @param password
     * @return
     */
    public static boolean matchPassword(String matchString,String salt,String password){
        String pwd = getEncryptedPassword(matchString,salt);
        return password.equals(pwd);
    }
}
