package cn.comtom.linkage.main.access.model.ebd.ebm;

import java.util.List;

/**
 * @author nobody
 * 调用资源播出信息
 */
public class ResBrdInfo {
	
	/**
	 * 调用资源播出条目集合一个或者多个
	 */
	private List<cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem> dataList;

	/**
	 * @return the 调用资源播出条目集合
	 */
	public List<cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem> getDataList() {
		return dataList;
	}

	/**
	 * @param 调用资源播出条目集合 the dataList to set
	 */
	public void setDataList(List<cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem> dataList) {
		this.dataList = dataList;
	}
	
}
