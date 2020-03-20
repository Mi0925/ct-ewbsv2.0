package cn.comtom.domain.system.dict.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author baichun
 * @Date 2018-10-29 8:54
 */
@Data
public class SysDictGroupUpdateRequest implements Serializable {

    @ApiModelProperty(value = "字典分组ID")
    @NotEmpty(message = "dictGroupId 不能为空！")
    private String dictGroupId;

    @ApiModelProperty(value = "字典分组名称")
    private String dictGroupName;

    @ApiModelProperty(value = "字典分组备注")
    private String dictGroupRemark;
    
    @ApiModelProperty(value = "字典分组状态：1-启用，2-停用")
    private String dictGroupStatus;
}
