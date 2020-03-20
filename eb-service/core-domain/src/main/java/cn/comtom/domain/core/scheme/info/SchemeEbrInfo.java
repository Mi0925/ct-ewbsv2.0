package cn.comtom.domain.core.scheme.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SchemeEbrInfo implements Serializable {

	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "关联资源编号")
	private String ebrId;

	@ApiModelProperty(value = "关联资源名")
	private String ebrName;

	@ApiModelProperty(value = "关联调度方案编号")
	private String schemeId;

	@ApiModelProperty(value = "关联调度方案名称")
	private String schemeName;

	@ApiModelProperty(value = "资源类型")
	private String ebrType;

	@ApiModelProperty(value = "资源类型名称")
	private String ebrTypeName;

	@ApiModelProperty(value = "资源覆盖区域")
	private String ebrArea;

	@ApiModelProperty(value = "资源覆盖区域名称")
	private String ebrAreaName;

}