package cn.comtom.linkage.main.constants;

import cn.comtom.tools.enums.ErrorCode;

/**
 * 联动服务模块错误
 */
public enum LinkageErrorEnum implements ErrorCode {

    /**
     * 查询数据为空
     */
    SYNC_DATA_FAILUE("1000","同步失败");

    private String code;

    private String msg;


    LinkageErrorEnum(String code, String msg) {
        this.code = "LINKAGE-" + code;
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
