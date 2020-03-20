package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRDTInfo;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.tools.constants.Constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wj
 * 平台设备及终端信息
 */
@Service
public class EBRDTInfoService extends AbstractEMDService{


	@Autowired
	private IResoFeginService resoFeginService;

	@Override
	public String serviceType() {
		return EBDType.EBRDTInfo.name();
	}

	//处理终端上报请求，获取终端上报数据并保存终端到数据库
	public void service(EBD ebd, List<File> resourceFiles) {

		EBRDTInfo devTrmInfo = ebd.getEBRDTInfo();
		List<EBRDT> ebrDevTrmList = devTrmInfo.getDataList();
		if(null == ebrDevTrmList || ebrDevTrmList.size() < 1) {
			return;
		}

		List<String> devTrmIds = new ArrayList<String>();
		for(EBRDT dt : ebrDevTrmList) {
			devTrmIds.add(dt.getEBRID());
		}


		List<EbrTerminalInfo> existList = resoFeginService.findListByTremIds(devTrmIds);

		List<EbrTerminalAddRequest> addRequestList = new ArrayList<EbrTerminalAddRequest>();

		List<EbrTerminalUpdateRequest> updateRequestList=new ArrayList<EbrTerminalUpdateRequest>();

		for(EBRDT dt : ebrDevTrmList) {
			EbrTerminalInfo devTrm = getByEbrId(dt.getEBRID(), existList);
			if(devTrm!=null){
				//更新
				EbrTerminalUpdateRequest updateRequest=new EbrTerminalUpdateRequest();
				updateRequest.setTerminalEbrId(dt.getEBRID());
				if(StringUtils.isNotBlank(dt.getLatitude())) {
					updateRequest.setLatitude(dt.getLatitude());
				}
				if(StringUtils.isNotBlank(dt.getLongitude())) {
					updateRequest.setLongitude(dt.getLongitude());
				}
				if(StringUtils.isNotBlank(dt.getEBRName())) {
					updateRequest.setTerminalEbrName(dt.getEBRName());
				}
				if(dt.getStateCode() != null) {
					updateRequest.setTerminalState(String.valueOf(dt.getStateCode()));
				}
				RelatedEBRPS relatedEBRPS = dt.getRelatedEBRPS();
				if(relatedEBRPS != null) {
					updateRequest.setRelatedPsEbrId(relatedEBRPS.getEBRID());
				}
				updateRequest.setUpdateTime(new Date());
				updateRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				updateRequestList.add(updateRequest);
			}else{
				//新增
				EbrTerminalAddRequest addRequest=new EbrTerminalAddRequest();
				addRequest.setTerminalEbrId(dt.getEBRID());
				addRequest.setTerminalEbrName(dt.getEBRName());
				addRequest.setTerminalState(String.valueOf(dt.getStateCode()));
				addRequest.setLatitude(dt.getLatitude());
				addRequest.setLongitude(dt.getLongitude());
				addRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				addRequest.setCreateTime(new Date());
				addRequest.setUpdateTime(new Date());
				addRequest.setStatusSyncFlag(SyncFlag.nosync.getValue()+"");
				RelatedEBRPS relatedEBRPS = dt.getRelatedEBRPS();
				if(relatedEBRPS != null) {
					addRequest.setRelatedPsEbrId(relatedEBRPS.getEBRID());
				}
				addRequestList.add(addRequest);
			}
		}

		if(addRequestList.size()>0){
			resoFeginService.addEbrTerminalBatch(addRequestList);
		}

		if(updateRequestList.size()>0){
			resoFeginService.updateEbrTerminalBatch(updateRequestList);
		}

	}

	private final EbrTerminalInfo getByEbrId(String ebrId, List<EbrTerminalInfo> devTrmsExist) {
		EbrTerminalInfo found = null;
		for(EbrTerminalInfo dt : devTrmsExist) {
			if(dt.getTerminalEbrId().equals(ebrId)) {
				found = dt;
				break;
			}
		}
		return found;
	}

}
