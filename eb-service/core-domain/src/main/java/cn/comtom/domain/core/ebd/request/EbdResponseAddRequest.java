package cn.comtom.domain.core.ebd.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EbdResponseAddRequest implements Serializable {

    @ApiModelProperty(value = "主键")
    private String ebdId;

    @ApiModelProperty(value = "业务数据类型")
    private String ebdType;

    @ApiModelProperty(value = "业务数据包版本")
    private String ebdVersion;

    @ApiModelProperty(value = "来源对象资源Id")
    private String ebdSrcEbrId;

    @ApiModelProperty(value = "业务数据包生成时间")
    private Date ebdTime;

    @ApiModelProperty(value = "关联业务数据包编号")
    private String relatedEbdId;

    @ApiModelProperty(value = "状态码")
    private String resultCode;

    @ApiModelProperty(value = "状态描述")
    private String resultDesc;

    @ApiModelProperty(value = "业务数据包标识")
    private String sendFlag;


}
