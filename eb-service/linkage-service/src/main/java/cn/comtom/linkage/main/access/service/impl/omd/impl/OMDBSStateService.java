package cn.comtom.linkage.main.access.service.impl.omd.impl;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.RSStateEnum;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRBSState;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wj
 * 播出系统状态同步请求处理
 */
@Service
@Slf4j
public class OMDBSStateService extends AbstractOMDInfoService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;

	@Override
	public String OMDType() {
		return EBDType.EBRBSState.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		PlatformWhereRequest whereRequest=new PlatformWhereRequest();
		if(params!=null){
			log.info("=======OMDBSStateService=======params=[{}]",JSON.toJSONString(params));
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
		log.info("=======需上报信息的播出系统列表=====size=[{}]",broadcastFound.size());
		EBRBSState broadcastState = new EBRBSState();
		List<EBRBS> ebrBsList = new ArrayList<EBRBS>();
		for(EbrBroadcastInfo fnd : broadcastFound) {
			EBRBS bs = new EBRBS();
			bs.setRptTime(DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
			bs.setEBRID(fnd.getBsEbrId());
			bs.setStateCode(fnd.getBsState());
			bs.setStateDesc(RSStateEnum.getNameByCode(fnd.getBsState()));
			ebrBsList.add(bs);
		}
		broadcastState.setDataList(ebrBsList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildBSState(eBRID, srcURL, ebdIndex, broadcastState,relatedEbdId,destEbrId);

		List<EbrBroadcastUpdateRequest> updateRequestList=new ArrayList<EbrBroadcastUpdateRequest>();
		for (EbrBroadcastInfo ebrBroadcast : broadcastFound) {
			EbrBroadcastUpdateRequest updateRequest=new EbrBroadcastUpdateRequest();
			BeanUtils.copyProperties(ebrBroadcast,updateRequest);
			updateRequest.setStatusSyncFlag(SyncFlag.sync.getValue()+"");
			updateRequestList.add(updateRequest);
		}
		resoFeginService.updEbrBroadcastBath(updateRequestList);

		return ebdResponse;
	}

}
