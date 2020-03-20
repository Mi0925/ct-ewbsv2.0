package cn.comtom.linkage.commons;

/**
 * 应急广播消息类型
 * @author comtom
 *
 */
public enum EBMType {
	
	/**
	 * 请求播发
	 */
	request(1, "请求播发"), 
	
	/**
	 * 取消播发
	 */
	cancel(2, "取消播发");

	private Integer value;

	private String name;

	private EBMType(Integer value, String name) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
