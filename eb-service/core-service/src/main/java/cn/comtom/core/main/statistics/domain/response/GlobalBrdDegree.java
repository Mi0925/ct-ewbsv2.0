package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播数量统计类
 */
@Data
public class GlobalBrdDegree {

    @ApiModelProperty("日常广播数量")
    private Integer dailyDegree;

    @ApiModelProperty("应急广播数量")
    private Integer emergencyDegree;

    @ApiModelProperty("一级事件广播数量")
    private Integer oneLevelDegree;

    @ApiModelProperty("二级事件广播数量")
    private Integer twoLevelDegree;

    @ApiModelProperty("三级事件广播数量")
    private Integer threeLevelDegree;

    @ApiModelProperty("四级事件广播数量")
    private Integer fourLevelDegree;
}
