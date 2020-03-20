package cn.comtom.system.main.params.entity.dbo;

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
@Table(name = "sys_params")
public class SysParams implements Serializable {

    /**
     * 用户ID
     */
    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name="id")
    private String id;

    @Column(name="param_name")
    private String paramName;

    @Column(name="param_value")
    private String paramValue;

    @Column(name="param_key")
    private String paramKey;

    @Column(name="description")
    private String description;

    @Column(name="order_num")
    private Integer orderNum;

    @Column(name="param_type")
    private Integer paramType;

}
