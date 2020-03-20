package cn.comtom.linkage.main.access.model.ebd.details.state;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;

/**
 * @author nobody
 * 应急广播平台状态
 */
public class EBRPSState {
	
	
	/**
	 * 应急广播平台集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	private List<EBRPS> dataList;

	/**
	 * @return the 应急广播平台集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	public List<EBRPS> getDataList() {
		return dataList;
	}

	/**
	 * @param 应急广播平台集合(rptTime,EBRID,stateCode,stateDesc设置) the dataList to set
	 */
	public void setDataList(List<EBRPS> dataList) {
		this.dataList = dataList;
	}

	
}
