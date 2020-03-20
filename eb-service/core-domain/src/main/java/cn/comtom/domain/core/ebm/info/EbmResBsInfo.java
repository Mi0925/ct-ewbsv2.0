package cn.comtom.domain.core.ebm.info;

import lombok.Data;

import java.util.Date;

@Data
public class EbmResBsInfo {

	/**
	 * 记录ID
	 */
	private String id;

	/**
	 * 播发记录ID
	 *
	 */
	private String brdItemId;

	/**
	 * rptTime
	 */
	private Date rptTime;

	/**
	 * brdSysType
	 */
	private String brdSysType;

	/**
	 * brdSysInfo
	 */
	private String brdSysInfo;

	/**
	 * startTime
	 */
	private Date startTime;

	/**
	 * endTime
	 */
	private Date endTime;

	/**
	 * fileURL
	 */
	private String fileURL;

	/**
	 * brdStateCode
	 */
	private Integer brdStateCode;

	/**
	 * brdStateDesc
	 */
	private String brdStateDesc;

	/**
	 * 资源覆盖区域
	 */
	private String areaCode;

	/**
	 * 资源覆盖区域名称
	 */
	private String areaName;

}