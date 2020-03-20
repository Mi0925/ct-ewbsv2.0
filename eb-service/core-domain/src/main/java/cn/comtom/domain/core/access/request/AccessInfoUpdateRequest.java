package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class AccessInfoUpdateRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "infoId 不能为空！")
    private String infoId;

    @ApiModelProperty(value = "时间类型编码")
    private String eventType;

    @ApiModelProperty(value = "事件级别")
    private String severity;

    @ApiModelProperty(value = "播发时间")
    private Date startTime;

    @ApiModelProperty(value = "播发结束时间")
    private Date endTime;

    @ApiModelProperty(value = "信息类型")
    private String infoType;

    @ApiModelProperty(value = "信息名称")
    private String infoName;

    @ApiModelProperty(value = "信息级别")
    private String infoLevel;

    @ApiModelProperty(value = "信息内容")
    private String infoContent;

    @ApiModelProperty(value = "语种代码")
    private String msgLanguageCode;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核时间")
    private String auditTime;

    @ApiModelProperty(value = "审核人")
    private String auditor;


}
