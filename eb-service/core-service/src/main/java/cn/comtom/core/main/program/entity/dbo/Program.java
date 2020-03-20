package cn.comtom.core.main.program.entity.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * <b>bc_program[bc_program]数据持久化对象</b>
 * 
 * @author baichun
 * @date 2019-03-19 17:08:42
 */
@Table(name = "bc_program")
@Data
public class Program implements Serializable {

	@Id
	@Column(name = "programId")
	@ApiModelProperty(name = "节目编号")
	private String programId;

	@Column(name = "programType")
	@ApiModelProperty(name = "节目类型（1:应急 2:日常 3:演练）")
	private Integer programType;

	@Column(name = "programName")
	@ApiModelProperty(name = "节目名称")
	private String programName;

	@Column(name = "programLevel")
	@ApiModelProperty(name = "节目级别（1,2,3,4,10）")
	private Integer programLevel;

	@Column(name = "createTime")
	@ApiModelProperty(name = "节目创建时间")
	private Date createTime;

	@Column(name = "contentType")
	@ApiModelProperty(name = "节目内容类型")
	private Integer contentType;

	@Column(name = "updateTime")
	@ApiModelProperty(name = "节目更新时间")
	private Date updateTime;

	@Column(name = "auditResult")
	@ApiModelProperty(name = "审核结果")
	private Integer auditResult;

	@Column(name = "auditOpinion")
	@ApiModelProperty(name = "审核意见")
	private String auditOpinion;

	@Column(name = "auditTime")
	@ApiModelProperty(name = "审核时间")
	private Date auditTime;

	@Column(name = "auditUser")
	@ApiModelProperty(name = "审核人")
	private String auditUser;

	@Column(name = "languageCode")
	@ApiModelProperty(name = "语种代码")
	private String languageCode;

	@Column(name = "createUser")
	@ApiModelProperty(name = "创建人员")
	private String createUser;

	@Column(name = "state")
	@ApiModelProperty(name = "节目状态")
	private Integer state;

	@Column(name = "ebmEventType")
	@ApiModelProperty(name = "事件分类")
	private String ebmEventType;

	@Column(name = "ebmEventDesc")
	@ApiModelProperty(name = "事件描述")
	private String ebmEventDesc;

	@Column(name = "ebrName")
	@ApiModelProperty(name = "发布机构名称")
	private String ebrName;

	@Column(name = "ebrId")
	@ApiModelProperty(name = "发布机构代码")
	private String ebrId;
	
	@Column(name = "drillType")
	@ApiModelProperty(name = "演练类别")
	private Integer drillType;




}