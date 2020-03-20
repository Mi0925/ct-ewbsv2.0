package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.domain.core.access.info.AccessInfo;
import cn.comtom.domain.core.access.request.AccessInfoAddRequest;
import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebd.request.EbdFilesAddBatchRequest;
import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.*;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.flow.request.DispatchFlowAddRequest;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.request.SchemeAddRequest;
import cn.comtom.domain.core.scheme.request.SchemeEbrAddBatchRequest;
import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;
import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.constant.SendFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.ebm.*;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.common.service.TTSComponent;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.main.fegin.service.ISystemFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.RegionUtil;
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
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.base.Joiner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nobody 应急消息服务处理类
 */
@Service
@Slf4j
public class EBMServiceImpl extends AbstractEMDService {


	@Autowired
	private ICoreFeginService coreFeginService;

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ISystemFeginService systemFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private MQMessageProducer mqMessageProducer;

	@Autowired
	private TTSComponent ttsComponent;

	@Autowired
	private FastFileStorageClient storageClient;

	private static final String SQUARE = "square";
	private static final String POPULATION = "population";
	private static final String MATCHED_PLAN = "matchedPlan";

	@Override
	public String serviceType() {
		return EBDType.EBM.name();
	}


	/**
	 * 实现具体的业务逻辑，保存到接入信息表中bc_access_info
	 * @param ebd
	 * @param resourceFiles
	 */
	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
		 log.info("【accessHandle.EBM】EBM消息接入处理开始，EBDID:{}", ebd.getEBDID());

		// 创建调度流程
		DispatchFlowInfo dispatchFlow = saveDispatchFlow(ebd.getEBDID());
		if(dispatchFlow == null){
			log.error("【accessHandle.EBM】创建调度流程失败！，EBDID:{}",ebd.getEBDID());
			return;
		}

		//2.保存数据到ebm表中
		EbmInfo ebmInfo = saveEbm(ebd,dispatchFlow,resourceFiles);
		if(ebmInfo == null){
			log.error("【accessHandle.EBM】保存数据到ebm表失败！，EBDID:{}",ebd.getEBDID());
			return;
		}
		//1、带辅助数据（媒体）文件的，需要上传到媒体文件中，并保存对应的文件信息表中。
		recordEbdFiles(ebd,resourceFiles);

		// 保存接入信息管理
		AccessInfo accessInfo = saveAccessInfo(ebmInfo,ebd.getSRC().getEBRID());
		if(accessInfo == null){
			log.error("【accessHandle.EBM】保存接入信息管理失败！，ebrId:{}",ebd.getSRC().getEBRID());
			return;
		}
		
		//校验区域
		String[] areaCodeArray = ebmInfo.getAreaCode().split(",");
		List<String> areaCodeList = Arrays.asList(areaCodeArray);
		
		List<RegionAreaInfo> regionAreaByAreaCodes = systemFeginService.getRegionAreaByAreaCodes(areaCodeList);
		EbrPlatformInfo epi = resoFeginService.getEbrPlatformById(commonService.getEbrPlatFormID());
		String currentAreaCode = epi.getAreaCode();
		if(isParentAreaCode(areaCodeList, currentAreaCode)) {
			//全国或者是上级区域
			EbmUpdateRequest ebmUpdateRequest = new EbmUpdateRequest();
			ebmUpdateRequest.setEbmId(ebmInfo.getEbmId());
			ebmUpdateRequest.setAreaCode(currentAreaCode);
			coreFeginService.updateEbm(ebmUpdateRequest);
			ebmInfo.setAreaCode(currentAreaCode);
		}else if(CollectionUtils.isEmpty(regionAreaByAreaCodes)) {
			//完全不匹配，状态反馈未处理
			ebmStateReport(ebd, ebmInfo.getEbmId(), StateDictEnum.EBM_BROADCAST_STATE_INIT);
			log.error("【accessHandle.EBM】接入的ebm播发区域完全不匹配，不进行后续处理！,EBDID:{}",ebd.getEBDID());
			return;
		}else if(areaCodeList.size() != regionAreaByAreaCodes.size()) {
			//不完全匹配，分开反馈，更新ebm播发区域为匹配上的区域
			List<String> areaCodeStrList = regionAreaByAreaCodes.stream().filter(Objects::nonNull).map(rabac -> {
	            return rabac.getAreaCode();
	        }).collect(Collectors.toList());
			ebmStateReport(ebd, ebmInfo.getEbmId(), StateDictEnum.EBM_BROADCAST_STATE_INIT);
			log.error("【accessHandle.EBM】接入的ebm播发区域不完全匹配，分开处理！,EBDID:{}",ebd.getEBDID());
			EbmUpdateRequest ebmUpdateRequest = new EbmUpdateRequest();
			ebmUpdateRequest.setEbmId(ebmInfo.getEbmId());
			ebmUpdateRequest.setAreaCode(Joiner.on(",").join(areaCodeStrList));
			coreFeginService.updateEbm(ebmUpdateRequest);
			ebmInfo.setAreaCode(Joiner.on(",").join(areaCodeStrList));
		}
		
		//过期消息
		if(ebmInfo.getAccessState()==3) {
			ebmStateReport(ebd, ebmInfo.getEbmId(), StateDictEnum.EBM_BROADCAST_STATE_INIT);
			log.error("【accessHandle.EBM】接入的ebm消息已经过期，不进行后续处理！,EBDID:{}",ebd.getEBDID());
			return;
		}

		//3.ebm消息自动审核
		autoAudit(ebmInfo);
		commonService.updateDispatchFlowState(dispatchFlow.getFlowId(),FlowConstants.STAGE_STARTING,FlowConstants.STATE_INFO_AUDIT);

		//4.ebm消息辅助数据保存
		ebmAuxiliarySave(ebd);

		// 更新调度流程状态  方案审核
		commonService.updateDispatchFlowState(dispatchFlow.getFlowId(),FlowConstants.STAGE_RESPONSE,FlowConstants.STATE_SCHEME_CREATE);


		String ebdMsgType = ebmInfo.getMsgType();
		if(TypeDictEnum.EBM_MSG_TYPE_CANCEL.getKey().equals(ebdMsgType)){
			log.info("【accessHandle.EBM】EBM消息-取消播发，EBDID:{}，EBMID:{}", ebd.getEBDID(),ebmInfo.getEbmId());
			//如果接入的信息是取消播发类型的
			String canceledEbmId = ebmInfo.getRelatedEbmId();    //待取消的EBM消息Id
			EbmInfo canceledEbmInfo = coreFeginService.getEbmById(canceledEbmId);
			if(canceledEbmInfo == null){
				return;
			}

			//不经过生成方案，预案匹配之类的流程，直接生成待分发的消息（ebmDispatch）
			List<EbmDispatchInfo> ebmDispatchInfoList = coreFeginService.getEbmDispatchByEbmId(ebmInfo.getRelatedEbmId());   //查询待取消的EBM信息的分发消息记录
			List<EbmDispatchInfo> ebmDispatchList = Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
					.filter(Objects::nonNull)
					.peek(ebmDispatchInfo -> {
						ebmDispatchInfo.setDispatchId(UUIDGenerator.getUUID());
						ebmDispatchInfo.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());    //分发状态为未调度
						ebmDispatchInfo.setEbmId(ebmInfo.getEbmId());             //关联取消播发的EBM消息
						ebmDispatchInfo.setFailCount(0);
					}).collect(Collectors.toList());
			if(!ebmDispatchList.isEmpty()){
				EbmDispatchAddBatchRequest ebmDispatchAddBatchRequest = new EbmDispatchAddBatchRequest();
				ebmDispatchAddBatchRequest.setEbmDispatchInfoList(ebmDispatchList);
				coreFeginService.saveEbmDispatchBatch(ebmDispatchAddBatchRequest);
			}

			//修改流程为消息分发
			commonService.updateDispatchFlowState(dispatchFlow.getFlowId(),FlowConstants.STAGE_PROCESS,FlowConstants.STATE_MSG_SEND);
			ebmStateReport(ebd, ebmInfo.getEbmId(), StateDictEnum.EBM_PLAY_STATE_CANCEL);
		}else {
			//5.如果是实际播放类型的EBM消息 ebm方案调度
			ebmDispatch(ebd,dispatchFlow.getFlowId(),accessInfo.getInfoId());
			ebmStateReport(ebd, ebmInfo.getEbmId(), StateDictEnum.EBM_BROADCAST_STATE_READY);
		}
		//生成并保存Ebm分发消息
		//saveEbmDispatchInfoInfo(ebmInfo,dispatchFlow.getFlowId());
		coreFeginService.callEbmNotice();
		log.info("【accessHandle.EBM】EBM消息接入处理结束，EBDID:{}，EBMID:{}", ebd.getEBDID(),ebmInfo.getEbmId());
	}
	
	private boolean isParentAreaCode(List<String> areaCodeList, String currentAreaCode) {
		try {
			String country = "0";
			String province = currentAreaCode.substring(0, 2);
			String city = currentAreaCode.substring(0, 4);
			for (String areaCode : areaCodeList) {
				if(areaCode.equals(country) || areaCode.equals(province) || areaCode.equals(city)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}


	private void ebmStateReport(EBD ebd, String ebmId, StateDictEnum ebmState) {
		//  播发状态反馈
		EbmStateResponseMessage message = new EbmStateResponseMessage();
		message.setEbmId(ebmId);
		message.setEbdId(ebd.getEBDID());
		message.setBroadcastState(ebmState.getKey());
		mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue,JSON.toJSONString(message));
	}

	private EbmDispatchInfoInfo saveEbmDispatchInfoInfo(String ebmId,String msgTitle,String flowId) {
		EbmDispatchInfoAddRequest request = new EbmDispatchInfoAddRequest();
		request.setEbmId(ebmId);
		request.setInfoTitle(commonService.getEbmDispatchInfoTitle(msgTitle));
		String autoAudit = commonService.getSysParamValue(Constants.EBM_DISPATCH_INFO_AUTO_AUDIT);
		String flowState = FlowConstants.STATE_MSG_AUDIT;                           //流程为消息审核
		if(StringUtils.isNotBlank(autoAudit) && CommonDictEnum.EBM_DISPATCH_INFO_AUTO_AUDIT_TRUE.getKey().equals(autoAudit)){
			//自动审核
			request.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
			request.setAuditTime(new Date());
			request.setAuditUser(CommonDictEnum.EBM_DEFAULT_USER.getKey());
			flowState = FlowConstants.STATE_MSG_SEND;                             //流程为消息分发
		}
		EbmDispatchInfoInfo ebmDispatchInfoInfo = coreFeginService.saveEbmDispatchInfoInfo(request);
		//更新流程 为消息审核
		commonService.updateDispatchFlowState(flowId,FlowConstants.STAGE_PROCESS,flowState);

		return ebmDispatchInfoInfo;
	}

	private void ebmDispatch(EBD ebd, String flowId, String infoId) {

		// Step.1 生成方案
		SchemeInfo schemeInfo = generateScheme(ebd,flowId,infoId);
		if (schemeInfo == null) {
			return;
		}
		// 更新调度流程状态  方案审核
		commonService.updateDispatchFlowState(flowId,FlowConstants.STAGE_RESPONSE,FlowConstants.STATE_SCHEME_AUDIT);

		// Step.2 资源发现优化和生成Ebm调度资源记录
		matchEbr(ebd,flowId, schemeInfo.getSchemeId());

		// Step.3 更新消息 关联调度方案Id
		String ebmId = ebd.getEBM().getEBMID();
		if (StringUtils.isNotBlank(ebmId)) {
			EbmUpdateRequest request = new EbmUpdateRequest();
			request.setEbmId(ebmId);
			request.setSchemeId(schemeInfo.getSchemeId());
			request.setEbmState(StateDictEnum.EBM_STATE_CREATE.getKey());
			request.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_READY.getKey()));
			coreFeginService.updateEbm(request);
		}



	}

	private void matchEbr(EBD ebd, String flowId, String schemeId) {
		// Step.1 资源发现、优化

		// 方案关联资源
		List<SchemeEbrInfo> schemeEbrList = new ArrayList<SchemeEbrInfo>();
		// EBM调度资源
		List<EbmDispatchInfo> ebmDispatchList = new ArrayList<EbmDispatchInfo>();

		Double square = 0.0;
		Double population = 0.0;
		Map<String,Object> map = new HashMap<>();
		log.info("【accessHandle.EBM】预案匹配开始！ebdId:{}，schemeId：{}",ebd.getEBDID(),schemeId);
		//匹配预案
		matchEbrByPlan(ebd,schemeId,schemeEbrList,ebmDispatchList,map);

		square = (Double)map.get(SQUARE);
		population = (Double)map.get(POPULATION);
		SysPlanMatchInfo sysPlanMatchInfo= (SysPlanMatchInfo)map.get(MATCHED_PLAN);   //匹配的预案信息
		String planId = null;
		String flowType = null;
		if(sysPlanMatchInfo != null){
			planId = sysPlanMatchInfo.getPlanId();
			flowType = sysPlanMatchInfo.getFlowType();

		}else {
			log.warn("【accessHandle.EBM】预案匹配失败！ebdId:{}，schemeId：{}",ebd.getEBDID(),schemeId);
		}
		// 如果无资源可用
		if (schemeEbrList.isEmpty()) {
			log.error("【accessHandle.EBM】没有查询到方案的关联资源信息！ebdId:{}，schemeId：{}",ebd.getEBDID(),schemeId);
			return ;
		}

		//方案审核。 根据流程类型判断方案是否自动审核
		if(StringUtils.isNotBlank(flowType)){
			log.info("【accessHandle.EBM】方案自动审核，生成消息分发记录EbmDispatch！flowType:{}，schemeId：{}",flowType,schemeId);
			if(flowType.equals(FlowConstants.FLOW_TYPE_1)){
				//流程类型为1时，自动审核
				SchemeUpdateRequest schemeUpdateRequest = new SchemeUpdateRequest();
				schemeUpdateRequest.setSchemeId(schemeId);
				schemeUpdateRequest.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
				schemeUpdateRequest.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
				schemeUpdateRequest.setAuditUser(Constants.DEFAULT_USER);
				schemeUpdateRequest.setAuditTime(new Date());
				schemeUpdateRequest.setState(StateDictEnum.SCHEME_STATE_COMMIT.getKey());
				coreFeginService.updateSchemeInfo(schemeUpdateRequest);

				// 保存EBM调度资源
				EbmDispatchAddBatchRequest ebmDispatchAddBatchRequest = new EbmDispatchAddBatchRequest();
				ebmDispatchAddBatchRequest.setEbmDispatchInfoList(ebmDispatchList);
				coreFeginService.saveEbmDispatchBatch(ebmDispatchAddBatchRequest);

				//流程状态更新为消息生成
				//commonService.updateDispatchFlowState(flowId,FlowConstants.STAGE_PROCESS,FlowConstants.STATE_MSG_CREATE);
				//生成并保存Ebm分发消息
				saveEbmDispatchInfoInfo(ebd.getEBM().getEBMID(),ebd.getEBM().getMsgContent().getMsgTitle(),flowId);
			}
		}


		// 更新方案预评估效果,保存匹配成功的预案ID
		SchemeInfo schemeInfo = coreFeginService.getSchemeInfoById(schemeId);
		if(schemeInfo == null){
			log.error("【accessHandle.EBM】查询调度方案信息为空！schemeId:{}",schemeId);
			return;
		}
		SchemeUpdateRequest request = new SchemeUpdateRequest();
		request.setAreaPercent(MathUtil.divide(square, schemeInfo.getTotalArea(), 2).doubleValue());
		request.setPopuPercent(MathUtil.divide(population, schemeInfo.getTotalPopu(), 2).doubleValue());
		request.setSchemeId(schemeInfo.getSchemeId());
		request.setPlanId(planId);
		coreFeginService.updateSchemeInfo(request);

		// 保存方案关联资源
		SchemeEbrAddBatchRequest schemeEbrAddBatchRequest = new SchemeEbrAddBatchRequest();
		schemeEbrAddBatchRequest.setSchemeEbrInfoList(schemeEbrList);
		coreFeginService.saveSchemeEbrBatch(schemeEbrAddBatchRequest);

		// 保存EBM调度资源  这部分逻辑转移到方案审核通过后执行
	/*	EbmDispatchAddBatchRequest ebmDispatchAddBatchRequest = new EbmDispatchAddBatchRequest();
		ebmDispatchAddBatchRequest.setEbmDispatchInfoList(ebmDispatchList);
		coreFeginService.saveEbmDispatchBatch(ebmDispatchAddBatchRequest);*/


	}

	private void matchEbrByPlan(EBD ebd,  String schemeId, List<SchemeEbrInfo> schemeEbrList,
								List<EbmDispatchInfo> ebmDispatchList,Map<String,Object> map ) {
		Double square = 0.0;
		Double population = 0.0;
		//事件级别
		String eventLevel = ebd.getEBM().getMsgBasicInfo().getSeverity().toString();
		if("0".equals(eventLevel)) { //对接测试-修改
			eventLevel = "4";
		}
		//事件类型
		String eventType = ebd.getEBM().getMsgBasicInfo().getEventType();
		//消息下发区域
		String areaCodes = ebd.getEBM().getMsgContent().getAreaCode();
		//消息来源
		String srcEbrId = ebd.getSRC().getEBRID();
		//播发时间
		Date startTime = DateUtil.stringToDate(ebd.getEBM().getMsgBasicInfo().getStartTime());
		//根据目标区域和事件级别查询出匹配的调度预案
		List<SysPlanMatchInfo> sysPlanMatchInfoList =systemFeginService.getMatchPlan(eventLevel,eventType, srcEbrId, areaCodes);
		if(sysPlanMatchInfoList != null && !sysPlanMatchInfoList.isEmpty()){
			log.info("【accessHandle.EBM】预案匹配成功！ebdId:{}，schemeId：{}",ebd.getEBDID(),schemeId);
			map.put(MATCHED_PLAN,sysPlanMatchInfoList.get(0));
		}
		//查询出调度预案关联的播出资源ID
		List<SysPlanResoRefInfo> sysPlanResoRefInfoList = new ArrayList<>();
		Optional.ofNullable(sysPlanMatchInfoList).orElse(Collections.emptyList()).forEach(sysPlanMatchInfo -> {
			List<SysPlanResoRefInfo> sysPlanResoRefInfos = systemFeginService.getPlanResRefByPlanId(sysPlanMatchInfo.getPlanId());
			if(sysPlanResoRefInfos!= null && !sysPlanResoRefInfos .isEmpty()){
                sysPlanResoRefInfoList.addAll(sysPlanResoRefInfos);
            }
		});

		if(!sysPlanResoRefInfoList.isEmpty()){
			for(SysPlanResoRefInfo sysPlanResoRefInfo : sysPlanResoRefInfoList) {

				EbmDispatchInfo ebmDispatch = new EbmDispatchInfo();
				SchemeEbrInfo schemeEbr = new SchemeEbrInfo();

				String areaCode = null;
				//根据资源类型，分别获取不同类型的播出资源的信息
				if(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(sysPlanResoRefInfo.getResoType())){
					//播出平台
					EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysPlanResoRefInfo.getResoCode());
					areaCode = ebrPlatformInfo.getAreaCode();
					square += ebrPlatformInfo.getSquare();
					population += ebrPlatformInfo.getPopulation();
					ebmDispatch.setPsEbrId(ebrPlatformInfo.getPsEbrId());

				}

				//播出系统
				if(Constants.EBR_SUB_SOURCE_TYPE_BROADCAST.equals(sysPlanResoRefInfo.getResoType())){
					//播出系统编码
					ebmDispatch.setBsEbrId(sysPlanResoRefInfo.getResoCode());

					EbrBroadcastInfo ebrBroadcastInfo = resoFeginService.getEbrBroadcastInfoById(sysPlanResoRefInfo.getResoCode());
					areaCode = ebrBroadcastInfo.getAreaCode();
					square += ebrBroadcastInfo.getSquare();
					population += ebrBroadcastInfo.getPopulation();
				}

				//台站
				if(Constants.EBR_SUB_SOURCE_TYPE_STATION.equals(sysPlanResoRefInfo.getResoType())){
					//台站系统编码
					ebmDispatch.setStEbrId(sysPlanResoRefInfo.getResoCode());
					EbrStationInfo ebrStationInfo = resoFeginService.getEbrStationInfoById(sysPlanResoRefInfo.getResoCode());
					areaCode = ebrStationInfo.getAreaCode();
					/*square += ebrStationInfo.getSquare();
					population += ebrStationInfo.getPopulation();*/
				}

				//适配器
				if(Constants.EBR_SUB_SOURCE_TYPE_ADAPTOR.equals(sysPlanResoRefInfo.getResoType())){
					//适配器系统编码
					ebmDispatch.setAsEbrId(sysPlanResoRefInfo.getResoCode());
					EbrAdapterInfo ebrAdapterInfo = resoFeginService.getEbrAdapterInfoById(sysPlanResoRefInfo.getResoCode());
					areaCode = ebrAdapterInfo.getAreaCode();
					/*square += ebrAdapterInfo.getSquare();
					population += ebrAdapterInfo.getPopulation();*/
				}

				schemeEbr.setSchemeId(schemeId);
				schemeEbr.setEbrId(sysPlanResoRefInfo.getResoCode());
				schemeEbr.setEbrType(sysPlanResoRefInfo.getResoType());
				schemeEbr.setEbrArea(areaCode);
				schemeEbrList.add(schemeEbr);

				String lanCode = Optional.of(ebd)
						.filter(ebd1 -> ebd1.getEBM() != null)
						.filter(ebd1 -> ebd1.getEBM().getMsgContent() != null)
						.map(ebd1 -> ebd1.getEBM().getMsgContent().getLanguageCode())
						.orElseGet(String::new);
				ebmDispatch.setEbmId(ebd.getEBM().getEBMID());
				ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
				ebmDispatch.setEbdId(ebd.getEBDID());
				ebmDispatch.setFailCount(0);
				ebmDispatch.setLanguageCode(lanCode);
				ebmDispatch.setPlayTime(startTime);
				ebmDispatchList.add(ebmDispatch);

			}
		}else{
			log.error("【accessHandle.EBM】没有查询到预案的关联资源信息！ebdId:{}，schemeId：{}",ebd.getEBDID(),schemeId);
		}

		map.put(SQUARE,square);
		map.put(POPULATION,population);

	}

	/**
	 * 匹配ebm中指定的播出资源
	 * @param dispatchList
	 */
	private void matchAssignedEbr(EBD ebd,List<Dispatch> dispatchList,String schemeId,String localPsEbrId,
								  List<SchemeEbrInfo> schemeEbrList,List<EbmDispatchInfo> ebmDispatchList,Map<String,Double> map) {
		Double square = 0.0;
		Double population = 0.0;
		for(Dispatch dispatch : dispatchList) {
			//指定的应急广播平台
			EBRPS ebrps = dispatch.getEBRPS();
			//指定的消息接收设备
			EBRAS ebras = dispatch.getEBRAS();
			//指定的调用播出系统信息
			EBRBS ebrbs = dispatch.getEBRBS();

			//播出平台
			if(ebrps != null && StringUtils.isNotBlank(ebrps.getEBRID())){
				EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(ebrps.getEBRID());
				if(ebrPlatformInfo != null){
					SchemeEbrInfo schemeEbr = new SchemeEbrInfo();
					schemeEbr.setSchemeId(schemeId);
					schemeEbr.setEbrId(ebrps.getEBRID());
					schemeEbr.setEbrType(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM);
					schemeEbr.setEbrArea(ebrPlatformInfo.getAreaCode());
					schemeEbrList.add(schemeEbr);

					square += ebrPlatformInfo.getSquare();
					population += ebrPlatformInfo.getPopulation();

					EbmDispatchInfo ebmDispatch = new EbmDispatchInfo();
					ebmDispatch.setPsEbrId(ebrPlatformInfo.getPsEbrId());
					ebmDispatch.setEbmId(ebd.getEBM().getEBMID());
					ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
					ebmDispatch.setEbdId(ebd.getEBDID());
					ebmDispatch.setFailCount(0);
					ebmDispatchList.add(ebmDispatch);
				}else{
					log.info("查询播出平台不存在！");
				}

			}

			if(ebras != null && StringUtils.isNotBlank(ebras.getEBRID())){
				//TODO 指定的消息接收设备


			}

			//播出系统
			if(ebrbs != null && StringUtils.isNotBlank(ebrbs.getEBRID())){
				EbrBroadcastInfo ebrBroadcastInfo = resoFeginService.getEbrBroadcastInfoById(ebrbs.getEBRID());
				if(ebrBroadcastInfo != null){
					SchemeEbrInfo schemeEbr = new SchemeEbrInfo();
					schemeEbr.setSchemeId(schemeId);
					schemeEbr.setEbrId(ebrbs.getEBRID());
					schemeEbr.setEbrType(Constants.EBR_SUB_SOURCE_TYPE_BROADCAST);
					schemeEbr.setEbrArea(ebrBroadcastInfo.getAreaCode());
					schemeEbrList.add(schemeEbr);

					square += ebrBroadcastInfo.getSquare();
					population += ebrBroadcastInfo.getPopulation();

					EbmDispatchInfo ebmDispatch = new EbmDispatchInfo();
					//ebmDispatch.setPsEbrId(localPsEbrId);
					ebmDispatch.setBsEbrId(ebrBroadcastInfo.getBsEbrId());
					ebmDispatch.setEbmId(ebd.getEBM().getEBMID());
					ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
					ebmDispatch.setEbdId(ebd.getEBDID());
					ebmDispatch.setFailCount(0);
					ebmDispatchList.add(ebmDispatch);
				}else{
					log.info("查询播出系统不存在！");
				}
			}

		}

		map.put("square",square);
		map.put("population",population);
	}




	/**
	 * 生成方案
	 * @param ebd
	 * @param flowId
	 * @param infoId
	 * @return
	 */
	private SchemeInfo generateScheme(EBD ebd, String flowId, String infoId) {
		String ebdId = ebd.getEBDID();
		String ebmId = ebd.getEBM().getEBMID();

		// 根据节目Id获取节目区域信息
		BigDecimal square = new BigDecimal(0);
		BigDecimal population = new BigDecimal(0);

		EBM ebm = ebd.getEBM();
		if(ebm.getMsgContent()!=null && ebm.getMsgContent().getAreaCode()!=null) {
			String[] areaCodes = RegionUtil.areaLong2Short(ebm.getMsgContent().getAreaCode())
					.split(SymbolConstants.GENERAL_SEPARATOR);
			List<String> areaCodeList = Arrays.asList(areaCodes);

			// 根据节目关联区域，计算区域覆盖面积、人口
			List<RegionAreaInfo> regionAreaInfoList = systemFeginService.getRegionAreaByAreaCodes(areaCodeList);
			if(regionAreaInfoList != null){
				for(RegionAreaInfo regionAreaInfo : regionAreaInfoList){
					square = square.add(new BigDecimal(regionAreaInfo.getAreaSquare()));
					population = population.add(new BigDecimal(regionAreaInfo.getAreaPopulation()));
				}
			}
		}

		String schemeName = FlowConstants.SCHEME_NAME_PREFIX + DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYYYMMDDHHMMSS);

		SchemeAddRequest schemeAddRequest = new SchemeAddRequest();
		schemeAddRequest.setSchemeTitle(schemeName);
		schemeAddRequest.setFlowId(flowId);
		schemeAddRequest.setEbdId(ebdId);
		schemeAddRequest.setEbmId(ebmId);
		schemeAddRequest.setTotalArea(square.doubleValue());
		schemeAddRequest.setTotalPopu(population.doubleValue());
		schemeAddRequest.setInfoId(infoId);
		schemeAddRequest.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
		schemeAddRequest.setState(StateDictEnum.SCHEME_STATE_CREATE.getKey());

		return coreFeginService.saveScheme(schemeAddRequest);
	}

	private AccessInfo saveAccessInfo(EbmInfo ebmInfo,String nodId) {
		if(ebmInfo == null) return null;
		AccessInfoAddRequest request = new AccessInfoAddRequest();
		BeanUtils.copyProperties(ebmInfo,request);
		request.setRelatedEbmId(ebmInfo.getEbmId());
		request.setInfoTitle(ebmInfo.getMsgTitle());
		request.setInfoContent(ebmInfo.getMsgDesc());
		request.setInfoType(ebmInfo.getMsgType());
		request.setNodeId(nodId);
		request.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
		request.setAuditTime(new Date());
		request.setAuditor(CommonDictEnum.EBM_DEFAULT_USER.getKey());
		request.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
		return coreFeginService.saveAccessInfo(request);
	}

	private DispatchFlowInfo saveDispatchFlow(String ebdId) {
		DispatchFlowAddRequest request = new DispatchFlowAddRequest();
		request.setRelatedEbdId(ebdId);
		request.setFlowStage(FlowConstants.STAGE_STARTING);
		request.setFlowState(FlowConstants.STATE_INFO);
		return coreFeginService.saveDispatchFlow(request);
	}

	private void ebmAuxiliarySave(EBD ebd) {
		EBM ebm = ebd.getEBM();
		MsgContent msgContent = ebm.getMsgContent();
		String ebmId = ebm.getEBMID();
		List<EbmAuxiliaryInfo> auxiliaryInfoList = new ArrayList<EbmAuxiliaryInfo>();
		// 可选 辅助数据
		List<Auxiliary> auxiliarys = msgContent.getAuxiliaryList();
		EbmAuxiliaryAddBatchRequest request = new EbmAuxiliaryAddBatchRequest();
		request.setAuxiliaryInfoList(auxiliaryInfoList);
		if (auxiliarys != null && !auxiliarys.isEmpty()) {
			for (Auxiliary auxiliary : auxiliarys) {
				// 必选 辅助数据类型
				Integer auxiliaryType = auxiliary.getAuxiliaryType();
				// 必选 辅助数据描述 文件地址和名称
				String auxiliaryDesc = auxiliary.getAuxiliaryDesc();
				// 可选 签名
				String auxiliaryDigest = auxiliary.getDigest();
				// 可选 大小
				Integer auxiliarySize = auxiliary.getSize();

				EbmAuxiliaryInfo ebmAuxiliary = new EbmAuxiliaryInfo();
				ebmAuxiliary.setAuxiliaryDesc(auxiliaryDesc);
				ebmAuxiliary.setAuxiliaryDigest(auxiliaryDigest);
				ebmAuxiliary.setAuxiliarySize(auxiliarySize);
				ebmAuxiliary.setAuxiliaryType(auxiliaryType == null ? null : String.valueOf(auxiliaryType));
				ebmAuxiliary.setEbmId(ebmId);
				auxiliaryInfoList.add(ebmAuxiliary);

			}
			request.setAuxiliaryInfoList(auxiliaryInfoList);
			coreFeginService.saveEbmAuxiliaryBatch(request);
		}

	}

	private void autoAudit(EbmInfo ebmInfo) {
		if(ebmInfo == null){
			return;
		}
		String autoAudit = commonService.getSysParamValue(Constants.EBM_AUDIT_PARAM_KEY);
		if( CommonDictEnum.EBM_AUTO_AUDIT_TRUE.getKey().equals(autoAudit)){
			//自动审核
			EbmAuditRequest request = new EbmAuditRequest();
			request.setEbmId(ebmInfo.getEbmId());
			request.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
			request.setAuditOpinion(StateDictEnum.AUDIT_STATUS_PASS.getDesc());
			request.setAuditTime(new Date());
			request.setAuditUser(Constants.DEFAULT_USER);
			coreFeginService.auditEbm(request);
		}
	}

	private EbmInfo saveEbm(EBD ebd,DispatchFlowInfo dispatchFlow,List<File> resourceFiles) {
		if(ebd == null || dispatchFlow == null){
			return  null;
		}
		EBM ebm = ebd.getEBM();
		// 必选
		String ebmVersion = ebm.getEBMVersion();
		// 必选
		String ebmId = ebm.getEBMID();

		// 可选
		RelatedInfo relatedInfo = ebm.getRelatedInfo();
		String relatedEbIId = null;
		String relatedEbmId = null;
		if (relatedInfo != null) {
			// 可选
			relatedEbIId = relatedInfo.getEBIID();
			// 可选
			relatedEbmId = relatedInfo.getEBMID();
		}
		// 必选
		MsgBasicInfo msgBasicInfo = ebm.getMsgBasicInfo();

		EbmAddRequest ebmEntity = new EbmAddRequest();

		Integer msgType = null;
		String sendName = null;
		String senderCode = null;
		Date sendTime = null;
		String eventType = null;
		Date startTime = null;
		Date endTime = null;
		Integer severity = null;

		msgType = msgBasicInfo.getMsgType();
		sendName = msgBasicInfo.getSenderName();
		senderCode = msgBasicInfo.getSenderCode();
		String sendTimeStr = msgBasicInfo.getSendTime();
		sendTime = DateTimeUtil.stringToDate(sendTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
		eventType = msgBasicInfo.getEventType();
		severity = msgBasicInfo.getSeverity();
		String startTimeStr = msgBasicInfo.getStartTime();
		if (startTimeStr != null) {
			startTime = DateTimeUtil.stringToDate(startTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
		}

		String endTimeStr = msgBasicInfo.getEndTime();
		if (endTimeStr != null) {
			endTime = DateTimeUtil.stringToDate(endTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
		}

		ebmEntity.setEbmVersion(ebmVersion);
		ebmEntity.setEbmId(ebmId);
		ebmEntity.setRelatedEbIId(relatedEbIId);
		ebmEntity.setRelatedEbmId(relatedEbmId);
		//ebmEntity.setMsgType(msgType == null ? null:String.valueOf(msgType));
		if(2 == msgType) {
			ebmEntity.setMsgType(msgType.toString());
		}else {
			ebmEntity.setMsgType("1");//对接测试-修改
		}
		ebmEntity.setSendName(sendName);
		ebmEntity.setSenderCode(senderCode);
		ebmEntity.setSendTime(sendTime);
		ebmEntity.setEventType(eventType);
		ebmEntity.setSeverity(severity == null ? "4":String.valueOf(severity));
		ebmEntity.setStartTime(startTime);
		ebmEntity.setEndTime(endTime);
		ebmEntity.setBroadcastState(Integer.valueOf(StateDictEnum.EBM_BROADCAST_STATE_INIT.getKey()));

		// 可选
		MsgContent msgContent = ebm.getMsgContent();
		if (msgContent != null) {
			String areaCode = msgContent.getAreaCode();
			String msgLanguageCode = msgContent.getLanguageCode();
			String msgDesc = msgContent.getMsgDesc();
			String msgTitle = msgContent.getMsgTitle();
			// 可选
			Integer programNum = msgContent.getProgramNum();
			ebmEntity.setMsgLanguageCode(msgLanguageCode);
			ebmEntity.setMsgTitle(msgTitle);
			ebmEntity.setMsgDesc(msgDesc);
			ebmEntity.setAreaCode(areaCode);
			ebmEntity.setProgramNum(programNum == null ? null : String.valueOf(programNum));
		}

		ebmEntity.setSendFlag(String.valueOf(SendFlag.receive.getValue()));
		ebmEntity.setFlowId(dispatchFlow.getFlowId());
		ebmEntity.setCreateTime(new Date());
		ebmEntity.setTimeOut(CommonDictEnum.EBM_TIMEOUT_FALSE.getKey());
		//接入状态
		Integer accessState = 1;
		if(endTime.getTime()<new Date().getTime()) {
			accessState = 3;
		}else if(sendTime.getTime()>new Date().getTime()) {
			accessState = 2;
		}
		ebmEntity.setAccessState(accessState);
		//文转换语音，媒体文件路径
		String apath = getEbmAudioPath(ebm,resourceFiles);
		ebmEntity.setAudioPath(apath);
		return coreFeginService.saveEbm(ebmEntity);
	}

	private String getEbmAudioPath(EBM ebm, List<File> resourceFiles) {
		String audioPath = null;
		try {
			if(!CollectionUtils.isEmpty(resourceFiles)) {
				String dirPath = systemFeginService.getByKey(Constants.TEMP_FILE_PATH) + "audio/";
				StringBuilder sb = new StringBuilder();
				for (File file : resourceFiles) {
					//需要保存的ebd文件关联信息列表
					String suffix = file.getName().substring(file.getName().lastIndexOf("."));
					String fileName = UUIDGenerator.getUUID() + suffix;
					FileUtils.copyFile(file, new File(dirPath+fileName));
					//asyncTask.doTask(dirPath, fileName, file);
					sb.append("/audio/").append(fileName).append(",");
				}
				audioPath = sb.deleteCharAt(sb.length() - 1).toString();;
			}else if(ebm.getMsgContent()!=null && StringUtils.isNotBlank(ebm.getMsgContent().getMsgDesc())){
				//文转语
				audioPath = ttsComponent.getTextToAudioFilePath(ebm.getMsgContent().getMsgDesc());
			}
		} catch (Exception e) {
			log.error("上传语音文件 || EBM文转语异常，error:{}",e.getMessage());
			e.printStackTrace();
		}
		return audioPath;
	}

	/**
	 * 上传媒体文件并保存媒体文件信息
	 * @param ebd
	 * @param resourceFiles
	 * @throws FileNotFoundException
	 */
	protected void recordEbdFiles(EBD ebd, List<File> resourceFiles) {
		EbdFilesAddBatchRequest request = new EbdFilesAddBatchRequest();
		List<EbdFilesInfo> list = new ArrayList<>();
		if (resourceFiles != null && !resourceFiles.isEmpty()) {
			for (File file : resourceFiles) {
				//保存文件信息并上传
				FileInfo fileInfo = null;
				try {
					fileInfo = uploadFile(file);
				} catch (FileNotFoundException e) {
					log.error("上传ebm媒体文件出错：{} \n ERROR:{}", file.getName(), e);
				}
				if(fileInfo != null){
					//需要保存的ebd文件关联信息列表
					EbdFilesInfo ebdFile = new EbdFilesInfo();
					ebdFile.setEbdId(ebd.getEBDID());
					ebdFile.setFileId(fileInfo.getId());
					list.add(ebdFile);
				}
			}
			request.setEbdFilesInfoList(list);
			//保存EBD文件关联信息
			coreFeginService.saveEbdFilesBatch(request);
		}
	}

	/**
	 * 保存并上传媒体文件
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	private FileInfo uploadFile(File file) throws FileNotFoundException {
		//获取文件的md5
		String md5 = DigestUtils.md5Hex(FileUtil.file2bytes(file));
		//通过MD5获取文件信息判断媒体文件是否上传过
		FileInfo fileInfo = resoFeginService.getFileByMd5(md5);
		if (fileInfo == null) {
			//文件未上传过，上传文件并记录文件信息到文件表
			FileAddRequest info = new FileAddRequest();
			String fileName = file.getName();
			//获取文件扩展名
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			info.setUploadedName(fileName);
			info.setByteSize(file.length());
			info.setCreateUser(CommonDictEnum.EBM_DEFAULT_USER.getKey());
			info.setFileExt(suffix);
			if(suffix.equalsIgnoreCase(TypeDictEnum.FILE_TYPE_TEXT.getDesc())){
				info.setFileType(TypeDictEnum.FILE_TYPE_TEXT.getKey());
			}else if(suffix.equalsIgnoreCase(TypeDictEnum.FILE_TYPE_MP3.getDesc())){
				info.setFileType(TypeDictEnum.FILE_TYPE_MP3.getKey());
			}
			//获取ebm文件存储文件夹
			info.setLibId(commonService.getEbmFileLib());
			info.setMd5Code(md5);
			info.setOriginName(fileName);
			//获取存储文件夹
			FileLibraryInfo libraryInfo = resoFeginService.getFileLibraryInfoByLibId(commonService.getEbmFileLib());
			String url = libraryInfo.getLibURI() + fileName;
			info.setFileUrl(url);
			//计算文件播放时长
			Long duration = cn.comtom.tools.utils.FileUtils.getDuration(file);
			info.setSecondLength(duration.intValue());
			//上传文件
			log.debug("开始上传ebm媒体文件到fastdfs：{}", file.getName());
			StorePath storePath = storageClient.uploadFile(new FileInputStream(file), file.length(), suffix, null);
			String fullPath = storePath.getFullPath();
			info.setFilePath(fullPath);
			//保存文件信息到数据库
			fileInfo = resoFeginService.saveFile(info);
		}
		//如果文件上传过返回上传过的文件信息
		return fileInfo;
	}

}
