package cn.comtom.domain.system.plan.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysPlanAreaAddRequest implements Serializable {

	@ApiModelProperty(value = "区域编码")
	private String areaCode;

	@ApiModelProperty(value = "区域编码名称")
	private String areaName;
}