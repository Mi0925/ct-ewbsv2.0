package cn.comtom.ebs.front.main.oauth2.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RefreshTokenRequest {
    @ApiModelProperty(required = true, value = "刷新AccessToken")
    private String refreshToken;

    private String userId;
}
