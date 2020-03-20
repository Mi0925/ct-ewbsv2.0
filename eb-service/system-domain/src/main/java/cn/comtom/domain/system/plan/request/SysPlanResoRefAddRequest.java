package cn.comtom.domain.system.plan.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysPlanResoRefAddRequest implements Serializable {


	@ApiModelProperty(value = "播出资源类型")
	private String resoType;

	@ApiModelProperty(value = "播出资源编码")
	private String resoCode;

	@ApiModelProperty(value = "播出资源名称")
	private String resoName;

}