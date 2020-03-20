package cn.comtom.domain.core.ebd.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EbdResponsePageRequest extends CriterionRequest implements Serializable {


    @ApiModelProperty(value = "业务数据类型")
    private String ebdType;

    @ApiModelProperty(value = "业务数据包版本")
    private String ebdVersion;

    @ApiModelProperty(value = "来源对象资源Id")
    private String ebdSrcEbrId;

    @ApiModelProperty(value = "关联业务数据包编号")
    private String relatedEbdId;

    @ApiModelProperty(value = "状态码")
    private String resultCode;

    @ApiModelProperty(value = "业务数据包标识")
    private String sendFlag;


}
