package cn.comtom.core.main.sichuan.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import cn.comtom.core.main.ebm.service.IEbmDispatchInfoService;
import cn.comtom.core.main.ebm.service.IEbmDispatchService;
import cn.comtom.core.main.ebm.service.IEbmService;
import cn.comtom.core.main.fegin.service.IResoFeginService;
import cn.comtom.core.main.fegin.service.ISystemFeginService;
import cn.comtom.core.main.flow.entity.dbo.DispatchFlow;
import cn.comtom.core.main.flow.service.IDispatchFlowService;
import cn.comtom.core.main.program.dao.ProgramUnitDao;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.core.main.program.service.IProgramService;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.core.main.scheme.service.ISchemeEbrService;
import cn.comtom.core.main.scheme.service.ISchemeService;
import cn.comtom.core.main.sichuan.Videodecode;
import cn.comtom.core.main.sichuan.entity.EBM;
import cn.comtom.core.main.sichuan.service.ISCEbmService;
import cn.comtom.core.main.utils.FileUtil;
import cn.comtom.core.main.utils.SequenceGenerate;
import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.program.info.*;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramUnitPageRequest;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.FileUploadRequest;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.utils.DateUtil;
import cn.comtom.tools.utils.FileUtils;
import cn.comtom.tools.utils.MathUtil;
import cn.comtom.tools.utils.UUIDGenerator;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SCEbmService extends BaseServiceImpl<ProgramUnit,String> implements ISCEbmService {

    @Autowired
    private ProgramUnitDao programUnitDao;

    @Autowired
    private IProgramService programService;

    @Autowired
    private IEbmDispatchService ebmDispatchService;

    @Autowired
    private IDispatchFlowService dispatchFlowService;

    @Autowired
    private SequenceGenerate sequenceGenerate;

    @Autowired
    private ISchemeEbrService schemeEbrService;

    @Autowired
    private IEbmDispatchInfoService ebmDispatchInfoService;

    @Autowired
    private IEbmService ebmService;

    @Autowired
    private ISchemeService schemeService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private ISystemFeginService systemFeginService;

    //@Autowired
    //private TTSComponent ttsComponent;

    @Value("${file.ftp.hostname}")
    private  String hostname ;

    @Value("${file.ftp.port}")
    private  Integer port  ;

    @Value("${file.ftp.username}")
    private  String username ;

    @Value("${file.ftp.password}")
    private  String password ;

    @Override
    public BaseDao<ProgramUnit, String> getDao() {
        return programUnitDao;
    }

    @Override
    public List<ProgramUnitInfo> getProgramUnitInfoList(ProgramUnitPageRequest request) {
        return programUnitDao.getProgramUnitInfoList(request);
    }

    private static String formatDateTime(String value) {
    	if(StringUtils.isBlank(value)) {
    		return null;
    	}
        String year = value.substring(0, 4);
        String month = value.substring(5, 7);
        String day = value.substring(8, 10);
        String hour = value.substring(11, 13);
        String min = value.substring(14, 16);
        String sec = value.substring(17, 19);
        String time =  year + "-" + month + "-" + day + " " + hour + ":" + min + ":"+sec;
       return time;
      }

    private static String formatDate(String value) {
    	if(StringUtils.isBlank(value)) {
    		return null;
    	}
        String year = value.substring(0, 4);
        String month = value.substring(5, 7);
        String day = value.substring(8, 10);
        String time =  year + "-" + month + "-" + day;
       return time;
      }


    private static String formatTime(String value) {
    	if(StringUtils.isBlank(value)) {
    		return null;
    	}
        String hour = value.substring(11, 13);
        String min = value.substring(14, 16);
        String sec = value.substring(17, 19);
        String time =  hour + ":" + min + ":"+sec;
       return time;
      }

    public static void main(String[] args) {
    	formatDate("");
    }
    @Override
    public void createEbms(EBM ebm) {
        try {
			if(ebm == null){
			    return;
			}
			SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
			EbrPlatformInfo platformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());
			//获取音频文件内容
			String content = ebm.getContent().getAuxiliary().getAuxiliaryBody();
			//文件后缀(2 对应 MP3)
			String filesub = ebm.getContent().getAuxiliary().getAuxiliaryType();
			//转码并返回生成的音频文件
			File videofile = getVideoFile(ebm,content,filesub);
			FileUploadRequest fileUploadRequest = new FileUploadRequest();
			fileUploadRequest.setAuditState("1");
			fileUploadRequest.setLibId("90515713fac7433c8e7af2ba3743f39c");
			fileUploadRequest.setOriginName(videofile.getName());
			fileUploadRequest.setUploadedName(videofile.getName());
			cn.comtom.tools.utils.FtpUtil ftpUtil = new cn.comtom.tools.utils.FtpUtil(hostname,port,username,password);
			//systemFeginService.getSysParamsInfoByKey(paramKey)
			FileLibraryInfo fileLibraryInfo = resoFeginService.getFileLibraryInfoByLibId(fileUploadRequest.getLibId());;
		    String uri = fileLibraryInfo.getLibURI();
		    uri = uri.substring(1) ;

			SysParamsInfo sysParamsInfo2 = systemFeginService.getSysParamsInfoByKey(Constants.FTP_FILE_PATH);
			String filePath = Optional.ofNullable(sysParamsInfo2).map(SysParamsInfo::getParamValue).orElseGet(String::new);
			filePath += uri;
			FileInputStream fileInputStream = new FileInputStream(videofile);
			ftpUtil.uploadFile(filePath,videofile.getName(),fileInputStream);

			FileInfo fileInfo = saveUploadFile(fileUploadRequest,filePath,videofile,"admin");

			ProgramAddRequest programAddRequest = new ProgramAddRequest();
			programAddRequest.setEbmEventType(ebm.getEventType());
			programAddRequest.setProgramLevel(Integer.valueOf(ebm.getSeverity()));
			programAddRequest.setAuditUser("admin");
			programAddRequest.setAuditResult(1);
			programAddRequest.setAuditTime(new Date());
			programAddRequest.setAuditOpinion("ok");
			programAddRequest.setProgramType(1);
			List<ProgramAreaInfo> areaList = Lists.newArrayList();
			ProgramAreaInfo programAreaInfo = new ProgramAreaInfo();
			programAreaInfo.setAreaCode(ebm.getGeocode());
			areaList.add(programAreaInfo);

			List<ProgramFilesInfo> fileList =  Lists.newArrayList();
			ProgramFilesInfo programFiles = new ProgramFilesInfo();
			programFiles.setFileId(fileInfo.getId());
			programFiles.setFileName(videofile.getName());
			fileList.add(programFiles);

			ProgramStrategyInfo programStrategy = new ProgramStrategyInfo();
			programStrategy.setPlayTime(formatDate(ebm.getSendTime()));
			List<ProgramTimeInfo> timeList = Lists.newArrayList();
			programStrategy.setTimeList(timeList);
			ProgramTimeInfo programTimeInfo = new ProgramTimeInfo();
			programTimeInfo.setTimeId(String.valueOf(System.currentTimeMillis()));
			programTimeInfo.setStartTime(formatTime(ebm.getSendTime()));
			programTimeInfo.setOverTime(formatTime(ebm.getEndTime()));
			programTimeInfo.setHandleFlag(1);
			timeList.add(programTimeInfo);
			programAddRequest.setContentType(2);
			ProgramContentInfo programContent = new ProgramContentInfo();
			if(ebm.getContent()!=null && StringUtils.isNotBlank(ebm.getContent().getDescription())) {
				programContent.setContent(ebm.getContent().getDescription());
				programAddRequest.setLanguageCode(ebm.getContent().getLanguageCod());
				programAddRequest.setContentType(2);
			}
			programAddRequest.setProgramName("四川应急广播");
			programAddRequest.setAreaList(areaList);
			programAddRequest.setFilesList(fileList);
			programAddRequest.setProgramStrategy(programStrategy);
			programAddRequest.setProgramContent(programContent);
			createProgramForSc(programAddRequest);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

    }

	/*
	 * 获取音频文件
	 */
	private File getVideoFile(EBM ebm,String content,String filesub){

		Videodecode videodecode = new Videodecode();
		File f = null;

		//如果文件小于2M，则解base64编码获取音频文件
		if(null!=content && !content.equals("")){
			//转码并保存
			f =  videodecode.analysis64coder(content,filesub);
		}
//		if(false) {}
		//如果文件大于2M，通过FTP协议地址传输获取文件
		/*
		 * else{ String url = ebm.getContent().getAuxiliary().getResourceDesc();
		 * //获取Ftp协议的基础参数 FtpConfig ftpConfig = FtpUtil.getURLDetail(url);
		 *
		 * //需要封装用户名和密码 ftpConfig.setFtpUser("ftpuser");
		 * ftpConfig.setFtpPassword("scyjgb"); try { if(FtpUtil.connectFtp(ftpConfig)){
		 * //文件存储位置 SysParamsInfo temp_file_path_ =
		 * systemFeginService.getByKey(Constants.TEMP_FILE_PATH); String localFilePath =
		 * temp_file_path_.getParamValue()+ File.separator + "sichuan_" +
		 * UUID.randomUUID().toString().toLowerCase()+"_"; //通过FTP下载文件到指定目录并返回文件名 String
		 * fileName = FtpUtil.DownFileByFTP(localFilePath, ftpConfig.getFtpPath());
		 *
		 * if(!fileName.equals("")){ f = new File(localFilePath + fileName); } }else{
		 * System.out.println("FTP Protocol connect failed！"); }
		 *
		 * } catch (Exception e) { e.printStackTrace();
		 * System.out.println("Error occurred from FTP Protocol Transmission！");
		 * }finally{ FtpUtil.closeFtp(); } }
		 */
		return f;
	}

    @Override
    public void cancel(List<ProgramUnitInfo> programUnitInfoList) {
        Optional.ofNullable(programUnitInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .forEach(this::cancelProgramUnit);

    }

    /**
     * 取消播发；
     * 生成取消播发的EBM消息，以及取消播发的EBM分发消息。
     * 在下一次消息分发定时任务执行时，将取消播发的EBM消息下发下去
     * @param programUnitInfo
     */
    private void cancelProgramUnit(ProgramUnitInfo programUnitInfo) {
        SchemeInfo schemeInfo = schemeService.getSchemeInfoByProgramId(programUnitInfo.getProgramId());
        if(schemeInfo == null){
            return;
        }
        Ebm ebm = ebmService.selectById(schemeInfo.getEbmId());

        //创建一条用于取消播发的EBM消息
        Ebm ebmAdd = new Ebm();
        BeanUtils.copyProperties(ebm,ebmAdd);
        String ebmId = ebm.getEbmId().substring(0,22)+ DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD)+sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
        ebmAdd.setEbmId(ebmId);
        ebmAdd.setRelatedEbmId(ebm.getEbmId());                          //设置关联的EBM消息为需要取消的EBM消息
        ebmAdd.setMsgType(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey());     //设置消息类型为取消播发
        ebmAdd.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());     //设置消息状态为创建
        ebmService.save(ebmAdd);

        //不经过生成方案，预案匹配之类的流程，直接生成待分发的消息（ebmDispatch）
        List<cn.comtom.domain.core.ebm.info.EbmDispatchInfo> ebmDispatchInfoList = ebmDispatchService.getEbmDispatchByEbmId(ebm.getEbmId());
        List<EbmDispatch> ebmDispatchList = Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebmDispatchInfo -> {
                    EbmDispatch ebmDispatch = new EbmDispatch();
                    BeanUtils.copyProperties(ebmDispatchInfo,ebmDispatch);
                    ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
                    ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());    //分发状态为取消
                    ebmDispatch.setEbmId(ebmId);             //关联取消播发的EBM消息
                    ebmDispatch.setFailCount(0);
                    return ebmDispatch;
                }).collect(Collectors.toList());
        ebmDispatchService.saveList(ebmDispatchList);
    }

    private EbmInfo createEbm(EBM ebm,String platformId,String platformName) {
    	log.info("【sichuan】~节目预处理，开始创建EBM消息！，programId:{}");
        if(ebm == null){
        	log.error("【programDecompose】~节目预处理，节目不存在！");
            return null;
        }
        //创建流程
        DispatchFlowInfo dispatchFlowInfo = saveDispatchFlow(ebm);

        //创建ebm消息
        Ebm ebmEntity = new Ebm();
        String ebmId = platformId + DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD)+sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
        ebmEntity.setEbmVersion(CommonDictEnum.EBM_VERSION_INIT.getKey());
        ebmEntity.setEbmId(ebmId);
        ebmEntity.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());
       // ebmEntity.setRelatedEbIId(null);
       // ebmEntity.setRelatedEbmId(relatedEbmId);
        ebmEntity.setMsgType(TypeDictEnum.EBM_MSG_TYPE_PLAY.getKey());
        ebmEntity.setSendName(platformName);
        ebmEntity.setSenderCode(platformId);
        ebmEntity.setSendTime(new Date());
        ebmEntity.setEventType(ebm.getEventType());
        ebmEntity.setSeverity(ebm.getSeverity());
        ebmEntity.setStartTime(DateUtil.stringToDate(ebm.getSendTime()));
        ebmEntity.setEndTime(DateUtil.stringToDate(ebm.getEndTime()));
        ebmEntity.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_INIT.getKey()));
        ebmEntity.setSendFlag(CommonDictEnum.FLAG_RECEIVE.getKey());
        ebmEntity.setFlowId(dispatchFlowInfo.getFlowId());
        ebmEntity.setMsgLanguageCode(TypeDictEnum.EBM_LAN_CODE_ZHO.getKey());
        if(ebm.getContent() != null && StringUtils.isNotBlank(ebm.getContent().getDescription())){
            ebmEntity.setMsgDesc(ebm.getContent().getDescription());
        }
        ebmEntity.setAreaCode(ebm.getGeocode());
        ebmEntity.setCreateTime(new Date());
        ebmEntity.setTimeOut(Integer.valueOf(CommonDictEnum.EBM_TIMEOUT_FALSE.getKey()));
        ebmEntity.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
        ebmEntity.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
        ebmEntity.setAuditUser(Constants.DEFAULT_USER);
        ebmEntity.setAuditTime(new Date());
        ebmService.save(ebmEntity);
        log.info("【sichuan】~节目预处理，创建EBM完毕！，programId:{},ebmId:{}",ebmEntity.getEbmId());
        EbmInfo ebmInfo = new EbmInfo();
        BeanUtils.copyProperties(ebmEntity,ebmInfo);
         //更新流程状态
        updateDispatchFlowState(dispatchFlowInfo.getFlowId(),FlowConstants.STAGE_RESPONSE,FlowConstants.STATE_SCHEME_CREATE);
        SchemeInfo schemeInfo = null;
        if(ebmInfo != null){
            //创建方案
            schemeInfo = createScheme(ebmEntity, dispatchFlowInfo.getFlowId(), ebmInfo);
            log.info("【sichuan】~节目预处理，创建调度方案完毕！，programId:{},schemeId:{}",schemeInfo.getSchemeId());
        }

        if(schemeInfo != null){
            //预案匹配
            matchEbrByPlan(ebmEntity,ebmInfo,schemeInfo);
            log.info("【sichuan】~节目预处理，预案匹配完毕！,ebmId:{}，schemeId:{}",ebmEntity.getEbmId(),schemeInfo.getSchemeId());
            Ebm request = new Ebm();
            request.setEbmId(ebmId);
            request.setSchemeId(schemeInfo.getSchemeId());
            request.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());
            request.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_READY.getKey()));
            ebmService.update(request);

        }


        return  ebmInfo;
    }

    public FileInfo saveUploadFile(FileUploadRequest req, String filePath, File multipartFile, String account) {
        String url = filePath + multipartFile.getName();
        FileAddRequest request = new FileAddRequest();
        String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(multipartFile));
        /*FileInfo fileInfo = resoFeginService.getFileByMd5(md5);
        if(fileInfo != null){
            return fileInfo;
        }*/
        Long duration = FileUtils.getDuration(multipartFile);
        request.setSecondLength(duration.intValue());
        request.setFileType(TypeDictEnum.FILE_TYPE_MEDIA.getKey());
        request.setFileUrl(url);
        request.setByteSize(multipartFile.length());
        request.setCreateUser(account);
        request.setFileExt(FilenameUtils.getExtension(multipartFile.getName()));
        request.setLibId(req.getLibId());
        request.setMd5Code(md5);
        request.setOriginName(multipartFile.getName());
        request.setUploadedName(multipartFile.getName());
        request.setAuditState(req.getAuditState());
        return  resoFeginService.saveFile(request);
    }

    private void matchEbrByPlan(Ebm ebm,EbmInfo ebmInfo,SchemeInfo schemeInfo) {
        Double square = 0.0;
        Double population = 0.0;
        //事件级别
        Integer eventLevel = Integer.valueOf(ebm.getSeverity());
        //事件类型
        String eventType =ebm.getEventType();
        //消息下发区域
        String areaCodes = ebm.getAreaCode();

        List<SchemeEbr> schemeEbrList = new ArrayList<>();
        List<EbmDispatch> ebmDispatchList = new ArrayList<>();

        List<SysPlanMatchInfo> sysPlanMatchInfoList =systemFeginService.getMatchPlan(eventLevel+"",eventType ,areaCodes);
        log.info("【programDecompose】~节目预处理，预案匹配: ebmId:{},eventLevel:{},eventType:{},areaCodes:{},匹配结果:{}",ebmInfo.getEbmId(),eventLevel,eventType,areaCodes,CollectionUtils.isEmpty(sysPlanMatchInfoList));
        //查询出调度预案关联的播出资源ID
        List<SysPlanResoRefInfo> sysPlanResoRefInfoList = new ArrayList<>();
        Optional.ofNullable(sysPlanMatchInfoList).orElse(Collections.emptyList()).forEach(sysPlanMatchInfo -> {
            List<SysPlanResoRefInfo> sysPlanResoRefInfos = systemFeginService.getPlanResRefByPlanId(sysPlanMatchInfo.getPlanId());
            if(sysPlanResoRefInfos!= null && !sysPlanResoRefInfos .isEmpty()){
                sysPlanResoRefInfoList.addAll(sysPlanResoRefInfos);
            }
        });
        if(!sysPlanResoRefInfoList.isEmpty()){
        	//资源过滤,去重
        	List<SysPlanResoRefInfo> sysPlanResoRefInfoLists = filterProgramEbr(ebm,sysPlanResoRefInfoList);
            for(SysPlanResoRefInfo sysPlanResoRefInfo : sysPlanResoRefInfoLists) {

                EbmDispatch ebmDispatch = new EbmDispatch();
                SchemeEbr schemeEbr = new SchemeEbr();

                String areaCode = null;
                //根据资源类型，分别获取不同类型的播出资源的信息
                if(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(sysPlanResoRefInfo.getResoType())){
                    //播出平台
                    EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysPlanResoRefInfo.getResoCode());
                    areaCode = ebrPlatformInfo.getAreaCode();
                    square += ebrPlatformInfo.getSquare() == null ? 0 : ebrPlatformInfo.getSquare();
                    population += ebrPlatformInfo.getPopulation() == null ? 0 : ebrPlatformInfo.getPopulation();
                    ebmDispatch.setPsEbrId(ebrPlatformInfo.getPsEbrId());
                }

                //播出系统
                if(Constants.EBR_SUB_SOURCE_TYPE_BROADCAST.equals(sysPlanResoRefInfo.getResoType())){
                    //播出系统编码
                    ebmDispatch.setBsEbrId(sysPlanResoRefInfo.getResoCode());
                    EbrBroadcastInfo ebrBroadcastInfo = resoFeginService.getEbrBroadcastInfoById(sysPlanResoRefInfo.getResoCode());
                    areaCode = ebrBroadcastInfo.getAreaCode();
                    square += ebrBroadcastInfo.getSquare() == null ? 0 : ebrBroadcastInfo.getSquare();
                    population += ebrBroadcastInfo.getPopulation() == null ? 0 : ebrBroadcastInfo.getPopulation();
                }
                //适配器
                if(Constants.EBR_SUB_SOURCE_TYPE_ADAPTOR.equals(sysPlanResoRefInfo.getResoType())){
                    ebmDispatch.setAsEbrId(sysPlanResoRefInfo.getResoCode());
                    EbrAdapterInfo adapterinfo = resoFeginService.getEbrAdapterByEbrId(sysPlanResoRefInfo.getResoCode());
                    areaCode = adapterinfo.getAreaCode();
                }
                //台站
                if(Constants.EBR_SUB_SOURCE_TYPE_STATION.equals(sysPlanResoRefInfo.getResoType())){
                    ebmDispatch.setStEbrId(sysPlanResoRefInfo.getResoCode());
                    EbrStationInfo ebrStation = resoFeginService.getEbrStationByEbrId(sysPlanResoRefInfo.getResoCode());
                    areaCode = ebrStation.getAreaCode();
                }

                schemeEbr.setSchemeId(schemeInfo.getSchemeId());
                schemeEbr.setEbrId(sysPlanResoRefInfo.getResoCode());
                schemeEbr.setEbrType(sysPlanResoRefInfo.getResoType());
                schemeEbr.setEbrArea(areaCode);
                schemeEbr.setId(UUIDGenerator.getUUID());
                schemeEbrList.add(schemeEbr);

                String lanCode = ebm.getMsgLanguageCode();
                ebmDispatch.setEbmId(ebmInfo.getEbmId());
                ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
             //   ebmDispatch.setEbdId(ebd.getEBDID());
                ebmDispatch.setFailCount(0);
                ebmDispatch.setLanguageCode(lanCode);
                ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
                ebmDispatchList.add(ebmDispatch);
            }

            //方案自动审核 方案预评估效果
            Scheme scheme = new Scheme();
            scheme.setSchemeId(schemeInfo.getSchemeId());
            scheme.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
            scheme.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
            scheme.setAuditUser(Constants.DEFAULT_USER);
            scheme.setAuditTime(new Date());
            scheme.setState(StateDictEnum.SCHEME_STATE_COMMIT.getKey());

            scheme.setAreaPercent(MathUtil.divide(square, schemeInfo.getTotalArea(), 2).doubleValue());
            scheme.setPopuPercent(MathUtil.divide(population, schemeInfo.getTotalPopu(), 2).doubleValue());
            scheme.setSchemeId(schemeInfo.getSchemeId());
            scheme.setPlanId(sysPlanResoRefInfoList.get(0).getPlanId());
            schemeService.update(scheme);

            saveEbmDispatchInfoInfo(ebmInfo.getEbmId(),ebm.getMsgTitle(),schemeInfo.getFlowId());

            // 保存EBM调度资源
            ebmDispatchService.saveList(ebmDispatchList);
            log.info("【programDecompose】~节目预处理，保存消息分发记录ebmDispatch！，ebmId:{},size:{}",ebmInfo.getEbmId(),ebmDispatchList.size());

            // 保存方案关联资源
            schemeEbrService.saveList(schemeEbrList);

        }else{
        	log.info("【programDecompose】~节目预处理，没有匹配的预案/预案没有关联的资源！，ebmId:{},schemeId:{}",ebmInfo.getEbmId(),schemeInfo.getSchemeId());
        }

    }

    private List<SysPlanResoRefInfo> filterProgramEbr(Ebm ebm, List<SysPlanResoRefInfo> list) {
       List<SysPlanResoRefInfo> list1 = list.stream().filter(p -> StringUtils.isNotBlank(p.getResoCode())).collect(Collectors.toList());
    	//匹配到多个预案的情况，过滤掉重复资源
       Set<SysPlanResoRefInfo> set = new TreeSet<SysPlanResoRefInfo>((s1, s2) -> s1.getResoCode().compareTo(s2.getResoCode()));
       set.addAll(list1);
       List<SysPlanResoRefInfo> list2 = new ArrayList<SysPlanResoRefInfo>(set);
    	//如果是带媒体文件的消息/日常节目&资源类型字幕插播，则过滤掉
       List<SysPlanResoRefInfo> list3 = Collections.synchronizedList(list2);
        if(StringUtils.isBlank(ebm.getMsgDesc())) {
        	for (SysPlanResoRefInfo spr : list3) {
        		String channel= resoFeginService.getChannelByEbrId(spr.getResoCode());
            	if(!CommonDictEnum.EBR_CHANNEL_2.getKey().equals(channel)) {
            		list3.remove(spr);
            	}
			}
        }
        log.info("【programDecompose】 filterProgramEbr,list size:{}",list3.size());
        return list3;
	}

	private EbmDispatchInfoInfo saveEbmDispatchInfoInfo(String ebmId, String msgTitle, String flowId) {
        EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
        ebmDispatchInfo.setInfoId(UUIDGenerator.getUUID());
        ebmDispatchInfo.setEbmId(ebmId);
        ebmDispatchInfo.setInfoTitle(DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD).concat("").concat(Constants.EBM_DIAPATCH_INFO_TITLE_SUFFIX));
        //自动审核
        ebmDispatchInfo.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
        ebmDispatchInfo.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
        ebmDispatchInfo.setAuditTime(new Date());
        ebmDispatchInfo.setAuditUser(CommonDictEnum.EBM_DEFAULT_USER.getKey());
        ebmDispatchInfoService.save(ebmDispatchInfo);
        EbmDispatchInfoInfo ebmDispatchInfoInfo = new EbmDispatchInfoInfo();
        BeanUtils.copyProperties(ebmDispatchInfo,ebmDispatchInfoInfo);
      //  String flowState = FlowConstants.STATE_SCHEME_AUDIT;                           //流程为消息分发
        //更新流程 为消息审核
     //  updateDispatchFlowState(flowId,FlowConstants.STAGE_PROCESS,flowState);

        return ebmDispatchInfoInfo;
    }

    //生成方案
    private SchemeInfo createScheme(Ebm ebm, String flowId, EbmInfo ebmInfo) {
        List<String> areaCodeList = Arrays.asList( ebm.getAreaCode().split(","));
        BigDecimal square = new BigDecimal(0);
        BigDecimal population = new BigDecimal(0);
        // 根据节目关联区域，计算区域覆盖面积、人口
        List<RegionAreaInfo> regionAreaInfoList = systemFeginService.getRegionAreaByAreaCodes(areaCodeList);
        if(regionAreaInfoList != null){
            for(RegionAreaInfo regionAreaInfo : regionAreaInfoList){
                square = square.add(new BigDecimal(regionAreaInfo.getAreaSquare()));
                population = population.add(new BigDecimal(regionAreaInfo.getAreaPopulation()));
            }
        }

        String schemeName = FlowConstants.SCHEME_NAME_PREFIX + DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYYYMMDDHHMMSS);

        Scheme schemeAddRequest = new Scheme();
        schemeAddRequest.setSchemeTitle(schemeName);
        schemeAddRequest.setFlowId(flowId);
       // schemeAddRequest.setEbdId(ebdId);
        schemeAddRequest.setEbmId(ebmInfo.getEbmId());
        schemeAddRequest.setTotalArea(square.doubleValue());
        schemeAddRequest.setTotalPopu(population.doubleValue());
        //schemeAddRequest.setInfoId(infoId);
        schemeAddRequest.setState(StateDictEnum.SCHEME_STATE_CREATE.getKey());
        schemeAddRequest.setProgramId(ebm.getEbmId());
         schemeService.save(schemeAddRequest);
        SchemeInfo schemeInfo = new SchemeInfo();
        BeanUtils.copyProperties(schemeAddRequest,schemeInfo);

        updateDispatchFlowState(flowId,FlowConstants.STAGE_RESPONSE,FlowConstants.STATE_SCHEME_AUDIT);
        return schemeInfo;
    }

    //更新流程状态
    private int updateDispatchFlowState(String flowId, String flowStage, String flowState) {
        DispatchFlow request = new DispatchFlow();
        request.setFlowId(flowId);
        request.setFlowStage(flowStage);
        request.setFlowState(flowState);
        request.setUpdateTime(new Date());
        return dispatchFlowService.update(request);
    }

    private DispatchFlowInfo saveDispatchFlow(EBM ebm) {
        DispatchFlowInfo dispatchFlowInfo = new DispatchFlowInfo();
     //   String ebmId = plateformId + DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD)+sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
        DispatchFlow dispatchFlow = new DispatchFlow();
        dispatchFlow.setFlowId(UUIDGenerator.getUUID());
        dispatchFlow.setCreateTime(new Date());
        dispatchFlow.setRelatedProgramId(ebm.getMsgID());
        dispatchFlow.setFlowStage(FlowConstants.STAGE_STARTING);
        dispatchFlow.setFlowState(FlowConstants.STATE_INFO);
        dispatchFlowService.save(dispatchFlow);
        BeanUtils.copyProperties(dispatchFlow,dispatchFlowInfo);
        return dispatchFlowInfo;
    }

    public void createProgramForSc(ProgramAddRequest request) {
	    Integer programType = request.getProgramType();
	    if(programType.equals(Constants.PROGRAM_TYPE_RC)){
	        //日常广播没有事件类型以及事件描述
	        request.setEbmEventDesc(null);
	        request.setEbmEventType(null);
	    }

	    SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
	    EbrPlatformInfo platformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());
	    request.setEbrId(platformInfo.getPsEbrId());
	    request.setEbrName(platformInfo.getPsEbrName());
	    request.setCreateUser("admin");
	    //如果是文本类型的节目，不保存文件列表
	    /*if(request.getProgramContent() != null && StringUtils.isNotBlank(request.getProgramContent().getContent())){
	        request.setFilesList(null);
	        Integer secondLength=10;
	        request.getProgramContent().setSecondLength(secondLength);
	    }*/
	    ProgramStrategyInfo programStrategyInfo = request.getProgramStrategy();
	    if(programStrategyInfo.getStrategyType()==null){//当没有获取到播放策略类型时默认为一次性，主要是解决应急播放策略类型为空的问题
	        request.getProgramStrategy().setStrategyType(1);
	    }

	    if(programStrategyInfo != null && programStrategyInfo.getStrategyType() == 1){
	        //一次性播放节目
	        programStrategyInfo.setOverTime(null);
	        programStrategyInfo.setStartTime(null);
	        programStrategyInfo.setWeekMask(null);
	    }else if(programStrategyInfo != null && programStrategyInfo.getStrategyType() == 2){
	        //每日播放节目
	        programStrategyInfo.setPlayTime(null);
	        programStrategyInfo.setWeekMask(null);
	    }else if(programStrategyInfo != null && programStrategyInfo.getStrategyType() == 3){
	        //每周播放任务
	        programStrategyInfo.setPlayTime(null);
	    }
	    ProgramInfo programInfo = programService.saveProgramInfo(request);
	    ProgramAuditRequest programAuditRequest = new ProgramAuditRequest();
	    programAuditRequest.setAuditUser("admin");
	    programAuditRequest.setAuditTime(new Date());
	    programAuditRequest.setAuditResult(1);
	    programAuditRequest.setAuditOpinion("ok");
	    programAuditRequest.setProgramId(programInfo.getProgramId());
	    programService.auditProgramInfo(programAuditRequest);
    }
}
