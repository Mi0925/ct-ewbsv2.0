package cn.comtom.domain.core.program.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author baichun
 * @date 2019-04-08
 */
@Data
public class ProgramUnitUpdateRequest implements Serializable {

	@ApiModelProperty(name = "主键")
	@NotEmpty(message = "id不能为空")
	private String id;

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "播放日期")
	private Date playDate;

	@ApiModelProperty(name = "开始时间")
	private String startTime;

	@ApiModelProperty(name = "结束时间")
	private String endTime;

	@ApiModelProperty(name = "状态")
	private String state;

}