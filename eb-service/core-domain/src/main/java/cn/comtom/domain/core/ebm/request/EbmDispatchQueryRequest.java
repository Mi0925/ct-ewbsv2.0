package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbmDispatchQueryRequest implements Serializable {


    @ApiModelProperty(value = "应急广播平台ID")
    private String psEbrId;

    @ApiModelProperty(value = "播出系统ID")
    private String bsEbrId;

    @ApiModelProperty(value = "适配器资源ID")
    private String asEbrId;

    @ApiModelProperty(value = "播出系统类型")
    private String brdSysType;

    @ApiModelProperty(value = "数据包Id")
    private String ebdId;

    @ApiModelProperty(value = "关联EBM")
    private String ebmId;

    @ApiModelProperty(value = "状态 调度分发状态（0:待调度 1:已调度 2:调度失败）")
    private String state;

}
