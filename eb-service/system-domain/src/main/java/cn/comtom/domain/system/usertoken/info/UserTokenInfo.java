package cn.comtom.domain.system.usertoken.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guomao
 * @Date 2018-10-29 8:54
 */
@Data
public class UserTokenInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String tokenId;

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

    public boolean valid(){
        if(accessExpireTime == null){
            return false;
        }
        return accessExpireTime.after(new Date());
    }
}
