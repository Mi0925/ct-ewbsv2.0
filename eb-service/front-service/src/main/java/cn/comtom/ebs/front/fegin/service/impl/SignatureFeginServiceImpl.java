package cn.comtom.ebs.front.fegin.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.signature.req.CertImportReq;
import cn.comtom.ebs.front.fegin.SignatureFegin;
import cn.comtom.ebs.front.fegin.service.FeginBaseService;
import cn.comtom.ebs.front.fegin.service.ISignatureFeginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SignatureFeginServiceImpl extends FeginBaseService implements ISignatureFeginService {
	 
	@Autowired
    private SignatureFegin signatureFegin;
	
	@Override
	public boolean verifySign(File xmlFile, String signValue) {
		return getBoolean(signatureFegin.verifySign(xmlFile, signValue));
	}

	@Override
	public String getSign(File xmlFile) {
		return getData(signatureFegin.getSign(xmlFile));
	}

	@Override
	public Boolean importCerth(CertImportReq request) {
		return getBoolean(signatureFegin.importCerth(request));
	}

	@Override
	public Boolean importCertauth(CertImportReq request) {
		return getBoolean(signatureFegin.importCertauth(request));
	}

}
