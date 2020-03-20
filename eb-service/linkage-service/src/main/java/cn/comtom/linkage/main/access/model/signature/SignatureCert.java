package cn.comtom.linkage.main.access.model.signature;

/**
 * @author nobody 签名证书《应急广播信息安全保护技术规范》
 */
public class SignatureCert {

	/**
	 * 证书类型
	 */
	private String CertType;

	/**
	 * 证书签发者ID
	 */
	private String IssuerID;

	/**
	 * 证书序列号
	 */
	private String CertSN;

	/**
	 * @return the certType
	 */
	public String getCertType() {
		return CertType;
	}

	/**
	 * @param certType
	 *            the certType to set
	 */
	public void setCertType(String certType) {
		CertType = certType;
	}

	/**
	 * @return the issuerID
	 */
	public String getIssuerID() {
		return IssuerID;
	}

	/**
	 * @param issuerID
	 *            the issuerID to set
	 */
	public void setIssuerID(String issuerID) {
		IssuerID = issuerID;
	}

	/**
	 * @return the certSN
	 */
	public String getCertSN() {
		return CertSN;
	}

	/**
	 * @param certSN
	 *            the certSN to set
	 */
	public void setCertSN(String certSN) {
		CertSN = certSN;
	}

}
