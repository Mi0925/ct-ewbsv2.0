package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/6
 * @desc 应急资源在线时长统计
 */
@Data
public class EmergencyResOnlineCount {

    @ApiModelProperty("应急资源名称")
    private String emergencyName;

    @ApiModelProperty("在线时长（单位：天）")
    private Double onlineDuration;

}
