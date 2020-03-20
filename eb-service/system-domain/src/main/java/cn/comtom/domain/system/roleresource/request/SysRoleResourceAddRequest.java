package cn.comtom.domain.system.roleresource.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Data
public class SysRoleResourceAddRequest implements Serializable {

    @ApiModelProperty(value = "角色ID")
    @NotEmpty(message = "roleId 不能为空")
    private String roleId;

    @ApiModelProperty(value = "系统资源ID")
    @NotEmpty(message = "resourceId 不能为空")
    private String resourceId;


}
