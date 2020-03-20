package cn.comtom.core.main.ebm.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebm")
public class Ebm implements Serializable {

    @Id
    @Column(name="ebmId")
    @ApiModelProperty(value = "主键")
    private String ebmId;

    @Column(name = "ebmVersion")
    @ApiModelProperty(value = "广播消息协议版本号")
    private String ebmVersion;

    @Column(name = "relatedEbmId")
    @ApiModelProperty(value = "关联广播消息Id")
    private String relatedEbmId;

    @Column(name = "relatedEbIId")
    @ApiModelProperty(value = "关联广播信息Id")
    private String relatedEbIId;

    @ApiModelProperty(value = "调度方案ID")
    @Column(name = "schemeId")
    private String schemeId;

    @ApiModelProperty(value = "调度流程ID")
    @Column(name = "flowId")
    private String flowId;

    @Column(name = "msgType")
    @ApiModelProperty(value = "消息类型")
    private String msgType;

    @Column(name = "sendName")
    @ApiModelProperty(value = "发布机构名称")
    private String sendName;

    @Column(name = "senderCode")
    @ApiModelProperty(value = "发布机构编码")
    private String senderCode;

    @Column(name = "sendTime")
    @ApiModelProperty(value = "发布时间")
    private Date sendTime;

    @Column(name = "eventType")
    @ApiModelProperty(value = "事件类型编码")
    private String eventType;

    @Column(name = "severity")
    @ApiModelProperty(value = "事件级别")
    private String severity;

    @Column(name = "startTime")
    @ApiModelProperty(value = "播发开始时间")
    private Date startTime;

    @Column(name = "endTime")
    @ApiModelProperty(value = "播发结束时间")
    private Date endTime;

    @Column(name = "msgLanguageCode")
    @ApiModelProperty(value = "语种代码")
    private String msgLanguageCode;

    @Column(name = "msgTitle")
    @ApiModelProperty(value = "消息标题文本")
    private String msgTitle;

    @Column(name = "msgDesc")
    @ApiModelProperty(value = "消息内容文本")
    private String msgDesc;

    @Column(name = "areaCode")
    @ApiModelProperty(value = "覆盖区域编码")
    private String areaCode;

    @Column(name = "programNum")
    @ApiModelProperty(value = "参考业务节目Id")
    private String programNum;

    @Column(name = "ebmState")
    @ApiModelProperty(value = "消息状态")
    private String ebmState;

    @Column(name = "broadcastState")
    @ApiModelProperty(value = "播发状态")
    private Integer broadcastState;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "sendFlag")
    @ApiModelProperty(value = "发送标志")
    private String sendFlag;

    @Column(name = "timeOut")
    @ApiModelProperty(value = "用于标记该条消息是否超过开始时间")
    private Integer timeOut;

    @Column(name = "auditResult")
    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @Column(name = "auditOpinion")
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @Column(name = "auditTime")
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @Column(name = "auditUser")
    @ApiModelProperty(value = "审核人")
    private String auditUser;
    
    @Column(name = "accessState")
    @ApiModelProperty(value = "接入状态")
    private Integer accessState;
    
    @Column(name = "audioPath")
    @ApiModelProperty(value = "音频文件路径")
    private String audioPath;
}
