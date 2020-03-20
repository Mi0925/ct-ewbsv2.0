package cn.comtom.core.main.access.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="bc_access_info")
public class Access implements Serializable {
    @Id
    @KeySql(genId = UUIdGenId.class)
    @ApiModelProperty(value = "主键")
    @Column(name = "infoId")
    private String infoId;

    @ApiModelProperty(value = "ebdId")
    @Column(name = "ebdId")
    private String ebdId;

    @ApiModelProperty(value = "relatedEbIId")
    @Column(name = "relatedEbIId")
    private String relatedEbIId;

    @ApiModelProperty(value = "消息标题")
    @Column(name = "infoTitle")
    private String infoTitle;

    @ApiModelProperty(value = "关联的消息id")
    @Column(name = "relatedEbmId")
    private String relatedEbmId;

    @ApiModelProperty(value = "接入节点")
    @NotEmpty(message = "nodeId can not be empty")
    @Column(name = "nodeId")
    private String nodeId;

    @ApiModelProperty(value = "发布时间")
    @Column(name = "sendTime")
    private Date sendTime;

    @ApiModelProperty(value = "时间类型编码")
    @Column(name = "eventType")
    private String eventType;

    @ApiModelProperty(value = "事件级别")
    @Column(name = "severity")
    private String severity;

    @ApiModelProperty(value = "播发时间")
    @Column(name = "startTime")
    private Date startTime;

    @ApiModelProperty(value = "播发结束时间")
    @Column(name = "endTime")
    private Date endTime;

    @ApiModelProperty(value = "信息类型")
    @Column(name = "infoType")
    private String infoType;

    @ApiModelProperty(value = "信息级别")
    @Column(name = "infoLevel")
    private String infoLevel;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "信息内容")
    @Column(name = "infoContent")
    private String infoContent;

    @ApiModelProperty(value = "语种代码")
    @Column(name = "msgLanguageCode")
    private String msgLanguageCode;

    @ApiModelProperty(value = "审核结果")
    @Column(name = "auditResult")
    private String auditResult;

    @ApiModelProperty(value = "审核意见")
    @Column(name = "auditOpinion")
    private String auditOpinion;

    @ApiModelProperty(value = "审核时间")
    @Column(name = "auditTime")
    private Date auditTime;

    @ApiModelProperty(value = "审核人")
    @Column(name = "auditor")
    private String auditor;


}
