package cn.comtom.linkage.main.access.service.impl.omd.impl;

import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordUpdateRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordWhereRequest;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.linkage.commons.BroadcastStateEnum;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMBrdLog;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.model.ebd.ebm.EBMBrdItem;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import cn.comtom.linkage.utils.RegionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Create By wujiang on 2018/12/04
 * 消息播发记录请求数据处理
 */
@Service
@Slf4j
public class OMDBrdLogService extends AbstractOMDInfoService{

	@Autowired
	private ICommonService commonService;

	@Autowired
	private ICoreFeginService coreFeginService;

	@Autowired
	private SequenceGenerate sequenceGenerate;
	
	@Override
	public String OMDType() {
		return EBDType.EBMBrdLog.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		EbmBrdRecordWhereRequest whereRequest=new EbmBrdRecordWhereRequest();
		//whereRequest.setSyncFlag(SyncFlag.nosync.getValue());//接口只支持增量模式
		if(params!=null){
			log.info("=======OMDBrdLogService=======params=[{}]",JSON.toJSONString(params));
			String rptStartTimeStr=params.getRptStartTime();
			String rptEndTimeStr=params.getRptEndTime();
			String rptType = params.getRptType();
			if(StringUtils.isNotEmpty(rptEndTimeStr)){
				Date rptStartTime=DateTimeUtil.stringToDate(rptStartTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
				whereRequest.setRptStartTime(rptStartTime);
			}
			if(StringUtils.isNotEmpty(rptEndTimeStr)){
				Date rptEndTime=DateTimeUtil.stringToDate(rptEndTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
				whereRequest.setRptEndTime(rptEndTime);
			}
			if(StringUtils.isNotEmpty(rptType)&&(ODMRptType.Incremental.name().equals(rptType))){
				whereRequest.setSyncFlag(SyncFlag.nosync.getValue());
			}
		}
		
		EBMBrdLog ebmBrdLog = new EBMBrdLog();
		cn.comtom.linkage.main.access.model.ebd.details.other.Params par = new cn.comtom.linkage.main.access.model.ebd.details.other.Params();
		par.setRptStartTime(params.getRptStartTime());
		par.setRptEndTime(params.getRptEndTime());
		ebmBrdLog.setParams(par);

		List<EBMBrdItem> ebmBrdList = new ArrayList<EBMBrdItem>();
		//根据条件查询需要同步的播发记录信息
		List<EbmBrdRecordInfo> ebmBrdRecordInfoList=coreFeginService.findEbmBrdRecordListByWhere(whereRequest);
		log.info("=======需上报信息的播发记录列表=====ebmBrdRecordInfoList size=[{}]",ebmBrdRecordInfoList.size());

		if(!CollectionUtils.isEmpty(ebmBrdRecordInfoList)){
			for (EbmBrdRecordInfo ebmBrdRecord : ebmBrdRecordInfoList) {
				EBMBrdItem brdItem=convert(ebmBrdRecord,whereRequest.getRptStartTime(),whereRequest.getRptEndTime());
				ebmBrdList.add(brdItem);
			}
		}
		ebmBrdLog.setDataList(ebmBrdList);
		String ebrId=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildBrdLog(ebrId, srcURL, ebdIndex, ebmBrdLog,relatedEbdId,destEbrId);
		Optional.ofNullable(ebmBrdRecordInfoList).orElse(Collections.emptyList()).stream()
				.filter(Objects::nonNull)
				.forEach(ebmBrdRecordInfo -> {
					ebmBrdRecordInfo.setSyncFlag(SyncFlag.sync.getValue());
					//更新同步标识为已同步
					EbmBrdRecordUpdateRequest updateRequest=new EbmBrdRecordUpdateRequest();
					BeanUtils.copyProperties(ebmBrdRecordInfo,updateRequest);
					updateRequest.setUpdateTime(new Date());
					coreFeginService.updateEbmBrdRecord(updateRequest);
				});
		return ebdResponse;
	}

	
	private EBMBrdItem convert(EbmBrdRecordInfo ebmBrdRecord, Date rptStartDate, Date rptEndDate){
		EBMBrdItem brdItm=new EBMBrdItem();
		EbmInfo ebm = coreFeginService.getEbmById(ebmBrdRecord.getEbmId());

		EBM ebmIdVO = convertEBM(ebmBrdRecord);
		//ebm对应的播发记录
		List<EbmBrdRecordInfo> ebmBrdRecordList=coreFeginService.getEbmBrdRecordListByEbmId(ebm.getEbmId());

//		//播发记录对应的人员信息
//		UnitInfo unitInfo=convertUnitList(ebmBrdRecord, ebmBrdRecordList);
//		//根据播发记录统计区域摆放比
//		Coverage coverage=getCoverage(ebm.getSchemeId(), ebmBrdRecordList);


//		//根据播发记录获取播放资源信息
//		ResBrdInfo resBrdInfo=converResBrdInfo(ebmBrdRecordList, null, null);
//

		//统计播放结果
		BroadcastStateEnum stateEnum=getBrdState(ebm.getSchemeId(), ebmBrdRecordList);
		
		brdItm.setEBM(ebmIdVO);
		brdItm.setBrdStateCode(stateEnum.getCode());
		brdItm.setBrdStateDesc(stateEnum.getBrdstate());
//		brdItm.setUnitInfo(unitInfo);
//		brdItm.setCoverage(coverage);
//		brdItm.setResBrdInfo(resBrdInfo);
		return brdItm;
	}

	private EBM convertEBM(EbmBrdRecordInfo ebmBrdRecord) {
		EBM ebmIdVO = new EBM();
		ebmIdVO.setEBMID(ebmBrdRecord.getEbmId());
		//消息基本信息
		MsgBasicInfo msgBasicInfo=new MsgBasicInfo();
		msgBasicInfo.setMsgType(ebmBrdRecord.getMsgType());
		msgBasicInfo.setSenderName(ebmBrdRecord.getSendName());
		msgBasicInfo.setSenderCode(ebmBrdRecord.getSenderCode());
		Date sendTime=ebmBrdRecord.getSendTime();
		if(sendTime!=null){
			String sendTimeStr=DateTimeUtil.dateToString(sendTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
			msgBasicInfo.setSendTime(sendTimeStr);
		}
		msgBasicInfo.setEventType(ebmBrdRecord.getEventType());
		msgBasicInfo.setSeverity(ebmBrdRecord.getSeverity());
		Date startTime=ebmBrdRecord.getStartTime();
		if(startTime!=null){
			String startTimeStr=DateTimeUtil.dateToString(startTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
			msgBasicInfo.setStartTime(startTimeStr);
		}
		
		Date endTime=ebmBrdRecord.getEndTime();
		if(endTime!=null){
			String endTimeStr=DateTimeUtil.dateToString(endTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
			msgBasicInfo.setEndTime(endTimeStr);
		}
		//TODO  区域转换
		String areaCode=RegionUtil.areaShort2Long(ebmBrdRecord.getAreaCode());
		
		//消息内容
		MsgContent msgContent=new MsgContent();
		msgContent.setLanguageCode(ebmBrdRecord.getLanguageCode());
		msgContent.setMsgTitle(ebmBrdRecord.getMsgTitle());
		msgContent.setMsgDesc(ebmBrdRecord.getMsgDesc());
		msgContent.setAreaCode(areaCode);
		msgContent.setProgramNum(ebmBrdRecord.getProgramNum());
		
		ebmIdVO.setMsgBasicInfo(msgBasicInfo);
		ebmIdVO.setMsgContent(msgContent);
		return ebmIdVO;
	}
	
//	private UnitInfo convertUnitList(EbmBrdRecord ebmRecord,List<EbmBrdRecord> ebmBrdRecords){
//		UnitInfo unitInfo=new UnitInfo();
//		List<Unit> dataList=new ArrayList<Unit>();
//		unitInfo.setDataList(dataList);
//		if(CollectionUtils.isEmpty(ebmBrdRecords)){
//			return unitInfo;
//		}
//		for (EbmBrdRecord ebmBrdRecord : ebmBrdRecords) {
//			//查询记录对应的人员部门信息
//			final String brdItemId=ebmBrdRecord.getBrdItemId();
//			List<EbmBrdItemUnit> unitList=ebmBrdItemUnitDAO.findAll(new Specification<EbmBrdItemUnit>() {
//				@Override
//				public Predicate toPredicate(Root<EbmBrdItemUnit> arg0,
//						CriteriaQuery<?> arg1, CriteriaBuilder arg2) {
//					Predicate condition=arg2.equal(arg0.<String> get("brdItemId"), brdItemId);
//					return condition;
//				}
//			});
//			if(CollectionUtils.isEmpty(unitList)){
//				continue ;
//			}
//			for (EbmBrdItemUnit ebmBrdItemUnit : unitList) {
//				Unit unit=new Unit();
//				EBRPS eBRPS=new EBRPS();
//				eBRPS.setEBRID(ebmBrdItemUnit.getEbrpsId());
//				unit.setEBRPS(eBRPS);
//				unit.setPersonID(ebmBrdItemUnit.getPersionId());
//				unit.setPersonName(ebmBrdItemUnit.getPersionName());
//				unit.setUnitId(ebmBrdItemUnit.getUnitId());
//				unit.setUnitName(ebmBrdItemUnit.getUnitName());
//				dataList.add(unit);
//			}
//		}
//		return unitInfo;
//	}

	
//	private Coverage getCoverage(final Integer schemeId,List<EbmBrdRecord> ebmBrdRecords){
//		List<SchemeEbr> schemeEbrList=schemeEbrDAO.findBySchemeId(schemeId);
//		Map<String,String> ebrIdMap=new HashMap<String,String>();
//		if(!CollectionUtils.isEmpty(schemeEbrList)){
//			for (SchemeEbr schemeEbr : schemeEbrList) {
//				String areaCode=schemeEbr.getEbrArea();
//				String ebrId=schemeEbr.getEbrId();
//				ebrIdMap.put(ebrId, areaCode);
//			}
//		}
//		int coverAreaSize=0;
//		StringBuffer arecCodeBuffer=new StringBuffer(50);
//		if(!CollectionUtils.isEmpty(ebmBrdRecords)){
//			for (EbmBrdRecord ebmBrdRecord : ebmBrdRecords) {
//				String ebrId=ebmBrdRecord.getResourceId();
//				if(ebrIdMap.containsKey(ebrId)){
//					arecCodeBuffer.append(ebrIdMap.get(ebrId)).append(",");
//					coverAreaSize++;
//				}
//			}
//		}
//		String areaCode=arecCodeBuffer.toString();
//		if(areaCode.endsWith(",")){
//			areaCode=areaCode.substring(0,areaCode.length()-1);
//		}
//		float coveragePercent=0;
//		int allAreaCodeSize=ebrIdMap.size();
//		if(allAreaCodeSize!=0){
//			coveragePercent=(float)coverAreaSize/allAreaCodeSize;
//		}
//		Coverage coverage=new Coverage();
//
//		//实际覆盖区域  TODO 为了测试为空则设置一个默认值
//		coverage.setAreaCode(StringUtils.defaultIfEmpty(RegionUtil.areaShort2Long(areaCode), "341523000000"));
//		//覆盖区域百分百
//		//coverage.setCoveragePercent(coveragePercent);
//		//  TODO 为了测试设个初始值
//		//coveragePercent =  coveragePercent!= 0 ? coveragePercent : 9;
//		coverage.setCoverageRate(coveragePercent);
//		coverage.setResBrdStat("2,10,10,10"); //实际调用资源响应统计
//		return coverage;
//	}
	
//	private List<ResBrdItem> convertResBrdItems(String brdItemId,Date rptStartTimeCon,Date rptEndTimeCon){
//		//调用资源播出数据
//		List<ResBrdItem> resBrdItemList=new ArrayList<ResBrdItem>();
//		List<EbmRes> ebmResList= ebmResDAO.findAll(new Specification<EbmRes>() {
//			@Override
//			public Predicate toPredicate(Root<EbmRes> root,
//					CriteriaQuery<?> criteriaquery, CriteriaBuilder criteriabuilder) {
//				return criteriabuilder.equal(root.get("brdItemId"), brdItemId);
//			}
//		});
//		if(CollectionUtils.isEmpty(ebmResList)){
//			return resBrdItemList;
//		}
//
//		for (EbmRes ebmRes : ebmResList) {
//			ResBrdItem resBrdItem=new ResBrdItem();
//			EBRAS eBRAS=new EBRAS();
//			if(StringUtils.isNotBlank(ebmRes.getEbrasId())) {
//				eBRAS.setEBRID(ebmRes.getEbrasId());
//				resBrdItem.setEBRAS(eBRAS);
//			}
//			EBRPS eBRPS=new EBRPS();
//			if(StringUtils.isNotBlank(ebmRes.getEbrpsId())) {
//				eBRPS.setEBRID(ebmRes.getEbrpsId());
//				resBrdItem.setEBRPS(eBRPS);
//			}
//			EBRST eBRST=new EBRST();
//			if(StringUtils.isNotBlank(ebmRes.getEbrstId())){
//				eBRST.setEBRID(ebmRes.getEbrstId());
//				resBrdItem.setEBRST(eBRST);
//			}
//
//			final String ebmResourceId=ebmRes.getEbmResourceId();
//			List<EbmResBs> ebmResBsList=ebmResBsDAO.findAll(new Specification<EbmResBs>() {
//				@Override
//				public Predicate toPredicate(Root<EbmResBs> root,
//						CriteriaQuery<?> criteriaquery, CriteriaBuilder criteriabuilder) {
//					Predicate predicate = criteriabuilder.conjunction();
//					predicate.getExpressions().add(criteriabuilder.equal(root.get("ebmResourceId"), ebmResourceId));
//					if (rptStartTimeCon!=null) {
//						predicate.getExpressions().add(criteriabuilder.greaterThanOrEqualTo(root.<Date> get("rptTime"), rptStartTimeCon));
//					}
//					if (rptEndTimeCon!=null) {
//						predicate.getExpressions().add(criteriabuilder.lessThanOrEqualTo(root.<Date> get("rptTime"),rptEndTimeCon));
//					}
//					return predicate;
//				}
//			});
//			List<EBRBS> ebrbsList=new ArrayList<EBRBS>();
//			if(!CollectionUtils.isEmpty(ebmResBsList)){
//				for (EbmResBs ebmResBs : ebmResBsList) {
//					EBRBS ebrbs=new EBRBS();
//					Date rptTime=ebmResBs.getRptTime();
//					if(rptTime!=null){
//						ebrbs.setRptTime(DateTimeUtil.dateToString(rptTime, DateStyle.YYYY_MM_DD_HH_MM_SS));
//					}
////					ebrbs.setBrdSysType(ebmResBs.getBrdSysType());
//					ebrbs.setBrdSysInfo(ebmResBs.getBrdSysInfo());
//
//					Date startTime2=ebmResBs.getStartTime();
//					if(startTime2!=null){
//						ebrbs.setStartTime(DateTimeUtil.dateToString(startTime2, DateStyle.YYYY_MM_DD_HH_MM_SS));
//
//					}
//					Date endTime2=ebmResBs.getEndTime();
//					if(endTime2!=null){
//						ebrbs.setEndTime(DateTimeUtil.dateToString(endTime2, DateStyle.YYYY_MM_DD_HH_MM_SS));
//
//					}
//					ebrbs.setFileURL(ebmResBs.getFileURL());
//					ebrbs.setBrdStateCode(ebmResBs.getBrdStateCode());
//					ebrbs.setBrdStateDesc(ebmResBs.getBrdStateDesc());
//					ebrbsList.add(ebrbs);
//				}
//			}
//			resBrdItem.setDataList(ebrbsList);
//			resBrdItemList.add(resBrdItem);
//		}
//		return resBrdItemList;
//	}

//	private ResBrdInfo converResBrdInfo(List<EbmBrdRecordInfo> ebmBrdRecords,final Date rptStartTimeCon,final Date rptEndTimeCon){
//		ResBrdInfo resBrdInfo=new ResBrdInfo();
//		List<ResBrdItem> dataListAll=new ArrayList<ResBrdItem>();
//		resBrdInfo.setDataList(dataListAll);
//		for (EbmBrdRecordInfo ebmBrdRecord : ebmBrdRecords) {
//			String brdItemId=ebmBrdRecord.getBrdItemId();
//			List<ResBrdItem> resBrdItems=convertResBrdItems(brdItemId,rptStartTimeCon,rptEndTimeCon);
//			if(CollectionUtils.isEmpty(resBrdItems)){
//				continue ;
//			}
//			dataListAll.addAll(resBrdItems);
//		}
//		return resBrdInfo;
//	}


	/**
	 * 根据播发记录确定当前信息播发状态--------------------这个逻辑后续需要确定修改--------------------
	 * @param schemeId
	 * @param ebmBrdRecords
	 * @return
	 */
	private BroadcastStateEnum getBrdState(String schemeId, List<EbmBrdRecordInfo> ebmBrdRecords) {

		List<SchemeEbrInfo> schemeEbrList = coreFeginService.findSchemeEbrListBySchemeId(schemeId);
		if (CollectionUtils.isEmpty(schemeEbrList)) {
			return BroadcastStateEnum.noprogress;
		}
		if(ebmBrdRecords==null || ebmBrdRecords.isEmpty()) {
			return BroadcastStateEnum.timetogo;
		}
		List<String> ebrIdList = new ArrayList<String>();
		for (SchemeEbrInfo schemeEbr : schemeEbrList) {
			String ebrId = schemeEbr.getEbrId();
			ebrIdList.add(ebrId);
		}
		for (EbmBrdRecordInfo ebmBrdRecord : ebmBrdRecords) {

			String ebrId = ebmBrdRecord.getResourceId();
			if (ebrIdList.contains(ebrId)) {
				Integer stateCode = ebmBrdRecord.getBrdStateCode();
				if (BroadcastStateEnum.failed.getCode().equals(stateCode)) {
					return BroadcastStateEnum.failed;
				}else if(BroadcastStateEnum.inprogress.getCode().equals(stateCode)) {
					return BroadcastStateEnum.inprogress;
				}else if(BroadcastStateEnum.cancelled.getCode().equals(stateCode)) {
					System.out.println("这段有点重复，后续提出来");
					return BroadcastStateEnum.cancelled;
				}

			}
		}
		return BroadcastStateEnum.succeeded;
	}
}
