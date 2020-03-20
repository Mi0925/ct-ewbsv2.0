package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class EbmDispatchInfoAddRequest implements Serializable {


    @ApiModelProperty(value = "ebmId")
    @NotEmpty(message = "ebmId不能为空")
    private String ebmId;

    @ApiModelProperty(value = "消息标题")
    @NotEmpty(message = "消息标题不能为空")
    private String infoTitle;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "审核人")
    private String auditUser;
}
