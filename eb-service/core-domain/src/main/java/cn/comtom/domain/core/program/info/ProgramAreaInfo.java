package cn.comtom.domain.core.program.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProgramAreaInfo implements Serializable {

	@ApiModelProperty(name = "主键")
	private String id;

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "关联区域编码")
	private String areaCode;

	@ApiModelProperty(name = "关联区域名称")
	private String areaName;


}