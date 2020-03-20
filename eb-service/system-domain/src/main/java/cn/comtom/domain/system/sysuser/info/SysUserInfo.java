package cn.comtom.domain.system.sysuser.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SysUserInfo {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "登录账户")
    private String account;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createrId;

    @ApiModelProperty(value = "盐值")
    private String salt;

    @ApiModelProperty(value = "删除标识")
    private Boolean deleteFlag;
}
