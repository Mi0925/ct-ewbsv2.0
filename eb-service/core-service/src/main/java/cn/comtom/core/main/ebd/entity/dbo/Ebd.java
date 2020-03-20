package cn.comtom.core.main.ebd.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebd")
public class Ebd implements Serializable {

    @Id
    @Column(name="ebdId")
    @ApiModelProperty(value = "主键")
    private String ebdId;

    @Column(name = "ebdVersion")
    @ApiModelProperty(value = "数据包版本")
    private String ebdVersion;

    @Column(name = "ebdType")
    @ApiModelProperty(value = "业务数据类型")
    private String ebdType;

    @Column(name = "ebdName")
    @ApiModelProperty(value = "数据包名称")
    private String ebdName;

    @Column(name = "ebdSrcEbrId")
    @ApiModelProperty(value = "来源对象资源Id")
    private String ebdSrcEbrId;

    @Column(name = "ebdDestEbrId")
    @ApiModelProperty(value = "目标对象资源Id")
    private String ebdDestEbrId;

    @Column(name = "ebdTime")
    @ApiModelProperty(value = "生成时间")
    private Date ebdTime;

    @Column(name = "relateEbdId")
    @ApiModelProperty(value = "关联业务数据包Id")
    private String relateEbdId;

    @Column(name = "ebdSrcUrl")
    @ApiModelProperty(value = "业务数据包来源对象URL")
    private String ebdSrcUrl;

    @Column(name = "ebdSendTime")
    @ApiModelProperty(value = "业务数据包发送时间")
    private Date ebdSendTime;

    @Column(name = "ebdRecvTime")
    @ApiModelProperty(value = "业务数据包接收时间")
    private Date ebdRecvTime;

    @Column(name = "sendFlag")
    @ApiModelProperty(value = "业务数据包标识")
    private String sendFlag;

    @Column(name = "ebdState")
    @ApiModelProperty(value = "业务数据包状态")
    private String ebdState;

    @Column(name = "flowId")
    @ApiModelProperty(value = "关联会话Id")
    private String flowId;

    @Column(name = "ebmId")
    @ApiModelProperty(value = "关联EBM消息Id")
    private String ebmId;

    @Column(name = "fileId")
    @ApiModelProperty(value = "ebd数据包文件ID")
    private String fileId;

}
