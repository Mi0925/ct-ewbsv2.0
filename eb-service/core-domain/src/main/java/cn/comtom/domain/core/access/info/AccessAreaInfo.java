package cn.comtom.domain.core.access.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccessAreaInfo implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "信息编码")
    private String infoId;

    @ApiModelProperty(value = "区域编码")
    private String areaCode;
}
