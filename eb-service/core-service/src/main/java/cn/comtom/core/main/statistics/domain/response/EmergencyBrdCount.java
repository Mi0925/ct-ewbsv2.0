package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/6
 * @desc 应急资源广播情况统计
 */
@Data
public class EmergencyBrdCount {

    @ApiModelProperty("应急资源名称")
    private String emergencyName;

    @ApiModelProperty("广播数量")
    private Integer broadcastCount;

    @ApiModelProperty("广播时长")
    private Double broadcastDuration;

}
