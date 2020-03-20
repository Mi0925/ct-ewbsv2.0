package cn.comtom.linkage.main.access.model.ebd.ebm;

/**
 * @author nobody
 * 数据包目标对象
 * 接收该数据包的对象
 */
public class DEST {
	/**
	 * 数据包来源对象ID，为18位数字码
	 */
	private String EBRID;

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
}
