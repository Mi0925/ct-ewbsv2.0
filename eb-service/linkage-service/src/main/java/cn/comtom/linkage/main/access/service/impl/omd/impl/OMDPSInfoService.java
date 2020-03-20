package cn.comtom.linkage.main.access.service.impl.omd.impl;


import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.EBRInfoType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRPSInfo;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import cn.comtom.tools.constants.MqQueueConstant;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  Create By wujiang on 2018/12/05
 * 应急广播平台信息处理
 */
@Service
@Slf4j
public class OMDPSInfoService extends AbstractOMDInfoService{

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private SequenceGenerate sequenceGenerate;

	@Override
	public String OMDType() {
		return EBDType.EBRPSInfo.name();
	}

	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		PlatformWhereRequest whereRequest=new PlatformWhereRequest();
		if(params!=null){
			log.info("=======OMDPSInfoService=======params=[{}]",JSON.toJSONString(params));
			String rptType=params.getRptType();
			String rptStartTimeStr=params.getRptStartTime();
			String rptEndTimeStr=params.getRptEndTime();
			if(StringUtils.isNotEmpty(rptType) && (ODMRptType.Incremental.name().equals(rptType))){
				whereRequest.setSyncFlag(SyncFlag.nosync.getValue());
			}
			if(StringUtils.isNotEmpty(rptStartTimeStr)){
				whereRequest.setRptStartTime(rptStartTimeStr);
			}
			if(StringUtils.isNotEmpty(rptEndTimeStr)){
				whereRequest.setRptEndTime(rptEndTimeStr);
			}			
		}
		//查询符合条件的数据
		String psEbrId = commonService.getEbrPlatFormID();
		EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(psEbrId);
		List<EbrPlatformInfo> platformList = Lists.newArrayList(ebrPlatform);
        if(ODMRptType.Incremental.name().equals(params.getRptType()) && ebrPlatform.getSyncFlag().equals(SyncFlag.sync.getValue())) {
        	platformList = Lists.newArrayList();
		}
		log.info("=======需上报状态的平台资源列表=====platformList=[{}]",JSON.toJSONString(platformList));
		List<EBRPS> ebrPsList = new ArrayList<EBRPS>();
		if(null != platformList) {
			for(EbrPlatformInfo fnd : platformList) {
				EBRPS ps = new EBRPS();
				if(fnd.getUpdateTime() == null){
					ps.setRptTime(DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
				}else{
					ps.setRptTime(DateTimeUtil.dateToString(fnd.getUpdateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
				}
				ps.setRptType(EBRInfoType.Sync.name());
				ps.setEBRID(fnd.getPsEbrId());
				ps.setEBRName(fnd.getPsEbrName());
				ps.setAddress(fnd.getPsAddress());
				ps.setContact(fnd.getContact());
				ps.setPhoneNumber(fnd.getPhoneNumber());
				ps.setLongitude(fnd.getLongitude());
				ps.setLatitude(fnd.getLatitude());
				ps.setURL(fnd.getPsUrl());
				
				String parentPsEbrId=fnd.getParentPsEbrId();
				if(StringUtils.isNotBlank(parentPsEbrId)){
					RelatedEBRPS relatedPs = new RelatedEBRPS();
					relatedPs.setEBRID(parentPsEbrId);
					ps.setRelatedEBRPS(relatedPs);
				}
				ebrPsList.add(ps);
			}
		}	
		
		EBRPSInfo ebrpsInfo=new EBRPSInfo();
		ebrpsInfo.setParams(params);
		ebrpsInfo.setDataList(ebrPsList);

		String eBRID=psEbrId;
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildEBRPSInfo(eBRID, srcURL, ebdIndex, ebrpsInfo,relatedEbdId,destEbrId);

		/**
		 * 更新平台信息同步状态为已同步
		 */
		List<PlatformUpdateRequest> updateList = Lists.newArrayList();
		for (EbrPlatformInfo platformInfo : platformList) {
			platformInfo.setSyncFlag(SyncFlag.sync.getValue());
			PlatformUpdateRequest updateRequest=new PlatformUpdateRequest();
			BeanUtils.copyProperties(platformInfo,updateRequest);
			updateRequest.setUpdateTime(new Date());
			updateList.add(updateRequest);
		}
		if(!CollectionUtils.isEmpty(updateList)) {
			resoFeginService.updateEbrPlatformBatch(updateList);
		}
		return ebdResponse;
	}

}
