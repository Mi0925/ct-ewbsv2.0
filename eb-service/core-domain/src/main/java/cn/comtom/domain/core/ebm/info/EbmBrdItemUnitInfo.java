package cn.comtom.domain.core.ebm.info;


import lombok.Data;

@Data
public class EbmBrdItemUnitInfo {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 播发记录ID
	 */
	private String brdItemId;

	/**
	 * 部门ID
	 */
	private String unitId;

	/**
	 * 部门名称
	 */
	private String unitName;

	/**
	 * 播放人员ID
	 */
	private String persionId;

	/**
	 * 播放人员名称
	 */
	private String persionName;

	/**
	 * 应急广播平台ID
	 */
	private String ebrpsId;

}