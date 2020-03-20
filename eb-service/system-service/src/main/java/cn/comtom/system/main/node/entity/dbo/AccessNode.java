package cn.comtom.system.main.node.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name="sys_access_node")
public class AccessNode implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="platformId")
    private String platformId;

    @Column(name="status")
    private String status;

    @Column(name="ip")
    private String ip;
}
