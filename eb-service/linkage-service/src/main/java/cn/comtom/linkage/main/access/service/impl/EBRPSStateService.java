package cn.comtom.linkage.main.access.service.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRPSState;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;

/**
 * @author nobody
 * 平台状态上报
 */
@Service
public class EBRPSStateService extends AbstractEMDService{


	@Autowired
	private IResoFeginService resoFeginService;
	
	@Autowired
	private ICoreFeginService coreFeginService;
	
	@Override
	public String serviceType() {
		return EBDType.EBRPSState.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
		EBRPSState platformState = ebd.getEBRPSState();
		List<EBRPS> ebrPlatStateList = platformState.getDataList();
		if(null == ebrPlatStateList || ebrPlatStateList.size() < 1) {
			return;
		}
		List<EbrStateUpdateRequest> updateRequestList=new ArrayList<EbrStateUpdateRequest>();
		for(EBRPS ps : ebrPlatStateList) {
			EbrStateUpdateRequest updateRequest=new EbrStateUpdateRequest();
			updateRequest.setEbrId(ps.getEBRID());
			updateRequest.setState(ps.getStateCode());
			updateRequestList.add(updateRequest);
		}
		if(updateRequestList.size()>0){
			resoFeginService.updateEbrPlatformState(updateRequestList);
			coreFeginService.callEbrState();//通知大屏展示更新地图展示资源状态
		}
	}

}
