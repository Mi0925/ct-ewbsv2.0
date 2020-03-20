package cn.comtom.linkage.main.access.service.impl.omd.impl;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.RSStateEnum;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRPSState;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import com.alibaba.fastjson.JSON;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Create By wujiang on 2018/12/05
 * 应急广播平台状态处理
 */
@Service
@Slf4j
public class OMDPSStateService extends AbstractOMDInfoService{
	
	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;
	
	@Override
	public String OMDType() {
		return EBDType.EBRPSState.name();
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
		//查询符合条件的数据
		String psEbrId = commonService.getEbrPlatFormID();
		EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(psEbrId);
		List<EbrPlatformInfo> platformList = Lists.newArrayList(ebrPlatform);
		if(ODMRptType.Incremental.name().equals(params.getRptType()) && ebrPlatform.getStatusSyncFlag().equals(SyncFlag.sync.getValue())) {
        	platformList = Lists.newArrayList();
		}
		log.info("=======需上报状态的平台资源列表=====platformList=[{}]",JSON.toJSONString(platformList));
		EBRPSState eBRPSState=new EBRPSState();
		List<EBRPS> ebrPsList = new ArrayList<EBRPS>();
		eBRPSState.setDataList(ebrPsList);
		if(null != platformList) {
			for(EbrPlatformInfo fnd : platformList) {
				EBRPS ps = new EBRPS();
				ps.setRptTime(DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
				ps.setEBRID(fnd.getPsEbrId());
                ps.setStateCode(fnd.getPsState());
				ps.setStateDesc(RSStateEnum.getNameByCode(fnd.getPsState()));
				ebrPsList.add(ps);
			}
		}
		
		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildPSState(eBRID, srcURL, ebdIndex, eBRPSState,relatedEbdId, destEbrId);
		List<PlatformUpdateRequest> updateList = Lists.newArrayList();
		for (EbrPlatformInfo platformInfo : platformList) {
			platformInfo.setStatusSyncFlag(SyncFlag.sync.getValue());
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
