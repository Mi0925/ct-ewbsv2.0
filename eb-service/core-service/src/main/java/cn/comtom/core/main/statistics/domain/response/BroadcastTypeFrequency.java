package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播类型频次
 */
@Data
public class BroadcastTypeFrequency {

    @ApiModelProperty("广播日期")
    private String broadcastDate;

    @ApiModelProperty("该日期内的广播总数")
    private Integer totalBroadcast;

    @ApiModelProperty("该日期内的日常广播总数")
    private Integer dailyBroadcast;

    @ApiModelProperty("该日期内的应急广播总数")
    private Integer emergencyBroadcast;

}
