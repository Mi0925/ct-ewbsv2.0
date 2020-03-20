package cn.comtom.domain.core.ebd.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author:WJ
 * @date: 2019/1/3 0003
 * @time: 下午 2:08
 */
@Data
public class EbdPageRequest extends CriterionRequest implements Serializable {

    @ApiModelProperty(value = "数据包编号")
    private String ebdId;

    @ApiModelProperty(value = "数据包来源单位")
    private String ebdSrcEbrId;

    @ApiModelProperty(value = "发送目标资源")
    private String ebdDestEbrId;

    @ApiModelProperty(value = "接收开始时间")
    private String startReceiveTime;

    @ApiModelProperty(value = "接收结束时间")
    private String endReceiveTime;

    @ApiModelProperty(value = "发送开始时间")
    private String startSendTime;

    @ApiModelProperty(value = "发送结束时间")
    private String endSendTime;

    @ApiModelProperty(value = "数据包标识（1：接收 2：发送）")
    private String sendFlag;
}

