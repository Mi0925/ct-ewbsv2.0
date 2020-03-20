package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class EbmUpdateRequest {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "ebmId 不能为空")
    private String ebmId;

    @ApiModelProperty(value = "广播消息协议版本号")
    private String ebmVersion;

    @ApiModelProperty(value = "关联广播消息Id")
    private String relatedEbmId;

    @ApiModelProperty(value = "关联广播信息Id")
    private String relatedEbIId;

    @ApiModelProperty(value = "调度流程ID")
    private String flowId;

    @ApiModelProperty(value = "调度方案ID")
    private String schemeId;

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

    @ApiModelProperty(value = "播发开始时间")
    private Date startTime;

    @ApiModelProperty(value = "播发结束时间")
    private Date endTime;

    @ApiModelProperty(value = "语种代码")
    private String msgLanguageCode;

    @ApiModelProperty(value = "消息标题文本")
    private String msgTitle;

    @ApiModelProperty(value = "消息内容文本")
    private String msgDesc;

    @ApiModelProperty(value = "覆盖区域编码")
    private String areaCode;

    @ApiModelProperty(value = "参考业务节目Id")
    private String programNum;

    @ApiModelProperty(value = "发送标志")
    private String sendFlag;

    @ApiModelProperty(value = "消息分发状态")
    private String ebmState;

    @ApiModelProperty(value = "消息播发状态")
    private Integer broadcastState;

    @ApiModelProperty(value = "用于标记该条消息是否超过开始时间")
    private Integer timeOut;
}
