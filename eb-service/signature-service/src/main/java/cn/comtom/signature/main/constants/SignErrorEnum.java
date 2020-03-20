package cn.comtom.signature.main.constants;

import cn.comtom.tools.enums.ErrorCode;

/**
 * 系统模块错误
 * @Date 2018-11-19 10:57
 */
public enum SignErrorEnum implements ErrorCode {
    /**
     * 参数错误
     */
    PARAMS_ERROR("1000","参数错误"),

    /**
     * 签名验证失败
     */
    VERIFY_SIGN_FAILURE("1001","签名验证失败"),

	/**
     * 导入证书失败
     */
    IMPORT_CERT_FAILURE("1002","导入证书失败"),
    
    /**
     * 导入证书链失败
     */
    IMPORT_CERTH_FAILURE("1003","导入证书链失败");
    
    private String code;

    private String msg;


    SignErrorEnum(String code, String msg) {
        this.code = "COR-" + code;
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
