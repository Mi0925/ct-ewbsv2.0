package cn.comtom.signature.main.service;

import cn.comtom.domain.signature.req.CertApplyReq;
import cn.comtom.domain.signature.resp.CertResp;
import cn.tass.exceptions.YJException;

public interface ISignatureService {

    String getSign(byte[] data);

    Boolean verifySign(byte[] data, String signValue) throws YJException;

    CertResp certApply(CertApplyReq certApplyReq);

	Boolean importCerth(byte[] certh) throws YJException ;

	Boolean importCertauth(byte[] certauth) throws YJException ;
	
	public String getFileSign(byte[] data);
	
	 public  Boolean verifyFileSign(byte[] data, String signValue)throws YJException;
}
