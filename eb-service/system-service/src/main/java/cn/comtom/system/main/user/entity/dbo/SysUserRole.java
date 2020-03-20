package cn.comtom.system.main.user.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "sys_user_role")
public class SysUserRole {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name = "id")
    private String id;

    @Column(name ="user_id")
    private String userId;

    @Column(name ="role_id")
    private String roleId;


}
