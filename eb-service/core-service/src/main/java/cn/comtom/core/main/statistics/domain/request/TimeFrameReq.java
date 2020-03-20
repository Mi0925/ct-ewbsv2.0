package cn.comtom.core.main.statistics.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/3
 * @desc 统计分析查询的时间范围
 */
@Data
public class TimeFrameReq {

    @ApiModelProperty("是否本年度")
    private Boolean isThisYear;

    @ApiModelProperty("是否本月度")
    private Boolean isThisMonth;

    @ApiModelProperty(value = "开始日期，格式：yyyy-MM-dd")
    private String startDate;

    @ApiModelProperty(value = "结束日期，格式：yyyy-MM-dd")
    private String endDate;

}
