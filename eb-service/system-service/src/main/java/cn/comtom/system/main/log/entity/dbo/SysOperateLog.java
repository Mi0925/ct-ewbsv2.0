package cn.comtom.system.main.log.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="sys_operate_log")
public class SysOperateLog implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="logId")
    private String logId;

    @Column(name="operator")
    private String operator;

    @Column(name="module")
    private String module;

    @Column(name="operation")
    private String operation;

    @Column(name="description")
    private String description;

    @Column(name="logTime")
    private Date logTime;

    @Column(name="serviceName")
    private String serviceName;

    @Column(name="clientIp")
    private String clientIp;
}
