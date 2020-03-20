package cn.comtom.domain.core.ebm.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbmPlanRefInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @ApiModelProperty(value = "预案名称")
    private String planName;

    @ApiModelProperty(value = "事件类型")
    private String eventType;

    @ApiModelProperty(value = "流程类型")
    private String flowType;

    @ApiModelProperty(value = "预案状态")
    private String status;

}
