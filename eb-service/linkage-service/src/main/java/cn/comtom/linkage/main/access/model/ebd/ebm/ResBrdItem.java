package cn.comtom.linkage.main.access.model.ebd.ebm;

import java.util.List;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRST;

/**
 * @author nobody
 * 调用资源播出条目
 */
public class ResBrdItem {
	
	/**
	 * 应急广播平台信息(只需要传递ID)(可选)
	 */
	private EBRPS EBRPS;
	
	/**
	 * 台站（前端）信息(只需要传递ID),可选
	 */
	private EBRST EBRST;
	
	/**
	 * 消息接收设备信息(只需要传递ID),可选
	 */
	private EBRAS EBRAS;
	
	/**
	 * 播出系统的播出详情,一个或者多个
	 * rptTime,brdSysType,brdSysInfo,startTime,endTime,fileURL,brdStateCode,brdStateDesc需要传递
	 */
	private List<EBRBS> dataList;

	/**
	 * @return the eBRPS(只需要传递ID)
	 */
	public EBRPS getEBRPS() {
		return EBRPS;
	}

	/**
	 * @param eBRPS(只需要传递ID) the eBRPS to set
	 */
	public void setEBRPS(EBRPS eBRPS) {
		EBRPS = eBRPS;
	}

	/**
	 * @return the eBRST(只需要传递ID)
	 */
	public EBRST getEBRST() {
		return EBRST;
	}

	/**
	 * @param eBRST(只需要传递ID) the eBRST to set
	 */
	public void setEBRST(EBRST eBRST) {
		EBRST = eBRST;
	}

	/**
	 * @return the eBRAS(只需要传递ID)
	 */
	public EBRAS getEBRAS() {
		return EBRAS;
	}

	/**
	 * @param eBRAS(只需要传递ID) the eBRAS to set
	 */
	public void setEBRAS(EBRAS eBRAS) {
		EBRAS = eBRAS;
	}

	/**
	 * @return the 播出系统的播出详情一个或者多个
	 * rptTime,brdSysType,brdSysInfo,startTime,endTime,fileURL,brdStateCode,brdStateDesc需要传递
	 */
	public List<EBRBS> getDataList() {
		return dataList;
	}

	/**
	 * @param 播出系统的播出详情一个或者多个 the dataList to set
	 * rptTime,brdSysType,brdSysInfo,startTime,endTime,fileURL,brdStateCode,brdStateDesc需要传递
	 */
	public void setDataList(List<EBRBS> dataList) {
		this.dataList = dataList;
	}

}
