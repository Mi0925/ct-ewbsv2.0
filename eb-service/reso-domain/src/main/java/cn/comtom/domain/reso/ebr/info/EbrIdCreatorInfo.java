package cn.comtom.domain.reso.ebr.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EbrIdCreatorInfo implements Serializable {

    @NotEmpty(message = "id")
    @ApiModelProperty(value = "主键")
    private String id;

    @NotEmpty(message = "sourceLevel不能为空")
    @ApiModelProperty(value = "资源级别")
    private String sourceLevel;

    @NotEmpty(message = "areaCode不能为空")
    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    @NotEmpty(message = "sourceTypeCode不能为空")
    @ApiModelProperty(value = "资源类型码")
    private String sourceTypeCode;

    @NotEmpty(message = "sourceTypeSN不能为空")
    @ApiModelProperty(value = "资源类型序列")
    private String sourceTypeSN;

    @NotEmpty(message = "sourceSubTypeCode不能为空")
    @ApiModelProperty(value = "资源子类型码")
    private String sourceSubTypeCode;

    @NotEmpty(message = "sourceSubTypeSN不能为空")
    @ApiModelProperty(value = "资源子类型序列")
    private String sourceSubTypeSN;

    @NotEmpty(message = "ebrId不能为空")
    @ApiModelProperty(value = "最终生成的EBRID")
    private String ebrId;
}
