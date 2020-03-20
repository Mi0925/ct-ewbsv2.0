package cn.comtom.domain.system.dict.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author baichun
 * @Date 2019-01-03
 */
@Data
public class SysDictGroupInfo implements Serializable {

    @ApiModelProperty(value = "字典分组ID")
    private String dictGroupId;

    @ApiModelProperty(value = "字典分组名称")
    private String dictGroupName;

    @ApiModelProperty(value = "字典分组编码")
    private String dictGroupCode;

    @ApiModelProperty(value = "字典分组备注")
    private String dictGroupRemark;

    @ApiModelProperty(value = "字典分组状态（1-启用，2-停用）")
    private String dictGroupStatus;
}
