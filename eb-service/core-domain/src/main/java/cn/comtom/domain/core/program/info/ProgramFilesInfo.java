package cn.comtom.domain.core.program.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b>bc_program_files[bc_program_files]数据持久化对象</b>
 * 
 * @author zhucanhui
 * @date 2016-12-17 17:08:43
 */
@Data
public class ProgramFilesInfo implements Serializable {

	@ApiModelProperty(name = "主键")
	private String id;

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "文件ID")
	private String fileId;

	@ApiModelProperty(name = "文件名")
	private String fileName;

	@ApiModelProperty(name = "文件时长")
	private Integer secondLength;


}