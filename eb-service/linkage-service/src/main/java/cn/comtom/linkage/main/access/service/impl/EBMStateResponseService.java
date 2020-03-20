package cn.comtom.linkage.main.access.service.impl;


import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.info.EbmResBsInfo;
import cn.comtom.domain.core.ebm.info.EbmResInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordUpdateRequest;
import cn.comtom.domain.core.ebm.request.EbmStateBackAddRequest;
import cn.comtom.domain.reso.ebr.request.TerminalConditionRequest;
import cn.comtom.linkage.commons.*;
import cn.comtom.linkage.main.access.constant.EbmResType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.*;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateResponse;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo;
import cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdInfo;
import cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.access.untils.SynchEbmPlayBroadcastState;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author nobody
 * 播发状态反馈数据上报
 */
@Service
public class EBMStateResponseService extends AbstractEMDService {


    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private SequenceGenerate sequenceGenerate;

    @Autowired
    private MQMessageProducer mqMessageProducer;
    
    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private SynchEbmPlayBroadcastState synchEbmPlayBroadcastState;
    
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String serviceType() {
        return EBDType.EBMStateResponse.name();
    }

    @Override
    public void service(EBD ebd, List<File> resourceFiles) {

        SRC src = ebd.getSRC();
        // 必选资源ID
        String ebdSrcEbrId = src.getEBRID();
        String orgEbmId = ebd.getEBMStateResponse().getEBM().getEBMID();
        EbmInfo ebm = coreFeginService.getEbmById(orgEbmId);
        if(ebm == null) {
        	log.error("播发状态反馈失败，ebm不存在，ebmId:{}",orgEbmId);
        	return ;
        }
        String ebmId = ebm.getRelatedEbmId();
        
        EBMStateResponse ebmStateResponse = ebd.getEBMStateResponse();
        ebmStateResponse.getEBM().setEBMID(ebmId);//换成上级的ebmId

        if(StringUtils.isNotBlank(ebmId)) {
	        EbmBrdRecordRequest brdRecord = convertEbmBrdRecord(ebmStateResponse, ebdSrcEbrId);
	        brdRecord.setNewEbmId(orgEbmId);
	
	        //如果播发记录存在则更新，不存在则增加
	        if (StringUtils.isNotEmpty(brdRecord.getBrdItemId())) {
	            EbmBrdRecordUpdateRequest request = new EbmBrdRecordUpdateRequest();
	            BeanUtils.copyProperties(brdRecord, request);
	            coreFeginService.updateEbmBrdRecord(request);
	        } else {
	            EbmBrdRecordInfo info = coreFeginService.saveEbmBrdRecord(brdRecord);
	            brdRecord.setBrdItemId(info.getBrdItemId());
	        }
        }
        //增加反馈记录
        EbmStateBackAddRequest addRequest=new EbmStateBackAddRequest();
        addRequest.setBackTime(new Date());
        //addRequest.setBrdItemId(brdRecord.getBrdItemId());
        addRequest.setBackStatus(ebmStateResponse.getBrdStateCode()+"");
        addRequest.setEbmId(orgEbmId);//最新发下去的EbmId
        coreFeginService.saveEbmStateBack(addRequest);
        log.info("EBMStateUpdate播发状态更新,ebmId:{},stateCode:{}",orgEbmId,ebmStateResponse.getBrdStateCode());
        // TODO: 2019/2/25 0025 维护新发出去的ebm数据的状态信息
        synchEbmPlayBroadcastState.updateNewEbmBroadcastStateByEbmId(orgEbmId,ebmStateResponse.getBrdStateCode());

        // TODO: 2018/12/13 0013 根据ebmId触发维护bc_ebm表中的播发字段broadcastState
        if(StringUtils.isNotBlank(ebmId))
          // synchEbmPlayBroadcastState.updateEbmBroadcastStateByEbmId(ebmId);
        	synchEbmPlayBroadcastState.updateNewEbmBroadcastStateByEbmId(ebmId,ebmStateResponse.getBrdStateCode());

        EbmStateResponseMessage message = new EbmStateResponseMessage();
        message.setEbmId(orgEbmId);
        message.setEbdId(ebd.getEBDID());
        message.setBroadcastState(ebmStateResponse.getBrdStateCode().toString());
        mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue, JSON.toJSONString(message));

    }


    public void sendResponse(String ebmId, String requestURL, String ebdId,String destEbrId,String ebmState) {
        EBD ebd = buildEBD(ebdId, ebmId,destEbrId, ebmState);
        sendEBD(ebd, requestURL);
    }

    /**
     * 创建回复数据包
     *
     * @param ebmId
     * @return
     */
    private EBD buildEBD(String ebdId, String ebmId,String destEbrId, String ebmState) {
        EBMStateResponse response = buildEBMStateResponse(ebmId,ebmState);
        String eBRID = commonService.getEbrPlatFormID();
        String srcURL = commonService.getPlatFormUrl();
        String ebdIndex = sequenceGenerate.createId(LinkageConstants.EBDID);
        EBD ebd = EBDModelBuild.buildStateResponse(ebdId, eBRID, srcURL, ebdIndex,destEbrId, response);
        return ebd;
    }

    /**
     * 创建回复数据
     *
     * @param ebmId
     * @return
     */
    private EBMStateResponse buildEBMStateResponse(String ebmId,String ebmState) {

        //根据ebmId触发维护bc_ebm表中的播发字段broadcastState
        synchEbmPlayBroadcastState.updateEbmBroadcastStateByEbmId(ebmId);

        //查询消息状态
        EbmInfo ebmInfo = coreFeginService.getEbmById(ebmId);

        if (null == ebmInfo) {
            throw new EbmException(EBDRespResultEnum.contentdamage, "查不到对应的消息实体数据");
        }
        String schemeId = ebmInfo.getSchemeId();

        //ebm对应的播发记录
        List<EbmBrdRecordInfo> ebmBrdRecordList = coreFeginService.getEbmBrdRecordListByEbmId(ebmId);

//		//重新统计覆盖区域
//		Coverage coverage=getCoverage(ebm,schemeId, ebmBrdRecords);
        //统计播放结果--------直接读取bc_ebm表中的播发状态为准。
        Integer broadcastState = ebmInfo.getBroadcastState();
        if(StringUtils.isNotBlank(ebmState)) {
        	broadcastState = Integer.parseInt(ebmState);
        }
        String broadcastStateDesc = BroadcastStateEnum.getNameByCode(broadcastState);

        //返回数据
        //播发状态数据返回
        EBMStateResponse response = new EBMStateResponse();
        String rptTime = DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS);
        response.setRptTime(rptTime);
        EBM eBM = new EBM();
        eBM.setEBMID(ebmId);
        response.setEBM(eBM);
        response.setBrdStateCode(broadcastState);
        response.setBrdStateDesc(broadcastStateDesc);
        
        TerminalConditionRequest condition = new TerminalConditionRequest();
        String[] areaCodeArray = ebmInfo.getAreaCode().split(",");
		List<String> areaCodeList = Arrays.asList(areaCodeArray);
		condition.setAreCodes(areaCodeList);
        int totalNum = resoFeginService.countTerminalByCondition(condition);
        condition.setTerminalState(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey());
        int onLineNum = resoFeginService.countTerminalByCondition(condition);
        if(broadcastState == BroadcastStateEnum.inprogress.getCode() || broadcastState == BroadcastStateEnum.succeeded.getCode()) {
        	double rate = Double.valueOf(onLineNum)/Double.valueOf(totalNum);
        	rate = (double) Math.round(rate * 100) / 100;
	        Coverage coverage = new Coverage();
	        response.setCoverage(coverage);
	        coverage.setCoverageRate(rate);
	        coverage.setAreaCode(fmtAreaCode(ebmInfo.getAreaCode()));
	        coverage.setResBrdStat("1,0,1,".concat(String.valueOf(onLineNum)));
        }
        if(CollectionUtils.isEmpty(ebmBrdRecordList)){
        	return response;
        }
        
        ResBrdInfo resBrdInfo = new ResBrdInfo();
        List<ResBrdItem> dataList = new ArrayList<ResBrdItem>();
        resBrdInfo.setDataList(dataList);
        response.setResBrdInfo(resBrdInfo);

        //查询所有的条目信息
        List<EbmResInfo> ebmResListAll = new ArrayList<EbmResInfo>();
        if (!CollectionUtils.isEmpty(ebmBrdRecordList)) {
            for (EbmBrdRecordInfo ebmBrdRecord2 : ebmBrdRecordList) {
                String brdItemId = ebmBrdRecord2.getBrdItemId();
                List<EbmResInfo> ebmResList = coreFeginService.getEbmResListByBrdItemId(brdItemId);//根据播发记录查询条目集合
                if (!CollectionUtils.isEmpty(ebmResList)) {
                    ebmResListAll.addAll(ebmResList);
                }
            }
        }

        ResBrdItem resBrdItem = new ResBrdItem();
        for (EbmResInfo ebmRes : ebmResListAll) {
            String brdItemId = ebmRes.getBrdItemId();
            //可选
            if (ebmRes.getResType().equals(EbmResType.EBRPS.getType())) {
                EBRPS eBRPS = new EBRPS();
                eBRPS.setEBRID(ebmRes.getResId());
                resBrdItem.setEBRPS(eBRPS);
            } else if (ebmRes.getResType().equals(EbmResType.EBRAS.getType())) {//必选
                EBRAS ebras = new EBRAS();
                ebras.setEBRID(ebmRes.getResId());
                resBrdItem.setEBRAS(ebras);
            } else if (ebmRes.getResType().equals(EbmResType.EBRST.getType())) {//可选
                EBRST eBRST = new EBRST();
                eBRST.setEBRID(ebmRes.getResId());
                resBrdItem.setEBRST(eBRST);
            }
        }
        //必选
        List<EBRBS> bsDataList = new ArrayList<EBRBS>();
        resBrdItem.setDataList(bsDataList);

        for (EbmBrdRecordInfo ebmBrdRecord2 : ebmBrdRecordList) {
            String brdItemId = ebmBrdRecord2.getBrdItemId();
            List<EbmResBsInfo> ebmResBsList = coreFeginService.getEbmResBsByBrdItemId(brdItemId);
            for (EbmResBsInfo ebmResBs : ebmResBsList) {
                Date rptDate = ebmResBs.getRptTime();
                Date endDate = ebmResBs.getEndTime();
                Date startDate = ebmResBs.getStartTime();
                String startTime2 = null;
                String rptTime2 = null;
                String endTime2 = null;
                if (rptDate != null) {
                    rptTime2 = DateTimeUtil.dateToString(rptDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
                }
                if (endDate != null) {
                    endTime2 = DateTimeUtil.dateToString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
                }
                if (startDate != null) {
                    startTime2 = DateTimeUtil.dateToString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
                }
                EBRBS ebrbs = new EBRBS();
                ebrbs.setRptTime(rptTime2);
//				ebrbs.setBrdSysType(ebmResBs.getBrdSysType());
                ebrbs.setBrdSysInfo(ebmResBs.getBrdSysInfo());
                ebrbs.setStartTime(startTime2);
                ebrbs.setEndTime(endTime2);
                ebrbs.setFileURL(ebmResBs.getFileURL());
                ebrbs.setBrdStateCode(ebmResBs.getBrdStateCode());
                ebrbs.setBrdStateDesc(ebmResBs.getBrdStateDesc());
                bsDataList.add(ebrbs);
            }
        }
        /*if(resBrdItem.getEBRPS()==null) {
        	  EBRPS eBRPS = new EBRPS();
              eBRPS.setEBRID(commonService.getParentPlatId());
              resBrdItem.setEBRPS(eBRPS);
        }
        if(CollectionUtils.isEmpty(bsDataList)) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        	 EBRBS ebrbs = new EBRBS();
             ebrbs.setRptTime(sdf.format(new Date()));
             ebrbs.setBrdSysInfo(null);
             ebrbs.setStartTime(sdf.format(ebmInfo.getStartTime()));
             ebrbs.setEndTime(sdf.format(ebmInfo.getEndTime()));
             ebrbs.setFileURL("rtp://224.0.0.1:20001");
             ebrbs.setBrdStateCode(ebmInfo.getBroadcastState());
             ebrbs.setBrdStateDesc(ebmInfo.getBroadcastState().toString());
        	bsDataList.add(ebrbs);
        }*/
        dataList.add(resBrdItem);
        return response;
    }
	private static String fmtAreaCode(String areaCode) {
		if(StringUtils.isNotBlank(areaCode) && areaCode.length()>11) {
			return areaCode;
		}
		areaCode+="0000000000";
		return areaCode.substring(0, 12);
	}
//	private Coverage getCoverage(Ebm ebm,final Integer schemeId,List<EbmBrdRecord> ebmBrdRecords){
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
//		coveragePercent= coveragePercent==0 ? 99 : coveragePercent;
//		areaCode = StringUtils.isNotEmpty(areaCode) ? areaCode : ebm.getAreaCode(); //TODO 无法获取到播发记录，直接获取消息广播消息表中的覆盖区域编码
//		//实际覆盖率
//		coverage.setCoverageRate(coveragePercent);
//		//实际覆盖区域
//		coverage.setAreaCode(areaCode);
//		coverage.setResBrdStat("2,10,10,10"); //实际调用资源响应统计
//		//覆盖区域百分百
//		// coverage.setCoveragePercent(coveragePercent);
//		return coverage;
//	}
//


    private void reportBrdItem(EBMStateResponse ebmStateResponse) {
        String parentPlatUrl = commonService.getParentPlatUrl();
        if (StringUtils.isEmpty(parentPlatUrl)) {
            return;
        }
        String ebmId = ebmStateResponse.getEBM().getEBMID();
        EbmInfo ebm = coreFeginService.getEbmById(ebmId);
        if (ebm == null) {
            return;
        }
        String schemeId = ebm.getSchemeId();
        //ebm对应的播发记录
        List<EbmBrdRecordInfo> ebmBrdRecords = coreFeginService.getEbmBrdRecordListByEbmId(ebmId);
//		//重新统计覆盖区域
//		Coverage coverage=getCoverage(schemeId, ebmBrdRecords);
        //统计播放结果
        BroadcastStateEnum stateEnum = synchEbmPlayBroadcastState.getBrdState(schemeId, ebmBrdRecords);

        ebmStateResponse.setBrdStateCode(stateEnum.getCode());
        ebmStateResponse.setBrdStateDesc(stateEnum.getBrdstate());
//		ebmStateResponse.setCoverage(coverage);

        String ebrId = commonService.getEbrPlatFormID();
        String srcUrl = commonService.getPlatFormUrl();
        String ebdIndex = sequenceGenerate.createId(LinkageConstants.EBDID);
        EBD ebd = EBDModelBuild.buildStateResponse(ebrId, srcUrl, ebdIndex, ebmStateResponse);
        sendEBD(ebd, parentPlatUrl);
    }


    private EbmBrdRecordRequest convertEbmBrdRecord(EBMStateResponse ebmStateResponse, String ebdSrcEbrId) {
        final String ebmId = ebmStateResponse.getEBM().getEBMID();
        //根据来源ID和消息ID唯一区分
        EbmBrdRecordInfo brdRecord = coreFeginService.getEbmBrdByEbmIdAndResourceId(ebmId, ebdSrcEbrId);
        if (brdRecord == null) {
            //查询消息状态
            EbmInfo ebm = coreFeginService.getEbmById(ebmId);
            if (ebm == null) {
                throw new EbmException(EBDRespResultEnum.contentdamage, "根据消息ID没有找到对应的消息");
            }
            brdRecord = new EbmBrdRecordInfo();
            brdRecord.setAreaCode(ebm.getAreaCode());
            brdRecord.setEbmId(ebmId);
            brdRecord.setEndTime(ebm.getEndTime());
            brdRecord.setEventType(ebm.getEventType());
            brdRecord.setLanguageCode(ebm.getMsgLanguageCode());
            brdRecord.setMsgDesc(ebm.getMsgDesc());
            brdRecord.setMsgTitle(ebm.getMsgTitle());
            if(ebm.getMsgType()!=null)
            brdRecord.setMsgType(Integer.parseInt(ebm.getMsgType()));
            if(ebm.getProgramNum()!=null)
            brdRecord.setProgramNum(Integer.parseInt(ebm.getProgramNum()));
            brdRecord.setResourceId(ebdSrcEbrId);
            brdRecord.setSenderCode(ebm.getSenderCode());
            brdRecord.setSendName(ebm.getSendName());
            brdRecord.setSendTime(ebm.getSendTime());
            if(ebm.getSeverity()!=null)
            brdRecord.setSeverity(Integer.parseInt(ebm.getSeverity()));
            brdRecord.setStartTime(ebm.getStartTime());
        }
        //必选
        Integer brdStateCode = ebmStateResponse.getBrdStateCode();
        //必选
        String brdStateDesc = ebmStateResponse.getBrdStateDesc();
        //必选
        Coverage coverage = ebmStateResponse.getCoverage();
        if (coverage != null) {
            //必选
            String coverageAreaCode = coverage.getAreaCode();
            //必选
            Double coverageRate = coverage.getCoverageRate();
            //必选
            String resBrdStat = coverage.getResBrdStat();

            brdRecord.setCoverageAreaCode(coverageAreaCode);
            brdRecord.setCoverageRate(coverageRate);
            brdRecord.setResBrdStat(resBrdStat);
        }
        brdRecord.setBrdStateCode(brdStateCode);
        brdRecord.setBrdStateDesc(brdStateDesc);
        EbmBrdRecordRequest recordRequest = new EbmBrdRecordRequest();
        BeanUtils.copyProperties(brdRecord, recordRequest);
        return recordRequest;
    }
}
