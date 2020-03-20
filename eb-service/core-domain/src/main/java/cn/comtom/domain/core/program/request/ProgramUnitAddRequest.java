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
public class ProgramUnitAddRequest implements Serializable {


	@ApiModelProperty(name = "节目编号")
	@NotEmpty(message = "programId不能为空")
	private String programId;

	@NotEmpty(message = "playDate不能为空")
	@ApiModelProperty(name = "播放日期")
	private Date playDate;

	@NotEmpty(message = "startTime不能为空")
	@ApiModelProperty(name = "开始时间")
	private String startTime;

	@NotEmpty(message = "endTime不能为空")
	@ApiModelProperty(name = "结束时间")
	private String endTime;

	@ApiModelProperty(name = "时间段ID")
	private String timeId;


}