package cn.comtom.core.main.ebm.entity.dbo;

import cn.comtom.core.fw.UUIdGenId;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "bc_ebm_res_bs")
public class EbmResBs implements Serializable {

	/**
	 * 记录ID
	 */
	@Id
	@KeySql(genId = UUIdGenId.class)
	@Column(name = "id")
	private String id;

	/**
	 * 播发记录ID
	 */
	@Column(name = "brdItemId")
	private String brdItemId;

	/**
	 * rptTime
	 */
	@Column(name = "rptTime")
	private Date rptTime;

	/**
	 * brdSysType
	 */
	@Column(name = "brdSysType")
	private String brdSysType;

	/**
	 * brdSysInfo
	 */
	@Column(name = "brdSysInfo")
	private String brdSysInfo;

	/**
	 * startTime
	 */
	@Column(name = "startTime")
	private Date startTime;

	/**
	 * endTime
	 */
	@Column(name = "endTime")
	private Date endTime;

	/**
	 * fileURL
	 */
	@Column(name = "fileURL")
	private String fileURL;

	/**
	 * brdStateCode
	 */
	@Column(name = "brdStateCode")
	private Integer brdStateCode;

	/**
	 * brdStateDesc
	 */
	@Column(name = "brdStateDesc")
	private String brdStateDesc;
}