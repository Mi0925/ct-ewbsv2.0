package cn.comtom.domain.core.program.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author baichun
 * @date 2019-04-08
 */
@Data
public class ProgramUnitAuditRequest implements Serializable {

	@ApiModelProperty(name = "主键")
	@NotEmpty(message = "ids不能为空")
	private List<String> ids;

	@ApiModelProperty(name = "审核状态")
	private String auditResult;

	@ApiModelProperty(name = "审核意见")
	private String auditOpinion;

	@ApiModelProperty(name = "审核时间")
	private Date auditTime;

	@ApiModelProperty(name = "审核人")
	private String auditUser;

}