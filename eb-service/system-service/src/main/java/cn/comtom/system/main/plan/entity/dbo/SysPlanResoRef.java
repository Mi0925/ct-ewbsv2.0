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
@Table(name = "sys_plan_reso_ref")
public class SysPlanResoRef implements Serializable {
	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "refId")
	@ApiModelProperty(value = "主键")
	private String refId;

	@ApiModelProperty(value = "预案ID")
	@Column(name = "planId")
	private String planId;

	@ApiModelProperty(value = "播出资源类型")
	@Column(name = "reso_type")
	private String resoType;

	@ApiModelProperty(value = "播出资源编码")
	@Column(name = "reso_code")
	private String resoCode;

	@ApiModelProperty(value = "播出资源名称")
	@Column(name = "reso_name")
	private String resoName;
}