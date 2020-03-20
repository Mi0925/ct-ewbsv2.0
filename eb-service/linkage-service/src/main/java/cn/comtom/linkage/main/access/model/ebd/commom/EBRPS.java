package cn.comtom.linkage.main.access.model.ebd.commom;



/**
 * @author nobody
 * 应急广播平台
 */
public class EBRPS {
	
	/**
	 * 上报时间 YYYY--MM--DD HH:MI:SS
	 */
	private String rptTime;
	
	/**
	 * 数据操作类型
	 * Sync: 同步（新增、更新）Delete:删除(暂不使用) 
	 */
	private String rptType;
	
	/**
	 * 关联应急广播平台，指所属的应急广播平台(可选)
	 */
	private cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS relatedEBRPS;
		
	/**
	 * 应急广播平台ID:18位资源编码
	 */
	private String EBRID;
	
	/**
	 * 应急广播平台名称
	 */
	private String EBRName;
	
	/**
	 * 应急广播平台地址
	 */
	private String address;
	
	/**
	 * 联系人
	 */
	private String contact;
	
	/**
	 * 联系电话
	 */
	private String phoneNumber;
	
	/**
	 * 经度 
	 */
	private String longitude;
	
	/**
	 * 纬度
	 */
	private String latitude;

	/**
	 * 网络地址
	 */
	private String URL;

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
	 * @return the 应急广播平台地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param 应急广播平台地址 the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the 联系人
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param 联系人 the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the 联系电话
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param 联系电话 the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	
}
