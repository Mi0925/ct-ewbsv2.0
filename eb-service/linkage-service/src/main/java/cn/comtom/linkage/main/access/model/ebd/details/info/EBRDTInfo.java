package cn.comtom.linkage.main.access.model.ebd.details.info;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;

/**
 * @author nobody
 * 平台设备及终端上报信息
 */
public class EBRDTInfo {
	
	/**
	 * 附加参数说明(可选)
	 */
	private Params params;
	
	/**
	 * 平台设备及终端信息，可为多个
	 */
	private List<EBRDT> dataList;

	/**
	 * @return the 附加参数说明(可选)
	 */
	public Params getParams() {
		return params;
	}

	/**
	 * @param 附加参数说明(可选) the params to set
	 */
	public void setParams(Params params) {
		this.params = params;
	}

	/**
	 * @return the 平台设备及终端信息，可为多个
	 */
	public List<EBRDT> getDataList() {
		return dataList;
	}

	/**
	 * @param 平台设备及终端信息，可为多个 the dataList to set
	 */
	public void setDataList(List<EBRDT> dataList) {
		this.dataList = dataList;
	}
	
}
