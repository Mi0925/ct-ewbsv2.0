package cn.comtom.domain.system.sysuserrole.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SysUserRoleAddBatchRequest {

    @ApiModelProperty(value ="用户ID")
    @NotEmpty(message = "userId 不能为空")
    private String userId;

    @ApiModelProperty(value ="角色ID")
    @NotEmpty(message = "roleIds 不能为空")
    private List<String> roleIds;


}
