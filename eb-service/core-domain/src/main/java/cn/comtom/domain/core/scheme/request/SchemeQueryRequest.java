package cn.comtom.domain.core.scheme.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SchemeQueryRequest extends CriterionRequest implements Serializable {

    @ApiModelProperty(value = "方案名称")
    private String schemeTitle;

    @ApiModelProperty(value = "方案ID")
    private String schemeId;

    @ApiModelProperty(value = "广播类型，4-日常广播 ，1-应急广播")
    private String bcType;

    @ApiModelProperty(value = "流程状态")
    private String flowState;

    @ApiModelProperty(value = "流程状态")
    private String flowStage;

    @ApiModelProperty(value = "审核状态")
    private String auditResult;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "广播开始时间")
    private String startTime;

    @ApiModelProperty(value = "广播结束时间")
    private String endTime;

    @ApiModelProperty(value = "已匹配的预案ID")
    private String planId;


}
