package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 应急资源分布
 */
@Data
public class ResourceDistribution {

    @ApiModelProperty("资源名称")
    private String resName;

    @ApiModelProperty("资源状态：1 运行，2 离线，3 故障，4 故障恢复，5 播放中")
    private Integer resStatus;

    @ApiModelProperty("安装经度")
    private Float longitude;

    @ApiModelProperty("安装纬度")
    private Float latitude;

}
