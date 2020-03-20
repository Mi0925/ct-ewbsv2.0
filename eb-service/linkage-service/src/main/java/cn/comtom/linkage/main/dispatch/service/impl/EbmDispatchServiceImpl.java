package cn.comtom.linkage.main.dispatch.service.impl;

import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdUpdateRequest;
import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmAddRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchPageRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchUpdateRequest;
import cn.comtom.domain.core.ebm.request.EbmUpdateRequest;
import cn.comtom.domain.core.program.info.ProgramFilesInfo;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.reso.ebr.info.*;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.main.access.constant.FileEnum;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRST;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.ebm.*;
import cn.comtom.linkage.main.access.model.signature.Signature;
import cn.comtom.linkage.main.access.service.impl.SignValidateService;
import cn.comtom.linkage.main.access.untils.HttpRequestUtil;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.dispatch.service.IEbmDispatchService;
import cn.comtom.linkage.main.dispatch.service.ITextInsertionService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.main.fegin.service.ISystemFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.TarFileUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import cn.comtom.tools.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EbmDispatchServiceImpl implements IEbmDispatchService {

    @Autowired
    private ICommonService commonService;

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private MQMessageProducer mqMessageProducer;

    @Autowired
    private ITextInsertionService textInsertionService;

    @Autowired
    private SignValidateService signValidateService;

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SQL_ORDER_COLUMN = "dispatchTime";


    /**
     * 分发之前，先生成待分发的新的ebd数据包；
     * 将待分发的新的ebd数据包上传到文件服务器
     * 保存新的ebd记录，ebd状态为创建
     *
     * @param ebd
     */
    @Override
    public void beforeDispatch(EBD ebd) {
    	log.info("【EBMBeforeDispatch】~MQ消息分发前置处理start,ebdId:{}",ebd.getEBDID());
        EbdInfo ebdInfo = coreFeginService.getEbdInfoByEbdId(ebd.getEBDID());
        if (ebdInfo != null) {
            //ebd数据包已经生成，判断为失败重发的，不再生成ebd数据包，将已存在的数据包重新发送
            if (auditedEbmDispatchInfo(ebdInfo.getEbmId())) {
                mqMessageProducer.sendData(MqQueueConstant.EBM_DISPATCH_QUEUE, ebd.getEBDID());
            }
            return;
        }

     //   commonService.updateDispatchFlowState(ebdInfo.getFlowId(), FlowConstants.STAGE_PROCESS, FlowConstants.STATE_MSG_CREATE);


        String requestUrl = null;

        EBM ebm = ebd.getEBM();
        if (ebm == null) {
        	log.error("【EBMBeforeDispatch】~EBM为空,ebdId:{}",ebd.getEBDID());
            return;
        }

        //产生ebm分发消息
      /*  EbmInfo ebmInfo = coreFeginService.getEbmInfoById(ebm.getEBMID());
        saveEbmDispatchInfoInfo(ebmInfo);*/


        List<Dispatch> dispatchList = ebm.getDispatchList();
        if (dispatchList == null || dispatchList.isEmpty()) {
        	log.error("【EBMBeforeDispatch】~消息下发资源Dispatch为空,ebdId:{}",ebd.getEBDID());
            return;
        }
        for (Dispatch dispatch : dispatchList) {
            EBRPS ebrPs = dispatch.getEBRPS();
            EBRBS ebrBs = dispatch.getEBRBS();
            EBRST ebrst = dispatch.getEBRST();
            EBRAS ebras = dispatch.getEBRAS();
            if (ebrPs != null) {
                requestUrl = ebrPs.getURL();
                ebrPs.setURL(null);
            } else if (ebrBs != null) {
                requestUrl = ebrBs.getURL();
                ebrBs.setURL(null);
            }else if(ebrst!=null) {
            	 requestUrl = ebrst.getAddress();
            }else if(ebras!=null) {
            	 requestUrl = ebras.getURL();
            	 ebras.setURL(null);
            }
        }

        if (StringUtils.isEmpty(ebm.getMsgContent().getAreaCode())) {
            log.error("【EBMBeforeDispatch】~该条消息的AreaCode为空,ebmId:{}",ebm.getEBMID());
            saveSysOperateLog(ebd.getEBDID(), requestUrl, StateDictEnum.EBD_STATE_SEND_FAILED.getKey());
            return;
        }

        //认为不应该设置事件级别，按照原级别发放消息即可
//        ebm.getMsgBasicInfo().setSeverity(EventSeverityEnum.blue.getCode());  //不管什么级别都设为普通;都按时间播发
        EbmInfo ebms = coreFeginService.getEbmInfoById(ebm.getRelatedInfo().getEBMID());   //查询关联的接入ebm的信息
//        if (ebms != null && ebms.getTimeOut() != null && ebms.getTimeOut().toString().equals(CommonDictEnum.EBM_TIMEOUT_TRUE.getKey())) {
//            ebm.getMsgBasicInfo().setSeverity(EventSeverityEnum.red.getCode()); //如果超过开始时间；并且结束时间大于当前时间则设为紧急；马上播发一次
//        }

        //保存ebm信息
        saveEbm(ebd,ebms);

        // EBD数据包文件
        List<File> fileList = new ArrayList<>();
        // 根据EBD获取关联文件
        StringBuilder sb = new StringBuilder(commonService.getSendTempPath(ebd.getEBDType()));
        sb.append(SymbolConstants.FILE_SPLIT + ebd.getDEST().getEBRID());
        String filePath = sb.toString();
        //查询ebd关联的原EBD数据包的媒体文件
        if(ebd.getRelatedEBD() != null && StringUtils.isNotBlank(ebd.getRelatedEBD().getEBDID())){
            List<EbdFilesInfo> ebdFileList = coreFeginService.getEbdFilesInfoByEbdId(ebd.getRelatedEBD().getEBDID());
            for(EbdFilesInfo ebdFilesInfo : ebdFileList){
                FileInfo fileInfo = resoFeginService.getFileInfoById(ebdFilesInfo.getFileId());
                if(fileInfo == null){
                    continue;
                }
                //获取文件fastdfs存储路径
                String fileUrl = fileInfo.getFilePath();
                //判断本地是否存在该tar包
                File file = new File(filePath + SymbolConstants.FILE_SPLIT + fileInfo.getOriginName());
                //文件不存在则从fastdfs下载
                if (!file.exists()) {
                    log.info("【EBMBeforeDispatch】~开始从FASTDFS服务器下载媒体文件【接入的EBM消息】 ,ebdid:{},tempath:{},ftppath:{}",ebd.getEBDID(),filePath,fileUrl);
                    File localFile = commonService.downloadFile(fileUrl, fileInfo.getOriginName(), filePath);
                    fileList.add(localFile);
                } else {
                    fileList.add(file);
                }
            }
        }else{
            SchemeInfo schemeInfo = coreFeginService.getSchemeInfoByEbmId(ebms.getEbmId());
            if(schemeInfo != null && StringUtils.isNotBlank(schemeInfo.getProgramId())){
                ProgramInfo programInfo = coreFeginService.getProgramInfoById(schemeInfo.getProgramId());
                if(programInfo != null){
                    if(TypeDictEnum.PROGRAM_CONTENT_TYPE_TEXT.getKey().equals(String.valueOf(programInfo.getContentType()))){
                        //TODO 文本内容的节目

                    }else{
                        //其他类型的节目

                        List<ProgramFilesInfo> programFilesInfoList = programInfo.getFilesList();
                        for(ProgramFilesInfo programFilesInfo : programFilesInfoList){
                            FileInfo fileInfo = resoFeginService.getFileInfoById(programFilesInfo.getFileId());
                            if(fileInfo == null){
                                continue;
                            }
                            //获取文件fastdfs存储路径
                            String fileUrl = fileInfo.getFilePath();
                            //判断文件是否存在本地临时目录
                            File file = new File(filePath + SymbolConstants.FILE_SPLIT + fileInfo.getOriginName());
                            //文件不存在则下载文件
                            if (!file.exists()) {
                                //从FASTDFS服务器上下载文件
                                log.info("EBMBeforeDispatch】~开始下载媒体文件到FASTDFS服务器【节目单生成的EBM消息】,ebdid:{},tempath:{},ftppath:{} ",ebd.getEBDID(),filePath,fileUrl);
                                File localFile = commonService.downloadFile(fileUrl, fileInfo.getOriginName(), filePath);
                                fileList.add(localFile);
                            } else {
                                fileList.add(file);
                            }
                        }
                    }
                }
            }
        }

        // 根据EBD生成文件和签名文件
        File xmlFile = FileUtil.converFile(filePath, ebd);
        fileList.add(xmlFile);

        String isSign = commonService.getSysParamValue(LinkageConstants.VERIFY_SIGN);
        if (StringUtils.equals("1", isSign)) {
            // 生成签名和签名文件============
    		Signature signature = signValidateService.buildSignature(xmlFile, ebd.getEBDID());
    		File signFile = FileUtil.converFile(filePath, signature);
    		fileList.add(signFile);
        }

        // 生成联动TAR包
        File tarFile = TarFileUtil.compressorsTar(ebd, fileList, filePath);

        //保存EBD数据并上传到FASTDFS服务器
        saveAndUploadEbd(ebd, tarFile);

        //记录操作日志
        saveSysOperateLog(ebd.getEBDID(), requestUrl, StateDictEnum.EBD_STATE_CREATE.getKey());

        log.info("【EBMBeforeDispatch】~MQ消息分发前置处理end,ebdId:{}",ebd.getEBDID());
    }

    private void saveEbm(EBD ebd,EbmInfo accessEbm) {
        EBM ebm = ebd.getEBM();
        String localPsEbrId = commonService.getSysParamValue(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(localPsEbrId);
        EbmAddRequest ebmInfo = new EbmAddRequest();
        //BeanUtils.copyProperties(accessEbm,ebmInfo);
        ebmInfo.setEbmId(ebm.getEBMID());
        ebmInfo.setSendFlag(CommonDictEnum.FLAG_SEND.getKey());
        ebmInfo.setRelatedEbmId(accessEbm.getEbmId());
        ebmInfo.setSenderCode(localPsEbrId);
        ebmInfo.setSendName(ebrPlatformInfo.getPsEbrName());
        ebmInfo.setAreaCode(accessEbm.getAreaCode());
        ebmInfo.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());
        ebmInfo.setStartTime(accessEbm.getStartTime());
        ebmInfo.setEndTime(accessEbm.getEndTime());
        ebmInfo.setMsgType(accessEbm.getMsgType());
        ebmInfo.setEventType(accessEbm.getEventType());
        ebmInfo.setSeverity(accessEbm.getSeverity());
        ebmInfo.setMsgDesc(accessEbm.getMsgDesc());
        ebmInfo.setMsgTitle(accessEbm.getMsgTitle());
        ebmInfo.setMsgLanguageCode(accessEbm.getMsgLanguageCode());
        ebmInfo.setEbmVersion(CommonDictEnum.EBM_VERSION_INIT.getKey());
        ebmInfo.setCreateTime(new Date());
        coreFeginService.saveEbm(ebmInfo);
    }



    /**
     * 消息开始分发；
     * 从文件服务器下载待分发的数据包进行下发；
     * 下发后修改ebd数据包的状态
     */
    @Override
    public void dispatch(String ebdId) {
        log.info("【ebmDisPatch】~分发EBM消息处理开始,ebdId:{}",ebdId);

        //获取待发送的ebd信息
        EbdInfo ebdInfo = coreFeginService.getEbdInfoByEbdId(ebdId);

        commonService.updateDispatchFlowState(ebdInfo.getFlowId(), FlowConstants.STAGE_PROCESS, FlowConstants.STATE_MSG_SEND);

        //判断ebm消息是否已审核 ,未审核则终止
        if(!auditedEbmDispatchInfo(ebdInfo.getEbmId())){
        	log.error("【ebmDisPatch】~分发的EBM消息未审核不下发,ebdId:{}",ebdId);
            return;
        }

        //没有发送的目标ebrId，程序中断
        if (StringUtils.isBlank(ebdInfo.getEbdDestEbrId())) {
        	log.error("【ebmDisPatch】~没有发送的目标ebrId消息不下发,ebdId:{}",ebdId);
            return;
        }
        //在播出资源视图中查询ebr资源信息,获取目标资源的url
        EbrInfo ebrInfo = resoFeginService.getEbrCommonInfoById(ebdInfo.getEbdDestEbrId());
        String requestUrl = ebrInfo.getEbrUrl();

        //查询ebd数据包文件的信息，获取文件下载地址
        OriginFileInfo originFileInfo = resoFeginService.getOriginFileById(ebdInfo.getFileId());
        StringBuilder sb = new StringBuilder(commonService.getSendTempPath(ebdInfo.getEbdType()));
        sb.append(SymbolConstants.FILE_SPLIT + ebdInfo.getEbdDestEbrId());
        String filePath = sb.toString();
        String fileUrl = originFileInfo.getUrl();
        //从fastdfs下载待发送的ebd数据包
        File tarFile = commonService.downloadFile(fileUrl, originFileInfo.getOriginName(), filePath);
        log.info("【ebmDisPatch】~开始下发消息,ebdId:{},下发地址requestUrl:{}",ebdId,requestUrl);
        //发送数据包
        EBD ebdPack = HttpRequestUtil.sendFile(tarFile, requestUrl, commonService.getReceiveTempPath());
        if (ebdPack != null) {
            log.info("【ebmDisPatch】~消息分发成功,ebdId:{},response ebd json:{}",ebdId,JSON.toJSONString(ebdPack));
            //发送成功
            updateEBDAndEBMState(ebdInfo.getEbmId(), ebdId, ebdInfo.getRelateEbdId(), true);
            //修改流程状态为播发中。
            commonService.updateDispatchFlowState(ebdInfo.getFlowId(),FlowConstants.STAGE_PLAYING,FlowConstants.STATE_PLAYING_WAIT);
            //记录操作日志
            saveSysOperateLog(ebdId, requestUrl, StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey());
        } else {
            log.error("【ebmDisPatch】~消息分发失败,ebdId:{}",ebdId);
            //发送失败
            updateEBDAndEBMState(ebdInfo.getEbmId(), ebdId, ebdInfo.getRelateEbdId(), false);
            //记录操作日志
            saveSysOperateLog(ebdId, requestUrl, StateDictEnum.EBD_STATE_SEND_FAILED.getKey());
        }

        /*EbmStateResponseMessage message = new EbmStateResponseMessage();
        message.setEbmId(ebdInfo.getEbmId());
        message.setEbdId(ebdId);
        message.setBroadcastState(StateDictEnum.EBM_BROADCAST_STATE_READY.getKey());
        mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue, JSON.toJSONString(message));
		*/
        log.info("【ebmDisPatch】~分发EBM消息处理结束,ebdId:{}",ebdId);
    }

    /**
     * 获取消息发送列表
     *
     * @param stateList
     * @return
     */
    @Override
    public List<EBD> getEBDPackageList(List<String> stateList) {
        List<EBD> list = new ArrayList<>();
        Optional.ofNullable(stateList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .forEach(state -> list.addAll(getEbdPack(state)));
        return list;
    }


    private List<EBD> getEbdPack(String ebmStateCreate) {

        // 获取本级平台信息

        String localPsEbrId = commonService.getSysParamValue(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(localPsEbrId);
        if (ebrPlatformInfo == null) {
            log.error("查询平台信息不存在！");
            return null;
        }
        String localPsURL = ebrPlatformInfo.getPsUrl();


        EbmDispatchPageRequest request = new EbmDispatchPageRequest();
        request.setLimit(Constants.MAX_SQL_ROWS);
        request.setOrder(Constants.SQL_ORDER_TYPE_DESC);
        request.setSidx(SQL_ORDER_COLUMN);
        request.setState(ebmStateCreate);
        // 获取待分发记录
        List<EbmDispatchInfo> dispatchInfoList = coreFeginService.getEbmDispatchByPage(request);

        List<EBD> ebdPackList = Optional.ofNullable(dispatchInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(this::auditedEbmScheme)  //过滤方案未审核和审核不通过的
                //     .filter(dispatchInfo -> auditedEbmDispatchInfo(dispatchInfo.getEbmId()))  //过滤未审核的ebm数据包
                .filter(this::allowDispatch)     //过滤超过失败重发次数的记录
                .filter(this::filterEbrChannel) //过滤掉使用其他协议发送的消息
                .map(ebmDispatch -> {
                    String matchedEbdId = ebmDispatch.getMatchEbdId();
                    //如果该条记录存在已匹配的ebd信息，说明是已经匹配过的，由于发送失败而重发的。
                    if (StringUtils.isNotBlank(matchedEbdId)) {
                        //查询匹配的ebd数据包
                        EbdInfo ebdInfo = coreFeginService.getEbdInfoByEbdId(matchedEbdId);
                        if (ebdInfo != null) {
                            //数据包存在则生成一个只带ebdId属性的最简单的EBD对象返回，在分发前置处理时，只需要通过这个ebdId获取已匹配的ebd数据包相关的数据
                            //无需再重新生成ebd数据包。
                            EBD ebd = new EBD();
                            ebd.setEBDID(matchedEbdId);
                            return ebd;
                        }
                    }


                    String ebmId = ebmDispatch.getEbmId();
                    EbmInfo ebm = coreFeginService.getEbmById(ebmId);
                    SRC src = new SRC();
                    src.setEBRID(localPsEbrId);
                    src.setURL(localPsURL);

                    DEST dest = new DEST();

                    String psEbrId = ebmDispatch.getPsEbrId();
                    String bsEbrId = ebmDispatch.getBsEbrId();
                    String stEbrId = ebmDispatch.getStEbrId();
                    String asEbrId = ebmDispatch.getAsEbrId();

                    //分发的播出资源，包括平台，播出系统等
                    List<Dispatch> ebmDispatchList = new ArrayList<>();
                    Dispatch dispatch = new Dispatch();

                    String areaCode = "";

                    // 如果调度分发为播出系统，则设置资源为播出系统，否则设置平台
                    if (StringUtils.isNotEmpty(bsEbrId)) {
                        dest.setEBRID(bsEbrId);

                        EbrBroadcastInfo ebrBroadcast = resoFeginService.getEbrBroadcastInfoById(bsEbrId);

                        EBRBS ebrBs = new EBRBS();
                        ebrBs.setEBRID(ebrBroadcast.getBsEbrId());
                        ebrBs.setURL(ebrBroadcast.getBsUrl());
                        dispatch.setEBRBS(ebrBs);
                        //areaCode = getAreaCode(ebrBroadcast.getAreaCode());
                    } else if(StringUtils.isNotEmpty(psEbrId)){
                        dest.setEBRID(psEbrId);

                        EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(psEbrId);

                        EBRPS ebrPs = new EBRPS();
                        ebrPs.setEBRID(psEbrId);
                        ebrPs.setURL(ebrPlatform.getPsUrl());
                        dispatch.setEBRPS(ebrPs);
                        //areaCode = getAreaCode(ebrPlatform.getAreaCode());
                    }else if(StringUtils.isNotEmpty(stEbrId)) {
                    	dest.setEBRID(stEbrId);

                    	EbrStationInfo ebrStation= resoFeginService.getEbrStationInfoById(stEbrId);

                    	EBRST ebrst = new EBRST();
                    	ebrst.setEBRID(ebrStation.getEbrStId());
                    	ebrst.setAddress(ebrStation.getAddress());
                    	dispatch.setEBRST(ebrst);
                    	//areaCode = getAreaCode(ebrStation.getAreaCode());
                    }else if(StringUtils.isNotEmpty(asEbrId)) {
                    	dest.setEBRID(asEbrId);

                    	EbrAdapterInfo ebrAdapter= resoFeginService.getEbrAdapterInfoById(asEbrId);

                    	EBRAS ebras = new EBRAS();
                    	ebras.setEBRID(ebrAdapter.getEbrAsId());
                    	ebras.setURL(ebrAdapter.getUrl());
                    	dispatch.setEBRAS(ebras);
                    	//areaCode = getAreaCode(ebrAdapter.getAreaCode());
                    }
                    dispatch.setLanguageCode(ebmDispatch.getLanguageCode());
                    ebmDispatchList.add(dispatch);
                    log.info("【generateEbdList】~分发的播出资源：{},DispatchId:{}", JSON.toJSONString(ebmDispatchList),ebmDispatch.getDispatchId());

                    EBD ebdPack = new EBD();
                    // ebdId 按规则重新生成，不沿用EBD包的EBDID。因为EBD包发送到多个平台或系统，会产生多条相同的ebdId的记录，保存到数据库会报错
                    String ebdType = EBDType.EBM.name();
                    String ebdId = ebmDispatch.getEbdId();
                    if(StringUtils.isBlank(ebdId)){
                        ebdId = Constants.EBDTYPE + localPsEbrId +  commonService.getEbdId();
                    }else{
                        ebdId = ebdId.substring(0, ebdId.length() - Constants.EBDID_SEQUENCE_LENGTH) + commonService.getEbdId();
                    }
                    ebdPack.setEBDID(ebdId);
                    ebdPack.setSRC(src);
                    ebdPack.setDEST(dest);
                    ebdPack.setEBDType(ebdType);
                    ebdPack.setEBDTime(DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
                    //关联原EBD数据包
                    if(StringUtils.isNotBlank(ebmDispatch.getEbdId())){
                        RelatedEBD relatedEBD = new RelatedEBD();
                        relatedEBD.setEBDID(ebmDispatch.getEbdId());
                        ebdPack.setRelatedEBD(relatedEBD);
                    }


                    //组装应急广播消息基本信息数据
                    MsgBasicInfo mbInfo = new MsgBasicInfo();
                    mbInfo.setMsgType(Integer.valueOf(ebm.getMsgType()));
                    mbInfo.setEventType(ebm.getEventType());
                    mbInfo.setSenderCode(ebm.getSenderCode());
                    mbInfo.setSenderName(ebm.getSendName());
                    mbInfo.setSeverity(Integer.valueOf(ebm.getSeverity()));
                    mbInfo.setSendTime(DateUtil.getDateTime());
                    mbInfo.setStartTime(DateUtil.format(ebm.getStartTime(),
                            DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
                    mbInfo.setEndTime(DateUtil.format(ebm.getEndTime(),
                            DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));

                    //组装ebm辅助信息数据
                    List<EbmAuxiliaryInfo> auxiliaryList = coreFeginService.getEbmAuxiliaryInfoEbmId(ebmId);

                    List<Auxiliary> auxiList = new ArrayList<Auxiliary>();
                    for (EbmAuxiliaryInfo ebmAuxiliary : auxiliaryList) {
                        Auxiliary auxiliary = new Auxiliary();
                        auxiliary.setAuxiliaryType(Integer.valueOf(ebmAuxiliary.getAuxiliaryType()));
                        auxiliary.setAuxiliaryDesc(ebmAuxiliary.getAuxiliaryDesc());
                        auxiliary.setDigest(ebmAuxiliary.getAuxiliaryDigest());
                        auxiliary.setSize(ebmAuxiliary.getAuxiliarySize());
                        auxiList.add(auxiliary);
                    }

                    //组装应急广播消息内容数据
                    MsgContent mContent = new MsgContent();
                    mContent.setMsgTitle(ebm.getMsgTitle());
                    mContent.setMsgDesc(StringUtils.isNotBlank(ebm.getMsgDesc())?ebm.getMsgDesc():"无");
                    mContent.setLanguageCode(ebm.getMsgLanguageCode());
                    mContent.setProgramNum(ebm.getProgramNum() == null? null:Integer.valueOf(ebm.getProgramNum()));
                    mContent.setAreaCode(ebm.getAreaCode());
                    mContent.setAuxiliaryList(auxiList);

                    RelatedInfo relatedInfo = new RelatedInfo();
                    if(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey().equals(ebm.getMsgType())){
                        //如果EBM消息类型为取消播发,关联为待取消的EBM消息
                        relatedInfo.setEBMID(ebm.getRelatedEbmId());
                    }else{
                        relatedInfo.setEBMID(ebm.getEbmId());          //关联拆分前的ebm的ID
                    }

                    /*
                     * 修改日期，2019-02-14
                     * 修改前：生成分发的EBD包后不生成新的ebm信息，沿用接入的ebmId
                     * 修改后：生成分发的EBD包同时生成新的ebm信息.ebmId重新生成
                     */
                    EBM ebmPack = new EBM();
                    String ebmId1 = localPsEbrId + DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD)+commonService.getEbmId();
                    if(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey().equals(ebm.getMsgType())){
                        //如果EBM消息类型为取消播发,不重新生成EebmId
                        ebmId1 = ebm.getEbmId();
                    }

                    ebmPack.setEBMID(ebmId1);
                    ebmPack.setEBMVersion(CommonDictEnum.EBM_VERSION_INIT.getKey());
                    if (StringUtils.isNotEmpty(relatedInfo.getEBMID())) {
                        ebmPack.setRelatedInfo(relatedInfo);
                    }
                    ebmPack.setMsgBasicInfo(mbInfo);
                    ebmPack.setMsgContent(mContent);
                    ebmPack.setDispatchList(ebmDispatchList);
                    ebdPack.setEBM(ebmPack);


                    //将新生成的EBD数据包关联到EbmDispatch表中
                    EbmDispatchUpdateRequest ebmDispatchUpdateRequest = new EbmDispatchUpdateRequest();
                    ebmDispatchUpdateRequest.setDispatchId(ebmDispatch.getDispatchId());
                    ebmDispatchUpdateRequest.setMatchEbdId(ebdPack.getEBDID());
                    coreFeginService.updateEbmDispatch(ebmDispatchUpdateRequest);
                    return ebdPack;
                }).collect(Collectors.toList());
        log.info("【generateEbdList】~获取待分发记录bc_ebm_dispatch生成EBDlist,emDispatchSize:{},ebdlistSize:{}", dispatchInfoList.size(),ebdPackList.size());
        return ebdPackList;
    }

    private boolean auditedEbmScheme(EbmDispatchInfo ebmDispatchInfo) {
        if (StringUtils.isBlank(ebmDispatchInfo.getEbmId())) {
            return false;
        }
        EbmInfo ebmInfo = coreFeginService.getEbmById(ebmDispatchInfo.getEbmId());
        if(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey().equals(ebmInfo.getMsgType())){
                //如果是取消播发的消息
            return true;
        }
        SchemeInfo schemeInfo = coreFeginService.getSchemeInfoByEbmId(ebmDispatchInfo.getEbmId());
        return schemeInfo != null && StateDictEnum.AUDIT_STATUS_PASS.getKey().equals(schemeInfo.getAuditResult());
    }

    //只分发已审核通过的ebm消息
    private boolean auditedEbmDispatchInfo(String ebmId) {
        EbmInfo ebmInfo = coreFeginService.getEbmInfoById(ebmId);
        if(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey().equals(ebmInfo.getMsgType())){
            //如果是取消播发的消息，直接下发。
            return true;
        }
        EbmDispatchInfoInfo ebmDispatchInfoInfo = coreFeginService.getEbmDispatchInfoInfoByEbmId(ebmInfo.getRelatedEbmId());
        if (ebmDispatchInfoInfo == null) {
            return false;
        }
        return StateDictEnum.AUDIT_STATUS_PASS.getKey().equals(ebmDispatchInfoInfo.getAuditResult());
    }

    /**
     * 过滤其他协议发送
     *
     * @return
     */
    private boolean filterEbrChannel(EbmDispatchInfo ebmDispatchInfo) {
    	String ebrId = null;
    	if(StringUtils.isNotBlank(ebmDispatchInfo.getPsEbrId())) {
    		ebrId = ebmDispatchInfo.getPsEbrId();
    	}else if(StringUtils.isNotBlank(ebmDispatchInfo.getBsEbrId())) {
    		ebrId = ebmDispatchInfo.getBsEbrId();
    	}else if(StringUtils.isNotBlank(ebmDispatchInfo.getStEbrId())) {
    		ebrId = ebmDispatchInfo.getStEbrId();
    	}else if(StringUtils.isNotBlank(ebmDispatchInfo.getAsEbrId())) {
    		ebrId = ebmDispatchInfo.getAsEbrId();
    	}
    	String channel= resoFeginService.getChannelByEbrId(ebrId);
    	if(CommonDictEnum.EBR_CHANNEL_2.getKey().equals(channel)) {
    		return false;
    	}
        //查询资源所用协议
    	return true;
    }

    /**
     * 过滤其tar包协议和非文本消息
     *
     * @return
     */
    private boolean filterTextAndTar(EbmDispatchInfo ebmDispatchInfo) {
    	return !filterEbrChannel(ebmDispatchInfo);
    }

    /**
     * 过滤掉发送失败并且重发次数超过上限的记录
     *
     * @return
     */
    private boolean allowDispatch(EbmDispatchInfo ebmDispatchInfo) {
        //查询最大重发次数
        String maxFailedTimes = commonService.getSysParamValue(Constants.MAX_SEND_RETRY_TIMES);
        //重发次数未设置，则默认为0次，不重发
        Integer maxTimes = maxFailedTimes == null ? 0 : Integer.valueOf(maxFailedTimes);
        if (StateDictEnum.DISPATCH_STATE_FAILED.getKey().equals(ebmDispatchInfo.getState())) {
            //发送失败的记录

            if (ebmDispatchInfo.getFailCount() >= maxTimes) {
                return false;
            }
        }
        //查询播发结束时间
        EbmInfo ebm = coreFeginService.getEbmById(ebmDispatchInfo.getEbmId());
        Date endTime = ebm.getEndTime();
        //查询提前播发时间
        if(ebmDispatchInfo.getPlayTime() != null) {
        	Integer advanceSec = Integer.valueOf(commonService.getSysParamValue(Constants.EBM_SEND_SEND_ADVANCE_SEC).trim());//时间差
        	Integer palyStartT = DateUtil.getSecondTimestamp(ebmDispatchInfo.getPlayTime());//播发开始时间
        	Integer palyEndT = DateUtil.getSecondTimestamp(endTime);//播发结束时间
        	Integer currentSecond = DateUtil.getSecondTimestamp(new Date());//当前时间
        	//已经过期的不发送,未到时的不发送
        	if(palyEndT<currentSecond) {
        		return false;
        	}
        	//未到时间不发送
        	if(palyStartT>currentSecond-advanceSec) {
        		log.info("消息未到发送时间，暂时不发送：ebmId:{}",ebm.getEbmId());
        		return false;
        	}
        }
        return true;
    }
    /**
     * 下发到大喇叭平台的区域码处理；
     *
     * @param areaCode
     * @return
     */
    private String getAreaCode(String areaCode) {
        String[] areaCodes = areaCode.split(String.valueOf(SymbolConstants.GENERAL_SEPARATOR));
        StringBuilder codesStr = new StringBuilder();
        StringBuilder xStr = new StringBuilder();
        for (int i = 0; i < areaCodes.length; i++) {
            RegionAreaInfo regionArea = systemFeginService.getRegionAreaByAreaCode(areaCodes[i].trim());
            if (regionArea == null) {
                continue;
            }
            if (areaCodes[i].length() == 12) {
                codesStr.append(areaCodes[i].trim()).append(SymbolConstants.GENERAL_SEPARATOR);
            } else {
                xStr.append(areaCodes[i].trim()).append(SymbolConstants.GENERAL_SEPARATOR);
            }
        }
        if (StringUtils.isEmpty(codesStr.toString())) {
            return xStr.length() != 0 ? xStr.substring(0, xStr.length() - 1) : StringUtils.EMPTY;
        }
        areaCode = codesStr.substring(0, codesStr.length() - 1);
        return areaCode;
    }

    /**
     * 保存tar包到fastdfs服务器
     * @param ebd
     * @param tarFile
     */
    private void saveAndUploadEbd(EBD ebd, File tarFile) {
        //上传tar包文件
        OriginFileInfo originFileInfo = commonService.toFastDFS(FileEnum.FileTypeEnum.send_file.getType(), tarFile);
        //获取接收的原生EBM消息的ID
        String ebmId = ebd.getEBM().getRelatedInfo().getEBMID();
        EbmInfo ebmInfo = coreFeginService.getEbmInfoById(ebmId);
        String flowId = null;
        if(StringUtils.isNotBlank(ebmInfo.getSchemeId())) {
	        SchemeInfo schemeInfo = coreFeginService.getSchemeInfoById(ebmInfo.getSchemeId());
	        // 更新流程：消息下发
	        flowId = schemeInfo.getFlowId();
	        //更新流程状态
	        commonService.updateDispatchFlowState(flowId, FlowConstants.STAGE_PROCESS, FlowConstants.STATE_MSG_AUDIT);
        }
        //保存数据包信息和tar包的关联信息
        commonService.saveEbdData(ebd, tarFile.getName(), StateDictEnum.EBD_STATE_CREATE.getKey(), CommonDictEnum.FLAG_SEND.getKey(), flowId, originFileInfo.getFileId());
        log.info("EBMBeforeDispatch】~上传tar文件到FASTDFS,tarFileName:{},filePath:{},ebdId:{}", tarFile.getName(), originFileInfo.getUrl(), ebd.getEBDID());
        //添加mq消息通知
        mqMessageProducer.sendData(MqQueueConstant.EBM_DISPATCH_QUEUE, ebd.getEBDID());
    }


    /**
     * 更新EBD和EBM调度状态
     *
     * @param ebmId    下发的ebm的Id
     * @param ebdId    下发的ebd的Id
     * @param srcEbdId 下发的ebd的源ebd的Id
     * @param success
     */
    private void updateEBDAndEBMState(String ebmId, String ebdId, String srcEbdId, Boolean success) {
        //发送的ebm信息
        EbmInfo ebmInfo = coreFeginService.getEbmInfoById(ebmId);
        //接收的源ebm的Id
        String srcEbmId = ebmInfo.getRelatedEbmId();
        //    EbmInfo srcEbmInfo = coreFeginService.getEbmInfoById(srcEbmId);
        if (success) {
            //发送成功
            //更新发送的新ebm状态
            EbmUpdateRequest ebmUpdateRequestNew = new EbmUpdateRequest();
            ebmUpdateRequestNew.setEbmId(ebmId);
            ebmUpdateRequestNew.setEbmState(StateDictEnum.EBM_STATE_SEND_SUCCESS.getKey());
            coreFeginService.updateEbm(ebmUpdateRequestNew);

            //修改拆分后的新ebd数据包的状态为发送成功
            EbdUpdateRequest ebdUpdateRequest1 = new EbdUpdateRequest();
            ebdUpdateRequest1.setEbdId(ebdId);
            ebdUpdateRequest1.setEbdState(StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey());
            coreFeginService.updateEbd(ebdUpdateRequest1);

            // 更新EBM调度分发记录状态为：已发送
            EbmDispatchInfo ebmDispatchInfo = coreFeginService.getEbmDispatchByMatchedEbdId(ebdId);
            EbmDispatchUpdateRequest ebmDispatchUpdateRequest = new EbmDispatchUpdateRequest();
            ebmDispatchUpdateRequest.setDispatchId(ebmDispatchInfo.getDispatchId());
            ebmDispatchUpdateRequest.setState(StateDictEnum.DISPATCH_STATE_SUCCESS.getKey());
            ebmDispatchUpdateRequest.setFailCount(0);               //发送成功初始失败次数
            ebmDispatchUpdateRequest.setDispatchTime(new Date());
            coreFeginService.updateEbmDispatch(ebmDispatchUpdateRequest);

            //扫描ebm_dispatch表，当ebmId对应的所有记录都发送成功，设置ebm状态为成功
            List<EbmDispatchInfo> ebmDispatchInfoList = coreFeginService.getEbmDispatchByEbmId(srcEbmId);
            //一个源ebd数据包在分发时会根据下发的ebr资源数拆分成对应个数的新的ebd数据包
            //判断该源ebd数据包拆分的所有新的ebd数据包是否已经都成功下发到各个ebr资源
            boolean sendAllSuccess = Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
                    .filter(Objects::nonNull)
                    .allMatch(ebmDispatchInfo1 -> {
                        String state = ebmDispatchInfo1.getState();
                        return StateDictEnum.DISPATCH_STATE_SUCCESS.getKey().equals(state);
                    });
            if (sendAllSuccess) {
                //更新接收的源ebm状态为成功
                EbmUpdateRequest ebmUpdateRequest = new EbmUpdateRequest();
                ebmUpdateRequest.setEbmId(srcEbmId);
                ebmUpdateRequest.setEbmState(StateDictEnum.EBM_STATE_SEND_SUCCESS.getKey());
                coreFeginService.updateEbm(ebmUpdateRequest);

                //所有拆分后的ebd数据包都下发成功，修改接入的源ebd信息的状态为成功
                EbdUpdateRequest ebdUpdateRequest = new EbdUpdateRequest();
                ebdUpdateRequest.setEbdId(srcEbdId);
                ebdUpdateRequest.setEbdState(StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey());
                coreFeginService.updateEbd(ebdUpdateRequest);
            }

        } else {
            //只要任何一拆分的新的ebd数据包发送失败
            //更新原EBD状态 为发送失败
            EbdUpdateRequest ebdUpdateRequest = new EbdUpdateRequest();
            ebdUpdateRequest.setEbdId(srcEbdId);
            ebdUpdateRequest.setEbdState(StateDictEnum.EBD_STATE_SEND_FAILED.getKey());
            coreFeginService.updateEbd(ebdUpdateRequest);

            //修改拆分后的新ebd数据包的状态为发送失败
            EbdUpdateRequest ebdUpdateRequest1 = new EbdUpdateRequest();
            ebdUpdateRequest1.setEbdId(ebdId);
            ebdUpdateRequest1.setEbdState(StateDictEnum.EBD_STATE_SEND_FAILED.getKey());
            coreFeginService.updateEbd(ebdUpdateRequest1);

            // 更新接收的源EBM状态
            EbmUpdateRequest ebmUpdateRequest = new EbmUpdateRequest();
            ebmUpdateRequest.setEbmId(srcEbmId);
            ebmUpdateRequest.setEbmState(StateDictEnum.EBM_STATE_SEND_FAILED.getKey());
            coreFeginService.updateEbm(ebmUpdateRequest);

            //更新发送的新ebm状态
            EbmUpdateRequest ebmUpdateRequestNew = new EbmUpdateRequest();
            ebmUpdateRequestNew.setEbmId(ebmId);
            ebmUpdateRequestNew.setEbmState(StateDictEnum.EBM_STATE_SEND_FAILED.getKey());
            coreFeginService.updateEbm(ebmUpdateRequestNew);

            // 更新EBM调度分发记录状态为：发送失败
            EbmDispatchInfo ebmDispatchInfo = coreFeginService.getEbmDispatchByMatchedEbdId(ebdId);
            EbmDispatchUpdateRequest ebmDispatchUpdateRequest = new EbmDispatchUpdateRequest();
            ebmDispatchUpdateRequest.setDispatchId(ebmDispatchInfo.getDispatchId());
            ebmDispatchUpdateRequest.setState(StateDictEnum.DISPATCH_STATE_FAILED.getKey());
            ebmDispatchUpdateRequest.setFailCount(ebmDispatchInfo.getFailCount() + 1);            //累加失败次数
            ebmDispatchUpdateRequest.setDispatchTime(new Date());
            coreFeginService.updateEbmDispatch(ebmDispatchUpdateRequest);

        }
    }


    private void saveSysOperateLog(String ebdId, String requestUrl, String state) {

        // 记录操作日志
        // 日志内容
        StringBuilder buf = new StringBuilder();
        if (StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey().equals(state)) {
            buf.append("调度分发【");
            buf.append(ebdId);
            buf.append("】数据包至【");
            buf.append(requestUrl);
            buf.append("】发送成功.");
        } else if (StateDictEnum.EBD_STATE_SEND_FAILED.getKey().equals(state)) {
            buf.append("调度分发【");
            buf.append(ebdId);
            buf.append("】数据包至【");
            buf.append(requestUrl);
            buf.append("】发送失败.");
        } else if (StateDictEnum.EBD_STATE_CREATE.getKey().equals(state)) {
            buf.append("调度分发【");
            buf.append(ebdId);
            buf.append("】数据包至【");
            buf.append(requestUrl);
            buf.append("】创建成功.");
        }

        SysOperateLogAddRequest request = new SysOperateLogAddRequest();
        request.setOperator("--");
        request.setModule("调度控制");
        request.setOperation("调度分发");
        request.setDescription(buf.toString());
        request.setServiceName(applicationName);
        request.setClientIp("127.0.0.1");

        // 记录用户操作日志
        systemFeginService.saveSysOperateLog(request);
    }

	@Override
	public void dispatchTextInsertionEBM(List<String> stateList) {
		for (String ebmState : stateList) {
	        EbmDispatchPageRequest request = new EbmDispatchPageRequest();
	        request.setLimit(Constants.MAX_SQL_ROWS);
	        request.setOrder(Constants.SQL_ORDER_TYPE_DESC);
	        request.setSidx(SQL_ORDER_COLUMN);
	        request.setState(ebmState);
	        // 获取待分发记录
	        List<EbmDispatchInfo> dispatchInfoList = coreFeginService.getEbmDispatchByPage(request);

	        Optional.ofNullable(dispatchInfoList).orElse(Collections.emptyList()).stream()
	                .filter(Objects::nonNull)
	                .filter(this::auditedEbmScheme)  //过滤方案未审核和审核不通过的
	                .filter(this::allowDispatch)     //过滤超过失败重发次数的记录
	                .filter(this::filterTextAndTar) //过滤掉使用其他协议发送的消息
	                .forEach(ebmDispatch -> {
	                	log.info("【TextInsertionEBM】~字幕插播消息下发开始,ebmDispatchId:{}", ebmDispatch.getDispatchId());
	                    EbmInfo ebm = coreFeginService.getEbmById(ebmDispatch.getEbmId());
	                    //播放次数
	                    Integer durationTime = null;
	                    try {
							SchemeInfo scheme = coreFeginService.getSchemeInfoByEbmId(ebm.getEbmId());
							if(scheme!=null && StringUtils.isNotBlank(scheme.getProgramId())) {
							    ProgramInfo programInfo = coreFeginService.getProgramInfoById(scheme.getProgramId());
							    if(programInfo!=null && programInfo.getProgramStrategy().getTimeList()!=null) {
							    	durationTime = programInfo.getProgramStrategy().getTimeList().get(0).getDurationTime();
							    }
							}
						} catch (Exception e) {}


	                    String content = ebm.getMsgDesc();
	                    if(StringUtils.isNotBlank(content)) {
	                    	String ebrId = null;
	                    	if(StringUtils.isNotBlank(ebmDispatch.getPsEbrId())) {
	                    		ebrId = ebmDispatch.getPsEbrId();
	                    	}else if(StringUtils.isNotBlank(ebmDispatch.getBsEbrId())) {
	                    		ebrId = ebmDispatch.getBsEbrId();
	                    	}else if(StringUtils.isNotBlank(ebmDispatch.getStEbrId())) {
	                    		ebrId = ebmDispatch.getStEbrId();
	                    	}else if(StringUtils.isNotBlank(ebmDispatch.getAsEbrId())) {
	                    		ebrId = ebmDispatch.getAsEbrId();
	                    	}
	                    	Map<String, Object> param = Maps.newHashMap();
	                    	param.put("content", content);
	                    	param.put("play_number", durationTime);
	                    	EbrInfo ebrInfo = resoFeginService.getEbrCommonInfoById(ebrId);
	                        String requestUrl = ebrInfo.getEbrUrl();
	                    	boolean isSuccess = textInsertionService.issueEBMessage(requestUrl,param);
	                    	if(isSuccess) {
	                    		log.error("【TextInsertionEBM】~字幕插播消息下发成功,,ebmDispatchId:{},ebmId:{}", ebmDispatch.getDispatchId(),ebmDispatch.getEbmId());
	                            // 更新EBM调度分发记录状态为：已调度
	                            EbmDispatchUpdateRequest ebmDispatchUpdateRequest = new EbmDispatchUpdateRequest();
	                            ebmDispatchUpdateRequest.setDispatchId(ebmDispatch.getDispatchId());
	                            ebmDispatchUpdateRequest.setState(StateDictEnum.DISPATCH_STATE_SUCCESS.getKey());
	                            ebmDispatchUpdateRequest.setDispatchTime(new Date());
	                            coreFeginService.updateEbmDispatch(ebmDispatchUpdateRequest);

	                            //扫描ebm_dispatch表，当ebmId对应的所有记录都发送成功，设置ebm状态为成功
	                            List<EbmDispatchInfo> ebmDispatchInfoList = coreFeginService.getEbmDispatchByEbmId(ebmDispatch.getEbmId());
	                            //一个源ebd数据包在分发时会根据下发的ebr资源数拆分成对应个数的新的ebd数据包
	                            //判断该源ebd数据包拆分的所有新的ebd数据包是否已经都成功下发到各个ebr资源
	                            boolean sendAllSuccess = Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
	                                    .filter(Objects::nonNull)
	                                    .allMatch(ebmDispatchInfo1 -> {
	                                        String state = ebmDispatchInfo1.getState();
	                                        return StateDictEnum.DISPATCH_STATE_SUCCESS.getKey().equals(state);
	                                    });
	                            if (sendAllSuccess) {
	                                //更新接收的源ebm状态为成功
	                                EbmUpdateRequest ebmUpdateRequest = new EbmUpdateRequest();
	                                ebmUpdateRequest.setEbmId(ebmDispatch.getEbmId());
	                                ebmUpdateRequest.setEbmState(StateDictEnum.EBM_STATE_SEND_SUCCESS.getKey());
	                                ebmUpdateRequest.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_PLAY_STATE_SUCCESS.getKey()));
	                                coreFeginService.updateEbm(ebmUpdateRequest);
	                                if(StringUtils.isNotBlank(ebmDispatch.getEbdId())) {
		                                //所有拆分后的ebd数据包都下发成功，修改接入的源ebd信息的状态为成功
		                                EbdUpdateRequest ebdUpdateRequest = new EbdUpdateRequest();
		                                ebdUpdateRequest.setEbdId(ebmDispatch.getEbdId());
		                                ebdUpdateRequest.setEbdState(StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey());
		                                coreFeginService.updateEbd(ebdUpdateRequest);
	                                }
	                                //更新流程状态
		                            commonService.updateDispatchFlowState(ebm.getFlowId(),FlowConstants.STAGE_COMPLETE,FlowConstants.STATE_COMPLETE_SUCCESS);
	                            }
	                    		//记录操作日志
	                            saveSysOperateLog("字幕插播EBMID:"+ebmDispatch.getEbmId(), requestUrl, StateDictEnum.EBD_STATE_SEND_SUCCESS.getKey());
	                            //通知大屏展示接口
	                            coreFeginService.callDVBNotice();
	                    	}else {
	                    		log.error("【TextInsertionEBM】~字幕插播消息下发异失败,,ebmDispatchId:{},ebmId:{}",ebmDispatch.getDispatchId(), ebmDispatch.getEbmId());
	                    	      //更新发送的新ebm状态
	                            EbmUpdateRequest ebmUpdateRequestNew = new EbmUpdateRequest();
	                            ebmUpdateRequestNew.setEbmId(ebmDispatch.getEbmId());
	                            ebmUpdateRequestNew.setEbmState(StateDictEnum.EBM_STATE_SEND_FAILED.getKey());
	                            ebmUpdateRequestNew.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_FAILED.getKey()));
	                            coreFeginService.updateEbm(ebmUpdateRequestNew);

	                            // 更新EBM调度分发记录状态为：发送失败
	                            EbmDispatchUpdateRequest ebmDispatchUpdateRequest = new EbmDispatchUpdateRequest();
	                            ebmDispatchUpdateRequest.setDispatchId(ebmDispatch.getDispatchId());
	                            ebmDispatchUpdateRequest.setState(StateDictEnum.DISPATCH_STATE_FAILED.getKey());
	                            ebmDispatchUpdateRequest.setFailCount(ebmDispatch.getFailCount() + 1);            //累加失败次数
	                            ebmDispatchUpdateRequest.setDispatchTime(new Date());
	                            coreFeginService.updateEbmDispatch(ebmDispatchUpdateRequest);
	                    		//记录操作日志
	                            saveSysOperateLog("字幕插播EBMID:"+ebmDispatch.getEbmId(), requestUrl, StateDictEnum.EBD_STATE_SEND_FAILED.getKey());
	                            //更新流程状态
	                            commonService.updateDispatchFlowState(ebm.getFlowId(),FlowConstants.STAGE_COMPLETE,FlowConstants.STATE_COMPLETE_FAILE);
	                    	}
	                    }else {
	                    	log.error("【TextInsertionEBM】~字幕插播消息下发异常,文本消息内容为空,ebmDispatchId:{}", ebmDispatch.getEbmId());
	                    	 // 更新EBM调度分发记录状态为：发送失败
                            EbmDispatchUpdateRequest ebmDispatchUpdateRequest = new EbmDispatchUpdateRequest();
                            ebmDispatchUpdateRequest.setDispatchId(ebmDispatch.getDispatchId());
                            ebmDispatchUpdateRequest.setState(StateDictEnum.DISPATCH_STATE_FAILED.getKey());
                            ebmDispatchUpdateRequest.setFailCount(ebmDispatch.getFailCount() + 1);            //累加失败次数
                            ebmDispatchUpdateRequest.setDispatchTime(new Date());
                            coreFeginService.updateEbmDispatch(ebmDispatchUpdateRequest);

	                    }
	                    log.error("【TextInsertionEBM】~字幕插播消息下发结束,ebmDispatchId:{}", ebmDispatch.getEbmId());
	                });
		}

	}


}
