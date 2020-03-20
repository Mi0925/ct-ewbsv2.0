package cn.comtom.linkage.main.access.model.ebd.details.other;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.ebm.EBMBrdItem;

/**
 * @author nobody
 * 应急广播消息播发记录
 */
public class EBMBrdLog {

	/**
	 * 附加参数(可选 只需要传递rptStartTime,rptEndTime)
	 */
	private Params params;
	
	/**
	 * 播发记录条目,可为0个，1个或多个
	 */
	private List<EBMBrdItem> dataList;

	/**
	 * @return the 附加参数(可选 只需要传递rptStartTime,rptEndTime)
	 */
	public Params getParams() {
		return params;
	}

	/**
	 * @param 附加参数(可选 只需要传递rptStartTime,rptEndTime) the params to set
	 */
	public void setParams(Params params) {
		this.params = params;
	}

	/**
	 * @return the 播发记录条目可为0个，1个或多个
	 */
	public List<EBMBrdItem> getDataList() {
		return dataList;
	}

	/**
	 * @param 播发记录条目可为1个或多个 the dataList to set
	 */
	public void setDataList(List<EBMBrdItem> dataList) {
		this.dataList = dataList;
	}
	
}
