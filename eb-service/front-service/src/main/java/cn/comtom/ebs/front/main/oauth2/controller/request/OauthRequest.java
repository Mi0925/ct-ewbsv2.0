package cn.comtom.ebs.front.main.oauth2.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class OauthRequest {


    @NotBlank
    @JSONField(ordinal = 3)
    @ApiModelProperty(notes = "登录名", required = true)
    private String username;

    @NotBlank
    @JSONField(ordinal = 4)
    @ApiModelProperty(notes = "密码", required = true, position = 1)
    private String password;

    @NotBlank
    @JSONField(ordinal = 5)
    @ApiModelProperty(notes = "授权平台", required = true, position = 2, example = "front")
    private String clientType;
}
