package cn.comtom.linkage.main.access.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.RSStateEnum;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRST;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRSTInfo;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.tools.constants.Constants;

/**
 * @author liuhy
 * 台站信息
 */
@Service
public class EBRSTInfoService extends AbstractEMDService{


	@Autowired
	private IResoFeginService resoFeginService;

	@Override
	public String serviceType() {
		return EBDType.EBRSTInfo.name();
	}

	//处理台站上报请求，获取台站上报数据并保存台站到数据库
	public void service(EBD ebd, List<File> resourceFiles) {
		
		EBRSTInfo staionInfo = ebd.getEBRSTInfo();
		List<EBRST> ebrStationList = staionInfo.getDataList();
		if(CollectionUtils.isEmpty(ebrStationList)) {
			return;
		}
		
		List<String> stationIds = new ArrayList<String>();
		for(EBRST dt : ebrStationList) {
			stationIds.add(dt.getEBRID());
		}
		
		List<EbrStationInfo> existList = resoFeginService.findListByStationIds(stationIds);

		List<StationAddRequest> addRequestList = new ArrayList<StationAddRequest>();

		List<StationUpdateRequest> updateRequestList=new ArrayList<StationUpdateRequest>();

		for(EBRST dt : ebrStationList) {
			EbrStationInfo devTrm = getByEbrId(dt.getEBRID(), existList);
			if(devTrm!=null){
				//更新
				StationUpdateRequest updateRequest=new StationUpdateRequest();
				updateRequest.setContact(dt.getContact());
				updateRequest.setAddress(dt.getAddress());
				updateRequest.setLatitude(dt.getLatitude());
				updateRequest.setLongitude(dt.getLongitude());
				updateRequest.setPhoneNumber(dt.getPhoneNumber());
				updateRequest.setUpdateTime(new Date());
				updateRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				updateRequest.setEbrStId(dt.getEBRID());
				updateRequest.setEbrStName(dt.getEBRName());
				updateRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				if(dt.getRelatedEBRPS()!=null) {
					updateRequest.setEbrPsId(dt.getRelatedEBRPS().getEBRID());
				}
				updateRequestList.add(updateRequest);
			}else{
				//新增
				StationAddRequest addRequest=new StationAddRequest();
				addRequest.setStType("02");
				addRequest.setPlatLevel("4");
				addRequest.setSubResType("05");
				addRequest.setContact(dt.getContact());
				addRequest.setAddress(dt.getAddress());
				addRequest.setLatitude(dt.getLatitude());
				addRequest.setLongitude(dt.getLongitude());
				addRequest.setPhoneNumber(dt.getPhoneNumber());
				addRequest.setStatusSyncFlag(SyncFlag.nosync.getValue()+"");
				addRequest.setCreateTime(new Date());
				addRequest.setUpdateTime(new Date());
				addRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				addRequest.setEbrStId(dt.getEBRID());
				addRequest.setEbrStName(dt.getEBRName());
				addRequest.setStationState(RSStateEnum.run.getCode().toString());
				if(dt.getRelatedEBRPS()!=null) {
					addRequest.setEbrPsId(dt.getRelatedEBRPS().getEBRID());
				}
				addRequestList.add(addRequest);
			}
		}

		if(addRequestList.size()>0){
			resoFeginService.addEbrStationBatch(addRequestList);
		}

		if(updateRequestList.size()>0){
			resoFeginService.updEbrStationBatch(updateRequestList);
		}

	}
	
	private final EbrStationInfo getByEbrId(String ebrId, List<EbrStationInfo> devTrmsExist) {
		EbrStationInfo found = null;
		for(EbrStationInfo dt : devTrmsExist) {
			if(dt.getEbrStId().equals(ebrId)) {
				found = dt;
				break;
			}
		}
		return found;
	}

}
