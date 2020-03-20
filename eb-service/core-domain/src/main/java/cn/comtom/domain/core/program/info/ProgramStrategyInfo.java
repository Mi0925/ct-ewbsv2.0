package cn.comtom.domain.core.program.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProgramStrategyInfo implements Serializable {

	@ApiModelProperty(name = "策略ID，主键")
	private String strategyId;

	@ApiModelProperty(name = "策略类型")
	private Integer strategyType;

	@ApiModelProperty(name = "播发日期")
	private String playTime;

	@ApiModelProperty(name = "有效开始日期")
	private String startTime;

	@ApiModelProperty(name = "有效结束日期")
	private String overTime;

	@ApiModelProperty(name = "周掩码（位运算）")
	private Integer weekMask;

	@ApiModelProperty(name = "节目编码")
	private String programId;
	
	@ApiModelProperty(name = "策略关联节目时间段")
	private List<ProgramTimeInfo> timeList;



}