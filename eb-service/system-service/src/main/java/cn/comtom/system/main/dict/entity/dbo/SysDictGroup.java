package cn.comtom.system.main.dict.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author baichun
 * @Date 2019-01-03
 */
@Data
@Table(name = "sys_dict_group")
public class SysDictGroup implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="dict_group_id")
    private String dictGroupId;

    @Column(name="dict_group_name")
    private String dictGroupName;

    @Column(name="dict_group_code")
    private String dictGroupCode;

    @Column(name="dict_group_remark")
    private String dictGroupRemark;

    @Column(name="dict_group_status")
    private String dictGroupStatus;
}
