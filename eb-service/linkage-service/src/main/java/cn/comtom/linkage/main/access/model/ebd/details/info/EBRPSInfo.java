package cn.comtom.linkage.main.access.model.ebd.details.info;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;

/**
 * @author nobody
 * 应急广播平台上报信息
 */
public class EBRPSInfo {
	
	/**
	 * 附加参数说明(可选)
	 */
	private Params params;
	
	/**
	 * 应急广播平台信息，可为多个(包括自身平台及下级平台)
	 * (stateCode,stateDesc)不需要设置
	 */
	private List<EBRPS> dataList;

	/**
	 * @return the 附加参数说明(可选)
	 */
	public Params getParams() {
		return params;
	}

	/**
	 * @param 附加参数说明(可选) the params to set
	 * (stateCode,stateDesc)不需要设置
	 */
	public void setParams(Params params) {
		this.params = params;
	}

	/**
	 * @return the 应急广播平台信息，可为多个
	 * (stateCode,stateDesc)不需要设置
	 */
	public List<EBRPS> getDataList() {
		return dataList;
	}

	/**
	 * @param 应急广播平台信息，可为多个 the dataList to set
	 */
	public void setDataList(List<EBRPS> dataList) {
		this.dataList = dataList;
	}
	
}
