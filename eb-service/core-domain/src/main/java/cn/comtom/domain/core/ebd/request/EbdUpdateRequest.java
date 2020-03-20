package cn.comtom.domain.core.ebd.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class EbdUpdateRequest implements Serializable {


    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "ebdId 不能为空")
    private String ebdId;

    @ApiModelProperty(value = "数据包版本")
    private String ebdVersion;

    @ApiModelProperty(value = "数据包名称")
    private String ebdName;

    @ApiModelProperty(value = "目标对象资源Id")
    private String ebdDestEbrId;

    @ApiModelProperty(value = "关联业务数据包Id")
    private String relateEbdId;

    @ApiModelProperty(value = "业务数据包发送时间")
    private Date ebdSendTime;

    @ApiModelProperty(value = "业务数据包接收时间")
    private Date ebdRecvTime;

    @ApiModelProperty(value = "业务数据包标识")
    private String sendFlag;

    @ApiModelProperty(value = "业务数据包状态")
    private String ebdState;

}
