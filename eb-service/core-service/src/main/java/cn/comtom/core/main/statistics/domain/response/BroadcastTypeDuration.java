package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播类型时长分布
 */
@Data
public class BroadcastTypeDuration {

    @ApiModelProperty("广播日期")
    private String broadcastDate;

    @ApiModelProperty("该日期广播总时长")
    private Float totalDuration;

    @ApiModelProperty("该日期日常广播总时长")
    private Float dailyDuration;

    @ApiModelProperty("该日期应急广播总时长")
    private Float emergencyDuration;

}
