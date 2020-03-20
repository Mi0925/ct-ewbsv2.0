package cn.comtom.linkage.main.access.model.ebd.ebm;

import cn.comtom.linkage.main.access.model.ebd.commom.Coverage;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;

/**
 * @author nobody
 * 播发记录条目
 */
public class EBMBrdItem {
	
	/**
	 * 应急广播消息
	 * (EBMID,msgBasicInfo,MsgContent(不需要辅助信息))
	 */
	private EBM EBM;
	
	/**
	 * 播发部门人员信息(可选)
	 */
	private UnitInfo unitInfo;
	
	/**
	 * 播发状态代码
	 * 0：未处理
	 * 1：等待播发（未到播发时间）
	 * 2：正在播发
	 * 3：播发成功
	 * 4：播发失败（包括播发全部失败、播发部分失败、未按要求播发等）
	 * 5：播发取消
	 */

	private Integer brdStateCode;
	
	/**
	 * 播发状态描述
	 */
	private String brdStateDesc;
	
	/**
	 * 实际覆盖行政区域
	 */
	private Coverage coverage;
	
	/**
	 * 调用资源播出信息(可选)
	 */
	private ResBrdInfo resBrdInfo;

	/**
	 * @return the eBM(EBMID,msgBasicInfo,MsgContent(不需要辅助信息))
	 */
	public EBM getEBM() {
		return EBM;
	}

	/**
	 * @param eBM(EBMID,msgBasicInfo,MsgContent(不需要辅助信息)) the eBM to set
	 */
	public void setEBM(EBM eBM) {
		EBM = eBM;
	}

	/**
	 * @return the 播发部门人员信息(可选)
	 */
	public UnitInfo getUnitInfo() {
		return unitInfo;
	}

	/**
	 * @param 播发部门人员信息(可选) the unitInfo to set
	 */
	public void setUnitInfo(UnitInfo unitInfo) {
		this.unitInfo = unitInfo;
	}

	/**
	 * @return the 播发状态代码0：未处理1：等待播发（未到播发时间）2：正在播发3：播发成功4：播发失败（包括播发全部失败、播发部分失败、未按要求播发等）5：播发取消
	 */
	public Integer getBrdStateCode() {
		return brdStateCode;
	}

	/**
	 * @param 播发状态代码0：未处理1：等待播发（未到播发时间）2：正在播发3：播发成功4：播发失败（包括播发全部失败、播发部分失败、未按要求播发等）5：播发取消 the brdStateCode to set
	 */
	public void setBrdStateCode(Integer brdStateCode) {
		this.brdStateCode = brdStateCode;
	}

	/**
	 * @return the 播发状态描述
	 */
	public String getBrdStateDesc() {
		return brdStateDesc;
	}

	/**
	 * @param 播发状态描述 the brdStateDesc to set
	 */
	public void setBrdStateDesc(String brdStateDesc) {
		this.brdStateDesc = brdStateDesc;
	}

	/**
	 * @return the 实际覆盖行政区域(可选)
	 */
	public Coverage getCoverage() {
		return coverage;
	}

	/**
	 * @param 实际覆盖行政区域(可选) the coverage to set
	 */
	public void setCoverage(Coverage coverage) {
		this.coverage = coverage;
	}

	/**
	 * @return the 调用资源播出信息(可选)
	 */
	public ResBrdInfo getResBrdInfo() {
		return resBrdInfo;
	}

	/**
	 * @param 调用资源播出信息(可选) the resBrdInfo to set
	 */
	public void setResBrdInfo(ResBrdInfo resBrdInfo) {
		this.resBrdInfo = resBrdInfo;
	}
	
}
