package cn.comtom.core.main.scheme.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Data
@Table(name = "bc_scheme_ebr")
public class SchemeEbr implements Serializable {

	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "id")
	@ApiModelProperty(value = "主键")
	private String id;

	@Column(name = "ebrId")
	@ApiModelProperty(value = "关联资源编号")
	private String ebrId;

	@Column(name = "schemeId")
	@ApiModelProperty(value = "关联调度方案编号")
	private String schemeId;

	@Column(name = "ebrType")
	@ApiModelProperty(value = "资源类型")
	private String ebrType;

	@Column(name = "ebrArea")
	@ApiModelProperty(value = "资源覆盖区域")
	private String ebrArea;

}