package cn.comtom.tools.enums;

/**
 * 所有状态字段枚举
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum StateDictEnum {



    /**
     * 用户状态枚举
     */
    USER_STATUS_NORMAL("1","启用"),
    USER_STATUS_DISABLE("0","停用"),


    DICT_STATUS_NORMAL("1","启用"),
    DICT_STATUS_DISABLE("2","停用"),

    SYNC_STATUS_NOT("1","未同步"),
    SYNC_STATUS_YES("2","已同步"),

    /**
     * 接入节点状态
     */
    ACCESS_NODE_STATUS_NORMAL("1","启用"),
    ACCESS_NODE_STATUS_DISABLE("0","停用"),

    /**
     * 预案状态枚举
     */
    PLAN_STATUS_NORMAL("1","正常"),
    PLAN_STATUS_DELETE("0","删除"),

    /**
     * 文件状态枚举
     */
    FILE_STATUS_NORMAL("1","正常"),
    FILE_STATUS_DELETE("0","删除"),

    AUDIT_STATUS_NOT_YET("0","未审核"),
    AUDIT_STATUS_PASS("1","审核通过"),
    AUDIT_STATUS_REFUSED("2","审核不通过"),

    /**
     * 调度分发状态
     */
    DISPATCH_STATE_READY("0","待调度"),
    DISPATCH_STATE_SUCCESS("1","已调度"),
    DISPATCH_STATE_FAILED("2","调度失败"),
    DISPATCH_STATE_CANCEL("3","取消"),


    /**
     * EBM消息状态枚举 初始
     */
    EBM_STATE_INIT("0","初始"),
    EBM_STATE_CREATE("1","创建"),
    EBM_STATE_SEND_SUCCESS("2","发送成功"),
    EBM_STATE_SEND_FAILED("3","发送失败"),
    EBM_STATE_CANCEL("9","取消"),

    EBM_BROADCAST_STATE_INIT("0","未处理"),
    EBM_BROADCAST_STATE_READY("1","等待播发"),
    EBM_BROADCAST_STATE_DOING("2","播发中"),
    EBM_PLAY_STATE_SUCCESS("3","播发成功"),
    EBM_BROADCAST_STATE_FAILED("4","播发失败"),
    EBM_PLAY_STATE_CANCEL("5","播发取消"),


    /**
     * EBM消息状态枚举 初始
     */
    EBD_STATE_CREATE("1","创建"),
    EBD_STATE_SEND_SUCCESS("2","发送成功"),
    EBD_STATE_SEND_FAILED("3","发送失败"),
    EBD_STATE_CANCEL("4","取消"),




    EBR_RESOURCE_STATE_RUN("1","运行"),
    EBR_RESOURCE_STATE_STOP("2","停止"),
    EBR_RESOURCE_STATE_FAULT("3","故障"),
    EBR_RESOURCE_STATE_RECOVER("4","故障恢复"),
    EBR_RESOURCE_STATE_PLAYING("5","播发中"),


    /**
     * 方案状态枚举 新建
     */
    SCHEME_STATE_CREATE("1","新建"),
    /**
     * 方案状态枚举 提交
     */
    SCHEME_STATE_COMMIT("2","提交"),
    /**
     * 方案状态枚举 取消
     */
    SCHEME_STATE_CANCEL("3","取消"),


    /**
     * 节目状态
     */
    PROGRAM_STATE_CREATE("1","新建"),
    PROGRAM_STATE_COMMIT("2","提交"),
    PROGRAM_STATE_CANCEL("3","取消"),


    PROGRAM_UNIT_STATE_CREATE("1","创建"),
    PROGRAM_UNIT_STATE_DONE("2","完成"),
    PROGRAM_UNIT_STATE_CANCEL("3","取消"),
    PROGRAM_UNIT_STATE_DELETE("4","删除"),



    ;

    private String key;

    private String desc;


    StateDictEnum(String key, String desc) {
        this.key = key;
        this.desc =desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {

        return this.desc;
    }
}
