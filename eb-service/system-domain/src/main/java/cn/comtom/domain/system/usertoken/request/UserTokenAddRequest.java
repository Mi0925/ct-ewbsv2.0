package cn.comtom.domain.system.usertoken.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guomao
 * @Date 2018-10-29 8:54
 */
@Data
public class UserTokenAddRequest implements Serializable {


    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "客户端类型")
    private String clientType;

    @ApiModelProperty(value = "客户访问Token")
    private String accessToken;

    @ApiModelProperty(value = "访问过期时间")
    private Date accessExpireTime;

    @ApiModelProperty(value = "刷新Token")
    private String refreshToken;

    @ApiModelProperty(value = "刷新过期时间")
    private Date refreshExpireTime;


}
