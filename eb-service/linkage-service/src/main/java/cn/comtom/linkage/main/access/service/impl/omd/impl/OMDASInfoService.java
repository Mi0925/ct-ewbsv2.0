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

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.EBRInfoType;
import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS;
import cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRASInfo;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.untils.SequenceGenerate;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.linkage.utils.EBDModelBuild;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuhy
 * 应急广播适配器信息处理
 */
@Service
@Slf4j
public class OMDASInfoService extends AbstractOMDInfoService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SequenceGenerate sequenceGenerate;
	
	@Override
	public String OMDType() {
		return EBDType.EBRASInfo.name();
	}
	
	@Override
	public EBD service(String relatedEbdId, OMDRequest odmRequest){
		Params params=odmRequest.getParams();
		AdapterWhereRequest whereRequest=new AdapterWhereRequest();
		if(params!=null){
			log.info("=======OMDASInfoService=======params=[{}]",JSON.toJSONString(params));
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

		List<EbrAdapterInfo> adapterFound=resoFeginService.findAdapterListByWhere(whereRequest);
		log.info("=======适配器信息上报=====adapterFound size=[{}]",adapterFound.size());
		EBRASInfo ebrasInfo = new EBRASInfo();
		ebrasInfo.setParams(params);

		List<EBRAS> devTrmList = Optional.ofNullable(adapterFound).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(fnd ->{
                    EBRAS ebras = new EBRAS();
                    ebras.setEBRID(fnd.getEbrAsId());
                    ebras.setEBRName(fnd.getEbrAsName());
                    ebras.setLatitude(fnd.getLatitude());
                    ebras.setLongitude(fnd.getLongitude());
                    ebras.setRptType(EBRInfoType.Sync.name());
                    ebras.setRptTime(DateTimeUtil.dateToString(fnd.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
                    ebras.setURL(fnd.getUrl());
                    String relatePsId=fnd.getEbrpsId();
                    if(StringUtils.isNotEmpty(relatePsId)){
                        RelatedEBRPS ebrPS = new RelatedEBRPS();
                        ebrPS.setEBRID(relatePsId);
                        ebras.setRelatedEBRPS(ebrPS);
                    }
                    String relateStId = fnd.getEbrStId();
                    if(StringUtils.isNotEmpty(relatePsId)){
                    	RelatedEBRST ebrST = new RelatedEBRST();
                    	ebrST.setEBRID(relateStId);
                        ebras.setRelatedEBRST(ebrST);
                    }
                    return ebras;
                }).collect(Collectors.toList());

		ebrasInfo.setDataList(devTrmList);

		String eBRID=commonService.getEbrPlatFormID();
		String srcURL=commonService.getPlatFormUrl();
		String ebdIndex=sequenceGenerate.createId(LinkageConstants.EBDID);
		String destEbrId = commonService.getParentPlatId();
		EBD ebdResponse=EBDModelBuild.buildEBRASInfo(eBRID, srcURL, ebdIndex, ebrasInfo,relatedEbdId,destEbrId);
		
		//对所有已经上报的设备信息执行更新操作。
		List<AdapterUpdateRequest> updateRequestList=new ArrayList<AdapterUpdateRequest>();
		for (EbrAdapterInfo adapterInfo2 : adapterFound) {
			AdapterUpdateRequest updateRequest=new AdapterUpdateRequest();
			BeanUtils.copyProperties(adapterInfo2,updateRequest);
			updateRequest.setSyncFlag(SyncFlag.sync.getValue()+"");
			updateRequestList.add(updateRequest);
		}
		resoFeginService.updEbrAdapterBatch(updateRequestList);

		return ebdResponse;
	}

}
