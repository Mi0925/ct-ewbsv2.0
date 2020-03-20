package cn.comtom.linkage.commons;

public class EbmException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private EBDRespResultEnum resultEnum;
	
	private String message;

	public EbmException(EBDRespResultEnum resultEnum, String message) {
		super();
		this.resultEnum = resultEnum;
		this.message = message;
	}

	/**
	 * @return the resultEnum
	 */
	public EBDRespResultEnum getResultEnum() {
		return resultEnum;
	}

	/**
	 * @param resultEnum the resultEnum to set
	 */
	public void setResultEnum(EBDRespResultEnum resultEnum) {
		this.resultEnum = resultEnum;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
