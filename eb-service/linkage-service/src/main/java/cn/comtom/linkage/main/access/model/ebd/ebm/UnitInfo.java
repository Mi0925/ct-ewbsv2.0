package cn.comtom.linkage.main.access.model.ebd.ebm;

import java.util.List;

/**
 * @author nobody
 * 播发部门人员信息集合
 */ 
public class UnitInfo {

	/**
	 * 播发部门人员信息，可为0个、1个或多个
	 */
	private List<Unit> dataList;

	/**
	 * @return the 播发部门人员信息，可为0个、1个或多个
	 */
	public List<Unit> getDataList() {
		return dataList;
	}

	/**
	 * @param 播发部门人员信息，可为0个、1个或多个 the dataList to set
	 */
	public void setDataList(List<Unit> dataList) {
		this.dataList = dataList;
	}
	
	
}
