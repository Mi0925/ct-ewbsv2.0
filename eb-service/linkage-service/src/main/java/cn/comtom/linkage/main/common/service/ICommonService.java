package cn.comtom.linkage.main.common.service;

import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.linkage.main.access.model.Resource;
import cn.comtom.linkage.main.access.model.ebd.EBD;

import java.io.File;
import java.util.List;

public interface ICommonService {

    public String getSysParamValue(String key);


    public String getSendTempPath(String dbdType);


    public String getReceiveTempPath();


    public String getFtpFilePath(String fileType,String subDir);

    public OriginFileInfo toFastDFS(String fileType , String fileName, String curFilePath, String md5Code,String subDir);

    OriginFileInfo toFastDFS(String fileType, File file);

    public String getEbrPlatFormID();

    public String getPlatFormUrl();

    public String getFtpEbmDispatchFilePath();

    public String getEbdResponseFtpPath();

    public void updateDispatchFlowState(String flowId,String flowStage,String flowState);

    public String getParentPlatUrl();

    public String getParentPlatId();

    String getEbdId();

    String getEbmId();

    String getConnectionCkeckKey(String ebrId);

    String getFtpMediaPath();

    void saveAndUploadResponseEbdPack(EBD ebd,String ebdState,String sendFlag);

    public Resource parseResourceId(String ebrId);

    void saveEbdData(EBD ebd, String ebdName,  String ebdState,String sendFlag ,String flowId,String fileId);

    File downloadFile(String fileUrl, String originName, String filePath);

    String getEbmDispatchInfoTitle(String content);


    String getEbmFileLib();
}

