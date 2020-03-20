package cn.comtom.linkage.main.access.model.ebd.details.state;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;

/**
 * @author nobody
 * 播出系统状态
 */
public class EBRBSState {
	
	
	/**
	 * 播出系统集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	private List<EBRBS> dataList;

	/**
	 * @return the 播出系统集合(rptTime,EBRID,stateCode,stateDesc设置)
	 */
	public List<EBRBS> getDataList() {
		return dataList;
	}

	/**
	 * @param 播出系统集合(rptTime,EBRID,stateCode,stateDesc设置) the dataList to set
	 */
	public void setDataList(List<EBRBS> dataList) {
		this.dataList = dataList;
	}

	
}
