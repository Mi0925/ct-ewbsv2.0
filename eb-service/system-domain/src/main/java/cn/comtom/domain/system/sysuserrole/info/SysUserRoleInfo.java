package cn.comtom.domain.system.sysuserrole.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserRoleInfo {

    @ApiModelProperty(value ="主键" )
    private String id;

    @ApiModelProperty(value ="用户ID")
    private String userId;

    @ApiModelProperty(value ="角色ID")
    private String roleId;


}
