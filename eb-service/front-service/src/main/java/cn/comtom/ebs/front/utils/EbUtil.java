package cn.comtom.ebs.front.utils;

public class EbUtil {

    public static String getAreaCodeFromEbrId(String ebrId) {
        if(ebrId.length() < 4){
            return null;
        }
        String areaCode = null;
        String areaCodeAll = ebrId.substring(1, 13);
        if (areaCodeAll.endsWith("0000000000")) {
            areaCode = areaCodeAll.substring(0, 2);
        } else if (areaCodeAll.endsWith("00000000")) {
            areaCode = areaCodeAll.substring(0, 4);
        } else if (areaCodeAll.endsWith("000000")) {
            areaCode = areaCodeAll.substring(0, 6);
        } else if (areaCodeAll.endsWith("000")) {
            areaCode = areaCodeAll.substring(0, 9);
        } else {
            areaCode = areaCodeAll;
        }
        return areaCode;
    }
}
