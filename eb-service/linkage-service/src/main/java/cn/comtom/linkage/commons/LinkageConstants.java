package cn.comtom.linkage.commons;

/**
 * Create By wujiang on 2018/11/14
 */
public interface LinkageConstants {

    public static final String RECEIVE= "receive";
    public static final String SEND= "send";
    public static final String MEDIA= "media";
    public static final String FTP_DIR_EBM_DISPATCH= "ebmDispatch";
    public static final String FTP_DIR_EBD_RESPONSE= "ebdResponse";

    public static final String EBMID = "EBMID";
    public static final String EBDID = "EBDID";

    /**
     * 分隔符号
     */
    public static final String SUFFIX_SPLIT = ".";
    public static final String FILE_SPLIT = "/";
    public static final String COMMA_SPLIT = ",";
    public static final String BLANK_SPLIT = " ";

    /**
     * EBD数据包状态
     */
    public static final Integer EBD_STATE_CREATE = 1;
    public static final Integer EBD_STATE_SEND_SUCCESS = 2;
    public static final Integer EBD_STATE_SEND_FAILED = 3;
    public static final Integer EBD_STATE_CANCEL = 9;

    /**
     * 是否进行签名验签
     */
    public static final String VERIFY_SIGN = "is_verify_sign";


}
