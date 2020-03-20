package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/6
 * @desc 预警源广播数量统计
 */
@Data
public class WarningSourceCount {

    @ApiModelProperty("预警源名称")
    private String warnigSourceName;

    @ApiModelProperty("预警源中各类型广播数量")
    private List<BroadcastTypeCount> broadcastTypeCounts;

    @Data
    public static class BroadcastTypeCount {
        @ApiModelProperty("预警类型：1 I级广播，2II级，3 III级，4 IV级，10 日常")
        private Integer broadcastType;

        @ApiModelProperty("预警数量")
        private Integer broadcastCount;
    }

}
