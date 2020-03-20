package cn.comtom.domain.core.program.request;

import cn.comtom.domain.core.fw.CriterionRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProgramPageRequest extends CriterionRequest implements Serializable {

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "节目类型")
	private Integer programType;

	@ApiModelProperty(name = "节目名称")
	private String programName;

	@ApiModelProperty(name = "节目级别")
	private Integer programLevel;

	@ApiModelProperty(name = "节目内容类型")
	private String contentType;

	@ApiModelProperty(name = "审核结果")
	private Integer auditResult;

	@ApiModelProperty(name = "节目状态")
	private Integer state;

	@ApiModelProperty(name = "事件分类")
	private String ebmEventType;

	@ApiModelProperty(name = "发布机构名称")
	private String ebrName;

	@ApiModelProperty(name = "发布机构ID")
	private String ebrId;

	@ApiModelProperty(name = "创建人")
	private String createUser;

	@ApiModelProperty(name = "创建开始时间")
	private String startCreateTime;

	@ApiModelProperty(name = "创建结束时间")
	private String endStartCreateTime;

}