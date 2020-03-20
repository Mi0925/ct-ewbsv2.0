package cn.comtom.domain.core.quartz;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <b>bc_program[bc_program]数据持久化对象</b>
 * 
 * @author zhucanhui
 * @date 2016-12-17 17:08:42
 */
@Data
public class QuartzInfo implements Serializable {
	
	@ApiModelProperty(name = "任务组名称")
	private String jobGroup;
	
	@ApiModelProperty(name = "任务名称")
	private String jobName;

	@ApiModelProperty(name = "任务描述")
	private String description;

	@ApiModelProperty(name = "任务类名")
	private String jobClassName;

	@ApiModelProperty(name = "运行状态")
	private String triggerState;

	@ApiModelProperty(name = "上次执行时间")
	private Date prevFireTime;

	@ApiModelProperty(name = "下次执行时间")
	private Date nextFireTime;

	@ApiModelProperty(name = "cron表达式")
	private String cronExpression;

}