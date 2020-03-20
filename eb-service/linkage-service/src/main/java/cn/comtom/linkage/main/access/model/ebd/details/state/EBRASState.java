package cn.comtom.linkage.main.access
		.model.ebd.details.state;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;

/**
 * @author nobody
 * 消息接收设备状态
 */
public class EBRASState {
	
	
	/**
	 * 消息接收设备集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	private List<EBRAS> dataList;

	/**
	 * @return the 消息接收设备集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	public List<EBRAS> getDataList() {
		return dataList;
	}

	/**
	 * @param 消息接收设备集合(rptTime,EBRID,stateCode,stateDesc设置) the dataList to set
	 */
	public void setDataList(List<EBRAS> dataList) {
		this.dataList = dataList;
	}

	
}
