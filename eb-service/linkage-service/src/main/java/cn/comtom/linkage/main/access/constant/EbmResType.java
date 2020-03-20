package cn.comtom.linkage.main.access.constant;

/**
 * @author:WJ
 * @date: 2018/12/6 0006
 * @time: 下午 5:24
 */
public enum EbmResType {

    EBRPS("EBRPS","应急广播平台"),
    EBRAS("EBRPS","适配器"),
    EBRST("EBRST","台站信息");

    private String type;
    private String msg;

    EbmResType(String type, String msg) {
        this.type=type;
        this.msg=msg;
    }


    public String getType() {
        return type;
    }

}
