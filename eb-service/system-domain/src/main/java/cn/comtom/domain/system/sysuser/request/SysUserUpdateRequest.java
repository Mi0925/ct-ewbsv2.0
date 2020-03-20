package cn.comtom.domain.system.sysuser.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SysUserUpdateRequest {

    @ApiModelProperty(value = "用户ID")
    @NotEmpty(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "登录账户")
    private String account;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "删除标识")
    private Boolean deleteFlag;
}
