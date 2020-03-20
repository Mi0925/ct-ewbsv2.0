package cn.comtom.domain.core.ebm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author:WJ
 * @date: 2018/12/5 0005
 * @time: 下午 2:32
 */
@Data
public class EbmBrdRecordWhereRequest {

    @ApiModelProperty(value = "数据记录开始时间")
    public Date rptStartTime;

    @ApiModelProperty(value = "数据记录结束时间")
    public Date rptEndTime;

    @ApiModelProperty(value = "播发记录同步标识")
    private Integer syncFlag;

}
