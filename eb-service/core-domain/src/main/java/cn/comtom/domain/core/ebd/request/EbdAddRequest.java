package cn.comtom.domain.core.ebd.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EbdAddRequest implements Serializable {


    @ApiModelProperty(value = "数据包版本")
    private String ebdVersion;

    @ApiModelProperty(value = "业务数据类型")
    private String ebdType;

    @ApiModelProperty(value = "数据包名称")
    private String ebdName;

    @ApiModelProperty(value = "来源对象资源Id")
    private String ebdSrcEbrId;

    @ApiModelProperty(value = "目标对象资源Id")
    private String ebdDestEbrId;

    @ApiModelProperty(value = "生成时间")
    private Date ebdTime;

    @ApiModelProperty(value = "关联业务数据包Id")
    private String relateEbdId;

    @ApiModelProperty(value = "业务数据包来源对象URL")
    private String ebdSrcUrl;

    @ApiModelProperty(value = "业务数据包发送时间")
    private Date ebdSendTime;

    @ApiModelProperty(value = "业务数据包接收时间")
    private Date ebdRecvTime;

    @ApiModelProperty(value = "业务数据包标识")
    private String sendFlag;

    @ApiModelProperty(value = "业务数据包状态")
    private String ebdState;

    @ApiModelProperty(value = "关联会话Id")
    private String flowId;

    @ApiModelProperty(value = "关联EBM消息Id")
    private String ebmId;

    @ApiModelProperty(value = "主键")
    private String ebdId;

    @ApiModelProperty(value = "ebd数据包文件ID")
    private String fileId;

}
