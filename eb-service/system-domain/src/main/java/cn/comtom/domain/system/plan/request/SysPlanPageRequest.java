package cn.comtom.domain.system.plan.request;

import cn.comtom.domain.system.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysPlanPageRequest extends CriterionRequest {

    @ApiModelProperty(value = "预案名称")
    private String planName;

    @ApiModelProperty(value = "事件级别")
    private String severity;

}
