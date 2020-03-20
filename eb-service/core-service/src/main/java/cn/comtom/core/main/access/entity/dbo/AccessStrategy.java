package cn.comtom.core.main.access.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_access_strategy")
public class AccessStrategy implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name = "strategyId")
    @ApiModelProperty(value = "主键")
    private String strategyId ;

    @Column(name = "strategyType")
    @ApiModelProperty(value = "策略类型")
    @NotEmpty(message = "strategyType 不能为空")
    private Integer strategyType;

    @Column(name = "playTime")
    @ApiModelProperty(value = "播发时间")
    private Date playTime;

    @Column(name = "vStartTime")
    @ApiModelProperty(value = "有效开始日期")
    private Date vStartTime;

    @Column(name = "vOverTime")
    @ApiModelProperty(value = "有效结束日期")
    private Date vOverTime;

    @Column(name = "weekMask")
    @ApiModelProperty(value = "周掩码(位运算)")
    private Integer weekMask;

    @Column(name = "infoId")
    @ApiModelProperty(value = "关联节目Id")
    private String infoId;
}
