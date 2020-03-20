package cn.comtom.domain.reso.ebr.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:liuhy
 * @date: 2019/5/9
 */
@Data
public class StationWhereRequest implements Serializable {

    @ApiModelProperty(value = "平台信息同步标识")
    public Integer syncFlag;

    @ApiModelProperty(value = "平台状态同步标识")
    public Integer statusSyncFlag;

    @ApiModelProperty(value = "数据记录开始时间")
    public String rptStartTime;

    @ApiModelProperty(value = "数据记录结束时间")
    public String rptEndTime;

}
