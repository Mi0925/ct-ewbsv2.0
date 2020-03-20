package cn.comtom.linkage.main.access.model.ebd.ebm;

/**
 * @author nobody
 * 关联信息
 */
public class RelatedInfo {
	
	/**
	 * 关联应急广播消息ID(可选)
	 */
	private String EBMID;
	
	/**
	 * 关联应急广播信息ID(可选)
	 */
	private String EBIID;

	/**
	 * @return the eBMID
	 */
	public String getEBMID() {
		return EBMID;
	}

	/**
	 * @param eBMID the eBMID to set
	 */
	public void setEBMID(String eBMID) {
		EBMID = eBMID;
	}

	/**
	 * @return the eBIID
	 */
	public String getEBIID() {
		return EBIID;
	}

	/**
	 * @param eBIID the eBIID to set
	 */
	public void setEBIID(String eBIID) {
		EBIID = eBIID;
	}
	
}
