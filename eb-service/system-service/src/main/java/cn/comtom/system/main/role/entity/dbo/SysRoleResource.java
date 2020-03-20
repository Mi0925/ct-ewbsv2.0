package cn.comtom.system.main.role.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Data
@Table(name = "sys_role_resource")
public class SysRoleResource implements Serializable {

    /**
     * 用户ID
     */
    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="role_id")
    private String roleId;

    @Column(name="resource_id")
    private String resourceId;


}
