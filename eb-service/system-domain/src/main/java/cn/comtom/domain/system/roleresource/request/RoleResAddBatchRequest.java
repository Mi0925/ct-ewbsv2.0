package cn.comtom.domain.system.roleresource.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Data
public class RoleResAddBatchRequest implements Serializable {

    @ApiModelProperty(value = "角色ID")
    @NotEmpty(message = "resourceId 不能为空")
    private List<String> resourceIdList;

    @ApiModelProperty(value = "系统资源ID")
    @NotEmpty(message = "roleId 不能为空")
    private String roleId;


}
