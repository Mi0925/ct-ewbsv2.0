package cn.comtom.linkage.main.access.service.impl.omd.impl;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.EBRInfoType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.*;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRBSInfo;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * 播出系统信息同步请求处理
 */
@Service
@Slf4j
public class OMDBSInfoService extends AbstractOMDInfoService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;

	@Override
	public String OMDType() {
		return EBDType.EBRBSInfo.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		PlatformWhereRequest whereRequest=new PlatformWhereRequest();
		if(params!=null){
			log.info("=======OMDBSInfoService=======params=[{}]",JSON.toJSONString(params));
			String rptType=params.getRptType();
			String rptStartTimeStr=params.getRptStartTime();
			String rptEndTimeStr=params.getRptEndTime();
			if(StringUtils.isNotEmpty(rptType)&&(ODMRptType.Incremental.name().equals(rptType))){
				whereRequest.setStatusSyncFlag(SyncFlag.nosync.getValue());
			}
			if(StringUtils.isNotEmpty(rptStartTimeStr)){
				whereRequest.setRptStartTime(rptStartTimeStr);
			}
			if(StringUtils.isNotEmpty(rptEndTimeStr)){
				whereRequest.setRptEndTime(rptEndTimeStr);
			}
		}
		List<EbrBroadcastInfo> broadcastFound=resoFeginService.findBroadcastListByWhere(whereRequest);
		log.debug("=======需上报信息的播出系统列表=====size=[{}]", broadcastFound.size());
		EBRBSInfo ebrbsInfo=new EBRBSInfo();
		ebrbsInfo.setParams(params);
		List<EBRBS> dataList=new ArrayList<EBRBS>();

			for(EbrBroadcastInfo fnd : broadcastFound) {
				EBRBS bs = new EBRBS();
				bs.setRptTime(DateTimeUtil.dateToString(fnd.getUpdateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
				bs.setRptType(EBRInfoType.Sync.name());
				
				String relatedPsId=fnd.getRelatedPsEbrId();
				String relatedStId=fnd.getRelatedRsEbrId();
				String relatedAsId=fnd.getRelatedAsEbrId();
				
				if(StringUtils.isNotEmpty(relatedPsId)){
					RelatedEBRPS ebrPS = new RelatedEBRPS();
					ebrPS.setEBRID(relatedPsId);
					bs.setRelatedEBRPS(ebrPS);
				}
				
				if(StringUtils.isNotEmpty(relatedStId)){
					RelatedEBRST ebrST = new RelatedEBRST();
					ebrST.setEBRID(relatedStId);
					bs.setRelatedEBRST(ebrST);
				}
				
				if(StringUtils.isNotEmpty(relatedAsId)){
					RelatedEBRAS ebrAS = new RelatedEBRAS();
					ebrAS.setEBRID(relatedAsId);
					bs.setRelatedEBRAS(ebrAS);
				}
				
				bs.setEBRID(fnd.getBsEbrId());
				
				bs.setEBRName(fnd.getBsName());
				bs.setURL(fnd.getBsUrl());
				bs.setLongitude(fnd.getLongitude());
				bs.setLatitude(fnd.getLatitude());
				bs.setSquare(String.valueOf(fnd.getSquare()));
//				Coverage cvg = new Coverage();
//				cvg.setAreaCode(fnd.getAreaCode());
				bs.setAreaCode(fnd.getAreaCode());
				if(fnd.getPopulation()!=null){
					bs.setPopulation(fnd.getPopulation().doubleValue());					
				}
				bs.setLanguageCode(fnd.getLanguageCode());
				bs.setEquipRoom(StringUtils.isEmpty(fnd.getEquipRoom())?"未知机房":fnd.getEquipRoom());
				
                RadioParams rdPrms = new RadioParams();
                rdPrms.setChannelName(fnd.getRadioChannelName());
                rdPrms.setFreq(fnd.getRadioFreq() == null ? null : fnd.getRadioFreq().intValue());
                rdPrms.setPower(fnd.getRadioPower());
                rdPrms.setBackup(fnd.getBackup()==null?null:Integer.parseInt(fnd.getBackup()));
                rdPrms.setAutoSwitch(fnd.getAutoSwitch()==null?null:Integer.parseInt(fnd.getAutoSwitch()));
                rdPrms.setRemoteControl(fnd.getRemoteControl()==null?null:Integer.parseInt(fnd.getRemoteControl()));
                rdPrms.setExperiment(fnd.getExperiment()==null?null:Integer.parseInt(fnd.getExperiment()));
                if (StringUtils.isNotEmpty(rdPrms.getChannelName()) && rdPrms.getFreq()!=null && rdPrms.getPower()!=null && rdPrms.getBackup()!=null &&  rdPrms.getExperiment()!=null && rdPrms.getAutoSwitch()!=null && rdPrms.getRemoteControl()!=null){
                    bs.setRadioParams(rdPrms);
                }else{
                	logger.info("播出系统信息 RadioParams 模拟广播附加参数不完整...");
                }
                TVParams tvPrms = new TVParams();
                tvPrms.setChannelName(fnd.getTvChannelName());
                tvPrms.setFreq(fnd.getTvFreq());
                tvPrms.setChannelNum(fnd.getTvChannelNum());
                tvPrms.setProgramNum(fnd.getProgramNum());
                if(StringUtils.isNotEmpty(tvPrms.getChannelName()) && tvPrms.getFreq()!=null && StringUtils.isNotEmpty(tvPrms.getChannelNum()) &&  StringUtils.isNotEmpty(tvPrms.getProgramNum())){
                    bs.settVParams(tvPrms);
                }
                dataList.add(bs);
			}

		ebrbsInfo.setDataList(dataList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildEBRBSInfo(eBRID, srcURL, ebdIndex, ebrbsInfo,relatedEbdId,destEbrId);

		List<EbrBroadcastUpdateRequest> updateRequestList=new ArrayList<EbrBroadcastUpdateRequest>();
		for (EbrBroadcastInfo ebrBroadcast : broadcastFound) {
			EbrBroadcastUpdateRequest updateRequest=new EbrBroadcastUpdateRequest();
			BeanUtils.copyProperties(ebrBroadcast,updateRequest);
			updateRequest.setSyncFlag(SyncFlag.sync.getValue()+"");
			updateRequestList.add(updateRequest);
		}
		resoFeginService.updEbrBroadcastBath(updateRequestList);

		return ebdResponse;
	}

}
