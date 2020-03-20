package cn.comtom.ebs.front.main.sign.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.signature.req.CertImportReq;
import cn.comtom.ebs.front.fegin.service.ISignatureFeginService;
import cn.comtom.ebs.front.main.sign.service.ISignatureService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignatureServiceImpl implements ISignatureService{

    @Autowired
    private ISignatureFeginService signatureFeginService;

	@Override
	public Boolean importCerth(byte[] certh) {
		CertImportReq certImportReq = new CertImportReq();
		certImportReq.setCerth(certh);
		return signatureFeginService.importCerth(certImportReq);
	}

	@Override
	public Boolean importCertauth(byte[] certauth) {
		CertImportReq certImportReq = new CertImportReq();
		certImportReq.setCertauth(certauth);
		return signatureFeginService.importCertauth(certImportReq);
	}
    

}
