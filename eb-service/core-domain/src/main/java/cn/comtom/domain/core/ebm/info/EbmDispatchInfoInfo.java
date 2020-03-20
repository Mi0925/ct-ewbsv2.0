package cn.comtom.domain.core.ebm.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EbmDispatchInfoInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String infoId;

    @ApiModelProperty(value = "ebmId")
    private String ebmId;

    @ApiModelProperty(value = "消息标题")
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
