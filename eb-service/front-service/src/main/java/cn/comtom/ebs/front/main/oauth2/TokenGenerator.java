package cn.comtom.ebs.front.main.oauth2;


import cn.comtom.tools.exception.RRException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * token生成器
 */
public class TokenGenerator {

    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

    /** 将byte数组转为16进制字符串
     * @param data
     * @return
     */
    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }

    /** 生成唯一token
     * @return 32位16进制字符串
     */
    public static String generateValue() {
        return generateMD5Value(UUID.randomUUID().toString());
    }

    /** 生成MD5摘要
     * @param param
     * @return
     */
    public static String generateMD5Value(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RRException(ErrorEnum.MD5_ERROR);
        }
    }


}

