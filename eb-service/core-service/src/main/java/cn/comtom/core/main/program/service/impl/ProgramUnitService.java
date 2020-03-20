package cn.comtom.core.main.program.service.impl;

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
import cn.comtom.core.main.mqListener.MQMessageProducer;
import cn.comtom.core.main.program.dao.ProgramUnitDao;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.core.main.program.service.IProgramService;
import cn.comtom.core.main.program.service.IProgramUnitService;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.core.main.scheme.service.ISchemeEbrService;
import cn.comtom.core.main.scheme.service.ISchemeService;
import cn.comtom.core.main.svs.service.SVSService;
import cn.comtom.core.main.utils.SequenceGenerate;
import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.program.info.ProgramAreaInfo;
import cn.comtom.domain.core.program.info.ProgramFilesInfo;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.EbmCreateRequest;
import cn.comtom.domain.core.program.request.ProgramUnitPageRequest;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import cn.comtom.tools.utils.DateUtil;
import cn.comtom.tools.utils.MathUtil;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadFileWriter;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgramUnitService extends BaseServiceImpl<ProgramUnit, String> implements IProgramUnitService {

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
    //private UploadFileAsyncTask asyncTask;
    @Autowired
    private MQMessageProducer mqMessageProducer;

    @Autowired
    private TTSComponent ttsComponent;

    @Autowired
    private SVSService svsService;

    @Autowired
    private FastFileStorageClient storageClient;


    @Override
    public BaseDao<ProgramUnit, String> getDao() {
        return programUnitDao;
    }

    @Override
    public List<ProgramUnitInfo> getProgramUnitInfoList(ProgramUnitPageRequest request) {
        return programUnitDao.getProgramUnitInfoList(request);
    }

    @Override
    public List<EbmInfo> createEbms(EbmCreateRequest request) {
        if (request == null || request.getUnitIds() == null || request.getUnitIds().isEmpty()) {
            return new ArrayList<>();
        }
        SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo platformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());
        List<ProgramUnit> programUnitList = programUnitDao.selectByIds(request.getUnitIds());
        List<EbmInfo> ebmInfoList = Optional.ofNullable(programUnitList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(programUnit -> createEbm(programUnit, platformInfo.getPsEbrId(), platformInfo.getPsEbrName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ebmInfoList;
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
     *
     * @param programUnitInfo
     */
    private void cancelProgramUnit(ProgramUnitInfo programUnitInfo) {
        SchemeInfo schemeInfo = schemeService.getSchemeInfoByProgramId(programUnitInfo.getProgramId());
        if (schemeInfo == null) {
            return;
        }
        Ebm ebm = ebmService.selectById(schemeInfo.getEbmId());

        //创建一条用于取消播发的EBM消息
        Ebm ebmAdd = new Ebm();
        BeanUtils.copyProperties(ebm, ebmAdd);
        String ebmId = ebm.getEbmId().substring(0, 22) + DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD) + sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
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
                    BeanUtils.copyProperties(ebmDispatchInfo, ebmDispatch);
                    ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
                    ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());    //分发状态为取消
                    ebmDispatch.setEbmId(ebmId);             //关联取消播发的EBM消息
                    ebmDispatch.setFailCount(0);
                    return ebmDispatch;
                }).collect(Collectors.toList());
        ebmDispatchService.saveList(ebmDispatchList);
    }

    private EbmInfo createEbm(ProgramUnit programUnit, String platformId, String platformName) {
        log.info("【programDecompose】~节目预处理，开始创建EBM消息！，programId:{}", programUnit.getProgramId());
        ProgramInfo programInfo = programService.getProgramInfoById(programUnit.getProgramId());
        if (programInfo == null) {
            log.error("【programDecompose】~节目预处理，节目不存在！");
            return null;
        }
        //创建流程
        DispatchFlowInfo dispatchFlowInfo = saveDispatchFlow(programInfo);

        //创建ebm消息
        Ebm ebmEntity = new Ebm();
        String ebmId = platformId + DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD) + sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
        ebmEntity.setEbmVersion(CommonDictEnum.EBM_VERSION_INIT.getKey());
        ebmEntity.setEbmId(ebmId);
        ebmEntity.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());
        // ebmEntity.setRelatedEbIId(null);
        // ebmEntity.setRelatedEbmId(relatedEbmId);
        ebmEntity.setMsgType(TypeDictEnum.EBM_MSG_TYPE_PLAY.getKey());
        ebmEntity.setSendName(platformName);
        ebmEntity.setSenderCode(platformId);
        ebmEntity.setSendTime(new Date());
        ebmEntity.setEventType(programInfo.getEbmEventType());
        ebmEntity.setSeverity(String.valueOf(programInfo.getProgramLevel()));
        ebmEntity.setStartTime(DateUtil.stringToDate(DateUtil.format(programUnit.getPlayDate()) + SymbolConstants.BLANK_SPLIT + programUnit.getStartTime()));
        ebmEntity.setEndTime(DateUtil.stringToDate(DateUtil.format(programUnit.getPlayDate()) + SymbolConstants.BLANK_SPLIT + programUnit.getEndTime()));
        ebmEntity.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_INIT.getKey()));
        ebmEntity.setSendFlag(CommonDictEnum.FLAG_RECEIVE.getKey());
        ebmEntity.setFlowId(dispatchFlowInfo.getFlowId());
        ebmEntity.setMsgLanguageCode(TypeDictEnum.EBM_LAN_CODE_ZHO.getKey());
        ebmEntity.setMsgTitle(programInfo.getProgramName());
        if (programInfo.getContentType().intValue() == Constants.PROGRAM_CONTENT_TYPE_TEXT.intValue() && programInfo.getProgramContent() != null) {
            ebmEntity.setMsgDesc(programInfo.getProgramContent().getContent());
            //文转语音
        } else if (programInfo.getContentType().intValue() == Constants.PROGRAM_CONTENT_TYPE_TXT_TO_AUDIO.intValue() && programInfo.getProgramContent() != null) {
            ebmEntity.setMsgDesc(programInfo.getProgramContent().getContent());
        }

        if (programInfo.getAreaList() != null && !programInfo.getAreaList().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < programInfo.getAreaList().size(); i++) {
                if (i == programInfo.getAreaList().size() - 1) {
                    sb.append(programInfo.getAreaList().get(i).getAreaCode());
                } else {
                    sb.append(programInfo.getAreaList().get(i).getAreaCode()).append(",");
                }
            }
            ebmEntity.setAreaCode(sb.toString());
        }
        ebmEntity.setCreateTime(new Date());
        ebmEntity.setTimeOut(Integer.valueOf(CommonDictEnum.EBM_TIMEOUT_FALSE.getKey()));
        ebmEntity.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
        ebmEntity.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
        ebmEntity.setAuditUser(Constants.DEFAULT_USER);
        ebmEntity.setAuditTime(new Date());
        //接入状态
        Integer accessState = 1;
        if (ebmEntity.getEndTime().getTime() < new Date().getTime()) {
            accessState = 3;
        } else if (ebmEntity.getEndTime().getTime() > new Date().getTime()) {
            accessState = 2;
        }
        ebmEntity.setAccessState(accessState);
        //文转语
        String apath = getEbmAudioPath(ebmEntity, programInfo.getFilesList());
        ebmEntity.setAudioPath(apath);

        ebmService.save(ebmEntity);
        if (accessState == 3) {
            log.error("节目消息已经过期，不再创建方案，匹配预案，不进行消息下发,programId:{}", programInfo.getProgramId());
            return null;
        }
        log.info("【programDecompose】~节目预处理，创建EBM完毕！，programId:{},ebmId:{}", programUnit.getProgramId(), ebmEntity.getEbmId());
        EbmInfo ebmInfo = new EbmInfo();
        BeanUtils.copyProperties(ebmEntity, ebmInfo);
        //更新流程状态
        updateDispatchFlowState(dispatchFlowInfo.getFlowId(), FlowConstants.STAGE_RESPONSE, FlowConstants.STATE_SCHEME_CREATE);
        SchemeInfo schemeInfo = null;
        if (ebmInfo != null) {
            //创建方案
            schemeInfo = createScheme(programUnit, programInfo, dispatchFlowInfo.getFlowId(), ebmInfo);
            log.info("【programDecompose】~节目预处理，创建调度方案完毕！，programId:{},schemeId:{}", programUnit.getProgramId(), schemeInfo.getSchemeId());
        }

        if (schemeInfo != null) {
            //预案匹配
            matchEbrByPlan(programInfo, ebmInfo, schemeInfo);
            log.info("【programDecompose】~节目预处理，预案匹配完毕！，programId:{},ebmId:{}，schemeId:{}", programUnit.getProgramId(), ebmEntity.getEbmId(), schemeInfo.getSchemeId());
            Ebm request = new Ebm();
            request.setEbmId(ebmId);
            request.setSchemeId(schemeInfo.getSchemeId());
            request.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());
            request.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_READY.getKey()));
            ebmService.update(request);

        }
        //  播发状态反馈
        EbmStateResponseMessage message = new EbmStateResponseMessage();
        message.setEbmId(ebmInfo.getEbmId());
        message.setBroadcastState(StateDictEnum.EBM_BROADCAST_STATE_INIT.getKey());
        mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue, JSON.toJSONString(message));
        svsService.callEbmNotice();
        return ebmInfo;
    }

    private String getReceiveTempPath() {
        SysParamsInfo paramsInfo = systemFeginService.getByKey(Constants.TEMP_FILE_PATH);
        if (paramsInfo != null) {
            return paramsInfo.getParamValue();
        }
        return null;
    }

    /**
     * 文转语路径
     *
     * @param ebm
     * @param fileList
     * @return
     */
    private String getEbmAudioPath(Ebm ebm, List<ProgramFilesInfo> fileList) {
        String audioPath = null;
        try {
            if (!CollectionUtils.isEmpty(fileList)) {
                String dirPath = getReceiveTempPath() + "audio/";
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                StringBuilder sb = new StringBuilder();
                for (ProgramFilesInfo info : fileList) {
                    //需要保存的ebd文件关联信息列表
                    FileInfo fileInfo = resoFeginService.getFileInfoById(info.getFileId());
                    //获取fastdfs路径
                    String filePath = fileInfo.getFilePath();
                    String fileName = fileInfo.getOriginName();
                    //获取文件后缀名生成新的本地名称
                    String suffix = fileName.substring(fileName.lastIndexOf("."));
                    String newfileName = UUIDGenerator.getUUID() + suffix;

                    //下载文转语到本地临时目录
                    DownloadFileWriter localWriter = new DownloadFileWriter(dirPath.concat(newfileName));
                    storageClient.downloadFile(filePath.substring(0, filePath.indexOf("/")),
                            filePath.substring(filePath.indexOf("/") + 1), localWriter);

                    sb.append("/audio/").append(newfileName).append(",");
                }
                audioPath = sb.deleteCharAt(sb.length() - 1).toString();
            } else if (StringUtils.isNotBlank(ebm.getMsgDesc())) {
                //文转语
                audioPath = ttsComponent.getTextToAudioFilePath(ebm.getMsgDesc());
            }
        } catch (Exception e) {
            log.error("上传语音文件 || EBM文转语异常，error:{}", e.getMessage());
            e.printStackTrace();
        }
        return audioPath;
    }

    private void matchEbrByPlan(ProgramInfo programInfo, EbmInfo ebmInfo, SchemeInfo schemeInfo) {
        Double square = 0.0;
        Double population = 0.0;
        //事件级别
        Integer eventLevel = programInfo.getProgramLevel();
        //事件类型
        String eventType = programInfo.getEbmEventType();
        //消息下发区域
        String areaCodes = ebmInfo.getAreaCode();

        List<SchemeEbr> schemeEbrList = new ArrayList<>();
        List<EbmDispatch> ebmDispatchList = new ArrayList<>();

        List<SysPlanMatchInfo> sysPlanMatchInfoList = systemFeginService.getMatchPlan(eventLevel + "", eventType, areaCodes);
        log.info("【programDecompose】~节目预处理，预案匹配: ebmId:{},eventLevel:{},eventType:{},areaCodes:{},匹配结果:{}", ebmInfo.getEbmId(), eventLevel, eventType, areaCodes, CollectionUtils.isEmpty(sysPlanMatchInfoList));
        //查询出调度预案关联的播出资源ID
        List<SysPlanResoRefInfo> sysPlanResoRefInfoList = new ArrayList<>();
        Optional.ofNullable(sysPlanMatchInfoList).orElse(Collections.emptyList()).forEach(sysPlanMatchInfo -> {
            List<SysPlanResoRefInfo> sysPlanResoRefInfos = systemFeginService.getPlanResRefByPlanId(sysPlanMatchInfo.getPlanId());
            if (sysPlanResoRefInfos != null && !sysPlanResoRefInfos.isEmpty()) {
                sysPlanResoRefInfoList.addAll(sysPlanResoRefInfos);
            }
        });
        if (!sysPlanResoRefInfoList.isEmpty()) {
            String flowType = sysPlanMatchInfoList.get(0).getFlowType();
            //资源过滤,去重
            List<SysPlanResoRefInfo> sysPlanResoRefInfoLists = filterProgramEbr(programInfo, sysPlanResoRefInfoList);
            for (SysPlanResoRefInfo sysPlanResoRefInfo : sysPlanResoRefInfoLists) {

                try {
                    EbmDispatch ebmDispatch = new EbmDispatch();
                    SchemeEbr schemeEbr = new SchemeEbr();

                    String areaCode = null;
                    //根据资源类型，分别获取不同类型的播出资源的信息
                    if (Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(sysPlanResoRefInfo.getResoType())) {
                        //播出平台
                        EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysPlanResoRefInfo.getResoCode());
                        areaCode = ebrPlatformInfo.getAreaCode();
                        square += ebrPlatformInfo.getSquare() == null ? 0 : ebrPlatformInfo.getSquare();
                        population += ebrPlatformInfo.getPopulation() == null ? 0 : ebrPlatformInfo.getPopulation();
                        ebmDispatch.setPsEbrId(ebrPlatformInfo.getPsEbrId());
                    }

                    //播出系统
                    if (Constants.EBR_SUB_SOURCE_TYPE_BROADCAST.equals(sysPlanResoRefInfo.getResoType())) {
                        //播出系统编码
                        ebmDispatch.setBsEbrId(sysPlanResoRefInfo.getResoCode());
                        EbrBroadcastInfo ebrBroadcastInfo = resoFeginService.getEbrBroadcastInfoById(sysPlanResoRefInfo.getResoCode());
                        areaCode = ebrBroadcastInfo.getAreaCode();
                        square += ebrBroadcastInfo.getSquare() == null ? 0 : ebrBroadcastInfo.getSquare();
                        population += ebrBroadcastInfo.getPopulation() == null ? 0 : ebrBroadcastInfo.getPopulation();
                    }
                    //适配器
                    if (Constants.EBR_SUB_SOURCE_TYPE_ADAPTOR.equals(sysPlanResoRefInfo.getResoType())) {
                        ebmDispatch.setAsEbrId(sysPlanResoRefInfo.getResoCode());
                        EbrAdapterInfo adapterinfo = resoFeginService.getEbrAdapterByEbrId(sysPlanResoRefInfo.getResoCode());
                        areaCode = adapterinfo.getAreaCode();
                    }
                    //台站
                    if (Constants.EBR_SUB_SOURCE_TYPE_STATION.equals(sysPlanResoRefInfo.getResoType())) {
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

                    String lanCode = programInfo.getLanguageCode();
                    ebmDispatch.setEbmId(ebmInfo.getEbmId());
                    ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
                    //   ebmDispatch.setEbdId(ebd.getEBDID());
                    ebmDispatch.setFailCount(0);
                    ebmDispatch.setLanguageCode(lanCode);
                    ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
                    ebmDispatch.setPlayTime(ebmInfo.getStartTime());
                    ebmDispatchList.add(ebmDispatch);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }

            //方案自动审核 方案预评估效果
            if (FlowConstants.FLOW_TYPE_1.equals(flowType)) {
                Scheme scheme = new Scheme();
                scheme.setSchemeId(schemeInfo.getSchemeId());
                scheme.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
                scheme.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
                scheme.setAuditUser(Constants.DEFAULT_USER);
                scheme.setAuditTime(new Date());
                schemeService.update(scheme);
                saveEbmDispatchInfoInfo(ebmInfo.getEbmId(), programInfo.getProgramName(), schemeInfo.getFlowId());
                // 保存EBM调度资源
                ebmDispatchService.saveList(ebmDispatchList);
                log.info("【programDecompose】~节目预处理，保存消息分发记录ebmDispatch！，ebmId:{},size:{}", ebmInfo.getEbmId(), ebmDispatchList.size());
            }
            Scheme scheme = new Scheme();
            scheme.setSchemeId(schemeInfo.getSchemeId());
            scheme.setState(StateDictEnum.SCHEME_STATE_COMMIT.getKey());
            scheme.setAreaPercent(MathUtil.divide(square, schemeInfo.getTotalArea(), 2).doubleValue());
            scheme.setPopuPercent(MathUtil.divide(population, schemeInfo.getTotalPopu(), 2).doubleValue());
            scheme.setSchemeId(schemeInfo.getSchemeId());
            scheme.setPlanId(sysPlanResoRefInfoList.get(0).getPlanId());
            schemeService.update(scheme);

            // 保存方案关联资源
            schemeEbrService.saveList(schemeEbrList);

        } else {
            log.info("【programDecompose】~节目预处理，没有匹配的预案/预案没有关联的资源！，ebmId:{},schemeId:{}", ebmInfo.getEbmId(), schemeInfo.getSchemeId());
        }

    }

    private List<SysPlanResoRefInfo> filterProgramEbr(ProgramInfo programInfo, List<SysPlanResoRefInfo> list) {
        List<SysPlanResoRefInfo> list1 = list.stream().filter(p -> StringUtils.isNotBlank(p.getResoCode())).collect(Collectors.toList());
        //匹配到多个预案的情况，过滤掉重复资源
        Set<SysPlanResoRefInfo> set = new TreeSet<SysPlanResoRefInfo>((s1, s2) -> s1.getResoCode().compareTo(s2.getResoCode()));
        set.addAll(list1);
        List<SysPlanResoRefInfo> list3 = new ArrayList<SysPlanResoRefInfo>(set);
        //如果是带媒体文件的消息/日常节目&资源类型字幕插播，则过滤掉
        if (1 != programInfo.getContentType() || programInfo.getProgramType() != 1) {
            if (CollectionUtils.isNotEmpty(list3)) {
                Iterator<SysPlanResoRefInfo> iterator = list3.iterator();
                while (iterator.hasNext()) {
                    SysPlanResoRefInfo spr = iterator.next();
                    String channel = resoFeginService.getChannelByEbrId(spr.getResoCode());
                    if (CommonDictEnum.EBR_CHANNEL_2.getKey().equals(channel)) {
                        iterator.remove();
                    }
                }
            }
        }
        log.info("【programDecompose】 filterProgramEbr,list size:{}", list3.size());
        return list3;
    }

    private EbmDispatchInfoInfo saveEbmDispatchInfoInfo(String ebmId, String msgTitle, String flowId) {
        EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
        ebmDispatchInfo.setInfoId(UUIDGenerator.getUUID());
        ebmDispatchInfo.setEbmId(ebmId);
        ebmDispatchInfo.setInfoTitle(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD).concat(msgTitle).concat(Constants.EBM_DIAPATCH_INFO_TITLE_SUFFIX));
        //自动审核
        ebmDispatchInfo.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
        ebmDispatchInfo.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
        ebmDispatchInfo.setAuditTime(new Date());
        ebmDispatchInfo.setAuditUser(CommonDictEnum.EBM_DEFAULT_USER.getKey());
        ebmDispatchInfoService.save(ebmDispatchInfo);
        EbmDispatchInfoInfo ebmDispatchInfoInfo = new EbmDispatchInfoInfo();
        BeanUtils.copyProperties(ebmDispatchInfo, ebmDispatchInfoInfo);
        //  String flowState = FlowConstants.STATE_SCHEME_AUDIT;                           //流程为消息分发
        //更新流程 为消息审核
        //  updateDispatchFlowState(flowId,FlowConstants.STAGE_PROCESS,flowState);

        return ebmDispatchInfoInfo;
    }

    //生成方案
    private SchemeInfo createScheme(ProgramUnit programUnit, ProgramInfo programInfo, String flowId, EbmInfo ebmInfo) {

        List<String> areaCodeList = Optional.ofNullable(programInfo.getAreaList()).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ProgramAreaInfo::getAreaCode)
                .collect(Collectors.toList());
        BigDecimal square = new BigDecimal(0);
        BigDecimal population = new BigDecimal(0);
        // 根据节目关联区域，计算区域覆盖面积、人口
        List<RegionAreaInfo> regionAreaInfoList = systemFeginService.getRegionAreaByAreaCodes(areaCodeList);
        if (regionAreaInfoList != null) {
            for (RegionAreaInfo regionAreaInfo : regionAreaInfoList) {
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
        schemeAddRequest.setProgramId(programInfo.getProgramId());
        schemeAddRequest.setCreateTime(new Date());
        schemeService.save(schemeAddRequest);
        SchemeInfo schemeInfo = new SchemeInfo();
        BeanUtils.copyProperties(schemeAddRequest, schemeInfo);

        updateDispatchFlowState(flowId, FlowConstants.STAGE_RESPONSE, FlowConstants.STATE_SCHEME_AUDIT);
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

    private DispatchFlowInfo saveDispatchFlow(ProgramInfo programInfo) {
        DispatchFlowInfo dispatchFlowInfo = new DispatchFlowInfo();
        //   String ebmId = plateformId + DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD)+sequenceGenerate.createId(Constants.EBM_SEQUENCE_ID);
        DispatchFlow dispatchFlow = new DispatchFlow();
        dispatchFlow.setFlowId(UUIDGenerator.getUUID());
        dispatchFlow.setCreateTime(new Date());
        dispatchFlow.setRelatedProgramId(programInfo.getProgramId());
        dispatchFlow.setFlowStage(FlowConstants.STAGE_STARTING);
        dispatchFlow.setFlowState(FlowConstants.STATE_INFO);
        dispatchFlowService.save(dispatchFlow);
        BeanUtils.copyProperties(dispatchFlow, dispatchFlowInfo);
        return dispatchFlowInfo;
    }
}
