package cn.comtom.domain.system.plan.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysPlanAreaInfo implements Serializable {

	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "预案ID")
	private String planId;

	@ApiModelProperty(value = "区域编码")
	private String areaCode;

	@ApiModelProperty(value = "区域编码名称")
	private String areaName;

}