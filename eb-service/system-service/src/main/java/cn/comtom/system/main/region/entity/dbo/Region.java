package cn.comtom.system.main.region.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "sys_region")
public class Region implements Serializable {

    @Id
    @Column(name = "areaCode")
    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    @Column(name = "areaName")
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @Column(name = "parentAreaCode")
    @ApiModelProperty(value = "上级区域编码")
    private String parentAreaCode;

    @Column(name = "areaLevel")
    @ApiModelProperty(value = "区域行政级别")
    private String areaLevel;

    @Column(name = "areaSquare")
    @ApiModelProperty(value = "区域面积")
    private String areaSquare;

    @Column(name = "areaPopulation")
    @ApiModelProperty(value = "区域人口")
    private String areaPopulation;

    @Column(name = "centerLongitude")
    @ApiModelProperty(value = "应急中心经度")
    private String centerLongitude;

    @Column(name = "centerLatitude")
    @ApiModelProperty(value = "应急中心纬度")
    private String centerLatitude;

}
