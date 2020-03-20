package cn.comtom.tools.enums;

/**
 * 所有类型枚举
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum TypeDictEnum {

    /**
     * 文件类型
     */
    FILE_TYPE_TEXT("1","txt"),
    FILE_TYPE_MP3("2","mp3"),
    FILE_TYPE_TAR("3","tar"),
    FILE_TYPE_XML("4","xml"),
    FILE_TYPE_AAC("5","aac"),


    FILE_TYPE_MEDIA("1","媒体文件"),
    FILE_TYPE_TXT("2","文本文件"),


    EBM_MSG_TYPE_PLAY("1","实际播发"),
    EBM_MSG_TYPE_CANCEL("2","取消播发"),
    EBM_MSG_TYPE_PLATFORM_MANOEUVRE("3","平台演练播发"),
    EBM_MSG_TYPE_FRONT_MANOEUVRE("4","前端演练播发"),
    EBM_MSG_TYPE_TERMINAL_MANOEUVRE("5","终端演练播发"),

    EBM_LAN_CODE_ZHO("zho","中文"),

    PROGRAM_CONTENT_TYPE_TEXT("1","文本类型"),
    PROGRAM_CONTENT_TYPE_UPLOAD_FILE("2","上传文件"),
    PROGRAM_CONTENT_TYPE_SELECT_FILE("3","选择曲目"),

    ;

    private String key;

    private String desc;


    TypeDictEnum(String key, String desc) {
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
