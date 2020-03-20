package cn.comtom.linkage.main.access.model.ebd.ebm;

/**
 * @author nobody
 * 应急广播消息基本信息
 */
public class MsgBasicInfo {
	
	/**
	 * 消息类型
	 * 1:请求播发
	 * 2:取消播发
	 * 3：平台演练播发
	 * 4：前端演练播发
	 * 5：终端演练播发
	 */
	private Integer msgType;

	/**
	 * 发布机构名称
	 */	
	private String senderName;

	/**
	 * 发布机构编码
	 */
	private String senderCode;

	/**
	 * 发布时间 YYYY--MM--DD HH:MI:SS
	 */
	private String sendTime;

	/**
	 * 事件类型编码
	 */
	private String eventType;
	
	/**
	 * 事件级别
	 * 0:未知级别
	 * 1:1级(特别重大/红色预警/Red)
	 * 2:2级(重大/橙色预警/Orange)
	 * 3:3级(较大/黄色预警/Yellow)
	 * 4:4级(一般/蓝色预警/Blue)
	 * 15:测试专用
	 */
	private Integer severity;

	/**
	 * 播发起始时间-应急广播消息播发开始时间 
	 * YYYY--MM--DD HH:MI:SS
	 */	
	private String startTime;

	/**
	 * 播发结束时间-应急广播消息播发结束时间
	 * YYYY--MM--DD HH:MI:SS
	 */
	private String endTime;

	private String BroadcastTime;

	public String getBroadcastTime() {
		return BroadcastTime;
	}

	public void setBroadcastTime(String broadcastTime) {
		BroadcastTime = broadcastTime;
	}

	/**
	 * @return the 消息类型1:请求播发2:取消播发
	 */
	public Integer getMsgType() {
		return msgType;
	}

	/**
	 *  消息类型1:请求播发2:取消播发 the msgType to set
	 */
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return the 发布机构名称
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 *  发布机构名称 the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the 发布机构编码
	 */
	public String getSenderCode() {
		return senderCode;
	}

	/**
	 *  发布机构编码 the senderCode to set
	 */
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the 事件类型编码
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 *  事件类型编码 the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the 事件级别0:未知级别1:1级(特别重大红色预警Red)2:2级(重大橙色预警Orange)3:3级(较大黄色预警Yellow)4:4级(一般蓝色预警Blue)15:测试专用
	 */
	public Integer getSeverity() {
		return severity;
	}

	/**
	 * 事件级别0:未知级别1:1级(特别重大红色预警Red)2:2级(重大橙色预警Orange)3:3级(较大黄色预警Yellow)4:4级(一般蓝色预警Blue)15:测试专用 the severity to set
	 */
	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	/**
	 * @return the 播发起始时间-应急广播消息播发开始时间YYYY--MM--DDHH:MI:SS
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 *  播发起始时间-应急广播消息播发开始时间YYYY--MM--DDHH:MI:SS the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the 播发结束时间-应急广播消息播发结束时间YYYY--MM--DDHH:MI:SS
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 *  播发结束时间-应急广播消息播发结束时间YYYY--MM--DDHH:MI:SS the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	
}
