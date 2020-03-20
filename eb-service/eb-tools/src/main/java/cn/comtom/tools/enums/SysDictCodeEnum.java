package cn.comtom.tools.enums;

/**
 * @author:WJ
 * @date: 2019/1/4 0004
 * @time: 上午 8:53
 */
public enum SysDictCodeEnum {

    ACCESS_RECORD_STATUS("acess_record_status","接入记录状态"),
    EBD_TYPE("ebd_type","协议类型"),
    EBD_BROADCAST_STATE("ebd_broadcast_state","播发状态"),
    SEVERITY_DICT("SEVERITY_DICT","事件类型"),
    PLAN_MATH_FLOW("plan_math_flow","预案流程"),
    EVENT_TYPE_DICT("EVENT_TYPE_DICT","事件类型"),
    MSG_LANG_DICT("MSG_LANG_DICT","语种"),
    MSG_TYPE_DICT("MSG_TYPE_DICT","预警消息类型"),
    AUXILIARY_TYPE_DICT("AUXILIARY_TYPE_DICT","应急消息辅助数据类型"),
    FLOW_STATE_DICT("FLOW_STATE_DICT","流程状态"),
    BROADCAST_TYPE_DICT("BROADCAST_TYPE_DICT","广播类型"),
    DISPATCH_STATUS("dispatch_status","消息分发状态"),
    RES_STATUS_DICT("RES_STATUS_DICT","资源状态"),
    EBR_TYPE("EBR_TYPE","播出资源类型枚举");

    private String code;

    private String desc;

    SysDictCodeEnum(String code, String desc) {
        this.code = code;
        this.desc =desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
