package cn.comtom.linkage.main.access.service.impl.omd.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.RSStateEnum;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRDTState;
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
public class OMDDTStateService extends AbstractOMDInfoService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;
	
	@Override
	public String OMDType() {
		return EBDType.EBRDTState.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		TerminalWhereRequest whereRequest=new TerminalWhereRequest();
		if(params!=null){
			log.info("=======OMDDTStateService=======params=[{}]",JSON.toJSONString(params));
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
		List<EbrTerminalInfo> terminalFound=resoFeginService.findTerminalListByWhere(whereRequest);
		log.info("=======需上报信息的终端列表=====terminalFound size=[{}]",terminalFound.size());
		EBRDTState devTrmState = new EBRDTState();
		//List<EBRDT> devTrmList = new ArrayList<EBRDT>();
        List<EBRDT> devTrmList = Optional.ofNullable(terminalFound).orElse(Collections.emptyList()).stream()
				.filter(Objects::nonNull)
				.map(fnd ->{
					EBRDT devTrm = new EBRDT();
					devTrm.setRptTime(DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
					devTrm.setEBRID(fnd.getTerminalEbrId());
					devTrm.setStateCode(Integer.parseInt(fnd.getTerminalState()));
					devTrm.setStateDesc(RSStateEnum.getNameByCode(Integer.parseInt(fnd.getTerminalState())));
					return devTrm;
				}).collect(Collectors.toList());

		devTrmState.setDataList(devTrmList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildDTState(eBRID, srcURL, ebdIndex, devTrmState,relatedEbdId,destEbrId);

		//更新状态信息已同步
		List<EbrTerminalUpdateRequest> updateRequestList=new ArrayList<EbrTerminalUpdateRequest>();
		for (EbrTerminalInfo ebrTerminalInfo : terminalFound) {
			EbrTerminalUpdateRequest ebrTerminalUpdate = new EbrTerminalUpdateRequest();
			BeanUtils.copyProperties(ebrTerminalInfo,ebrTerminalUpdate);
			ebrTerminalUpdate.setStatusSyncFlag(SyncFlag.sync.getValue()+"");
			updateRequestList.add(ebrTerminalUpdate);
		}
		log.info("update Ebr Terminal state Batch, size:{}",updateRequestList.size());
		resoFeginService.updateEbrTerminalBatch(updateRequestList);
		return ebdResponse;
	}

}
