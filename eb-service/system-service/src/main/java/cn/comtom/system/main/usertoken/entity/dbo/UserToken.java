package cn.comtom.system.main.usertoken.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author guomao
 * @Date 2018-10-29 8:54
 */
@Data
@Table(name = "user_token")
public class UserToken implements Serializable {

    /**
     * 用户ID
     */
    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="token_id")
    private String tokenId;

    @Column(name="user_id")
    private String userId;

    @Column(name="client_type")
    private String clientType;

    @Column(name="access_token")
    private String accessToken;

    @Column(name="access_expire_time")
    private Date accessExpireTime;

    @Column(name="refresh_token")
    private String refreshToken;

    @Column(name="refresh_expire_time")
    private Date refreshExpireTime;


}
