package cn.comtom.linkage.main.common.service.impl;

import cn.comtom.domain.core.ebd.request.EbdAddRequest;
import cn.comtom.domain.core.flow.request.DispatchFlowUpdateRequest;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.main.access.constant.FileEnum;
import cn.comtom.linkage.main.access.constant.SendFlag;
import cn.comtom.linkage.main.access.model.Resource;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.DEST;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.ResoFegin;
import cn.comtom.linkage.main.fegin.SystemFegin;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.TarFileUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.constants.RedisKeyConstants;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.utils.DateUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 专门用来调用fegin的service，将fegin返回的接口响应对象的业务数据提取出来
 */
@Service
@Slf4j
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private SystemFegin systemFegin;

    @Autowired
    private ResoFegin resoFegin;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private SequenceGenerate sequenceGenerate;

    @Autowired
    private FastFileStorageClient storageClient;


    /**
     * 获取系统参数值
     * 先从静态Map中取值，取不到再查询数据库，并将查询到的系统参数放到map中.避免重复访问数据库
     *
     * @param key
     * @return
     */
    public String getSysParamValue(String key) {
        if (log.isDebugEnabled()) {
            log.debug("get system params by key . key=[{}]", key);
        }
        ApiEntityResponse<SysParamsInfo> paramsInfo = systemFegin.getByKey(key);
        if (paramsInfo.getSuccessful()) {
            return paramsInfo.getData().getParamValue();
        }
        return null;
    }

    /**
     * 获取发送文件临时路径
     *
     * @param ebdType
     * @return
     */
    @Override
    public String getSendTempPath(String ebdType) {
        String tempFilePath = getSysParamValue(Constants.TEMP_FILE_PATH);
        if (tempFilePath != null) {
            tempFilePath += LinkageConstants.SEND + SymbolConstants.FILE_SPLIT + ebdType;
        }
        return tempFilePath;
    }

    /**
     * 获取接收文件临时目录
     * @return
     */
    @Override
    public String getReceiveTempPath() {
        String tempFilePath = getSysParamValue(Constants.TEMP_FILE_PATH);
        if (tempFilePath != null) {
            tempFilePath += LinkageConstants.RECEIVE;
        }
        return tempFilePath;
    }


    /**
     * 线程池上传数据包到文件服务器并保存到资源服务器
     */
    @Override
    public OriginFileInfo toFastDFS(String fileType, String fileName, String curFilePath, String md5Code, String subDir) {
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(SymbolConstants.SUFFIX_SPLIT) + 1);
        //根据MD5查询文件，如果文件不存在则上传。
        OriginFileInfo fileInfo = resoFeginService.getByMd5(md5Code);
        if (fileInfo == null) {
            //保存媒资表
            OriginFileAddRequest fileAddRequest = new OriginFileAddRequest();
            fileAddRequest.setFileType(fileType);
            fileAddRequest.setOriginName(fileName);
            fileAddRequest.setStatus(FileEnum.FileStatuEnum.normal_statu.getStatu());
            fileAddRequest.setFileExt(suffix);
            fileAddRequest.setMd5Code(md5Code);
            File file = new File(curFilePath);
            if (file.exists()) {
                Long fileSize = file.length();
                fileAddRequest.setFileSize(fileSize);//文件大小
            } else {
                log.error("文件：{} 不存在", curFilePath);
                return null;
            }
            //上传文件
            try {
                StorePath storePath = storageClient.uploadFile(new FileInputStream(file), file.length(), suffix, null);
                //URl存储在fastdfs上的路径
                fileAddRequest.setUrl(storePath.getFullPath());
                fileInfo = resoFeginService.saveOriginFile(fileAddRequest);
            } catch (FileNotFoundException e) {
                log.error("上传文件失败：{}\nERROR：{}", file.getName(), e);
                return null;
            }
        }
        return fileInfo;
    }

    @Override
    public OriginFileInfo toFastDFS(String fileType, File file) {
        String fileName = file.getName();
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(SymbolConstants.SUFFIX_SPLIT) + 1);
        //根据MD5查询文件，如果文件不存在则上传。
        String md5Code = DigestUtils.md5Hex(FileUtil.file2bytes(file));
        OriginFileInfo fileInfo = resoFeginService.getByMd5(md5Code);
        if (fileInfo == null) {
            //保存媒资表
            OriginFileAddRequest fileAddRequest = new OriginFileAddRequest();
            fileAddRequest.setFileType(fileType);
            fileAddRequest.setOriginName(fileName);
            fileAddRequest.setStatus(FileEnum.FileStatuEnum.normal_statu.getStatu());
            fileAddRequest.setFileExt(suffix);
            fileAddRequest.setMd5Code(md5Code);
            fileAddRequest.setFileSize(file.length());
            //上传文件
            try {
                StorePath storePath = storageClient.uploadFile(new FileInputStream(file), file.length(), suffix, null);
                //URl存储在fastdfs上的路径
                fileAddRequest.setUrl(storePath.getFullPath());
                fileInfo = resoFeginService.saveOriginFile(fileAddRequest);
            } catch (FileNotFoundException e) {
                log.error("上传文件失败：{}\nERROR：{}", file.getName(), e);
                return null;
            }
        }
        return fileInfo;
    }

    @Override
    public String getEbrPlatFormID() {
        ApiEntityResponse<SysParamsInfo> ebrIdParamsInfo = systemFegin.getByKey(Constants.EBR_PLATFORM_ID);
        if (ebrIdParamsInfo.getSuccessful()) {
            return ebrIdParamsInfo.getData().getParamValue();

        }
        return null;
    }

    @Override
    public String getPlatFormUrl() {
        String ebrId = getEbrPlatFormID();
        ApiEntityResponse<EbrPlatformInfo> platResponse = resoFegin.getEbrPlatformById(ebrId);
        if (platResponse.getSuccessful()) {
            return platResponse.getData().getPsUrl();
        }
        return null;
    }

    @Override
    public String getFtpEbmDispatchFilePath() {
        String tempFilePath = getSysParamValue(Constants.FTP_FILE_PATH);
        if (tempFilePath != null) {
            tempFilePath += LinkageConstants.SEND + SymbolConstants.FILE_SPLIT + EBDType.EBM.name();
        }
        return tempFilePath;
    }

    @Override
    public String getEbdResponseFtpPath() {
        String tempFilePath = getSysParamValue(Constants.FTP_FILE_PATH);
        if (tempFilePath != null) {
            tempFilePath += LinkageConstants.RECEIVE + SymbolConstants.FILE_SPLIT + EBDType.EBDResponse.name();
        }
        return tempFilePath;
    }


    @Override
    public String getFtpFilePath(String fileType, String subDir) {
        String ftpReceiveFilePath = "";
        ApiEntityResponse<SysParamsInfo> ftpReceiveParamsInfo = systemFegin.getByKey(Constants.FTP_FILE_PATH);
        if (ftpReceiveParamsInfo.getSuccessful()) {
            ftpReceiveFilePath = ftpReceiveParamsInfo.getData().getParamValue();
            if (fileType.equals(FileEnum.FileTypeEnum.receive_file.getType())) {
                ftpReceiveFilePath = ftpReceiveFilePath + LinkageConstants.RECEIVE;
            } else {
                ftpReceiveFilePath = ftpReceiveFilePath + LinkageConstants.SEND;
            }
            if (StringUtils.isNotBlank(subDir)) {
                ftpReceiveFilePath += SymbolConstants.FILE_SPLIT + subDir;
            }
        }
        return ftpReceiveFilePath;
    }

    /**
     * 更新调度流程状态
     */
    public void updateDispatchFlowState(String flowId, String flowStage, String flowState) {
        DispatchFlowUpdateRequest request = new DispatchFlowUpdateRequest();
        request.setFlowId(flowId);
        request.setFlowStage(flowStage);
        request.setFlowState(flowState);
        request.setUpdateTime(new Date());
        boolean flag = coreFeginService.updateDispatchFlow(request);
        if (!flag) {
            log.error("##########   更新调度流程状态失败。");
        }
    }

    @Override
    public String getParentPlatUrl() {
        String parentPlatformId = getSysParamValue(Constants.PARENT_PLATFORM_ID);
        if (StringUtils.isBlank(parentPlatformId) || ("NULL".equals(parentPlatformId))) {
            return null;
        }
        EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(parentPlatformId);
        return ebrPlatform == null ? null : ebrPlatform.getPsUrl();
    }

    @Override
    public String getParentPlatId() {
        return getSysParamValue(Constants.PARENT_PLATFORM_ID);
    }

    @Override
    public String getEbdId() {
        return sequenceGenerate.createId(LinkageConstants.EBDID);
    }

    @Override
    public String getEbmId() {
        return sequenceGenerate.createId(LinkageConstants.EBMID);
    }

    @Override
    public String getConnectionCkeckKey(String ebrId) {
        return String.format(RedisKeyConstants.CONNECTION_CHECK_KEY_FORMAT, ebrId);
    }

    @Override
    public String getFtpMediaPath() {
        String tempFilePath = getSysParamValue(Constants.FTP_FILE_PATH);
        if (tempFilePath != null) {
            tempFilePath += LinkageConstants.MEDIA;
        }
        return tempFilePath;
    }


    /**
     * 解析ebrId获取对应信息
     *
     * @param ebrId
     * @return
     */
    public Resource parseResourceId(String ebrId) {
        Resource resourceModel = new Resource();
        Integer platLevel = null;
        String areaCode = null;
        String psType = ebrId.substring(2, 4);
        String areaCodeAll = ebrId.substring(4, 16);
        if (areaCodeAll.endsWith("0000000000")) {
            platLevel = 1;
            areaCode = areaCodeAll.substring(0, 2);
        } else if (areaCodeAll.endsWith("00000000")) {
            platLevel = 2;
            areaCode = areaCodeAll.substring(0, 4);
        } else if (areaCodeAll.endsWith("000000")) {
            platLevel = 3;
            areaCode = areaCodeAll.substring(0, 6);
        } else if (areaCodeAll.endsWith("000")) {
            platLevel = 4;
            areaCode = areaCodeAll.substring(0, 9);
        } else {
            platLevel = 5;
            areaCode = areaCodeAll;
        }
        resourceModel.setAreaCode(areaCode);
        resourceModel.setPlatLevel(platLevel);
        resourceModel.setResourceType(psType);
        return resourceModel;
    }

    /**
     * 增加反馈包到FastDFS
     * @param ebd
     * @param ebdState
     * @param sendFlag
     */
    @Override
    public void saveAndUploadResponseEbdPack(EBD ebd, String ebdState, String sendFlag) {
        //获取接收文件临时目录
        String receiveFilePath = getReceiveTempPath();
        receiveFilePath += SymbolConstants.FILE_SPLIT + EBDType.EBDResponse.name();
        //生成反馈xml文件
        File responseXmlFile = FileUtil.converFile(receiveFilePath, ebd);
        List<File> rspFileList = new ArrayList<>();
        rspFileList.add(responseXmlFile);
        //将反馈文件打包
        File responseTarFile = TarFileUtil.compressorsTar(ebd, rspFileList, receiveFilePath);
        String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(responseTarFile));
        //将tar包上传到fastDFS
        OriginFileInfo originFileInfo = toFastDFS(FileEnum.FileTypeEnum.receive_file.getType(), responseTarFile.getName(), responseTarFile.getAbsolutePath(), md5, EBDType.EBDResponse.name());

        //保存数据包信息到本地数据库
        saveEbdData(ebd, responseTarFile.getName(), ebdState, sendFlag, null, originFileInfo.getFileId());
    }

    /**
     * 保存数据包信息到本地数据库
     * @param ebd
     * @param ebdName
     * @param ebdState
     * @param sengFlag
     * @param flowId
     * @param fileId
     */
    public void saveEbdData(EBD ebd, String ebdName, String ebdState, String sengFlag, String flowId, String fileId) {
        // 必选
        String ebdVersion = ebd.getEBDVersion();
        // 必选
        String ebdId = ebd.getEBDID();
        // 必选
        String ebdType = ebd.getEBDType();
        // 必选
        String ebdTimeString = ebd.getEBDTime();
        Date ebdTime = DateTimeUtil.stringToDate(ebdTimeString,
                DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
        // 必选
        SRC src = ebd.getSRC();
        // 必选资源ID
        String ebdSrcEbrId = src.getEBRID();
        // 可选
        String ebdSrcUrl = src.getURL();
        // 可选
        DEST dest = ebd.getDEST();
        String ebdDestEbrId = null;
        if (dest != null) {
            // 目标对象的资源ID 必选
            ebdDestEbrId = dest.getEBRID();
        }
        // 可选
        RelatedEBD relatedEBD = ebd.getRelatedEBD();
        String relateEbdId = null;
        if (relatedEBD != null) {
            // 必选
            relateEbdId = relatedEBD.getEBDID();
        }

        EbdAddRequest ebdAddRequest = new EbdAddRequest();
        ebdAddRequest.setEbdId(ebdId);
        ebdAddRequest.setEbdVersion(ebdVersion);
        ebdAddRequest.setEbdType(ebdType);
        ebdAddRequest.setEbdName(ebdName);
        ebdAddRequest.setEbdSrcEbrId(ebdSrcEbrId);
        ebdAddRequest.setEbdDestEbrId(ebdDestEbrId);
        ebdAddRequest.setEbdTime(ebdTime);
        ebdAddRequest.setRelateEbdId(relateEbdId);
        ebdAddRequest.setEbdSrcUrl(ebdSrcUrl);
        ebdAddRequest.setFileId(fileId);

        ebdAddRequest.setSendFlag(sengFlag);
        if (StringUtils.isNotBlank(ebdState)) {
            ebdAddRequest.setEbdState(ebdState);
        } else {
            ebdAddRequest.setEbdState(StateDictEnum.EBD_STATE_CREATE.getKey());
        }
        ebdAddRequest.setFlowId(flowId);

        //保存时间====================================
        if (SendFlag.receive.equals(sengFlag)) {
            ebdAddRequest.setEbdRecvTime(new Date());
        } else if (SendFlag.send.equals(sengFlag)) {
            ebdAddRequest.setEbdSendTime(new Date());
        }

        // 如果是EBM消息，设置EBD关联的EBM消息Id
        if (ebd.getEBM() != null) {
            ebdAddRequest.setEbmId(ebd.getEBM().getEBMID());
        }
        coreFeginService.saveEbd(ebdAddRequest);
    }

    /**
     * 下载FastDFS上的数据包文件
     * @param fileUrl fastDFS路径
     * @param originName 文件名
     * @param filePath 文件本地存储路径
     * @return
     */
    @Override
    public File downloadFile(String fileUrl, String originName, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            log.debug("文件夹[{}]不存在，创建文件夹", filePath);
            file.mkdirs();
        }
        //拼装本地文件路径
        String localPath = filePath.concat(SymbolConstants.FILE_SPLIT).concat(originName);
        //使用fastdfsclient下载文件
//        DownloadFileWriter localWriter = new DownloadFileWriter(localPath);
//        storageClient.downloadFile(fileUrl.substring(0, fileUrl.indexOf("/")), fileUrl.substring(fileUrl.indexOf("/") + 1), localWriter);
//        //返回文件
//        return new File(localPath);
        OutputStream out;
        try {
            out = new FileOutputStream(localPath);
        } catch (FileNotFoundException e) {
            log.error("本地文件不存在", e);
            return null;
        }
         return storageClient.downloadFile(fileUrl.substring(0, fileUrl.indexOf("/")), fileUrl.substring(fileUrl.indexOf("/") + 1), ins -> {
            try {
                IOUtils.copy(ins, out);
                out.flush();
            } finally {
                IOUtils.closeQuietly(ins);
                IOUtils.closeQuietly(out);
            }
            return new File(localPath);
        });
    }

    @Override
    public String getEbmDispatchInfoTitle(String content) {
        return DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD).concat(content).concat(Constants.EBM_DIAPATCH_INFO_TITLE_SUFFIX);
    }

    @Override
    public String getEbmFileLib() {
        return getSysParamValue(Constants.EBM_FILE_LIBRARY_ID);
    }
}
