package cn.comtom.domain.system.plan.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysPlanDelRequest {

    @ApiModelProperty(value = "预案Id")
    @NotEmpty(message = "预案Id不能为空")
    private String planId;


}
