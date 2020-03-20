package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播时长分布
 */
@Data
public class BroadcastDuration {

    @ApiModelProperty("广播总时长")
    private Float totalDuration;

    @ApiModelProperty("日常广播总时长")
    private Float dailyDuration;

    @ApiModelProperty("应急广播总时长")
    private Float emergencyDuration;

    @ApiModelProperty("广播类型时长分布")
    private List<BroadcastTypeDuration> typeDurations;

    @ApiModelProperty("广播等级时长分布")
    private List<BroadcastLevelDuration> levelDurations;

}
