package cn.comtom.domain.core.program.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <b>bc_program[bc_program]数据持久化对象</b>
 * 
 * @author baichun
 * @date 2019-03-19
 */
@Data
public class ProgramAuditRequest implements Serializable {


	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "审核状态")
	private Integer auditResult;

	@ApiModelProperty(name = "审核意见")
	private String auditOpinion;

	@ApiModelProperty(name = "审核时间")
	private Date auditTime;

	@ApiModelProperty(name = "审核人")
	private String auditUser;

	@ApiModelProperty(name = "节目状态")
	private Integer state;

}