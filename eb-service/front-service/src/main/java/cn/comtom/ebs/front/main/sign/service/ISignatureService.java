package cn.comtom.ebs.front.main.sign.service;

public interface ISignatureService {
	
    Boolean importCerth(byte[] certauth);
    
    Boolean importCertauth(byte[] certh);

}
