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
public class SysDictUpdateRequest implements Serializable {

    @ApiModelProperty(value = "字典ID")
    @NotEmpty(message = "dictId 不能为空！")
    private String dictId;

    @ApiModelProperty(value = "字典分组ID")
    private String dictGroupId;

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
