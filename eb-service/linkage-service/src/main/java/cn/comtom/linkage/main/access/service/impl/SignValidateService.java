package cn.comtom.linkage.main.access.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD;
import cn.comtom.linkage.main.access.model.signature.Signature;
import cn.comtom.linkage.main.access.model.signature.SignatureCert;
import cn.comtom.linkage.main.fegin.service.ISignatureFeginService;
import cn.comtom.linkage.utils.XmlUtil;

/**
 * @author nobody 签名文件的解析验证
 */
@Service
public class SignValidateService {
	
	@Autowired
	private ISignatureFeginService signatureFeginService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public boolean verifySign(File file, File ebdFile, EBD eBD) {

		if (logger.isInfoEnabled()) {
			logger.info("SignValidateService.verifySign start.");
		}
		boolean result = false;
		// 将文件转换xml
		String xmlString;
		try {
			xmlString = FileUtils.readFileToString(file, "utf-8");

			// 签名对象
			Signature sigNature = XmlUtil.fromXml(xmlString);
			/*String relatedEBDID = sigNature.getRelatedEBD().getEBDID();
			if (!eBD.getEBDID().equals(relatedEBDID)) {
				return false;
			}*/
			 String certSN = sigNature.getCertSN();
			 //String signCounter = sigNature.getSignatureCert().getIssuerID();
			 //String digestAlgorithm = sigNature.getDigestAlgorithm();
			 String signatureAlgorithm = sigNature.getSignatureAlgorithm();
			 String signatureValue = sigNature.getSignatureValue();

			// 验证签名
			 result = signatureFeginService.verifySign(ebdFile, signatureValue);
		} catch (IOException e) {
			logger.error("Sign validate exception.", e.getMessage());
		}

		return result;
	}
	
	/**
	 * 生成Signature对象
	 * 
	 * @param eBDID
	 *            数据包ID
	 * @return
	 */
	public  Signature buildSignature(File xmlFile, String eBDID) {

		// 生成签名文件
		Signature sign = new Signature();
		sign.setVersion("1.0");

		RelatedEBD rEbd = new RelatedEBD();
		rEbd.setEBDID(eBDID);
		sign.setRelatedEBD(rEbd);
		SignatureCert cert = new SignatureCert();
		cert.setCertType("");
		cert.setIssuerID("");
		cert.setCertSN("");
		sign.setCertSN("");
		// 生成签名
		String signStr = signatureFeginService.getSign(xmlFile);
		// TODO 签名用不了的情况下使用一个假的
		// String signStr = "123456789test";
		sign.setSignatureValue(signStr);
		//sign.setSignatureTime(DateTimeUtil.dateToString(new Date()));
		//sign.setDigestAlgorithm("SM3");
		sign.setSignatureAlgorithm("SM2-SM3");
		//sign.setSignatureCert(cert);

		return sign;
	}
	
}
