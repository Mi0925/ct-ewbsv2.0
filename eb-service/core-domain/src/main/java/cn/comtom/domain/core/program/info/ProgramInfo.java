package cn.comtom.domain.core.program.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <b>bc_program[bc_program]数据持久化对象</b>
 * 
 * @author zhucanhui
 * @date 2016-12-17 17:08:42
 */
@Data
public class ProgramInfo implements Serializable {

	@ApiModelProperty(name = "节目编号")
	private String programId;

	@ApiModelProperty(name = "节目类型")
	private Integer programType;

	@ApiModelProperty(name = "节目名称")
	private String programName;

	@ApiModelProperty(name = "节目级别")
	private Integer programLevel;

	@ApiModelProperty(name = "节目创建时间")
	private Date createTime;

	@ApiModelProperty(name = "节目更新时间")
	private Date updateTime;

	@ApiModelProperty(name = "审核状态")
	private Integer auditResult;

	@ApiModelProperty(name = "审核意见")
	private String auditOpinion;

	@ApiModelProperty(name = "审核时间")
	private Date auditTime;

	@ApiModelProperty(name = "审核人")
	private String auditUser;

	@ApiModelProperty(name = "节目内容类型")
	private Integer contentType;

	@ApiModelProperty(name = "语种代码")
	private String languageCode;

	@ApiModelProperty(name = "创建人")
	private String createUser;

	@ApiModelProperty(name = "节目状态")
	private Integer state;

	@ApiModelProperty(name = "事件分类")
	private String ebmEventType;

	@ApiModelProperty(name = "事件描述")
	private String ebmEventDesc;

	@ApiModelProperty(name = "发布机构名称")
	private String ebrName;

	@ApiModelProperty(name = "发布机构ID")
	private String ebrId;

	@ApiModelProperty(name = "节目关联区域列表")
	private List<ProgramAreaInfo> areaList;

	@ApiModelProperty(name = "节目关联文件列表")
	private List<ProgramFilesInfo> filesList;

	@ApiModelProperty(name = "节目策略")
	private ProgramStrategyInfo programStrategy;

	@ApiModelProperty(name = "节目关联内容")
	private ProgramContentInfo programContent;
	
	@ApiModelProperty(name = "演练类别")
	private Integer drillType;


}