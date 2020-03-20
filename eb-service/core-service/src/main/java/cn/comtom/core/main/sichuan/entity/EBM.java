package cn.comtom.core.main.sichuan.entity;
/**
 * 四川应急广播业务数据
 * 
 */
public class EBM {
	/*<!-- 报文制作下发平台ID  12位数字码，由省中心定义-->*/
	private String platformID;
	
	/*<!-- 应急广播消息ID  17位数字码 不重复-->*/
	private String msgID;
	
	/*<!-- 应急广播消息播发请求类型（取值0-15，标识当前播发请求的类型；0 保留，1 直接播发，2委托播发（待定），3 停止播发，4~15保留） 枚举项内容改变-->*/
	private String processType;
	
	/*<!-- 应急广播消息发布机构名称-->*/
	private String sender;
	
	/*<!-- 下发的目标应急广播平台 12位数字码，新增标签，省中心定义-->*/
	private String targetPlatformID;
	
	/*<!-- 演练类型 0~15，表示当前播发演练的类型；0保留，1应急消息播发（非演练测试），2实际演练，3平台演练，4前端演练，5终端演练，6系统测试，7~15保留；新增标签-->*/
	private String drillType;
	
	/*<!-- 预警事件类型  必选 5位字符串，GB/T15273.1-1994-->*/
	private String eventType;
	
	/*<!-- 预警事件级别 参见严重程度字典-->*/
	private String severity;
	
	/*<!-- 播发时间-->*/
	private String sendTime;
	
	/*<!--结束时间-->*/
	private String endTime;
	
	/*<!-- 持续时长 单位：秒；持续时间为16777215即0xFFFFFF的时候标识该应急广播消息持续有效-->*/
	private String duration;
	
	/*<!-- 预警区域编码即播发覆盖范围 多覆盖范围用","分隔-->*/
	private String geocode;
	
	/*<!-- 0~65536个资源调度 -->*/
	private Dispatch dispatch;
	
	/* <!-- 0~16个播发内容-->*/
	private Content content;

	public String getPlatformID() {
		return platformID;
	}

	public void setPlatformID(String platformID) {
		this.platformID = platformID;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTargetPlatformID() {
		return targetPlatformID;
	}

	public void setTargetPlatformID(String targetPlatformID) {
		this.targetPlatformID = targetPlatformID;
	}

	public String getDrillType() {
		return drillType;
	}

	public void setDrillType(String drillType) {
		this.drillType = drillType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getGeocode() {
		return geocode;
	}

	public void setGeocode(String geocode) {
		this.geocode = geocode;
	}

	public Dispatch getDispatch() {
		return dispatch;
	}

	public void setDispatch(Dispatch dispatch) {
		this.dispatch = dispatch;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	
}
