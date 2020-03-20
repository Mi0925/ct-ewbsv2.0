package cn.comtom.linkage.main.access.service.impl.omd.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.EBRInfoType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRST;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRSTInfo;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wj
 * 台站信息同步请求处理
 */
@Service
@Slf4j
public class OMDSTInfoService extends AbstractOMDInfoService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;
	
	@Override
	public String OMDType() {
		return EBDType.EBRSTInfo.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		StationWhereRequest whereRequest=new StationWhereRequest();
		if(params!=null){
			log.info("=======OMDSTInfoService=======params=[{}]",JSON.toJSONString(params));
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

		List<EbrStationInfo> stationFound = resoFeginService.findStationListByWhere(whereRequest);
		log.info("=======需上报信息的台站列表=====stationFound size=[{}]",stationFound.size());
		EBRSTInfo stationInfo = new EBRSTInfo();
		stationInfo.setParams(params);

		List<EBRST> devTrmList = Optional.ofNullable(stationFound).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(fnd ->{
                	EBRST ebrSt = new EBRST();
                	BeanUtils.copyProperties(fnd,ebrSt);
                	ebrSt.setEBRID(fnd.getEbrStId());
                	ebrSt.setEBRName(fnd.getEbrStName());
                	ebrSt.setLatitude(fnd.getLatitude());
                	ebrSt.setLongitude(fnd.getLongitude());
                	ebrSt.setRptType(EBRInfoType.Sync.name());
                	ebrSt.setRptTime(DateTimeUtil.dateToString(fnd.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
                    String relatePsId=fnd.getEbrPsId();
                    if(StringUtils.isNotEmpty(relatePsId)){
                        RelatedEBRPS ebrPS = new RelatedEBRPS();
                        ebrPS.setEBRID(relatePsId);
                        ebrSt.setRelatedEBRPS(ebrPS);
                    }
                    return ebrSt;
                }).collect(Collectors.toList());

		stationInfo.setDataList(devTrmList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildEBRSTInfo(eBRID, srcURL, ebdIndex, stationInfo,relatedEbdId,destEbrId);
		
		//对所有已经上报的设备信息执行更新操作。
		List<StationUpdateRequest> updateRequestList=new ArrayList<StationUpdateRequest>();
		for (EbrStationInfo stationInfo2 : stationFound) {
			StationUpdateRequest updateRequest=new StationUpdateRequest();
			BeanUtils.copyProperties(stationInfo2,updateRequest);
			updateRequest.setSyncFlag(SyncFlag.sync.getValue()+"");
			updateRequestList.add(updateRequest);
		}
		resoFeginService.updEbrStationBatch(updateRequestList);
		
		return ebdResponse;
	}

}
