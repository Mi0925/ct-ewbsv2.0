package cn.comtom.system.main.user.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
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
@Table(name = "sys_user")
public class SysUser implements Serializable {

    /**
     * 用户ID
     */
    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="user_id")
    private String userId;

    @Column(name="account")
    private String account;

    @Column(name="password")
    private String password;

    @Column(name="user_name")
    private String userName;

    @Column(name="status")
    private Integer status;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="creater_id")
    private String createrId;

    @ApiModelProperty(value = "盐值")
    @Column(name="salt")
    private String salt;

    @Column(name="delete_flag")
    private Boolean deleteFlag;


}
