package cn.comtom.ebs.front.main.oauth2.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TokenInfo implements Serializable {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String clientType;
    private Date expireTime;
}
