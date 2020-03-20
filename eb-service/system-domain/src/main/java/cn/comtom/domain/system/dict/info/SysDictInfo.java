package cn.comtom.domain.system.dict.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author baihcun
 * @Date 2018-10-29 8:54
 */
@Data
public class SysDictInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String dictId;

    @ApiModelProperty(value = "字典分组ID")
    private String dictGroupId;

    @ApiModelProperty(value = "字典分组编码")
    private String dictGroupCode;

    @ApiModelProperty(value = "字典键")
    private String dictKey;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "字典备注")
    private String dictRemark;

    @ApiModelProperty(value = "字典状态：1-启用，2-停用")
    private String dictStatus;

    @ApiModelProperty(value = "排序号")
    private Integer orderNum;


}
