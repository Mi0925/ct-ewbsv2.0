package cn.comtom.tools.enums;

/**
 * 最基础的StringKeyValue
 * @author guomao
 */
public enum BasicError implements ErrorCode {

    /**
     * 操作成功
     */
    OK_KEY_VALUE("000","操作成功!"),

    /**
     * 内部服务器错误
     */
    INNER_ERROR("500","内部服务器错误!"),

    /**
     * 远程服务异常
     */
    FEIGN_ERROR("520","远程服务异常"),

    /**
     * 远程服务失败
     */
    API_ERROR("521","远程服务失败"),

    /**
     * 对象不存在
     */
    NOT_EXIST("601","对象不存在"),

    /**
     * 操作失败
     */
    OPT_FALID_VALUE("604","操作失败"),

    /**
     * 参数验证失败
     */
    VALID_ERROR("602","参数验证失败"),

    /**
     * 唯一性验证失败
     */
    UNIQUE_ERROR("603","唯一性验证失败"),

    /**
     * 未知异常
     */
    ERROR_KEY_VALUE("999","未知异常"),

    /**
     * FTP文件或目录不存在
     */
    NO_FTP_FILE_ERROR("604", "FTP文件或目录不存在");

    /**
     * 编码
     */
    private String code;

    private String msg;


    BasicError(String code,String msg) {
        this.code = BASE_PREFIX + code;
        this.msg=msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {

        return this.msg;
    }

}
