package cn.comtom.linkage.main.access.service.impl;


import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordAddRequest;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.system.accessnode.constant.AccessEnum;
import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.linkage.commons.EBDRespResultEnum;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.EbmException;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.main.access.constant.AccessRecordEnum;
import cn.comtom.linkage.main.access.constant.FileEnum;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.AccessRecord;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBDResponse;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo;
import cn.comtom.linkage.main.access.model.ebd.validate.EBDValidator;
import cn.comtom.linkage.main.access.model.signature.Signature;
import cn.comtom.linkage.main.access.service.AccessService;
import cn.comtom.linkage.main.access.untils.HttpRequestUtil;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.common.service.TTSComponent;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.ISystemFeginService;
import cn.comtom.linkage.utils.*;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class AccessServiceImpl  implements AccessService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICommonService commonService;

    @Autowired
    private ISystemFeginService systemFeiginService;

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private EBMServiceManager ebmServiceManager;

    @Autowired
    private SequenceGenerate sequenceGenerate;

    @Autowired
    private SignValidateService signValidateService;

    @Autowired
    private TTSComponent ttsComponent;


    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        //1.接收流程处理
        EBDResponse eBDResponse=handleRequest(request);

       /* if(eBDResponse.getAccessRecord()!= null && EBDType.ConnectionCheck.name().equals(eBDResponse.getAccessRecord().getType()))
            return;*/

        //2.保存接入记录
        saveAccessRecord(eBDResponse.getAccessRecord());

        //3.组装通用反馈数据包并发送
        handleResponse(eBDResponse,response);
    }

    /**
     * 处理请求
     * step1.IP白名单校验
     * step2.数据接收
     * step3.数据解析
     * step4.身份认证
     *　step5.数据效验
     * @param request
     * @return
     */
    private EBDResponse handleRequest(HttpServletRequest request) {
        AccessRecord accessRecord=new AccessRecord();

        //step1.IP白名单校验
        String reqIp= IpUtil.getIpAddress(request);
        accessRecord.setReqIp(reqIp);
        logger.info(reqIp);
        Boolean whiteFlag = systemFeiginService.isWhite(reqIp);
        if(!whiteFlag){
            logger.error("【accessHandle】非法IP请求，ReqIp:{}",reqIp);
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.IP_CHECK_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.othererrors,accessRecord);
        }

        //step2.数据接收
        String filePath=commonService.getReceiveTempPath() + SymbolConstants.FILE_SPLIT + EBDType.EBD.name();

        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        MultipartFile t_multipartFile = null;
        String orginalFileName = null;
        Collection<MultipartFile> partFiles = mrequest.getFileMap().values();
        for (MultipartFile multipartFile : partFiles) {
            orginalFileName = multipartFile.getOriginalFilename();
            if (orginalFileName.endsWith(".tar")) {
                t_multipartFile = multipartFile;
                break;
            }
        }
        if (t_multipartFile == null) {
            logger.error("【accessHandle】接收tar文件为空");
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.RECEIVE_DATA_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.parsefailure,accessRecord);
        }
        // 接收的tar文件保存路径
        String tarFilePath = filePath;
        File tarDir = new File(tarFilePath);
        if (!tarDir.exists()) {
            tarDir.mkdirs();
        }
        // 设定所接收的文件的名称
        File file = new File(tarDir.getAbsolutePath() + SymbolConstants.FILE_SPLIT + orginalFileName);
        try {
            t_multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("【accessHandle】转换文件异常:" + e.getMessage());
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.RECEIVE_DATA_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.parsefailure,accessRecord);
        }
        String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(file));
        // 解压tar包到tar的文件夹路径下
        List<File> fileList = TarFileUtil.decompressorsTar(file);


        //step3.数据解析
        List<File> resources = new ArrayList<File>();
        File EBDBFile = null;
        File EBDSFile = null;

        for (File file2 : fileList) {
            String name = file2.getName();
            if (name.startsWith("EBDB")) {
                EBDBFile = file2;
            } else if (name.startsWith("EBDS")) {
                EBDSFile = file2;
            } else {
                resources.add(file2);
            }
        }
        // 业务数据文件
        if (EBDBFile == null) {
            logger.error("【accessHandle】获取业务数据文件为空");
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.PARSE_DATA_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.parsefailure,accessRecord);
        }
        // 将文件转换xml
        String eBDxmlString = null;
        try {
            eBDxmlString = FileUtils.readFileToString(EBDBFile, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("【accessHandle】读取业务文件异常:" + e.getMessage());
        }
        if (StringUtils.isEmpty(eBDxmlString)) {
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.PARSE_DATA_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.parsefailure,accessRecord);
        }
        // 转换对象
        EBD eBD= XmlUtil.fromXml(eBDxmlString, EBD.class);

        OriginFileInfo fileInfo = null;
        //增加心跳检测包不上传到FastDFS
        if(!eBD.getEBDType().equals(EBDType.ConnectionCheck.name())){
            //==========文件上传到fastDFS服务器并保存到资源服务，上传成功则删除本地临时文件，后续可以增加定时器扫描临时文件进行补漏=======
            String ftpReceiveFileName=file.getName();
            fileInfo = commonService.toFastDFS(FileEnum.FileTypeEnum.receive_file.getType(),ftpReceiveFileName, file.getAbsolutePath(), md5,eBD.getEBDType());
            if(fileInfo!=null){
                accessRecord.setFileId(fileInfo.getFileId());
            }
        }
        String fileId = Optional.ofNullable(fileInfo).map(OriginFileInfo::getFileId).orElse(SymbolConstants.EMPTY_STRING);


        //step4.身份认证
        String ebrid=eBD.getSRC().getEBRID();
        //根据ebrid查询接入节点对象，并判断状态是否正常，是则身份认证成功 ，反之身份认证失败
        AccessNodeInfo nodeInfo = systemFeiginService.getByPlatformId(ebrid);
        accessRecord.setNodeId(ebrid);
        accessRecord.setType(eBD.getEBDType());
        accessRecord.setEbdId(eBD.getEBDID());
        if(null==nodeInfo||nodeInfo.getStatus().equals(AccessEnum.AccessStatuEnum.STOP_STATU.getStatu())){
            logger.error("【accessHandle】身份认证失败，sys_access_node表无该节点资源记录或者节点已停用，ebrid:{}",ebrid);
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.AUTH_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.othererrors,accessRecord);
        }

        //step5.数据效验
        // 签名文件校验和非空字段校验
        String isSign = commonService.getSysParamValue(LinkageConstants.VERIFY_SIGN);
        if (EBDSFile != null && StringUtils.equals("1", isSign)) {
            // 签名验证
        	boolean verifySign = signValidateService.verifySign(EBDSFile, EBDBFile, eBD);
        	if(!verifySign) {
        		 logger.error("【accessHandle】签名验证不通过:");
                 return buildEBDResponse(EBDRespResultEnum.wrongsignature,accessRecord);
        	}
        }
        try{//非空验证
            validateDate(eBD);
        }catch (EbmException ebmException){
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.VERIFY_DATA_FAIL.getStatu());
            if (ebmException.getResultEnum() != null) {
                logger.error("【accessHandle】数据校验异常:" + ebmException.getMessage());
                return buildEBDResponse(ebmException.getResultEnum(),accessRecord);
            } else {
                return buildEBDResponse(EBDRespResultEnum.othererrors,accessRecord );
            }
        }
        /*if( StringUtils.equals(eBD.getEBDType(), EBDType.EBM.name())) {
        	if(eBD.getEBM().getMsgContent()!=null) {
        		String areaCode = eBD.getEBM().getMsgContent().getAreaCode();
        		areaCode = areaCode.substring(0, areaCode.length()-6);
        		eBD.getEBM().getMsgContent().setAreaCode(areaCode);
        	}
        }*/
        //step6.数据存储（业务处理）
        try {
        	/*String isLkxf = commonService.getSysParamValue("LKXF");
        	logger.info("isLkxf:{},type:{}", isLkxf,eBD.getEBDType());
	        	if(StringUtils.equals("1", isLkxf) && StringUtils.equals(eBD.getEBDType(), EBDType.EBM.name())) {
	        		RelatedInfo relatedInfo = new RelatedInfo();
	        		relatedInfo.setEBMID(eBD.getEBM().getEBMID());
	        		eBD.getEBM().setRelatedInfo(relatedInfo);
	        		//临时特殊处理，消息立刻下发
	        		 File tarAudioFile = null;
	        		 String requestUrl ="http://192.168.100.116:8488/linkage/access";
	        		if(CollectionUtils.isEmpty(resources)) {
	        			//文转语
	        			File textToAudioFile = ttsComponent.getTextToAudioFile(eBD.getEBM().getMsgContent().getMsgDesc());
	        			 fileList.add(textToAudioFile);
	        			 tarAudioFile = TarFileUtil.compressorsTar(fileList,  commonService.getReceiveTempPath());
	        		}else {
	        			//媒体文件
	            		tarAudioFile = file;
	        		}
	        		logger.info("下发文件tarAudioFile:{},地址requestUrl:{},TempPath:{}", tarAudioFile.getAbsolutePath(), requestUrl, commonService.getReceiveTempPath());
	        		EBD ebdPack = HttpRequestUtil.sendFile(tarAudioFile, requestUrl, commonService.getReceiveTempPath());
	        		logger.info("ebdPack"+ebdPack);
	        	}else {*/
	        		ebmServiceManager.dispatchService(eBD, fileId, resources);
	        	//}

        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("业务异常:" + e.getMessage());
            accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.SAVE_DATA_FAIL.getStatu());
            return buildEBDResponse(EBDRespResultEnum.othererrors,accessRecord);
        }
        logger.info("【accessHandle】接入成功!");
        accessRecord.setStatus(AccessRecordEnum.accessRecordStatuEnum.SUCCESS.getStatu());
        return buildEBDResponse(EBDRespResultEnum.receivevalid,accessRecord);
    }


    /**
     * 保存接入记录
     * @param accessRecord
     */
    private void saveAccessRecord(AccessRecord accessRecord) {
        AccessRecordAddRequest addRequest=new AccessRecordAddRequest();
        BeanUtils.copyProperties(accessRecord,addRequest);
        AccessRecordInfo info= coreFeginService.saveRecord(addRequest);
    }


    /**
     * 数据非空校验
     * @param ebd
     */
    private void validateDate(EBD ebd) {
        String errorMsg = new EBDValidator().validateEntity(ebd);
        if (null != errorMsg) {
            throw new EbmException(EBDRespResultEnum.contentdamage, errorMsg);
        }
    }

    private static EBDResponse buildEBDResponse(EBDRespResultEnum respResultEnum, AccessRecord accessRecord) {
        EBDResponse response = new EBDResponse();
        response.setResultCode(respResultEnum.getCode());
        response.setResultDesc(respResultEnum.getEbdResponseResult());
        response.setAccessRecord(accessRecord);
        return response;
    }

    /**
     * 发送通用反馈
     * @param eBDResponse
     * @param response
     */
    private void handleResponse(EBDResponse eBDResponse, HttpServletResponse response) {
        String destEbrId = eBDResponse.getAccessRecord().getNodeId();
        eBDResponse.setAccessRecord(null);//去除接入记录对象
        //1、封装回复数据包模型
        String ebrId=commonService.getEbrPlatFormID();
        String srcURL=commonService.getPlatFormUrl();
        String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
        EBD ebd = EBDModelBuild.buildResponse(ebrId, srcURL, ebdIndex, eBDResponse,destEbrId);

        // 通用反馈文件临时保存路径
        String ebdResponseFilePath=commonService.getSendTempPath(EBDType.EBDResponse.name());

        // 将对象转换为文件
        File file = FileUtil.converFile(ebdResponseFilePath, ebd);

        // 将回复数据包打成tar包
        List<File> files = new ArrayList<File>();
        files.add(file);
        String isSign = commonService.getSysParamValue(LinkageConstants.VERIFY_SIGN);
        if (StringUtils.equals("1", isSign)) {
            // 生成签名和签名文件============
            Signature signature = signValidateService.buildSignature(file, ebd.getEBDID());
            File signFile = FileUtil.converFile(ebdResponseFilePath, signature);
        	files.add(signFile);
        }

        File tarFile = TarFileUtil.compressorsTar(ebd, files, ebdResponseFilePath);
        // 2、发送通用反馈
        sendResponse(tarFile, response);

        //3、上传通用反馈tar包到fastDFS服务器
        String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(tarFile));
        OriginFileInfo fileInfoResponse = commonService.toFastDFS(FileEnum.FileTypeEnum.send_file.getType(),tarFile.getName(), tarFile.getAbsolutePath(),md5,EBDType.EBDResponse.name());

        //4、通用反馈记录存储
        ebmServiceManager.dispatchAfterService(ebd, fileInfoResponse.getFileId());


    }

    /**
     * 发送tar包文件回复请求
     *
     * @param tarfile
     * @param response
     */
    private void sendResponse(File tarfile, HttpServletResponse response) {
        synchronized(this) {
            // 以流的形式下载文件。
            response.setHeader("content-disposition", "attachment;filename=" + tarfile.getName());
            InputStream fis = null;
            OutputStream toClient = null;
            try {
                fis = new BufferedInputStream(new FileInputStream(tarfile));
                toClient = new BufferedOutputStream(response.getOutputStream());
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = fis.read(b)) != -1) {
                    toClient.write(b, 0, len);
                }
                toClient.flush();
            } catch (Exception e) {
                logger.error("回复请求异常:" + e.getMessage());
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fis);
                IOUtils.closeQuietly(toClient); //TODO  您的主机中的软件中止了一个已建立的连接。
            }
        }

    }

}
