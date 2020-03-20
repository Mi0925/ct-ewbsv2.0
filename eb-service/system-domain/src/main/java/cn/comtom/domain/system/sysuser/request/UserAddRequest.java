package cn.comtom.domain.system.sysuser.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserAddRequest implements Serializable {


    @ApiModelProperty(value = "登录账户")
    @NotEmpty(message = "account 不能为空")
    private String account;

    @ApiModelProperty(value = "登录密码")
    @NotEmpty(message = "password 不能为空")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    @NotEmpty(message = "userName 不能为空")
    private String userName;

    @ApiModelProperty(value = "创建人")
    private String createrId;

}
