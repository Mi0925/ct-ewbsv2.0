package cn.comtom.reso.main.ebr.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebr_platform")
public class EbrPlatform implements Serializable {

    @Id
    @Column(name = "psEbrId")
    @ApiModelProperty(value = "主键")
    private String psEbrId;

    @Column(name = "psUrl")
    @ApiModelProperty(value = "平台网络地址")
    private String psUrl;

    @Column(name = "psEbrName")
    @ApiModelProperty(value = "平台名称")
    private String psEbrName;

    @Column(name = "psState")
    @ApiModelProperty(value = "平台状态")
    private Integer psState;

    @Column(name = "areaCode")
    @ApiModelProperty(value = "平台覆盖区域编码")
    private String areaCode;

    @Column(name = "psType")
    @ApiModelProperty(value = "平台类型")
    private String psType;

    @Column(name = "resType")
    @ApiModelProperty(value = "应急广播资源类型")
    private String resType;

    @Column(name = "subResType")
    @ApiModelProperty(value = "应急广播资源类型")
    private String subResType;

    @Column(name = "parentPsEbrId")
    @ApiModelProperty(value = "父平台资源编号")
    private String parentPsEbrId;

    @Column(name = "psAddress")
    @ApiModelProperty(value = "平台地址")
    private String psAddress;

    @Column(name = "contact")
    @ApiModelProperty(value = "联系人名称")
    private String contact;

    @Column(name = "phoneNumber")
    @ApiModelProperty(value = "联系人电话")
    private String phoneNumber;

    @Column(name = "longitude")
    @ApiModelProperty(value = "平台经度")
    private String longitude;

    @Column(name = "latitude")
    @ApiModelProperty(value = "平台纬度")
    private String latitude;

    @Column(name = "platLevel")
    @ApiModelProperty(value = "平台级别")
    private String platLevel;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "updateTime")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Column(name = "syncFlag")
    @ApiModelProperty(value = "信息是否已同步标识")
    private Integer syncFlag;

    @Column(name = "square")
    @ApiModelProperty(value = "覆盖面积")
    private Double square;

    @Column(name = "population")
    @ApiModelProperty(value = "覆盖人口")
    private Double population;

    @Column(name = "statusSyncFlag")
    @ApiModelProperty(value = "状态是否已同步标识")
    private Integer statusSyncFlag;

    @Column(name = "orgName")
    @ApiModelProperty(value = "单位名称")
    private String orgName;

    @Column(name = "orgType")
    @ApiModelProperty(value = "单位类型")
    private String orgType;

    @Column(name = "remark")
    @ApiModelProperty(value = "单位类型")
    private String remark;
}
