package cn.comtom.linkage.main.access.model.ebd.ebm;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;

/**
 * @author nobody 播发部门及人员条目信息
 */
public class Unit {

	/**
	 * 播出部门所对应的应急广播平台(只需要传递ID)
	 */
	private EBRPS EBRPS;
	/**
	 * 播发部门ID
	 */
	private String unitId;
	/**
	 * 播发部门名称
	 */
	private String unitName;

	/**
	 * 播发人员ID
	 */
	private String personID;

	/**
	 * 播发人员姓名
	 */
	private String personName;

	/**
	 * @return the eBRPS(只需要传递ID)
	 */
	public EBRPS getEBRPS() {
		return EBRPS;
	}

	/**
	 * @param eBRPS
	 *            (只需要传递ID) the eBRPS to set
	 */
	public void setEBRPS(EBRPS eBRPS) {
		EBRPS = eBRPS;
	}

	/**
	 * @return the 播发部门ID
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * @param 播发部门ID
	 *            the unitId to set
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the 播发部门名称
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param 播发部门名称
	 *            the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the 播发人员ID
	 */
	public String getPersonID() {
		return personID;
	}

	/**
	 * @param 播发人员ID
	 *            the persionID to set
	 */
	public void setPersonID(String personID) {
		this.personID = personID;
	}

	/**
	 * @return the 播发人员姓名
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * @param 播发人员姓名
	 *            the persionName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

}
