package cn.comtom.domain.system.plan.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysPlanSeverityInfo implements Serializable {


	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "预案ID")
	private String planId;

	@ApiModelProperty(value = "事件级别")
	private Integer severity;

	@ApiModelProperty(value = "事件级别名称")
	private String severityName;
}