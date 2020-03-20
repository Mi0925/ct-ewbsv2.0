package cn.comtom.core.main.sichuan.service;

import cn.comtom.domain.reso.file.info.OriginFileInfo;

import java.io.File;
import java.util.List;

public interface ICommonService {


    public String getReceiveTempPath();


    public String getFtpFilePath(String fileType,String subDir);

    public OriginFileInfo toFtp(String fileType , String fileName, String curFilePath, String md5Code,String subDir);

    public String getEbrPlatFormID();

    public String getPlatFormUrl();


    public String getParentPlatUrl();

    public String getParentPlatId();


    String getConnectionCkeckKey(String ebrId);

    String getFtpMediaPath();

    List<File> downloadFile(String fileUrl, String originName, String filePath);



}

