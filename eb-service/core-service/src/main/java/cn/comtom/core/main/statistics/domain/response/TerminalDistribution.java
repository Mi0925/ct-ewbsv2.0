package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 终端分布图
 */
@Data
public class TerminalDistribution {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("终端总数")
    private Integer totalCount;

    @ApiModelProperty("终端在线数")
    private Integer onlineCount;

}
