package cn.comtom.core.main.program.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author baichun
 * @date 2019-04-08
 */
@Data
@Table(name = "bc_program_unit")
public class ProgramUnit implements Serializable {

	@ApiModelProperty(name = "主键")
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "programId")
	@ApiModelProperty(name = "节目编号")
	private String programId;

	@Column(name = "playDate")
	@ApiModelProperty(name = "播放日期")
	private Date playDate;

	@Column(name = "startTime")
	@ApiModelProperty(name = "开始时间")
	private String startTime;

	@Column(name = "endTime")
	@ApiModelProperty(name = "结束时间")
	private String endTime;

	@Column(name = "auditResult")
	@ApiModelProperty(name = "审核状态")
	private String auditResult;

	@Column(name = "auditOpinion")
	@ApiModelProperty(name = "审核意见")
	private String auditOpinion;

	@Column(name = "auditTime")
	@ApiModelProperty(name = "审核时间")
	private Date auditTime;

	@Column(name = "auditUser")
	@ApiModelProperty(name = "审核人")
	private String auditUser;

	@Column(name = "state")
	@ApiModelProperty(name = "状态")
	private String state;

	@Column(name = "timeId")
	@ApiModelProperty(name = "时间段ID")
	private String timeId;

}