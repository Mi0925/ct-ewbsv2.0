package cn.comtom.domain.system.role.request;

import cn.comtom.domain.system.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysRolePageRequest extends CriterionRequest {

    @ApiModelProperty(value = "角色名称")
    private String name;
}
