package cn.comtom.core.main.access.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "bc_access_time")
public class AccessTime implements Serializable {

    @Id
    @KeySql(genId = UUIdGenId.class)
    @Column(name = "timeId")
    @ApiModelProperty(value = "主键")
    private String timeId ;

    @Column(name = "startTime")
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @Column(name = "overTime")
    @ApiModelProperty(value = "结束时间")
    private String overTime;

    @Column(name = "durationTime")
    @ApiModelProperty(value = "持续时间")
    private Integer durationTime;

    @Column(name = "strategyId")
    @ApiModelProperty(value = "关联节目策略Id")
    private String strategyId;
}
