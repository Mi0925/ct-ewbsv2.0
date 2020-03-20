package cn.comtom.linkage.main.access.model.ebd.details.other;

import cn.comtom.linkage.main.access.model.ebd.commom.Params;

/**
 * @author nobody
 * 运维数据请求
 */
public class OMDRequest {
	
	/**
	 * 运维数据类型</br>
	 * 可为如下值：
	 * EBRPSInfo EBRSTInfo EBRASInfo EBRBSInfo 
	 * EBRDTInfo EBMBrdLog EBRPSState EBRASState 
	 * EBRBSState EBRDTState
	 */
	private String OMDType;
	
	/**
	 * 附加参数说明(可选)
	 */
	private Params params;

	/**
	 * @return the oMDType
	 */
	public String getOMDType() {
		return OMDType;
	}

	/**
	 * @param oMDType the oMDType to set
	 */
	public void setOMDType(String oMDType) {
		OMDType = oMDType;
	}

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
	
}
