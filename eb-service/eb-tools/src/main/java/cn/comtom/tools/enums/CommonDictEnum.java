package cn.comtom.tools.enums;

/**
 * 其他无分类枚举
 *
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum CommonDictEnum {

    EBM_DEFAULT_USER("admin", "默认用户"),
    EBM_DEFAULT_VERSION("1", "默认用户"),


    FLAG_SEND("2", "发送"),
    FLAG_RECEIVE("1", "接收"),


    EBM_VERSION_INIT("1", "初始版本"),


    EBM_TIMEOUT_TRUE("2", "已超时"),
    EBM_TIMEOUT_FALSE("1", "未超时"),

    EBM_AUTO_AUDIT_TRUE("1", "自动审核"),
    EBM_AUTO_AUDIT_FALSE("0", "不自动审核"),
    EBM_DISPATCH_INFO_AUTO_AUDIT_FALSE("0", "不自动审核"),
    EBM_DISPATCH_INFO_AUTO_AUDIT_TRUE("1", "自动审核"),

    FILE_DEFAULT_LIBRARY_ID("90515713fac7433c8e7af2ba3743f39c", "默认文件夹ID"),
    PROGRAM_ADMIN_ROLE("9facb6ebbed64942b0e445965a3a0733","节目制播管理员"),



    PROGRAM_PLAY_FLAG_READY("1","未播放"),
    PROGRAM_PLAY_FLAG_ACTING("2","正在播放"),
    PROGRAM_PLAY_FLAG_DONE("3","播放完成"),
    PROGRAM_PLAY_FLAG_CANCEL("4","播放取消"),

    EBR_CHANNEL_1("1","消息下发通道1-广科院协议"),
    EBR_CHANNEL_2("2","消息下发通道2-http协议"),
    ;

    private String key;

    private String desc;


    CommonDictEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {

        return this.desc;
    }
}
