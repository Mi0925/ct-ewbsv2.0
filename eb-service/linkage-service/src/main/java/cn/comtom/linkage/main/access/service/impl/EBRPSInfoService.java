package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.Resource;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRPSInfo;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.tools.enums.StateDictEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nobody
 * 应急广播平台信息上报
 */
@Service
public class EBRPSInfoService extends AbstractEMDService {

    @Autowired
	private IResoFeginService resoFeginService;

    @Autowired
	private ICommonService commonService;


	@Override
	public String serviceType() {
		return EBDType.EBRPSInfo.name();
	}

	//处理应急广播平台上报请求，更新平台数据
	public void service(EBD ebd, List<File> resourceFiles) {
		EBRPSInfo platformInfo = ebd.getEBRPSInfo();
		List<EBRPS> ebrPlatformList = platformInfo.getDataList();
		if(null == ebrPlatformList || ebrPlatformList.size() < 1) {
			return;
		}
		List<PlatformUpdateRequest> updateRequestList=new ArrayList<PlatformUpdateRequest>();
		List<PlatformAddRequest> saveRequestList=new ArrayList<PlatformAddRequest>();
		for(EBRPS ps : ebrPlatformList) {
			String ebrId=ps.getEBRID();
			//1、首先根据ebrId获取数据库对应数据
			EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(ebrId);
			if(null!=ebrPlatform){
			//2、如果存在，则执行更新操作
				PlatformUpdateRequest updateRequest=new PlatformUpdateRequest();
				BeanUtils.copyProperties(ebrPlatform,updateRequest);
				updateRequest.setPsUrl(ps.getURL());
				updateRequest.setLatitude(ps.getLatitude());
				updateRequest.setLongitude(ps.getLongitude());
				updateRequest.setPhoneNumber(ps.getPhoneNumber());
				updateRequest.setContact(ps.getContact());
				updateRequest.setPsAddress(ps.getAddress());
				updateRequest.setPsEbrName(ps.getEBRName());
				updateRequest.setUpdateTime(new Date());
				updateRequest.setSyncFlag(SyncFlag.sync.getValue());
				updateRequest.setPsState(Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey()));
				updateRequestList.add(updateRequest);
			}else{
			//3、如果不存在，则执行增加操作
				PlatformAddRequest saveRequest=new PlatformAddRequest();
				saveRequest.setPsEbrId(ebrId);
				saveRequest.setPsUrl(ps.getURL());
				saveRequest.setLatitude(ps.getLatitude());
				saveRequest.setLongitude(ps.getLongitude());
				saveRequest.setPhoneNumber(ps.getPhoneNumber());
				saveRequest.setContact(ps.getContact());
				saveRequest.setPsAddress(ps.getAddress());
				saveRequest.setPsEbrName(ps.getEBRName());
				saveRequest.setUpdateTime(new Date());
				saveRequest.setSyncFlag(SyncFlag.sync.getValue());

				//解析ebrId获取所需信息
				Resource resource=commonService.parseResourceId(ebrId);
				saveRequest.setAreaCode(resource.getAreaCode());
				saveRequest.setPlatLevel(resource.getPlatLevel()+"");
				saveRequest.setPsType(resource.getResourceType());

				saveRequest.setStatusSyncFlag(SyncFlag.nosync.getValue());
				saveRequest.setPsState(Integer.parseInt(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey()));

				if(ps.getRelatedEBRPS()!=null){
					saveRequest.setParentPsEbrId(ps.getRelatedEBRPS().getEBRID());
				}else{
					saveRequest.setParentPsEbrId(commonService.getEbrPlatFormID());
				}

				saveRequestList.add(saveRequest);
			}

		}

		if(saveRequestList.size()>0){
			resoFeginService.saveEbrPlatformBatch(saveRequestList);
		}
		if(updateRequestList.size()>0){
			resoFeginService.updateEbrPlatformBatch(updateRequestList);
		}

	}


}
