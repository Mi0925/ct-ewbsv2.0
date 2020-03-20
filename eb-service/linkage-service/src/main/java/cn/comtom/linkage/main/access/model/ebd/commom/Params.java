package cn.comtom.linkage.main.access.model.ebd.commom;

/**
 * @author nobody
 * 附加参数
 */
public class Params {
	
	/**
	 * 数据起始时间(可选)
	 */
	private String rptStartTime;
	
	/**
	 * 数据结束时间(可选)
	 */
	private String rptEndTime;

	
	/**
	 * 数据包操作类型
	 * Full:全量数据
	 * Incremental:增量数据
	 */
	private String rptType;
	/**
	 * @return the 数据起始时间
	 */
	public String getRptStartTime() {
		return rptStartTime;
	}

	/**
	 * @param 数据起始时间 the rptStartTime to set
	 */
	public void setRptStartTime(String rptStartTime) {
		this.rptStartTime = rptStartTime;
	}

	/**
	 * @return the 数据结束时间
	 */
	public String getRptEndTime() {
		return rptEndTime;
	}

	/**
	 * @param 数据结束时间 the rptEndTime to set
	 */
	public void setRptEndTime(String rptEndTime) {
		this.rptEndTime = rptEndTime;
	}

	/**
	 * @return the 数据包操作类型Full:全量数据Incremental:增量数据
	 */
	public String getRptType() {
		return rptType;
	}

	/**
	 * @param 数据包操作类型Full:全量数据Incremental:增量数据 the rptType to set
	 */
	public void setRptType(String rptType) {
		this.rptType = rptType;
	}
	
}
