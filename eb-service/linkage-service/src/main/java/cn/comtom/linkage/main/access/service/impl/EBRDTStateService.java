package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRDTState;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wj
 * 平台设备及终端状态
 */
@Service
public class EBRDTStateService extends AbstractEMDService{


	@Autowired
	private IResoFeginService resoFeginService;
    
	@Override
	public String serviceType() {
		return EBDType.EBRDTState.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
		EBRDTState devTrmState = ebd.getEBRDTState();
		List<EBRDT> ebrDevTrmList = devTrmState.getDataList();
		if(null == ebrDevTrmList || ebrDevTrmList.size() < 1) {
			return;
		}

		List<EbrTerminalUpdateRequest>  ebrStateUpdateRequestList=new ArrayList<EbrTerminalUpdateRequest>();

		for (EBRDT dt : ebrDevTrmList) {
			String ebrId = dt.getEBRID();
			//执行更新操作
			EbrTerminalUpdateRequest updateRequest=new EbrTerminalUpdateRequest();
			updateRequest.setTerminalEbrId(ebrId);
			updateRequest.setTerminalState(String.valueOf(dt.getStateCode()));
			updateRequest.setUpdateTime(new Date());
			updateRequest.setStatusSyncFlag(SyncFlag.nosync.getValue()+"");
			ebrStateUpdateRequestList.add(updateRequest);
		}
		resoFeginService.updateEbrTerminalBatch(ebrStateUpdateRequestList);
	}
}
