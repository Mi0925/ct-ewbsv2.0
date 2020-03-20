package cn.comtom.domain.system.plan.info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysPlanMatchInfo implements Serializable {

	@ApiModelProperty(value = "主键,预案ID")
	private String planId;

	@ApiModelProperty(value = "预案名称")
	private String planName;

	@ApiModelProperty(value = "事件类型编码")
	private String eventType;

	@ApiModelProperty(value = "事件类型")
	private String eventTypeName;

	@ApiModelProperty(value = "预案流程类型")
	private String flowType;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "预案状态")
	private String status;

	@ApiModelProperty(value = "预案流程名称")
	private String flowTypeName;

	@ApiModelProperty(value = "预案关联资源")
	List<SysPlanResoRefInfo> refList;

	@ApiModelProperty(value = "关联事件级别信息")
	@NotEmpty(message = "aseverityList不能为空")
	List<SysPlanSeverityInfo>  severityList;

	@ApiModelProperty(value = "关联区域信息")
	@NotEmpty(message = "areaList不能为空")
	List<SysPlanAreaInfo>  areaList;

	@ApiModelProperty(value = "预案关联的区域总数")
	private Integer areaCount;
	
	@ApiModelProperty(value = "信息源ebrId")
	private String srcEbrId;

}