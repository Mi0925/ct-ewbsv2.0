package cn.comtom.domain.system.role.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Data
public class SysRoleUpdateRequest implements Serializable {

    @ApiModelProperty(value = "角色ID")
    @NotEmpty(message = "角色ID不能为空")
    private String id;

    @ApiModelProperty(value = "角色名称")
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "角色类型")
    private String type;




}
