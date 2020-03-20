package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AccessAuditRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "infoId 不能为空")
    private String infoId;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核人")
    private String auditor;


}
