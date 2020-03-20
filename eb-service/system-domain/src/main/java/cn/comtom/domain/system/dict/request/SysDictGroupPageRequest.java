package cn.comtom.domain.system.dict.request;

import cn.comtom.domain.system.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author baichun
 * @Date 2018-10-29 8:54
 */
@Data
public class SysDictGroupPageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "字典分组名称")
    private String dictGroupName;

    @ApiModelProperty(value = "字典分组编码")
    private String dictGroupCode;

    @ApiModelProperty(value = "字典分组状态：1-启用，2-停用")
    private String dictGroupStatus;


}
