package cn.comtom.linkage.main.access.constant;

/**
 * 消息是否超时状态
 * @author comtom
 *
 */
public enum TimeOutStatusEnum {

	/**
	 * 未超时
	 */

	NoTimeout(1, "未超时"),

	/**
	 * 已超时
	 */
	overtime(2, "已超时");

	private Integer value;

	private String name;

	private TimeOutStatusEnum(Integer value, String name) {
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
