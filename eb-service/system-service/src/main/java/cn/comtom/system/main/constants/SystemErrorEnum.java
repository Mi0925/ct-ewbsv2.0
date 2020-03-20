package cn.comtom.system.main.constants;

import cn.comtom.tools.enums.ErrorCode;

/**
 * 系统模块错误
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum SystemErrorEnum implements ErrorCode {

    /**
     * 查询数据为空
     */
    QUERY_NO_DATA("1000","查询数据为空"),
    QUERY_IS_EXIST("1001","数据已经存在"),
    USER_AUTHORIZE_FAILURE("1003","用户登录认证失败"),
    USER_ACCOUNT_NOT_EXISTS("1004","用户名不存在"),
    USER_ACCOUNT_DELETED("1008","账号已删除"),
    USER_ACCOUNT_DISABLED("1009","账号已停用"),
    USER_PASSWORD_INCORRECT("1005","用户密码不正确"),
    REQUIRED_PARAM_EMPTY("1006","必要参数为空"),
    DELETE_FAILURE_BY_TOKEN_NOT_EXISTS("1007","根据accessToken查询用户token信息为空"),

    OPERATION_ERROR("1011","操作失败"),
    /**
     * Ip不在白名单中
     */
    IP_NOT_IN_WHITE_LIST("1002","Ip不在白名单中");

    private String code;

    private String msg;


    SystemErrorEnum(String code,String msg) {
        this.code = "SYS-" + code;
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
