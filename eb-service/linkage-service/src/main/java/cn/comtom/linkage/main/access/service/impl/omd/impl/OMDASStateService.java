package cn.comtom.linkage.main.access.service.impl.omd.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.RSStateEnum;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRASState;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wj
 * 设备及终端的状态处理
 */
@Service
@Slf4j
public class OMDASStateService extends AbstractOMDInfoService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;

	@Override
	public String OMDType() {
		return EBDType.EBRASState.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		AdapterWhereRequest whereRequest=new AdapterWhereRequest();
		if(params!=null){
			log.info("=======OMDASStateService=======params=[{}]",JSON.toJSONString(params));
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

		List<EbrAdapterInfo> adapterFound=resoFeginService.findAdapterListByWhere(whereRequest);
		log.info("=======适配器状态上报=====adapterFound size=[{}]",adapterFound.size());
		EBRASState adapterState = new EBRASState();
		List<EBRAS> ebrASList = new ArrayList<EBRAS>();
		for(EbrAdapterInfo fnd : adapterFound) {
			EBRAS bs = new EBRAS();
			bs.setRptTime(DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
			bs.setEBRID(fnd.getEbrAsId());
			if(StringUtils.isNotBlank(fnd.getAdapterState())) {
				bs.setStateCode(Integer.valueOf(fnd.getAdapterState()));
				bs.setStateDesc(RSStateEnum.getNameByCode(Integer.valueOf(fnd.getAdapterState())));
			}
			ebrASList.add(bs);
		}
		adapterState.setDataList(ebrASList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildASState(eBRID, srcURL, ebdIndex, adapterState,relatedEbdId,destEbrId);

		List<AdapterUpdateRequest> updateRequestList=new ArrayList<AdapterUpdateRequest>();
		for (EbrAdapterInfo adapterInfo2 : adapterFound) {
			AdapterUpdateRequest updateRequest = new AdapterUpdateRequest();
			BeanUtils.copyProperties(adapterInfo2,updateRequest);
			updateRequest.setStatusSyncFlag(SyncFlag.sync.getValue().toString());
			updateRequest.setUpdateTime(new Date());
			updateRequestList.add(updateRequest);
		}
		resoFeginService.updEbrAdapterBatch(updateRequestList);

		return ebdResponse;
	}

}
