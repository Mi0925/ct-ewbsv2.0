package cn.comtom.linkage.main.access.model.ebd.commom;


import java.util.List;

/**
 * @author nobody
 * 运行图
 */
public class Schedule {
	
	/**
	 * 开关机时间1个或多个
	 */
	private List<Switch> dataList;

	/**
	 * @return the 开关机时间1个或多个
	 */
	public List<Switch> getDataList() {
		return dataList;
	}

	/**
	 * @param 开关机时间1个或多个 the dataList to set
	 */
	public void setDataList(List<Switch> dataList) {
		this.dataList = dataList;
	}
	
}
