package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播时长统计类
 */
@Data
public class GlobalBrdDuration {

    @ApiModelProperty("日常广播时长")
    private Float dailyDuration;

    @ApiModelProperty("应急广播时长")
    private Float emergencyDuration;

    @ApiModelProperty("一级事件广播时长")
    private Float oneLevelDuration;

    @ApiModelProperty("二级事件广播时长")
    private Float twoLevelDuration;

    @ApiModelProperty("三级事件广播时长")
    private Float threeLevelDuration;

    @ApiModelProperty("四级事件广播时长")
    private Float fourLevelDuration;

}
