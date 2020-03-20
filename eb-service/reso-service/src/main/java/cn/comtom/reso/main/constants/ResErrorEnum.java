package cn.comtom.reso.main.constants;

import cn.comtom.tools.enums.ErrorCode;

/**
 * 系统模块错误
 * @author guomao
 * @Date 2018-10-29 10:57
 */
public enum ResErrorEnum implements ErrorCode {

    /**
     * 查询数据为空
     */
    QUERY_NO_DATA("1000","查询数据为空"),
    /**
     * 文件保存失败
     */
    FILE_SAVE_FAILURE("1001","文件保存失败"),
    /**
     * 文件不存在
     */
    FILE_NOT_EXSITS("1002","文件不存在"),



    /**
     * 文件夹保存失败
     */
    FILE_LIB_SAVE_FAILURE("1003","文件夹保存失败"),
    /**
     * 必要参数为空
     */
    REQUIRED_PARAMS_EMPTY("1004","必要参数为空"),

    PARAMS_NOT_ALL_EMPTY("1005","参数不能都为空"),

    OUT_OF_SEQUENCE_BOUNDS("1006","序列号越界"),



    ;

    private String code;

    private String msg;


    ResErrorEnum(String code, String msg) {
        this.code = "RES-" + code;
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
