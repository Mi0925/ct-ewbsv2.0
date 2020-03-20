package cn.comtom.domain.core.scheme.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SchemeAddRequest implements Serializable {

	@ApiModelProperty(value = "方案标题")
	private String schemeTitle;

	@ApiModelProperty(value = "关联会话编号")
	private String flowId;

	@ApiModelProperty(value = "覆盖面积")
	private Double totalArea;

	@ApiModelProperty(value = "覆盖人口")
	private Double totalPopu;

	@ApiModelProperty(value = "覆盖区域百分比")
	private Integer areaPercent;

	@ApiModelProperty(value = "覆盖人口百分比")
	private Integer popuPercent;

	@ApiModelProperty(value = "审核结果(0:未审核 1:审核通过 2:审核不通过)")
	private String auditResult;

	@ApiModelProperty(value = "审核意见")
	private String auditOpinion;

	@ApiModelProperty(value = "审核时间")
	private Date auditTime;

	@ApiModelProperty(value = "审核人员")
	private String auditUser;

	@ApiModelProperty(value = "方案状态(1:新建 2:提交 3:取消)")
	private String state;

	@ApiModelProperty(value = "关联消息编号")
	private String ebmId;

	@ApiModelProperty(value = "关联节目Id")
	private String programId;

	@ApiModelProperty(value = "关联数据包Id")
	private String ebdId;

	@ApiModelProperty(value = "关联信息Id")
	private String infoId;

	@ApiModelProperty(value = "方案标题")
	private String schemeType;

	@ApiModelProperty(value = "资源覆盖人口")
	private Double ebrPopu;

	@ApiModelProperty(value = "资源覆盖面积")
	private Double ebrArea;

	@ApiModelProperty(value = "已匹配的预案ID")
	private String planId;

}