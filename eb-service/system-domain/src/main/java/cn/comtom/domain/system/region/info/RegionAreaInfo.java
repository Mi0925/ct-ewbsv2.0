package cn.comtom.domain.system.region.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegionAreaInfo implements Serializable {

	
	private String id;
	
    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "上级区域编码")
    private String parentAreaCode;

    @ApiModelProperty(value = "区域行政级别")
    private String areaLevel;

    @ApiModelProperty(value = "区域面积")
    private String areaSquare;

    @ApiModelProperty(value = "区域人口")
    private String areaPopulation;

    @ApiModelProperty(value = "应急中心经度")
    private String centerLongitude;

    @ApiModelProperty(value = "应急中心纬度")
    private String centerLatitude;

    @ApiModelProperty(value = "是否是父节点")
    private Boolean parent;

}
