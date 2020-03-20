package cn.comtom.ebs.front.common;

import cn.comtom.tools.enums.ErrorCode;

/**
 * 系统模块错误
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum FrontErrorEnum implements ErrorCode {

    /**
     * 查询数据为空
     */
    PLAN_MATH_IS_EXIST("1001","预案已经存在"),
    QUERY_NO_DATA("1000","查询数据为空"),
    USER_AUTHORIZE_FAILURE("1003","用户登录认证失败"),
    USER_ACCOUNT_NOT_EXISTS("1004","用户账号不存在"),
    USER_ACCOUNT_EXISTS("1012","用户账号已存在"),
    USER_PASSWORD_INCORRECT("1005","用户密码不正确"),
    REQUIRED_PARAM_EMPTY("1006","必要参数为空"),
    DELETE_FAILURE_BY_TOKEN_NOT_EXISTS("1007","根据accessToken查询用户token信息为空"),
    CAPTCHA_VERIFY_FAILURE("1008","验证码错误！"),
    CAPTCHA_INVALID("1009","验证码失效！"),
    CAPTCHA_EMPTY("1010","验证码不能为空！"),
    USER_SAVE_FAILURE("1011","用户账号信息保存失败！"),
    USER_ROLE_ADD_FAILURE("1013","用户角色绑定失败！"),
    DELETE_ROLE_FAILURE("1014","删除角色信息失败！"),
    DELETE_ROLE_RESOURCE_FAILURE("1015","删除角色资源绑定关系失败！"),
    DELETE_USER_ROLE_FAILURE("1016","删除用户角色绑定关系失败！"),
    ROLE_SYSRESOURCE_BIND_FAILURE("1017","角色批量绑定系统资源失败！"),
    QUERY_LOCAL_EBR_NOT_EXISTS("1018","查询本级平台信息不存在！"),
    UPDATE_SCHEME_INFO_FAILED("1019","更新方案信息失败！"),
    EBMDISPATCHINFO_IS_NOT_EXIST("2000","消息不存在"),
    DIRECTORY_NOT_FOUND("2002", "文件上传文件夹不存在"),
    /**
     * Ip不在白名单中
     */

    MAP_INIT_NOT_EXIST("2001","地图初始化参数未配置"),

    IP_NOT_IN_WHITE_LIST("1002","Ip不在白名单中"),

    UNSUPPORT_FILE_TYPE("1020","不支持的文件类型"),

    INIT_AREA_ERROR("1022","区域初始化失败"),

    UPLOAD_FILE_ERROR("1021","上传文件出错！"),

    DOWNLOAD_FILE_ERROR("1024","下载文件出错！"),
    FILE_NOTFOUND_ERROR("1025","下载文件不存在！");



    private String code;

    private String msg;


    FrontErrorEnum(String code, String msg) {
        this.code = "FNT-" + code;
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
