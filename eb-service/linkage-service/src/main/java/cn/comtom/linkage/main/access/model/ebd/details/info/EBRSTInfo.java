package cn.comtom.linkage.main.access.model.ebd.details.info;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRST;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;

/**
 * @author nobody
 * 台站上报信息
 */
public class EBRSTInfo {
	
	/**
	 * 附加参数说明(可选)
	 */
	private Params params;
	
	/**
	 * 台站信息，可为多个
	 */
	private List<EBRST> dataList;

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
	 * @return the 台站信息，可为多个
	 */
	public List<EBRST> getDataList() {
		return dataList;
	}

	/**
	 * @param 台站信息，可为多个 the dataList to set
	 */
	public void setDataList(List<EBRST> dataList) {
		this.dataList = dataList;
	}
	
}
