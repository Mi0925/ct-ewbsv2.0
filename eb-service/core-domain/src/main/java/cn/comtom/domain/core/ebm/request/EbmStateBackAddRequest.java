package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EbmStateBackAddRequest {


    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @ApiModelProperty(value = "反馈时间")
    private Date backTime;

    @ApiModelProperty(value = "反馈类型")
    private String backType="自动";

    @ApiModelProperty(value = "反馈状态")
    private String backStatus;

    @ApiModelProperty(value = "条目ID")
    private String brdItemId;

}
