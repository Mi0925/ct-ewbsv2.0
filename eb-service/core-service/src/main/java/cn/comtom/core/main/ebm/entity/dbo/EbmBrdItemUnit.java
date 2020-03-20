package cn.comtom.core.main.ebm.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Data
@Table(name = "bc_ebm_brd_item_unit")
public class EbmBrdItemUnit implements Serializable {


	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "id")
	@ApiModelProperty(value= "主键")
	private String id;

	@Column(name = "brdItemId")
	@ApiModelProperty(value= "播发记录ID")
	private String brdItemId;

	@Column(name = "unitId")
	@ApiModelProperty(value= "部门ID")
	private String unitId;
	
	@Column(name = "unitName")
	@ApiModelProperty(value= "部门名称")
	private String unitName;
	
	@Column(name = "persionId")
	@ApiModelProperty(value= "播放人员ID")
	private String persionId;
	
	@Column(name = "persionName")
	@ApiModelProperty(value= "播放人员名称")
	private String persionName;
	
	@Column(name = "ebrpsId")
	@ApiModelProperty(value= "应急广播平台ID")
	private String ebrpsId;
	


}