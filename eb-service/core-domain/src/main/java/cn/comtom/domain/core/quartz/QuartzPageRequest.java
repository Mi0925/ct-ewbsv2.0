package cn.comtom.domain.core.quartz;

import java.io.Serializable;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuartzPageRequest extends CriterionRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4769222422560282124L;

	@ApiModelProperty(name = "任务名称")
	private String jobName;

	@ApiModelProperty(name = "任务描述")
	private String description;

	@ApiModelProperty(name = "任务类名")
	private String jobClassName;
}