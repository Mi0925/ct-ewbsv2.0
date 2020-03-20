package cn.comtom.core.main.ebd.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="bc_ebd_response")
public class EbdResponse implements Serializable {
    @Id
    @ApiModelProperty(value = "业务数据类型")
    @Column(name = "ebdId")
    private String ebdId;

    @ApiModelProperty(value = "业务数据类型")
    @Column(name = "ebdType")
    private String ebdType;

    @ApiModelProperty(value = "业务数据包版本")
    @Column(name = "ebdVersion")
    private String ebdVersion;

    @ApiModelProperty(value = "来源对象资源Id")
    @Column(name = "ebdSrcEbrId")
    private String ebdSrcEbrId;

    @ApiModelProperty(value = "业务数据包生成时间")
    @Column(name = "ebdTime")
    private Date ebdTime;

    @ApiModelProperty(value = "关联业务数据包编号")
    @Column(name = "relatedEbdId")
    private String relatedEbdId;

    @ApiModelProperty(value = "状态码")
    @Column(name = "resultCode")
    private String resultCode;

    @ApiModelProperty(value = "状态描述")
    @Column(name = "resultDesc")
    private String resultDesc;

    @ApiModelProperty(value = "业务数据包标识")
    @Column(name = "sendFlag")
    private String sendFlag;


}
