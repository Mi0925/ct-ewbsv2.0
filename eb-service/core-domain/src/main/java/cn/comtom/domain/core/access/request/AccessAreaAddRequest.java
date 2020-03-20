package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccessAreaAddRequest implements Serializable {

    @ApiModelProperty(value = "信息编码")
    private String infoId;

    @ApiModelProperty(value = "区域编码")
    private String areaCode;
}
