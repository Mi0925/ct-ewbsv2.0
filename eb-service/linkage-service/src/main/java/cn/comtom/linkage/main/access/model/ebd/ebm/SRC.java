package cn.comtom.linkage.main.access.model.ebd.ebm;

/**
 * @author nobody
 * 数据包来源对象
 */
public class SRC {
	
	/**
	 * 数据包来源对象ID，为18位数字码
	 */
	private String EBRID;
	
	/**
	 * 数据包来源对象URL地址(可选)
	 */
	private String URL;

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
	
}
