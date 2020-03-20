/**
 * 
 */
package cn.comtom.linkage.commons;

/**
 * @author Administrator
 *
 */
public enum BroadcastStateEnum {
	noprogress(0, "未处理"),
	timetogo(1, "等待播发"), 
	inprogress(2, "播发中"), 
	succeeded(3, "播发成功"), 
	failed(4, "播发失败"),
	cancelled(5, "播发取消");

	private String brdstate;
	private Integer code;

	private BroadcastStateEnum(Integer code, String brdstate) {
		this.code = code;
		this.brdstate = brdstate;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getBrdstate() {
		return brdstate;
	}

	public void setBrdstate(String brdstate) {
		this.brdstate = brdstate;
	}

	public static String getNameByCode(Integer code) {
		if (code == null) {
			return null;
		}
		for (BroadcastStateEnum brdStateEnum : values()) {
			if (brdStateEnum.getCode() == code) {
				return brdStateEnum.getBrdstate();
			}
		}
		return "";
	}

	public static Integer getCodeByName(String brdstate) {
		if (brdstate == null) {
			return null;
		}
		for (BroadcastStateEnum brdStateEnum : values()) {
			if (brdStateEnum.getBrdstate().equals(brdstate)) {
				return brdStateEnum.getCode();
			}
		}
		return null;
	}
}
