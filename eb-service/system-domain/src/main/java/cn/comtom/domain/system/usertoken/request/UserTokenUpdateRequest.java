package cn.comtom.domain.system.usertoken.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public class UserTokenUpdateRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "tokenId 不能为空")
    private String tokenId;

    @ApiModelProperty(value = "客户访问Token")
    private String accessToken;

    @ApiModelProperty(value = "访问过期时间")
    private Date accessExpireTime;

    @ApiModelProperty(value = "刷新Token")
    private String refreshToken;

    @ApiModelProperty(value = "刷新过期时间")
    private Date refreshExpireTime;


}
