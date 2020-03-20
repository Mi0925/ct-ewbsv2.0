package cn.comtom.domain.core.access.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccessRecordPageRequest extends CriterionRequest implements Serializable {

    @ApiModelProperty(value = "接入节点")
    private String nodeId;

    @ApiModelProperty(value = "接入状态")
    private String status;

    @ApiModelProperty(value = "信息类型")
    private String type;

    @ApiModelProperty(value = "接收开始时间")
    private String startReceiveTime;

    @ApiModelProperty(value = "接收结束时间")
    private String endReceiveTime;
}
