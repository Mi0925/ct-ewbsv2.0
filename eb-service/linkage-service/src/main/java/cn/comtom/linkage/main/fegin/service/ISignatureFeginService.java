package cn.comtom.linkage.main.fegin.service;

import java.io.File;

public interface ISignatureFeginService {

    Boolean verifySign(File xmlFile, String signValue);

    String getSign(File xmlFile);
}
