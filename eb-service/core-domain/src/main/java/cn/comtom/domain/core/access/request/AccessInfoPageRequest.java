package cn.comtom.domain.core.access.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccessInfoPageRequest extends CriterionRequest implements Serializable {

    @ApiModelProperty(value = "关联的消息id")
    private String relatedEbmId;

    @ApiModelProperty(value = "接入节点")
    private String nodeId;

    @ApiModelProperty(value = "时间类型编码")
    private String eventType;

    @ApiModelProperty(value = "事件级别")
    private String severity;

    @ApiModelProperty(value = "信息类型")
    private String infoType;

    @ApiModelProperty(value = "信息名称")
    private String infoTitle;

    @ApiModelProperty(value = "信息级别")
    private String infoLevel;


}
