package cn.comtom.system.main.plan.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "sys_plan_match")
public class SysPlanMatch implements Serializable {

	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "planId")
	@ApiModelProperty(value = "主键,预案ID")
	private String planId;

	@ApiModelProperty(value = "事件类型")
	@Column(name = "eventType")
	private String eventType;

	@ApiModelProperty(value = "预案名称")
	@Column(name = "plan_name")
	private String planName;

	@ApiModelProperty(value = "预案流程类型")
	@Column(name = "flow_type")
	private String flowType;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "create_time")
	private Date createTime;

	@ApiModelProperty(value = "预案状态")
	@Column(name = "status")
	private String status;
	
	@ApiModelProperty(value = "信息源ebrId")
	@Column(name = "src_ebr_id")
	private String srcEbrId;
}