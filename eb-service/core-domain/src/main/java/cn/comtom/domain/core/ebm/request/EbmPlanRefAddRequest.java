package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EbmPlanRefAddRequest implements Serializable {


    @ApiModelProperty(value = "ebmId")
    @NotEmpty(message = "ebmId 不能为空！")
    private String ebmId;

    @ApiModelProperty(value = "预案名称")
    @NotEmpty(message = "planName 不能为空！")
    private String planName;

    @ApiModelProperty(value = "事件级别")
    @NotEmpty(message = "severity 不能为空！")
    private String severity;

    @ApiModelProperty(value = "目标区域")
    @NotEmpty(message = "targetAreas 不能为空！")
    private String targetAreas;

    @ApiModelProperty(value = "流程类型")
    @NotEmpty(message = "flowType 不能为空！")
    private String flowType;
}
