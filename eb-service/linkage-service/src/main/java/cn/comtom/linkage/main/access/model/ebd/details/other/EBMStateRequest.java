package cn.comtom.linkage.main.access.model.ebd.details.other;

/**
 * @author nobody
 * 应急广播消息播发状态查询
 */
public class EBMStateRequest {
	
	/**
	 * 应急广播消息(只需要传递ID查询)
	 */
	private EBM EBM;

	/**
	 * @return the eBM(只需要传递ID查询)
	 */
	public EBM getEBM() {
		return EBM;
	}

	/**
	 * @param eBM(只需要传递ID查询) the eBM to set
	 */
	public void setEBM(EBM eBM) {
		EBM = eBM;
	}
	
}
