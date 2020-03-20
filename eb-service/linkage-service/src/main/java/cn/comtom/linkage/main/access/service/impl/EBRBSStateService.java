package cn.comtom.linkage.main.access.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRBSState;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;

/**
 * @author wj
 * 播出系统状态
 */
@Service
public class EBRBSStateService extends AbstractEMDService{

	@Autowired
	private IResoFeginService resoFeginService;
	
	@Autowired
	private ICoreFeginService coreFeginService;
	
	@Override
	public String serviceType() {
		return EBDType.EBRBSState.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
		EBRBSState broadcastState = ebd.getEBRBSState();
		List<EBRBS> bsStateList = broadcastState.getDataList();
		if(null == bsStateList || bsStateList.size() < 1) {
			return;
		}

		List<EbrStateUpdateRequest>  ebrStateUpdateRequestList=new ArrayList<EbrStateUpdateRequest>();

		for (EBRBS bs : bsStateList) {
			String ebrId = bs.getEBRID();
			//1、首先根据ebrId获取数据库对应数据
			EbrBroadcastInfo ebrBroadcast = resoFeginService.getEbrBroadcastInfoById(ebrId);
			if (null != ebrBroadcast) {
				//执行更新操作
				EbrStateUpdateRequest updateRequest=new EbrStateUpdateRequest();
				updateRequest.setEbrId(ebrId);
				updateRequest.setState(bs.getStateCode());
				ebrStateUpdateRequestList.add(updateRequest);
			}
		}
		resoFeginService.updateEbrBroadcastState(ebrStateUpdateRequestList);
		coreFeginService.callEbrState();//通知大屏展示更新地图展示资源状态
	}

}
