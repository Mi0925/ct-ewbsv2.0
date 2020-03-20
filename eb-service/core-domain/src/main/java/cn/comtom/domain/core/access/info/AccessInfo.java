package cn.comtom.domain.core.access.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccessInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String infoId;

    @ApiModelProperty(value = "关联的消息id")
    private String relatedEbmId;

    @ApiModelProperty(value = "接入节点")
    private String nodeId;

    @ApiModelProperty(value = "发布时间")
    private Date sendTime;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "信息内容")
    private String infoContent;

    @ApiModelProperty(value = "语种代码")
    private String msgLanguageCode;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "审核人")
    private String auditor;


}
