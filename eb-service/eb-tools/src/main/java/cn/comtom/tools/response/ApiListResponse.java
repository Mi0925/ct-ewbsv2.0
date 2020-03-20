package cn.comtom.tools.response;

import cn.comtom.tools.enums.BasicError;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiListResponse<T> extends ApiResponse {

    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "返回数据")
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public ApiListResponse<T> setData(List<T> dataList) {
        this.data = dataList;
        return this;
    }

    public static ApiListResponse error(String code, String msg) {
        ApiListResponse response = new ApiListResponse();
        response.setSuccessful(false);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }



    public static ApiListResponse ok(String msg) {
        ApiListResponse response = new ApiListResponse();
        response.setSuccessful(true);
        response.setCode(BasicError.OK_KEY_VALUE.getCode());
        response.setMsg(msg);
        return response;
    }

    public static <T> ApiListResponse<T> ok(List<T> dataList) {
        ApiListResponse response = ok("OK");
        response.setData(dataList);
        return response;
    }

}
