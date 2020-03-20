package cn.comtom.linkage.main.access.service.impl;


import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.Resource;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRBSInfo;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nobody
 * 播出系统信息上报处理
 */
@Service
public class EBRBSInfoService extends AbstractEMDService{

	@Autowired
	private IResoFeginService resoFeginService;

	@Autowired
	private ICommonService commonService;

	@Override
	public String serviceType() {
		return EBDType.EBRBSInfo.name();
	}

	//处理播出系统上报请求，获取播出系统上报数据并保存播出系统到数据库
	public void service(EBD ebd, List<File> resourceFiles) {
		EBRBSInfo broadcastInfo = ebd.getEBRBSInfo();
		List<EBRBS> ebrBroadcastList = broadcastInfo.getDataList();
		if (null == ebrBroadcastList || ebrBroadcastList.size() == 0) {
			return;
		}


		List<EbrBroadcastAddRequest>  addRequestList=new ArrayList<EbrBroadcastAddRequest>();
		List<EbrBroadcastUpdateRequest> updateRequestList=new ArrayList<EbrBroadcastUpdateRequest>();

		for (EBRBS bs : ebrBroadcastList) {
			String ebrId = bs.getEBRID();
			//1、首先根据ebrId获取数据库对应数据
			EbrBroadcastInfo ebrBroadcast = resoFeginService.getEbrBroadcastInfoById(ebrId);
			if (null != ebrBroadcast) {
				//执行更新操作
				EbrBroadcastUpdateRequest updateRequest=new EbrBroadcastUpdateRequest();
				BeanUtils.copyProperties(updateRequest,ebrBroadcast);
				BeanUtils.copyProperties(updateRequest,bs);
				updateRequestList.add(updateRequest);
			} else {
				//执行新增操作
				EbrBroadcastAddRequest addRequest=newEbrBroadcast(bs);
				addRequestList.add(addRequest);
			}
		}

		if(addRequestList.size()>0){
			resoFeginService.saveEbrBroadcastBath(addRequestList);
		}

		if(updateRequestList.size()>0){
			resoFeginService.updEbrBroadcastBath(updateRequestList);
		}
	}

	private EbrBroadcastAddRequest newEbrBroadcast(EBRBS ebrbs) {
		EbrBroadcastAddRequest broadcast=new EbrBroadcastAddRequest();
		broadcast.setBsEbrId(ebrbs.getEBRID());
		broadcast.setBsName(ebrbs.getEBRName());
		broadcast.setBsUrl(ebrbs.getURL());
		broadcast.setBsType(ebrbs.getEBRID().substring(2, 4));
		broadcast.setLongitude(ebrbs.getLongitude());
		broadcast.setLatitude(ebrbs.getLatitude());
		if(null != ebrbs.getSquare()){
			broadcast.setSquare(Double.parseDouble(ebrbs.getSquare()));
		}
		broadcast.setAreaCode(ebrbs.getAreaCode());
		if(null != ebrbs.getPopulation()) {
			broadcast.setPopulation(ebrbs.getPopulation());
		}
		broadcast.setLanguageCode(ebrbs.getLanguageCode());
		broadcast.setEquipRoom(ebrbs.getEquipRoom());
		if(null != ebrbs.getRadioParams()) {
			broadcast.setRadioChannelName(ebrbs.getRadioParams().getChannelName());
			if(ebrbs.getRadioParams().getFreq()!=null){
				broadcast.setRadioFreq(ebrbs.getRadioParams().getFreq());
			}
			broadcast.setRadioPower(ebrbs.getRadioParams().getPower());
			broadcast.setBackup(ebrbs.getRadioParams().getBackup()+"");
			broadcast.setAutoSwitch(ebrbs.getRadioParams().getAutoSwitch()+"");
			broadcast.setRemoteControl(ebrbs.getRadioParams().getRemoteControl()+"");
			broadcast.setExperiment(ebrbs.getRadioParams().getExperiment()+"");
		}
		if(null != ebrbs.gettVParams()) {
			broadcast.setTvChannelName(ebrbs.gettVParams().getChannelName());
			broadcast.setTvFreq(ebrbs.gettVParams().getFreq());
			broadcast.setProgramNum(ebrbs.gettVParams().getProgramNum());
			broadcast.setTvChannelNum(ebrbs.gettVParams().getChannelNum());
		}
		if(ebrbs.getRelatedEBRPS()!=null){
			broadcast.setRelatedPsEbrId(ebrbs.getRelatedEBRPS().getEBRID());
		}
		if(ebrbs.getRelatedEBRST()!=null){
			broadcast.setRelatedRsEbrId(ebrbs.getRelatedEBRST().getEBRID());
		}
		if(ebrbs.getRelatedEBRAS()!=null){
			broadcast.setRelatedAsEbrId(ebrbs.getRelatedEBRAS().getEBRID());
		}

		broadcast.setUpdateTime(new Date());
		broadcast.setSyncFlag(SyncFlag.nosync.getValue()+"");
		Resource resource=commonService.parseResourceId(ebrbs.getEBRID());
		broadcast.setAreaCode(resource.getAreaCode());
		broadcast.setBsType(resource.getResourceType());
		return broadcast;
	}


}
