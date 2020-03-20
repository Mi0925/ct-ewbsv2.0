package cn.comtom.domain.core.program.info;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProgramContentInfo implements Serializable {

	@ApiModelProperty(name = "主键")
	private String id;

	@ApiModelProperty(name = "节目Id")
	private String programId;

	@ApiModelProperty(name = "节目内容")
	private String content;

	@ApiModelProperty(name = "文件时长")
	private Integer secondLength;


}