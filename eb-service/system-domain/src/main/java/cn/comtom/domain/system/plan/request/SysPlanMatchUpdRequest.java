package cn.comtom.domain.system.plan.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Data
public class SysPlanMatchUpdRequest implements Serializable {

	@ApiModelProperty(value = "预案Id",required = true)
	@NotEmpty(message = "预案Id不能为空")
	private String planId;

	@ApiModelProperty(value = "事件类型",required = true)
	@NotEmpty(message = "事件类型不能为空")
	private String eventType;

	@ApiModelProperty(value = "预案名称",required = true)
	@NotEmpty(message = "预案名称不能为空")
	private String planName;

	@ApiModelProperty(value = "预案流程类型",required = true)
	@NotEmpty(message = "处理流程不能为空")
	private String flowType;

	@ApiModelProperty(value = "预案状态")
	private String status;

	@ApiModelProperty(value = "关联资源信息",required = true)
	@NotEmpty(message = "使用资源不能为空")
	List<SysPlanResoRefAddRequest>  refList;

	@ApiModelProperty(value = "关联事件级别信息",required = true)
	@NotEmpty(message = "事件级别不能为空")
	List<SysPlanSeverityAddRequest>  severityList;

	@ApiModelProperty(value = "关联区域信息",required = true)
	List<SysPlanAreaAddRequest>  areaList;
	
	@ApiModelProperty(value = "信息源ebrId")
	private String srcEbrId;

}