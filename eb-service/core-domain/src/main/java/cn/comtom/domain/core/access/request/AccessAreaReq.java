package cn.comtom.domain.core.access.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccessAreaReq implements Serializable {

    @ApiModelProperty(value = "信息编码")
    private String infoId;

    @ApiModelProperty(value = "区域编码")
    private String areaCode;

}
