package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/3
 * @desc 整个系统的应急广播统计
 */
@Data
public class GlobalBroadcast {

    @ApiModelProperty("广播总数量")
    private Integer totalDegree;

    @ApiModelProperty("广播总时长")
    private Float totalHours;

    @ApiModelProperty("各类广播数量")
    private GlobalBrdDegree globalBrdDegree;

    @ApiModelProperty("各类广播时长")
    private GlobalBrdDuration globalBrdDuration;

}
