package cn.comtom.system.main.resource.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "sys_resource")
public class SysResource implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="pid")
    private String pid;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private String type;

    @Column(name="icon")
    private String icon;

    @Column(name="perms")
    private String perms;

    @Column(name="url")
    private String url;

    @Column(name="order_num")
    private Integer orderNum;
}
