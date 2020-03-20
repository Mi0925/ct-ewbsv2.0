package cn.comtom.domain.system.sysparam.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guomao
 * @Date 2018-10-29 8:54
 */
@Data
public class SysParamsInfo implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "参数名")
    private String paramName;

    @ApiModelProperty(value = "参数值")
    private String paramValue;

    @ApiModelProperty(value = "参数键")
    private String paramKey;

    @ApiModelProperty(value = "参数说明")
    private String description;

    @ApiModelProperty(value = "参数排序")
    private Integer orderNum;

    @ApiModelProperty(value = "参数类型")
    private Integer paramType;


}
