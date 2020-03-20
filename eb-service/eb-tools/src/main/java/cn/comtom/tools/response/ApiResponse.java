package cn.comtom.tools.response;

import cn.comtom.tools.enums.BasicError;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author guomao
 */
@Data
public class ApiResponse {

    @JSONField(ordinal = 1)
    @ApiModelProperty(value = "是否成功")
    private Boolean successful;

    @JSONField(ordinal = 2)
    @ApiModelProperty(value = "编码")
    private String code;

    @JSONField(ordinal = 3)
    @ApiModelProperty(value = "消息")
    private String msg;

    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "错误辅助信息")
    private Object errorInfo;

    public ApiResponse(){

    }

    public ApiResponse(Boolean successful, String code, String msg) {
        this.successful = successful;
        this.code = code;
        this.msg = msg;
    }

    public static ApiResponse ok() {

        return new ApiResponse(true, BasicError.OK_KEY_VALUE.getCode(), BasicError.OK_KEY_VALUE.getMsg());
    }

    public static ApiResponse ok(String code, String msg) {
        return new ApiResponse(true, code, msg);
    }

    public static ApiResponse error(String code, String msg) {
        return new ApiResponse(false, code, msg);
    }

}
