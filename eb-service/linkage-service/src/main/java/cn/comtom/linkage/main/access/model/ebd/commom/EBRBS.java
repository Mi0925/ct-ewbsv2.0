package cn.comtom.linkage.main.access.model.ebd.commom;


import cn.comtom.linkage.utils.RegionUtil;

/**
 * @author nobody
 * 播出系统
 */
public class EBRBS {

	/**
	 * 数据时间 YYYY--MM--DDHH
	 */
	private String rptTime;
	
	/**
	 * 播出系统类型
	 * 格式为： 播出系统类型1,播出系统类型2
	 * 播出系统类型为4位数字码，格式为：资源类型码（2位）资源子类型码（2位）
	 */
	private String brdSysType;
	/**
	 * 调用播出系统信息
	 * 表示在消息接收设备连接的多个播出系统中，
	 * 需要调用的播出系统的编号、节目频率标识、应急广播节目名或节目号或频率信息的集合信息。
	 * 节目频率标识：1表示节目名，2表示节目号，3表示频率。
	 * (18位数字码,1,交通电台),(18位数字码,2,节目号)，(18位数字码,3,88MHZ)
	 */
	private String brdSysInfo;
	/**
	 * 播发起始时间 YYYY--MM--DD HH:MI:SS
	 */	
	private String startTime;
	
	/**
	 * 播发结束时间
	 */
	private String endTime;
	
	/**
	 * 播发录音文件地址
	 */
	private String fileURL;
	
	/**
	 * 播发状态代码<br>
	 *  0：未处理 
	 *  1：等待播发（未到播发时间） 
	 *  2：正在播发 
	 *  3：播发成功 
	 *  4：播发失败（包括播发全部失败、播发部分失败、未按要求播发等）
	 *  5：播发取消
	 */
	private Integer brdStateCode;
	
	/**
	 * 播发状态描述
	 */
	private String brdStateDesc;
	
	/**
	 * 数据操作类型
	 * Sync: 同步（新增、更新）Delete:删除(暂不使用) 
	 */
	private String rptType;
	
	/**
	 * 联应急广播平台，指所属的应急广播平台(可选)
	 */
	private cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS relatedEBRPS;

	/**
	 * 关联台站(可选)
	 */
	private cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST relatedEBRST;
	
	/**
	 * 关联消息接收设备(可选)
	 */
	private RelatedEBRAS relatedEBRAS;
	
	/**
	 * 播出系统ID:18位资源编码
	 */
	private String EBRID;
	
	/**
	 * 播出系统名称
	 */
	private String EBRName;
	
	/**
	 * 网络地址(可选)
	 */
	private String URL;
	
	/**
	 * 经度 
	 */
	private String longitude;
	
	/**
	 * 纬度
	 */
	private String latitude;
	
	/**
	 * 覆盖面积
	 */
	private String square;
	

//	private Coverage coverage;

	/**
	 * 覆盖行政区域编码
	 */
	private String areaCode;
	
	/**
	 * 覆盖人口
	 */
	private Double population;
	
	/**
	 * 原播语种
	 */
	private String languageCode;
	
	/**
	 * 机房名称
	 */
	private String equipRoom;
	
	/**
	 * 模拟广播(发射机)附加参数
	 */
	private cn.comtom.linkage.main.access.model.ebd.commom.RadioParams radioParams;
	
	/**
	 * 数字电视附加参数(可选)
	 */
	private cn.comtom.linkage.main.access.model.ebd.commom.TVParams tVParams;
	
	/**
	 * 运行图(可选)
	 */
	private cn.comtom.linkage.main.access.model.ebd.commom.Schedule schedule;
	
	/**
	 * 状态代码
	 * 1:开机 ;2关机;3故障;4故障恢复
	 */
	private Integer stateCode;
	
	/**状态描述*/
	private String stateDesc;
	
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

	/**
	 * @return the 数据操作类型Sync:同步（新增、更新）Delete:删除(暂不使用)
	 */
	public String getRptType() {
		return rptType;
	}

	/**
	 * @param 数据操作类型Sync:同步（新增、更新）Delete:删除(暂不使用) the rptType to set
	 */
	public void setRptType(String rptType) {
		this.rptType = rptType;
	}

	/**
	 * @return the 联应急广播平台，指所属的应急广播平台(可选)
	 */
	public cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS getRelatedEBRPS() {
		return relatedEBRPS;
	}

	/**
	 * @param 联应急广播平台，指所属的应急广播平台(可选) the relatedEBRPS to set
	 */
	public void setRelatedEBRPS(cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS relatedEBRPS) {
		this.relatedEBRPS = relatedEBRPS;
	}

	/**
	 * @return the eBRID
	 */
	public String getEBRID() {
		return EBRID;
	}

	/**
	 * @param eBRID the eBRID to set
	 */
	public void setEBRID(String eBRID) {
		EBRID = eBRID;
	}

	/**
	 * @return the eBRName
	 */
	public String getEBRName() {
		return EBRName;
	}

	/**
	 * @param eBRName the eBRName to set
	 */
	public void setEBRName(String eBRName) {
		EBRName = eBRName;
	}

	/**
	 * @return the 经度
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param 经度 the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the 纬度
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param 纬度 the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the 关联台站(可选)
	 */
	public cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST getRelatedEBRST() {
		return relatedEBRST;
	}

	/**
	 * @param 关联台站(可选) the relatedEBRST to set
	 */
	public void setRelatedEBRST(cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST relatedEBRST) {
		this.relatedEBRST = relatedEBRST;
	}

	/**
	 * @return the 关联消息接收设备(可选)
	 */
	public RelatedEBRAS getRelatedEBRAS() {
		return relatedEBRAS;
	}

	/**
	 * @param 关联消息接收设备(可选) the relatedEBRAS to set
	 */
	public void setRelatedEBRAS(RelatedEBRAS relatedEBRAS) {
		this.relatedEBRAS = relatedEBRAS;
	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the 覆盖面积
	 */
	public String getSquare() {
		return square;
	}

	/**
	 * @param 覆盖面积 the square to set
	 */
	public void setSquare(String square) {
		this.square = square;
	}


	/**
	 * @return the 覆盖人口
	 */
	public Double getPopulation() {
		return population;
	}

	/**
	 * @param 覆盖人口 the population to set
	 */
	public void setPopulation(Double population) {
		this.population = population;
	}

	/**
	 * @return the 原播语种
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * @param 原播语种 the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	/**
	 * @return the 机房名称
	 */
	public String getEquipRoom() {
		return equipRoom;
	}

	/**
	 * @param 机房名称 the equipRoom to set
	 */
	public void setEquipRoom(String equipRoom) {
		this.equipRoom = equipRoom;
	}

	/**
	 * @return the 模拟广播(发射机)附加参数
	 */
	public cn.comtom.linkage.main.access.model.ebd.commom.RadioParams getRadioParams() {
		return radioParams;
	}

	/**
	 * @param 模拟广播(发射机)附加参数 the radioParams to set
	 */
	public void setRadioParams(cn.comtom.linkage.main.access.model.ebd.commom.RadioParams radioParams) {
		this.radioParams = radioParams;
	}

	/**
	 * @return the 数字电视附加参数(可选)
	 */
	public cn.comtom.linkage.main.access.model.ebd.commom.TVParams gettVParams() {
		return tVParams;
	}

	/**
	 * @param 数字电视附加参数(可选) the tVParams to set
	 */
	public void settVParams(cn.comtom.linkage.main.access.model.ebd.commom.TVParams tVParams) {
		this.tVParams = tVParams;
	}

	/**
	 * @return the 运行图(可选)
	 */
	public cn.comtom.linkage.main.access.model.ebd.commom.Schedule getSchedule() {
		return schedule;
	}

	/**
	 * @param 运行图(可选) the schedule to set
	 */
	public void setSchedule(cn.comtom.linkage.main.access.model.ebd.commom.Schedule schedule) {
		this.schedule = schedule;
	}


	/**
	 * @return the 调用播出系统信息表示在消息接收设备连接的多个播出系统中，需要调用的播出系统的编号、节目频率标识、应急广播节目名或节目号或频率信息的集合信息。节目频率标识：1表示节目名，2表示节目号，3表示频率。(18位数字码1交通电台)(18位数字码2界面2)，(18位数字码388MHZ)
	 */
	public String getBrdSysInfo() {
		return brdSysInfo;
	}

	/**
	 * @param 调用播出系统信息表示在消息接收设备连接的多个播出系统中，需要调用的播出系统的编号、节目频率标识、应急广播节目名或节目号或频率信息的集合信息。节目频率标识：1表示节目名，2表示节目号，3表示频率。(18位数字码1交通电台)(18位数字码2界面2)，(18位数字码388MHZ) the brdSysInfo to set
	 */
	public void setBrdSysInfo(String brdSysInfo) {
		this.brdSysInfo = brdSysInfo;
	}

	/**
	 * @return the 播发起始时间YYYY--MM--DDHH:MI:SS
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param 播发起始时间YYYY--MM--DDHH:MI:SS the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the 播发结束时间
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param 播发结束时间 the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the 播发录音文件地址
	 */
	public String getFileURL() {
		return fileURL;
	}

	/**
	 * @param 播发录音文件地址 the fileURL to set
	 */
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	/**
	 * @return the 播发状态代码<br>0：未处理1：等待播发（未到播发时间）2：正在播发3：播发成功4：播发失败（包括播发全部失败、播发部分失败、未按要求播发等）5：播发取消
	 */
	public Integer getBrdStateCode() {
		return brdStateCode;
	}

	/**
	 * @param 播发状态代码<br>0：未处理1：等待播发（未到播发时间）2：正在播发3：播发成功4：播发失败（包括播发全部失败、播发部分失败、未按要求播发等）5：播发取消 the brdStateCode to set
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
	 * @return the 状态代码1:开机;2关机;3故障;4故障恢复
	 */
	public Integer getStateCode() {
		return stateCode;
	}

	/**
	 * @param 状态代码1:开机;2关机;3故障;4故障恢复 the stateCode to set
	 */
	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return the 状态描述
	 */
	public String getStateDesc() {
		return stateDesc;
	}

	/**
	 * @param 状态描述 the stateDesc to set
	 */
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = RegionUtil.areaShort2Long(areaCode);
	}
}
