package cn.comtom.system.main.role.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "sys_role")
public class SysRole implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="type")
    private String type;

    @Column(name="createTime")
    private Date createTime;

}
