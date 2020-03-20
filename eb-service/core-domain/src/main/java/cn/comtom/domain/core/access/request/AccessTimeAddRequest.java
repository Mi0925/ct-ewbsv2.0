package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AccessTimeAddRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    private String timeId ;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String overTime;

    @ApiModelProperty(value = "持续时间")
    private Integer durationTime;

    @NotEmpty(message = "strategyId 不能为空")
    @ApiModelProperty(value = "关联节目策略Id")
    private String strategyId;
}
