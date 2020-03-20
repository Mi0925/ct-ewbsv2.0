/**
 * 
 */
package cn.comtom.linkage.commons;

/**
 * @author Administrator
 * 资源状态描述
 */
public enum RSStateEnum {
	run(1, "开机/正常运行"), 
	stop(2, "关机/停止运行"), 
	fault(3, "故障"), 
	recover(4, "故障恢复"),
	broadcast(5, "播发中");


	private String rsstate;
	private Integer code;

	private RSStateEnum(Integer code, String rsstate) {
		this.code = code;
		this.rsstate = rsstate;
	}

	/**
	 * @return the rsstate
	 */
	public String getRsstate() {
		return rsstate;
	}

	/**
	 * @param rsstate the rsstate to set
	 */
	public void setRsstate(String rsstate) {
		this.rsstate = rsstate;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	public static String getNameByCode(Integer code) {
		if (code == null) {
			return null;
		}
		for (RSStateEnum brdStateEnum : values()) {
			if (brdStateEnum.getCode() == code) {
				return brdStateEnum.getRsstate();
			}
		}
		return "";
	}

	public static Integer getCodeByName(String rsstate) {
		if (rsstate == null) {
			return null;
		}
		for (RSStateEnum brdStateEnum : values()) {
			if (brdStateEnum.getRsstate().equals(rsstate)) {
				return brdStateEnum.getCode();
			}
		}
		return null;
	}
}
