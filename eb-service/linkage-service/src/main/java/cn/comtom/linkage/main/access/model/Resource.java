package cn.comtom.linkage.main.access.model;

public class Resource {
	
	/**
	 * 平台级别
	 */
	private Integer platLevel;
	
	/**
	 * 资源类型
	 */
	private String resourceType;
	
	/**
	 * 区域编码
	 */
	private String areaCode;

	/**
	 * @return the 资源类型
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @param 资源类型 the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return the 区域编码
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param 区域编码 the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return the 平台级别
	 */
	public Integer getPlatLevel() {
		return platLevel;
	}

	/**
	 * @param 平台级别 the platLevel to set
	 */
	public void setPlatLevel(Integer platLevel) {
		this.platLevel = platLevel;
	}
	
}
