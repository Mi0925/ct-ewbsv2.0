package cn.comtom.system.main.dict.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author guomao
 * @Date 2018-10-29 8:54
 */
@Data
@Table(name = "sys_dict")
public class SysDict implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="dict_id")
    private String dictId;

    @Column(name="dict_group_id")
    private String dictGroupId;

    @Column(name="dict_group_code")
    private String dictGroupCode;

    @Column(name="dict_key")
    private String dictKey;

    @Column(name="dict_value")
    private String dictValue;

    @Column(name="dict_remark")
    private String dictRemark;

    @Column(name="dict_status")
    private String dictStatus;

    @Column(name="order_num")
    @ApiModelProperty(value = "排序号")
    private Integer orderNum;
}
