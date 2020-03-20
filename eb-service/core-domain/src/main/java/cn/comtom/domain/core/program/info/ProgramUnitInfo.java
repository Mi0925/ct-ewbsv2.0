package cn.comtom.domain.core.program.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author baichun
 * @date 2019-04-08
 */
@Data
public class ProgramUnitInfo implements Serializable {

	@ApiModelProperty(name = "主键")
	private String id;

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "节目名称")
	private String programName;

	@ApiModelProperty(name = "播放日期")
	private Date playDate;

	@ApiModelProperty(name = "开始时间")
	private String startTime;

	@ApiModelProperty(name = "结束时间")
	private String endTime;

	@ApiModelProperty(name = "审核状态")
	private String auditResult;

	@ApiModelProperty(name = "审核意见")
	private String auditOpinion;

	@ApiModelProperty(name = "审核时间")
	private Date auditTime;

	@ApiModelProperty(name = "审核人")
	private String auditUser;

	@ApiModelProperty(name = "状态")
	private String state;

	@ApiModelProperty(name = "时间段ID")
	private String timeId;

	@ApiModelProperty(name = "关联节目信息")
	private ProgramInfo programInfo;

	@ApiModelProperty(name = "节目单播放标识，1-未播放，2-正在播放，3-已播放")
	private String playFlag;

	@ApiModelProperty(name = "时间是否有冲突")
	private Boolean conflicting;

}