package cn.comtom.system.main.plan.entity.dbo;

import cn.comtom.system.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Data
@Table(name = "sys_plan_area")
public class SysPlanArea implements Serializable {
	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "id")
	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "预案ID")
	@Column(name = "planId")
	private String planId;

	@ApiModelProperty(value = "区域编码")
	@Column(name = "areaCode")
	private String areaCode;

	@ApiModelProperty(value = "区域编码名称")
	@Column(name = "areaName")
	private String areaName;
}