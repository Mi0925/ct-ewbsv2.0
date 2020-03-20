package cn.comtom.linkage.main.access.service.impl.omd.impl;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.EBRInfoType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRDTInfo;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wj
 * 设备及终端信息请求处理
 */
@Service
@Slf4j
public class OMDDTInfoService extends AbstractOMDInfoService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;
	
	@Override
	public String OMDType() {
		return EBDType.EBRDTInfo.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		TerminalWhereRequest whereRequest=new TerminalWhereRequest();
		if(params!=null){
			log.info("=======OMDDTInfoService=======params=[{}]",JSON.toJSONString(params));
			String rptType=params.getRptType();
			String rptStartTimeStr=params.getRptStartTime();
			String rptEndTimeStr=params.getRptEndTime();
			if(StringUtils.isNotEmpty(rptType)&&(ODMRptType.Incremental.name().equals(rptType))){
				whereRequest.setSyncFlag(SyncFlag.nosync.getValue());
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
		EBRDTInfo devTrmInfo = new EBRDTInfo();
		devTrmInfo.setParams(params);

		//List<EBRDT> devTrmList = new ArrayList<EBRDT>();
		List<EBRDT> devTrmList = Optional.ofNullable(terminalFound).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(fnd ->{
                    EBRDT devTrm = new EBRDT();
                    devTrm.setEBRID(fnd.getTerminalEbrId());
                    devTrm.setEBRName(fnd.getTerminalEbrName());
                    devTrm.setLatitude(fnd.getLatitude());
                    devTrm.setLongitude(fnd.getLongitude());
                    devTrm.setRptType(EBRInfoType.Sync.name());
                    devTrm.setRptTime(DateTimeUtil.dateToString(fnd.getUpdateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));

                    String relatePsId=fnd.getRelatedPsEbrId();
                    if(StringUtils.isNotEmpty(relatePsId)){
                        RelatedEBRPS ebrPS = new RelatedEBRPS();
                        ebrPS.setEBRID(relatePsId);
                        devTrm.setRelatedEBRPS(ebrPS);
                    }
                    return devTrm;
                }).collect(Collectors.toList());

		devTrmInfo.setDataList(devTrmList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildEBRDTInfo(eBRID, srcURL, ebdIndex, devTrmInfo,relatedEbdId,destEbrId);
		
		//对所有已经上报的设备信息执行更新操作。

		List<EbrTerminalUpdateRequest> updateRequestList=new ArrayList<EbrTerminalUpdateRequest>();
		for (EbrTerminalInfo terminalinfo : terminalFound) {
			EbrTerminalUpdateRequest updateRequest=new EbrTerminalUpdateRequest();
			BeanUtils.copyProperties(terminalinfo,updateRequest);
			updateRequest.setSyncFlag(SyncFlag.sync.getValue()+"");
			updateRequestList.add(updateRequest);
		}
		log.info("update Ebr Terminal info Batch, size:{}",updateRequestList.size());
		resoFeginService.updateEbrTerminalBatch(updateRequestList);
		return ebdResponse;
	}

}
