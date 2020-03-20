package cn.comtom.domain.core.ebm.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EbmPageRequest extends CriterionRequest implements Serializable {

    @ApiModelProperty(value = "ebmID")
    private String ebmId;

    @ApiModelProperty(value = "调度方案ID")
    private String schemeId;

    @ApiModelProperty(value = "调度流程ID")
    private String flowId;

    @ApiModelProperty(value = "消息类型")
    private String msgType;

    @ApiModelProperty(value = "发布机构名称")
    private String sendName;

    @ApiModelProperty(value = "发布机构编码")
    private String senderCode;

    @ApiModelProperty(value = "发布时间")
    private Date sendTime;

    @ApiModelProperty(value = "事件类型编码")
    private String eventType;

    @ApiModelProperty(value = "事件级别")
    private String severity;

    @ApiModelProperty(value = "查询时间区间，起始时间")
    private String startTime;

    @ApiModelProperty(value = "查询时间区间，结束时间")
    private String endTime;

    @ApiModelProperty(value = "消息标题文本")
    private String msgTitle;

    @ApiModelProperty(value = "消息状态")
    private String ebmState;

    @ApiModelProperty(value = "播发状态")
    private Integer broadcastState;

    @ApiModelProperty(value = "发送标志")
    private String sendFlag;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;
}
