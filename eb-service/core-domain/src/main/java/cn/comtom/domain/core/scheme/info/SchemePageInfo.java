package cn.comtom.domain.core.scheme.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SchemePageInfo extends SchemeInfo implements Serializable {

    @ApiModelProperty(value = "节目名称/关联信息名称")
    private String msgTitle;

    @ApiModelProperty(value = "流程状态")
    private String flowState;

    @ApiModelProperty(value = "流程阶段")
    private String flowStage;

    @ApiModelProperty(value = "广播类型")
    private String bcTypeName;

    @ApiModelProperty(value = "事件级别")
    private String severity;

    @ApiModelProperty(value = "来源单位名称")
    private String sendName;

    @ApiModelProperty(value = "开始播放时间")
    private Date startTime;

}
