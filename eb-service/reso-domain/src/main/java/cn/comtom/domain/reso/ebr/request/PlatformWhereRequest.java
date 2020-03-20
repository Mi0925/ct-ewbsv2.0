package cn.comtom.domain.reso.ebr.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author:WJ
 * @date: 2018/12/5 0005
 * @time: 上午 9:31
 */
@Data
public class PlatformWhereRequest implements Serializable {

    @ApiModelProperty(value = "平台信息同步标识")
    public Integer syncFlag;

    @ApiModelProperty(value = "平台状态同步标识")
    public Integer statusSyncFlag;

    @ApiModelProperty(value = "数据记录开始时间")
    public String rptStartTime;

    @ApiModelProperty(value = "数据记录结束时间")
    public String rptEndTime;

}
