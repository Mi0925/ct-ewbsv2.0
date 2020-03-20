package cn.comtom.tools.response;

import cn.comtom.tools.enums.BasicError;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiEntityResponse<T> extends ApiResponse {

    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "返回数据")
    private T data;

    public T getData() {
        return data;
    }

    public ApiEntityResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static ApiEntityResponse error(String code, String msg) {
        ApiEntityResponse response = new ApiEntityResponse();
        response.setSuccessful(false);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }



    public static ApiEntityResponse ok(String msg) {
        ApiEntityResponse response = new ApiEntityResponse();
        response.setSuccessful(true);
        response.setCode(BasicError.OK_KEY_VALUE.getCode());
        response.setMsg(msg);
        return response;
    }

    public static <T> ApiEntityResponse<T> ok(T data) {
        ApiEntityResponse response = ok("OK");
        response.setData(data);
        return response;
    }

}
