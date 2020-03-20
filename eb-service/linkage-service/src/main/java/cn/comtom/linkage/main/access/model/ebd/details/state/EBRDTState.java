package cn.comtom.linkage.main.access.model.ebd.details.state;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;

/**
 * @author nobody
 * 设备及终端状态
 */
public class EBRDTState {
	
	
	/**
	 * 设备及终端集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	private List<EBRDT> dataList;

	/**
	 * @return the 设备及终端集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	public List<EBRDT> getDataList() {
		return dataList;
	}

	/**
	 * @param 设备及终端集合(rptTime,EBRID,stateCode,stateDesc设置) the dataList to set
	 */
	public void setDataList(List<EBRDT> dataList) {
		this.dataList = dataList;
	}

	
}
