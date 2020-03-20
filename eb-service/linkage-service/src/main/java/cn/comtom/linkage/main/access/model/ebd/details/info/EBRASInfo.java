package cn.comtom.linkage.main.access.model.ebd.details.info;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;

/**
 * @author nobody
 * 接收设备上报信息
 */
public class EBRASInfo {
	
	/**
	 * 附加参数说明(可选)
	 */
	private Params params;
	
	/**
	 * 接收设备信息，可为多个
	 * (stateCode,stateDesc不需要传递)
	 */
	private List<EBRAS> dataList;

	/**
	 * @return the 附加参数说明(可选)
	 */
	public Params getParams() {
		return params;
	}

	/**
	 * @param 附加参数说明(可选) the params to set
	 * (stateCode,stateDesc不需要传递)
	 */
	public void setParams(Params params) {
		this.params = params;
	}

	/**
	 * @return the 接收设备信息，可为多个
	 * (stateCode,stateDesc不需要传递)
	 */
	public List<EBRAS> getDataList() {
		return dataList;
	}

	/**
	 * @param 接收设备信息，可为多个 the dataList to set
	 */
	public void setDataList(List<EBRAS> dataList) {
		this.dataList = dataList;
	}
	
}
