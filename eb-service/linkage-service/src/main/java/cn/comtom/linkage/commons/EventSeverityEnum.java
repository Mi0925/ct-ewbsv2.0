/**
 * 
 */
package cn.comtom.linkage.commons;

/**
 * 事件级别枚举定义
 * 
 * @author Administrator
 *
 */
public enum EventSeverityEnum {
	unknown(0, "未知级别"), red(1, "特别重大"), orange(2, "重大"), yellow(3, "较大"), blue(4, "一般"), common(10,
			"日常"), drill(15, "测试专用"), demo1(21, "平台演练"), demo2(22, "模拟演练"), demo3(23, "实际演练");

	private String severity;
	private Integer code;

	private EventSeverityEnum(Integer code, String severity) {
		this.code = code;
		this.severity = severity;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public static String getNameByCode(Integer code) {
		if (code == null) {
			return null;
		}
		for (EventSeverityEnum severityEnum : values()) {
			if (severityEnum.getCode() == code) {
				return severityEnum.getSeverity();
			}
		}
		return "";
	}

	public static Integer getCodeByName(String name) {
		if (name == null) {
			return null;
		}
		for (EventSeverityEnum severityEnum : values()) {
			if (severityEnum.getSeverity().equals(name)) {
				return severityEnum.getCode();
			}
		}
		return null;
	}
}
