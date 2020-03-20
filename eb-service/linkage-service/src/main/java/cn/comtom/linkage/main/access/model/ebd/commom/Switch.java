package cn.comtom.linkage.main.access.model.ebd.commom;

/**
 * @author nobody
 * 开关机时间
 */
public class Switch {
	
	/**
	 * 星期数1,2,3
	 */
	private Integer weekday;
	
	/**
	 * 开机时间 YYYY--MM--DD HH:MI:SS
	 */
	private String startTime;
	
	/**
	 * 关机时间 YYYY--MM--DD HH:MI:SS
	 */
	private String endTime;

	/**
	 * @return the 星期123
	 */
	public Integer getWeekday() {
		return weekday;
	}

	/**
	 * @param 星期123 the weekday to set
	 */
	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	/**
	 * @return the 开机时间YYYY--MM--DDHH:MI:SS
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param 开机时间YYYY--MM--DDHH:MI:SS the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the 关机时间YYYY--MM--DDHH:MI:SS
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param 关机时间YYYY--MM--DDHH:MI:SS the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
