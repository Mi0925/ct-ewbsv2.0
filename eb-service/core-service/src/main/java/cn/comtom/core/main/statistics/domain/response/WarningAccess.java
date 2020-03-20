package cn.comtom.core.main.statistics.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangbingyan
 * @date 2020/1/3
 * @desc 预警接入排名实体类
 */
@Data
public class WarningAccess {

    @ApiModelProperty("预警源名称")
    private String warningSourceName;

    @ApiModelProperty("预警数量")
    private Integer warningNum;

}
