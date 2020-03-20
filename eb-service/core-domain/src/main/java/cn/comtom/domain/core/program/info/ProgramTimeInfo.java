package cn.comtom.domain.core.program.info;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b>bc_program_time[bc_program_time]数据持久化对象</b>
 * 
 * @author zhucanhui
 * @date 2016-12-17 17:08:43
 */
@Data
public class ProgramTimeInfo implements Serializable {

	@ApiModelProperty(name = "主键")
	private String timeId;

	@ApiModelProperty(name = "开始时间")
	private String startTime;

	@ApiModelProperty(name = "结束时间")
	private String overTime;

	@ApiModelProperty(name = "持续时间")
	private Integer durationTime;

	@ApiModelProperty(name = "关联策略Id")
	private String strategyId;

	@ApiModelProperty(name = "处理标识（1-已处理，2-未处理）")
	private Integer handleFlag;
	
	@ApiModelProperty(name = "月中的某天")
	private Integer dayOfMonth;


}