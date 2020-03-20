package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播等级的广播频次
 */
@Data
public class BroadcastLevelFrequency {

    @ApiModelProperty("广播日期")
    private String broadcastDate;

    @ApiModelProperty("I级广播数量")
    private Integer oneLevelCount;

    @ApiModelProperty("II级广播数量")
    private Integer twoLevelCount;

    @ApiModelProperty("III级广播数量")
    private Integer threeLevelCount;

    @ApiModelProperty("IV级广播数量")
    private Integer fourLevelCount;

}
