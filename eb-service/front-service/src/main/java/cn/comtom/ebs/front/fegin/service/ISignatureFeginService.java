package cn.comtom.ebs.front.fegin.service;

import java.io.File;

import cn.comtom.domain.signature.req.CertImportReq;

public interface ISignatureFeginService {

	boolean verifySign(File xmlFile, String signValue);

	String getSign(File xmlFile);

	Boolean importCerth(CertImportReq request);

	Boolean importCertauth(CertImportReq request);
}
