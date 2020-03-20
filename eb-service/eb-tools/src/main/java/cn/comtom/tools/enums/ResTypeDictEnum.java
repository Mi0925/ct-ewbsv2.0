package cn.comtom.tools.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 系统资源类型枚举
 *
 * @author baichun
 * @Date 2019-05-13 10:57
 */
public enum ResTypeDictEnum {

    SYS_RES_TYPE_SRC("00", "消息来源单位"),
    SYS_RES_TYPE_PLATFORM("01", "应急广播平台"),
    SYS_RES_TYPE_TV("02", "广播电视频率频道播出系统"),
    SYS_RES_TYPE_STATION("03", "传输覆盖台站/前端"),


    SYS_RES_TYPE_SRC_DEFAULT("0001", "消息来源单位"),

    SYS_RES_TYPE_PLATFORM_COMMON("0101", "通用平台"),
    SYS_RES_TYPE_PLATFORM_CREATE("0102", "制作播发系统"),
    SYS_RES_TYPE_PLATFORM_CONTROL("0103", "调度控制系统"),
    SYS_RES_TYPE_PLATFORM_EVALUATE("0104", "效果监测评估系统"),

    SYS_RES_TYPE_TV_BROADCAST("0201", "广播电视台"),
    SYS_RES_TYPE_TV_RADIO("0202", "电台"),
    SYS_RES_TYPE_TV_STATION("0203", "电视台"),

    SYS_RES_TYPE_STATION_FM("0301", "调频广播"),
    SYS_RES_TYPE_STATION_CDT("0302", "有线数字电视"),
    SYS_RES_TYPE_STATION_TDT("0303", "地面数字电视"),
    SYS_RES_TYPE_STATION_CT("0304", "有线模拟电视"),
    SYS_RES_TYPE_STATION_TT("0305", "地面模拟电视"),
    SYS_RES_TYPE_STATION_MW("0306", "中波广播"),
    SYS_RES_TYPE_STATION_SW("0307", "短波广播"),
    SYS_RES_TYPE_STATION_DBS("0308", "直播卫星"),
    SYS_RES_TYPE_STATION_SATV("0309", "卫星电视"),
    SYS_RES_TYPE_STATION_MMTV("0310", "移动多媒体广播电视"),
    SYS_RES_TYPE_STATION_DAB("0311", "数字音频广播"),
    SYS_RES_TYPE_STATION_MEB("0312", "机动应急广播"),
    SYS_RES_TYPE_STATION_IPTV("0313", "IPTV"),
    SYS_RES_TYPE_STATION_EBS("0314", "应急广播大喇叭系统"),

    SYS_SUB_RES_TYPE_PLATFORM("01", "本级平台/机构/系统"),
    SYS_SUB_RES_TYPE_ADAPTER("02", "应急广播适配器"),
    SYS_SUB_RES_TYPE_TRANS_DEV("03", "传输覆盖播出设备"),
    SYS_SUB_RES_TYPE_TERMINAL("04", "终端"),
    SYS_SUB_SOURCE_TYPE_STATION("05", "台站"),

    ;

    private String key;

    private String desc;


    ResTypeDictEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {

        return this.desc;
    }
    
    public static String getDescByKey(String code){
        for(ResTypeDictEnum typeEnum : ResTypeDictEnum.values()){
          if(StringUtils.equals(code, typeEnum.getKey())){
            return typeEnum.getDesc();
          }
        }
        return "";
    }
}
