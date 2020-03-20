package cn.comtom.linkage.main.access.service.impl;


import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebm.info.*;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordUpdateRequest;
import cn.comtom.domain.core.flow.request.DispatchFlowUpdateRequest;
import cn.comtom.linkage.commons.BroadcastStateEnum;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.EbmResType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.*;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMBrdLog;
import cn.comtom.linkage.main.access.model.ebd.ebm.*;
import cn.comtom.linkage.main.access.untils.SynchEbmPlayBroadcastState;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.tools.utils.UUIDGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author nobody 接收应急广播消息播发记录处理
 */

@Service
public class EBMBrdLogService extends AbstractEMDService {

	private static final Logger logger = LoggerFactory.getLogger(AbstractEMDService.class);

	@Autowired
	private ICoreFeginService coreFeginService;

	@Autowired
	private SynchEbmPlayBroadcastState synchEbmPlayBroadcastState;


	@Override
	public String serviceType() {
		return EBDType.EBMBrdLog.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {

		// 数据来源的资源ID
		String resourceId = ebd.getSRC().getEBRID();
		// 必选
		EBMBrdLog ebmBrdLog = ebd.getEBMBrdLog();

		// 播发记录结果集
		List<EbmBrdRecordInfo> ebmBrdRecords = new ArrayList<EbmBrdRecordInfo>();
		// 播发记录关联人员集合
		List<EbmBrdItemUnitInfo> ebmBrdItemUnits = new ArrayList<EbmBrdItemUnitInfo>();
		// 播放记录关联资源集合
		List<EbmResInfo> ebmResList = new ArrayList<EbmResInfo>();
		// 播放记录关联资源对于的播出系统集合
		List<EbmResBsInfo> ebmResBsList = new ArrayList<EbmResBsInfo>();


		// 必选
		List<EBMBrdItem> ebmBrdItems = ebmBrdLog.getDataList();

		if(ebmBrdItems.size()==0){
			return;
		}

		for (EBMBrdItem ebmBrdItem : ebmBrdItems) {

			//数据来源最新EBMID，需要换成原EBMID（上级）
			String orgEbmId=ebmBrdItem.getEBM().getEBMID();
			String ebmId=coreFeginService.getEbmById(orgEbmId).getRelatedEbmId();
			ebmBrdItem.getEBM().setEBMID(ebmId);

			EbmBrdRecordInfo brdRecord = convert(ebmBrdItem, resourceId);
			brdRecord.setNewEbmId(orgEbmId);

			//如果播发记录存在则更新，不存在则增加
			if (StringUtils.isNotEmpty(brdRecord.getBrdItemId())) {
				EbmBrdRecordUpdateRequest request = new EbmBrdRecordUpdateRequest();
				BeanUtils.copyProperties(brdRecord, request);
				coreFeginService.updateEbmBrdRecord(request);
			} else {
				EbmBrdRecordRequest addRequest=new EbmBrdRecordRequest();
				BeanUtils.copyProperties(brdRecord, addRequest);
				EbmBrdRecordInfo info = coreFeginService.saveEbmBrdRecord(addRequest);
				brdRecord.setBrdItemId(info.getBrdItemId());
			}

			String brdItemId = brdRecord.getBrdItemId();

			List<EbmBrdItemUnitInfo> brdItemUnits = convertBrdItemUnits(ebmBrdItem, brdItemId);
			if (!CollectionUtils.isEmpty(brdItemUnits)) {
				ebmBrdItemUnits.addAll(brdItemUnits);
			}

			List<EbmResInfo> resList = convertEbmResList(ebmBrdItem, brdItemId, ebmResBsList);
			if (!CollectionUtils.isEmpty(resList)) {
				ebmResList.addAll(resList);
			}


			// TODO: 2019/2/25 0025 维护新发出去的ebm数据的状态信息
			synchEbmPlayBroadcastState.updateNewEbmBroadcastStateByEbmId(orgEbmId,brdRecord.getBrdStateCode());


			// TODO: 2018/12/13 0013 根据ebmId触发维护bc_ebm表中的播发字段broadcastState
			synchEbmPlayBroadcastState.updateEbmBroadcastStateByEbmId(ebmId);

		}

		if (!CollectionUtils.isEmpty(ebmBrdItemUnits)) {
			// 批量保存播发记录关联人员信息
			coreFeginService.saveEbmBrdItemUnitBatch(ebmBrdItemUnits);
		}
		if (!CollectionUtils.isEmpty(ebmResList)) {
			// 批量保存播发记录关联资源信息
			coreFeginService.saveEbmResBatch(ebmResList);
		}
		if (!CollectionUtils.isEmpty(ebmResBsList)) {

			List<EbmResBsInfo> addEbmResBsInfoList=new ArrayList<EbmResBsInfo>();
			List<EbmResBsInfo> updEbmResBsInfoList=new ArrayList<EbmResBsInfo>();

			for (EbmResBsInfo info:ebmResBsList){
				if(info.getId()==null){
					addEbmResBsInfoList.add(info);
				}else{
					updEbmResBsInfoList.add(info);
				}
			}

			if(addEbmResBsInfoList.size()!=0){
				// 1.批量保存播发记录关联资源的播出系统信息
				coreFeginService.saveEbmResBsBatch(addEbmResBsInfoList);
			}

			if(updEbmResBsInfoList.size()!=0){
				//1.批量更新播发记录关联资源播出系统详情
				coreFeginService.updateEbmResBsBatch(updEbmResBsInfoList);
			}

		}

	}


	private EbmBrdRecordInfo convert(EBMBrdItem ebmBrdItem, String resourceId) {
		// 必选
		EBM ebm = ebmBrdItem.getEBM();
		// 必选消息ID
        String ebmId = ebm.getEBMID();
		// 可选
		MsgBasicInfo msgBasicInfo = ebm.getMsgBasicInfo();
		Integer msgType = null;
		String sendName = null;
		String senderCode = null;

		Date sendTime = null;
		String eventType = null;
		Date startTime = null;
		Date endTime = null;
		Integer severity = null;
		if (msgBasicInfo != null) {
			msgType = msgBasicInfo.getMsgType();
			sendName = msgBasicInfo.getSenderName();
			senderCode = msgBasicInfo.getSenderCode();
			String sendTimeStr = msgBasicInfo.getSendTime();
			sendTime = DateTimeUtil.stringToDate(sendTimeStr,
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
			eventType = msgBasicInfo.getEventType();
			severity = msgBasicInfo.getSeverity();
			String startTimeStr = msgBasicInfo.getStartTime();
			startTime = DateTimeUtil.stringToDate(startTimeStr,
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
			String endTimeStr = msgBasicInfo.getEndTime();
			endTime = DateTimeUtil.stringToDate(endTimeStr,
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
		}
		// 必选
		MsgContent msgContent = ebm.getMsgContent();
		String areaCode = msgContent.getAreaCode();
		String languageCode = msgContent.getLanguageCode();
		String msgDesc = msgContent.getMsgDesc();
		String msgTitle = msgContent.getMsgTitle();
		// 可选
		Integer programNum = msgContent.getProgramNum();
		// 必选
		Integer brdStateCode = ebmBrdItem.getBrdStateCode();
		String brdStateDesc = ebmBrdItem.getBrdStateDesc();
		// 可选
		Coverage coverage = ebmBrdItem.getCoverage();
		String coverageAreaCode = null;
		Double coverageRate=null;
		String resBrdStat=null;
		if (coverage != null) {
			// 必选
			coverageRate=coverage.getCoverageRate();
			// 必选
			coverageAreaCode = coverage.getAreaCode();

			resBrdStat = coverage.getResBrdStat();
		}
		// 根据来源ID和消息ID唯一区分
		EbmBrdRecordInfo brdRecord = coreFeginService.getEbmBrdByEbmIdAndResourceId(ebmId,resourceId);
		if (brdRecord == null) {
			// 新增
			brdRecord = new EbmBrdRecordInfo();
		}

		EbmInfo ebmInfo=coreFeginService.getEbmInfoById(ebmId);
		if(ebmInfo!=null){
			msgType=Integer.parseInt(ebmInfo.getMsgType());
			eventType= ebmInfo.getEventType();
			sendName = ebmInfo.getSendName();
			senderCode = ebmInfo.getSenderCode();
			severity = Integer.parseInt(ebmInfo.getSeverity());
		}
		// 修改
		brdRecord.setResourceId(resourceId);
		brdRecord.setMsgType(msgType);
		brdRecord.setEbmId(ebmId);
		brdRecord.setSendName(sendName);
		brdRecord.setSenderCode(senderCode);
		brdRecord.setSendTime(sendTime);
		brdRecord.setEventType(eventType);
		brdRecord.setSeverity(severity);
		brdRecord.setStartTime(startTime);
		brdRecord.setEndTime(endTime);
		brdRecord.setLanguageCode(languageCode);
		brdRecord.setMsgTitle(msgTitle);
		brdRecord.setMsgDesc(msgDesc);
		brdRecord.setAreaCode(areaCode);
		brdRecord.setProgramNum(programNum);
		brdRecord.setBrdStateCode(brdStateCode);
		brdRecord.setBrdStateDesc(brdStateDesc);
		brdRecord.setCoverageAreaCode(coverageAreaCode);
		brdRecord.setCoverageRate(coverageRate);
		brdRecord.setResBrdStat(resBrdStat);
		brdRecord.setMsgTitle(msgTitle);
		brdRecord.setUpdateTime(new Date());
		brdRecord.setSyncFlag(SyncFlag.nosync.getValue());
		return brdRecord;
	}

	private List<EbmBrdItemUnitInfo> convertBrdItemUnits(EBMBrdItem ebmBrdItem, String brdItemId) {
		List<EbmBrdItemUnitInfo> brdItemUnits = new ArrayList<EbmBrdItemUnitInfo>();
		UnitInfo unitInfo = ebmBrdItem.getUnitInfo();
		if (unitInfo == null) {
			return brdItemUnits;
		}
		List<Unit> unitList = unitInfo.getDataList();
		if (CollectionUtils.isEmpty(unitList)) {
			return brdItemUnits;
		}
		for (Unit unit : unitList) {
			// 必选 播发部门ID
			String unitId = unit.getUnitId();
			// 必选 播发部门名称
			String unitName = unit.getUnitName();
			// 必选 播发部门名称
			 String persionId = unit.getPersonID();
			// 必选 播发部门名称
			String persionName = unit.getPersonName();
			// 必选 应急广播平台ID
			String ebrpsId = unit.getEBRPS().getEBRID();

			//根据brdItemId和unitId查询EbmBrdItemUnit数据是否存在
			EbmBrdItemUnitInfo ebmBrdItemUnit = coreFeginService.findEbmBrdItemUnitByBrdItemIdAndUnitId(brdItemId,unitId);
			if (ebmBrdItemUnit != null) {
				continue;
			}
			EbmBrdItemUnitInfo brdItemUnit = new EbmBrdItemUnitInfo();
			brdItemUnit.setBrdItemId(brdItemId);
			brdItemUnit.setEbrpsId(ebrpsId);
			brdItemUnit.setPersionId(persionId);
			brdItemUnit.setPersionName(persionName);
			brdItemUnit.setUnitId(unitId);
			brdItemUnit.setUnitName(unitName);
			brdItemUnits.add(brdItemUnit);
		}
		return brdItemUnits;
	}

	private List<EbmResInfo> convertEbmResList(EBMBrdItem ebmBrdItem,String brdItemId, List<EbmResBsInfo> ebmResBs) {
		List<EbmResInfo> ebmResList = new ArrayList<EbmResInfo>();
		// 可选 调用资源播出信息
		ResBrdInfo resBrdInfo = ebmBrdItem.getResBrdInfo();
		if (resBrdInfo == null) {
			return ebmResList;
		}
		List<ResBrdItem> brdItems = resBrdInfo.getDataList();
		if (CollectionUtils.isEmpty(brdItems)) {
			return ebmResList;
		}
		for (ResBrdItem resBrdItem : brdItems) {
			// 可选
			EBRPS ebrps = resBrdItem.getEBRPS();
			// 可选
			EBRST ebrst = resBrdItem.getEBRST();
			// 必选
			EBRAS ebras = resBrdItem.getEBRAS();



			//根据brdItemId查询EbmRes数据
			List<EbmResInfo> ebmRes = coreFeginService.getEbmResListByBrdItemId(brdItemId);
			List<String> resIdList=new ArrayList<String>();
			for(EbmResInfo info:ebmRes){
				resIdList.add(info.getResId());
			}

			if(ebrps!=null){
				String ebrpsId=ebrps.getEBRID();
				if(!resIdList.contains(ebrpsId)){
					EbmResInfo psEbmRes = new EbmResInfo();
					psEbmRes.setBrdItemId(brdItemId);
					psEbmRes.setResId(ebrpsId);
					psEbmRes.setResType(EbmResType.EBRPS.getType());
					ebmResList.add(psEbmRes);
				}
			}

			if (ebrst != null) {
				String ebrstId = ebrst.getEBRID();
				if(!resIdList.contains(ebrstId)){
					EbmResInfo stEbmRes = new EbmResInfo();
					stEbmRes.setBrdItemId(brdItemId);
					stEbmRes.setResId(ebrstId);
					stEbmRes.setResType(EbmResType.EBRST.getType());
					ebmResList.add(stEbmRes);
				}
			}

			String ebrasId = null;
			if (ebras != null) {
				ebrasId = ebras.getEBRID();
				if(!resIdList.contains(ebrasId)){
					EbmResInfo asEbmRes = new EbmResInfo();
					asEbmRes.setBrdItemId(brdItemId);
					asEbmRes.setResId(ebrasId);
					asEbmRes.setResType(EbmResType.EBRAS.getType());
					ebmResList.add(asEbmRes);
				}
			}
			List<EbmResBsInfo> resBs = convertEbmResBsList(resBrdItem, brdItemId);
			if (!CollectionUtils.isEmpty(resBs)) {
				ebmResBs.addAll(resBs);
			}
		}
		return ebmResList;
	}

	private List<EbmResBsInfo> convertEbmResBsList(ResBrdItem resBrdItem, String brdItemId) {
		List<EbmResBsInfo> ebmResBsList = new ArrayList<EbmResBsInfo>();
		// 播出系统详情 必选
		List<EBRBS> ebrbsList = resBrdItem.getDataList();
		if (CollectionUtils.isEmpty(ebrbsList)) {
			return ebmResBsList;
		}
		for (EBRBS ebrbs : ebrbsList) {
			String rptTimeStr = ebrbs.getRptTime();
			final Date rptTime = DateTimeUtil.stringToDate(rptTimeStr,
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
			String brdSysInfo = ebrbs.getBrdSysInfo();
			String startTimeStr = ebrbs.getStartTime();
			Date startTime = DateTimeUtil.stringToDate(startTimeStr,
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
			String endTimeStr = ebrbs.getEndTime();
			Date endTime = DateTimeUtil.stringToDate(endTimeStr,
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());

			// 存放播音文件的网络地址
			String fileURL = ebrbs.getFileURL();
			Integer brdStateCode = ebrbs.getBrdStateCode();
			String brdStateDesc = ebrbs.getBrdStateDesc();

			//根据brdItemId和brdSysInfo查询EbmResBs信息
			EbmResBsInfo ebmResBs = coreFeginService.getEbmResBsInfoByBrdItemIdAndBrdSysInfo(brdItemId,brdSysInfo);
			if (ebmResBs == null) {
				ebmResBs = new EbmResBsInfo();
				ebmResBs.setBrdStateCode(brdStateCode);
				ebmResBs.setBrdStateDesc(brdStateDesc);
				ebmResBs.setBrdSysInfo(brdSysInfo);
			//	ebmResBs.setBrdSysType("");
				ebmResBs.setEndTime(endTime);
				ebmResBs.setFileURL(fileURL);
				ebmResBs.setBrdItemId(brdItemId);
				ebmResBs.setRptTime(rptTime);
				ebmResBs.setStartTime(startTime);
				ebmResBsList.add(ebmResBs);
			} else {
				ebmResBs.setBrdStateCode(brdStateCode);
				ebmResBs.setBrdStateDesc(brdStateDesc);
				ebmResBsList.add(ebmResBs);
			}
		}
		return ebmResBsList;
	}
}
