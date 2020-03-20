package cn.comtom.ebs.front.main.oauth2;
import cn.comtom.tools.enums.ErrorCode;

public enum ErrorEnum implements ErrorCode {

    UNKNOWN_EXCEPTION("100", "未知异常"),
    UNKNOWN_ACCOUNT("101", "未知账户"),
    INVALID_TOKEN("102", "失效令牌"),
    MD5_ERROR("104", "MD5摘要异常"),
    ACCOUNT_UNAUTHORIZED("103","账户未授权")

    ;



    /**
     * 异常编码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String errorMsg;

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = BASE_PREFIX + errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getCode() {
        return this.errorCode;
    }

    @Override
    public String getMsg() {

        return this.errorMsg;
    }

}
