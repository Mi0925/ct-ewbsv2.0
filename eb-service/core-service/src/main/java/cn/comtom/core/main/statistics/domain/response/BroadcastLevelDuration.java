package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播等级时长分布
 */
@Data
public class BroadcastLevelDuration {

    @ApiModelProperty("广播日期")
    private String broadcastDate;

    @ApiModelProperty("I级广播时长")
    private Float oneLevelDuration;

    @ApiModelProperty("II级广播时长")
    private Float twoLevelDuration;

    @ApiModelProperty("III级广播时长")
    private Float threeLevelDuration;

    @ApiModelProperty("IV级广播时长")
    private Float fourLevelDuration;

}
