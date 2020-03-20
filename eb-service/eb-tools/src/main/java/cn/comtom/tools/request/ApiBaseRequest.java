package cn.comtom.tools.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiBaseRequest {

    @ApiModelProperty(value = "排序字段")
    protected String order;

    @ApiModelProperty(value = "升序或者降序")
    protected String sort ;
}
