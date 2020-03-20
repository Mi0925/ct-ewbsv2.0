package cn.comtom.linkage.main.access.model.ebd.commom;

import cn.comtom.linkage.utils.RegionUtil;


/**
 * @author nobody
 * 时间覆盖行政区域
 */
public class Coverage {
	
//	/**
//	 * 实际覆盖区域百分比
//	 */
//	private Double coveragePercent;
	
	/**
	 * 实际覆盖区域比例
	 */
	private Double coverageRate;
	
	/**
	 * 实际覆盖区域编码集合(格式：区域编码1,区域编码2)
	 */
	private String areaCode;


	/**
	 * 实际调用资源响应统计
	 * 格式为（半角字符逗号）: 如： 5,10,10,1000
	 实际调用应急广播平台数,实际调用应急广播适配器数,实际调用传输覆盖播出设备数,实际调用终端数
	 */
	private String resBrdStat;

	public Double getCoverageRate() {
		return coverageRate;
	}

	public void setCoverageRate(Double coverageRate) {
		this.coverageRate = coverageRate;
	}
//	/**
//	 * @return the 实际覆盖区域百分比
//	 */
//	public Double getCoveragePercent() {
//		return coveragePercent;
//	}
//
//	/**
//	 * @param 实际覆盖区域百分比 the coveragePercent to set
//	 */
//	public void setCoveragePercent(Double coveragePercent) {
//		this.coveragePercent = coveragePercent;
//	}


	
	/**
	 * @return the 实际覆盖区域编码集合(格式：区域编码1区域编码2)
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param 实际覆盖区域编码集合(格式：区域编码1区域编码2) the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = RegionUtil.areaShort2Long(areaCode);
	}

	public String getResBrdStat() {
		return resBrdStat;
	}

	public void setResBrdStat(String resBrdStat) {
		this.resBrdStat = resBrdStat;
	}
}
