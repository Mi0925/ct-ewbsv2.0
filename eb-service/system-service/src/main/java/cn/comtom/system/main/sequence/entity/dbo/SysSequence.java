package cn.comtom.system.main.sequence.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="sys_sequence")
public class SysSequence {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private String type;

    @Column(name="prefix")
    private String prefix;

    @Column(name="start")
    private String start;

    @Column(name="step")
    private String step;

    @Column(name="cur_value")
    private String curValue;

    @Column(name="status")
    private String status;

    @Column(name="connector")
    private String connector;

    @Column(name="suffix")
    private String suffix;

    @Column(name="db_seq_name")
    private String dbSeqName;

    @Column(name="max_value")
    private String maxValue;

    @Column(name="is_circul")
    private String isCircul;

    @Column(name="min_value")
    private String minValue;

    @Column(name="is_leftpad")
    private String isLeftpad;

    @Column(name="format_value")
    private String formatValue;

    @Column(name="remark")
    private String remark;









}
