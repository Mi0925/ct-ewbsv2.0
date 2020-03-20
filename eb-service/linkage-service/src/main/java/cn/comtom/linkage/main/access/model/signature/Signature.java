package cn.comtom.linkage.main.access.model.signature;

import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBInfo;

/**
 * @author nobody
 *
 */
public class Signature {

	/**
	 * 协议版本，目前为1.0
	 */
	private String Version = "1.0";

	/**
	 * 关联EBD数据包
	 */
	private RelatedEBD RelatedEBD;
	
	private RelatedEBInfo RelatedEBInfo;

	/**
	 * 签名证书信息
	 */
	//private SignatureCert SignatureCert;

	/**
	 * 签名时间，16进制数字符串
	 */
	//private String SignatureTime;
	private String CertSN;
	/**
	 * 摘要算法，此处采用SM3算法
	 */
	//private String DigestAlgorithm;

	/**
	 * 签名算法，此处采用SM2算法
	 */
	private String SignatureAlgorithm;

	/**
	 * 签名值,采用base64编码
	 */
	private String SignatureValue;

	/**
	 * @return the version
	 */
	public String getVersion() {
		return Version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		Version = version;
	}

	/**
	 * @return the relatedEBD
	 */
	public RelatedEBD getRelatedEBD() {
		return RelatedEBD;
	}

	/**
	 * @param relatedEBD
	 *            the relatedEBD to set
	 */
	public void setRelatedEBD(RelatedEBD relatedEBD) {
		RelatedEBD = relatedEBD;
	}

	/**
	 * @return the signatureCert
	 */
//	public SignatureCert getSignatureCert() {
//		return SignatureCert;
//	}

	/**
	 * @param signatureCert
	 *            the signatureCert to set
	 */
//	public void setSignatureCert(SignatureCert signatureCert) {
//		SignatureCert = signatureCert;
//	}

	/**
	 * @return the signatureTime
	 */
//	public String getSignatureTime() {
//		return SignatureTime;
//	}

	/**
	 * @param signatureTime
	 *            the signatureTime to set
	 */
//	public void setSignatureTime(String signatureTime) {
//		SignatureTime = signatureTime;
//	}

	/**
	 * @return the digestAlgorithm
	 */
//	public String getDigestAlgorithm() {
//		return DigestAlgorithm;
//	}

	/**
	 * @param digestAlgorithm
	 *            the digestAlgorithm to set
	 */
//	public void setDigestAlgorithm(String digestAlgorithm) {
//		DigestAlgorithm = digestAlgorithm;
//	}

	/**
	 * @return the signatureAlgorithm
	 */
	public String getSignatureAlgorithm() {
		return SignatureAlgorithm;
	}

	/**
	 * @param signatureAlgorithm
	 *            the signatureAlgorithm to set
	 */
	public void setSignatureAlgorithm(String signatureAlgorithm) {
		SignatureAlgorithm = signatureAlgorithm;
	}

	/**
	 * @return the signatureValue
	 */
	public String getSignatureValue() {
		return SignatureValue;
	}

	/**
	 * @param signatureValue
	 *            the signatureValue to set
	 */
	public void setSignatureValue(String signatureValue) {
		SignatureValue = signatureValue;
	}

	public String getCertSN() {
		return CertSN;
	}

	public void setCertSN(String certSN) {
		CertSN = certSN;
	}

	public RelatedEBInfo getRelatedEBInfo() {
		return RelatedEBInfo;
	}

	public void setRelatedEBInfo(RelatedEBInfo relatedEBInfo) {
		RelatedEBInfo = relatedEBInfo;
	}
	
}
