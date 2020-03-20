package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播频次分布
 */
@Data
public class BroadcastFrequency {

    @ApiModelProperty("广播总数")
    private Integer totalBroadcast;

    @ApiModelProperty("日常广播")
    private Integer dailyBroadcast;

    @ApiModelProperty("应急广播")
    private Integer emergencyBroadcast;

    @ApiModelProperty("广播类型的广播数量")
    private List<BroadcastTypeFrequency> typeFrequencies;

    @ApiModelProperty("广播等级的广播数量")
    private List<BroadcastLevelFrequency> levelFrequencies;

}
