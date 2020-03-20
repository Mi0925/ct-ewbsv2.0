package cn.comtom.domain.core.access.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class AccessStrategyInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String strategyId ;

    @ApiModelProperty(value = "策略类型",required = true)
    @NotEmpty(message = "strategyType 不能为空")
    private Integer strategyType;

    @ApiModelProperty(value = "播发时间")
    private Date playTime;

    @ApiModelProperty(value = "有效开始日期")
    private Date vStartTime;

    @ApiModelProperty(value = "有效结束日期")
    private Date vOverTime;

    @ApiModelProperty(value = "周掩码(位运算)")
    private Integer weekMask;

    @NotEmpty(message = "infoId 不能为空")
    @ApiModelProperty(value = "关联节目Id",required = true)
    private String infoId;
}
