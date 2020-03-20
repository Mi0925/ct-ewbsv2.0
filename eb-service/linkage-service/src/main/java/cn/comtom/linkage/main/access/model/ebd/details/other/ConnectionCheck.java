package cn.comtom.linkage.main.access.model.ebd.details.other;

/**
 * @author nobody
 * 心跳检测
 */
public class ConnectionCheck {
	
	/**
	 * 上报时间 YYYY--MM--DD HH:MI:SS
	 */
	private String rptTime;

	/**
	 * @return the 上报时间YYYY--MM--DDHH:MI:SS
	 */
	public String getRptTime() {
		return rptTime;
	}

	/**
	 * @param 上报时间YYYY--MM--DDHH:MI:SS the rptTime to set
	 */
	public void setRptTime(String rptTime) {
		this.rptTime = rptTime;
	}
	
}
