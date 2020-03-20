/**
 * 
 */
package cn.comtom.linkage.commons;

/**
 * EBD.EBDResponse.ResultCode <br/>
 * general EBD response received for an EBD request, 通用结果反馈，执行结果代码枚举 <br/>
 * 
 * when an EBD response result code received greater than 1 and less than 6, no further process upon the EBD request.  <br/>
 * 对于执行结果代码为2-5的情况下，不会再进一步处理及发送相应的业务数据  <br/>
 * 
 * @author Kidsoul Qu
 *
 */
public enum EBDRespResultEnum {
	notprocessed(0, "收到数据未处理"),
	receivevalid(1, "接收解析及数据校验成功"), 
	parsefailure(2, "接收解析失败"), 
	contentdamage(3, "数据内容缺失"), 
	wrongsignature(4, "签名验证失败"),
	othererrors(5, "其它错误");


	private String ebdResponseResult;
	private Integer code;

	private EBDRespResultEnum(Integer code, String ebdResponseResult) {
		this.code = code;
		this.ebdResponseResult = ebdResponseResult;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getEbdResponseResult() {
		return ebdResponseResult;
	}

	public void setEbdResponseResult(String ebdResponseResult) {
		this.ebdResponseResult = ebdResponseResult;
	}

	public static String getNameByCode(Integer code) {
		if (code == null) {
			return null;
		}
		for (EBDRespResultEnum respResultEnum : values()) {
			if (respResultEnum.getCode() == code) {
				return respResultEnum.getEbdResponseResult();
			}
		}
		return "";
	}

	public static Integer getCodeByName(String ebdResponseResult) {
		if (ebdResponseResult == null) {
			return null;
		}
		for (EBDRespResultEnum respResultEnum : values()) {
			if (respResultEnum.getEbdResponseResult().equals(ebdResponseResult)) {
				return respResultEnum.getCode();
			}
		}
		return null;
	}
}
