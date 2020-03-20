package cn.comtom.core.main.ebm.entity.dbo;


import cn.comtom.core.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "bc_ebm_res")
public class EbmRes implements Serializable {

	/**
	 * 消息资源ID
	 */
	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "ebmResourceId")
	private String ebmResourceId;
	
	/**
	 * 关联记录ID
	 */
	@Column(name = "brdItemId")
	private String brdItemId;

	/**
	 * 台站ID
	 */
	@Column(name = "resId")
	private String resId;
	
	/**
	 * 接收设备ID
	 */
	@Column(name = "resType")
	private String resType;
}