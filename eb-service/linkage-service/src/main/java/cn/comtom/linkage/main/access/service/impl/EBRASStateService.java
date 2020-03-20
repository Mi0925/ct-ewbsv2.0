package cn.comtom.linkage.main.access.service.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRASState;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;

/**
 * @author liuhy
 * 应急广播适配器状态
 */
@Service
public class EBRASStateService extends AbstractEMDService{


	@Autowired
	private IResoFeginService resoFeginService;
	
	@Autowired
	private ICoreFeginService coreFeginService;
	
	@Override
	public String serviceType() {
		return EBDType.EBRASState.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
		EBRASState adapterState = ebd.getEBRASState();
		List<EBRAS> adapterStaList = adapterState.getDataList();
		if(CollectionUtils.isEmpty(adapterStaList)) {
			return;
		}
		List<AdapterUpdateRequest> updateRequestList=new ArrayList<AdapterUpdateRequest>();
		for(EBRAS as : adapterStaList) {
			AdapterUpdateRequest updateRequest=new AdapterUpdateRequest();
			updateRequest.setEbrAsId(as.getEBRID());
			updateRequest.setAdapterState(Integer.toString(as.getStateCode()));
			updateRequest.setUpdateTime(new Date());
			updateRequest.setStatusSyncFlag(SyncFlag.nosync.getValue()+"");
			updateRequestList.add(updateRequest);
		}
		if(updateRequestList.size()>0){
			resoFeginService.updEbrAdapterBatch(updateRequestList);
			coreFeginService.callEbrState();//通知大屏展示更新地图展示资源状态
		}
		
	}

}
