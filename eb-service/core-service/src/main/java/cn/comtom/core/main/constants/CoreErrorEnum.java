package cn.comtom.core.main.constants;

import cn.comtom.tools.enums.ErrorCode;

/**
 * 系统模块错误
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum CoreErrorEnum  implements ErrorCode {
    /**
     * 用户密码数据不存在
     */
    QUERY_NO_DATA("1000","查询数据为空"),
    REQUIRED_PARAMS_EMPTY("1002","必要参数不能为空"),

    FILE_NOT_EXSITS("1001","文件不存在"),

    PARAMS_NOT_ALL_EMPTY("1005","参数不能都为空"),
    ;


    private String code;

    private String msg;


    CoreErrorEnum(String code,String msg) {
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
