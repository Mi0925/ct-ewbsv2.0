package cn.comtom.domain.system.roleresource.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysRoleResourceInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "系统资源ID")
    private String resourceId;


}
