package cn.comtom.domain.system.sysparam.request;

import cn.comtom.domain.system.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysParamPageRequest extends CriterionRequest {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "参数名")
    private String paramName;

    @ApiModelProperty(value = "参数键")
    private String paramKey;

    @ApiModelProperty(value = "参数值")
    private String paramValue;

    @ApiModelProperty(value = "参数类型")
    private Integer paramType;
}
