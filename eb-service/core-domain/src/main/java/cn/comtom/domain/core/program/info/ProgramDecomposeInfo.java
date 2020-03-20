package cn.comtom.domain.core.program.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:WJ
 * @date: 2019/4/8 0008
 * @time: 下午 3:45
 */
@Data
public class ProgramDecomposeInfo implements Serializable {

    @ApiModelProperty(name = "节目编号")
    private String programId;

    @ApiModelProperty(name = "节目类型")
    private Integer programType;

    @ApiModelProperty(name = "策略类型")
    private Integer strategyType;

    @ApiModelProperty(name = "内容类型")
    private Integer contentType;

    @ApiModelProperty(name = "播发日期")
    private Date playDate;

    @ApiModelProperty(name = "有效开始日期")
    private Date startDate;

    @ApiModelProperty(name = "有效结束日期")
    private Date endDate;

    @ApiModelProperty(name = "周掩码（位运算）")
    private Integer weekMask;

    @ApiModelProperty(name = "开始时间")
    private String startTime;

    @ApiModelProperty(name = "结束时间")
    private String overTime;

    @ApiModelProperty(name = "持续时间")
    private Integer durationTime;

    @ApiModelProperty(name = "主键")
    private String timeId;
    
    @ApiModelProperty(name = "月中的某天")
    private Integer dayOfMonth;

}
