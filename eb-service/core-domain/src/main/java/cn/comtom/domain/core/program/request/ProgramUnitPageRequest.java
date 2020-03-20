package cn.comtom.domain.core.program.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author baichun
 * @date 2019-04-08
 */
@Data
public class ProgramUnitPageRequest extends CriterionRequest implements Serializable {

	@ApiModelProperty(name = "主键")
	private String id;

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "播放日期")
	private String playDate;

	@ApiModelProperty(name = "播放起始日期")
	private String startDate;

	@ApiModelProperty(name = "播放终止日期")
	private String endDate;

	@ApiModelProperty(name = "开始时间")
	private String startTime;

	@ApiModelProperty(name = "结束时间")
	private String endTime;

	@ApiModelProperty(name = "状态")
	private String state;

	@ApiModelProperty(name = "年份")
	private String year;

	@ApiModelProperty(name = "月份")
	private String month;

}