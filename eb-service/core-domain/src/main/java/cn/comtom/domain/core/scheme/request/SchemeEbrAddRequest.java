package cn.comtom.domain.core.scheme.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SchemeEbrAddRequest implements Serializable {


	@ApiModelProperty(value = "关联资源编号")
	private String ebrId;

	@ApiModelProperty(value = "关联调度方案编号")
	private String schemeId;

	@ApiModelProperty(value = "资源类型")
	private String ebrType;

	@ApiModelProperty(value = "资源覆盖区域")
	private String ebrArea;

}