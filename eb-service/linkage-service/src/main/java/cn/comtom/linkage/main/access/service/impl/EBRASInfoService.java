package cn.comtom.linkage.main.access.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRASInfo;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.tools.constants.Constants;

/**
 * @author liuhy
 * 应急广播适配器信息
 */
@Service
public class EBRASInfoService extends AbstractEMDService{


	@Autowired
	private IResoFeginService resoFeginService;

	@Override
	public String serviceType() {
		return EBDType.EBRASInfo.name();
	}

	//处理适配器上报请求，获取适配器上报数据并保存到数据库
	public void service(EBD ebd, List<File> resourceFiles) {
		
		EBRASInfo asInfo = ebd.getEBRASInfo();
		List<EBRAS> ebrAsList = asInfo.getDataList();
		if(CollectionUtils.isEmpty(ebrAsList)) {
			return;
		}
		
		List<String> adapterIds = new ArrayList<String>();
		for(EBRAS dt : ebrAsList) {
			adapterIds.add(dt.getEBRID());
		}

		List<EbrAdapterInfo> existList = resoFeginService.findListByAdapterIds(adapterIds);

		List<AdapterAddRequest> addRequestList = new ArrayList<AdapterAddRequest>();

		List<AdapterUpdateRequest> updateRequestList=new ArrayList<AdapterUpdateRequest>();

		for(EBRAS dt : ebrAsList) {
			EbrAdapterInfo devTrm = getByEbrId(dt.getEBRID(), existList);
			if(devTrm!=null){
				//更新
				AdapterUpdateRequest updateRequest=new AdapterUpdateRequest();
				updateRequest.setAdapterState(String.valueOf(dt.getStateCode()));
				updateRequest.setAsType("03");
				updateRequest.setPlatLevel("4");
				updateRequest.setSubResType("02");
				updateRequest.setLatitude(dt.getLatitude());
				updateRequest.setLongitude(dt.getLongitude());
				updateRequest.setUpdateTime(new Date());
				updateRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				updateRequest.setEbrAsId(dt.getEBRID());
				updateRequest.setEbrAsName(dt.getEBRName());
				updateRequest.setUrl(dt.getURL());
				if(dt.getRelatedEBRPS()!=null){
					updateRequest.setEbrpsId(dt.getRelatedEBRPS().getEBRID());
				}
				if(dt.getRelatedEBRST()!=null){
					updateRequest.setEbrStId(dt.getRelatedEBRST().getEBRID());
				}
				updateRequestList.add(updateRequest);
			}else{
				//新增
				AdapterAddRequest addRequest=new AdapterAddRequest();
				addRequest.setAdapterState(String.valueOf(dt.getStateCode()));
				addRequest.setAsType("03");
				addRequest.setPlatLevel("4");
				addRequest.setSubResType("02");
				addRequest.setLatitude(dt.getLatitude());
				addRequest.setLongitude(dt.getLongitude());
				addRequest.setCreateTime(new Date());
				addRequest.setUpdateTime(new Date());
				addRequest.setSyncFlag(SyncFlag.nosync.getValue()+"");
				addRequest.setStatusSyncFlag(SyncFlag.nosync.getValue()+"");
				addRequest.setEbrAsId(dt.getEBRID());
				addRequest.setEbrAsName(dt.getEBRName());
				addRequest.setUrl(dt.getURL());
				if(dt.getRelatedEBRPS()!=null){
					addRequest.setEbrpsId(dt.getRelatedEBRPS().getEBRID());
				}
				if(dt.getRelatedEBRST()!=null){
					addRequest.setEbrStId(dt.getRelatedEBRST().getEBRID());
				}
				addRequestList.add(addRequest);
			}
		}

		if(addRequestList.size()>0){
			resoFeginService.addEbrAdapterBatch(addRequestList);
		}

		if(updateRequestList.size()>0){
			resoFeginService.updEbrAdapterBatch(updateRequestList);
		}

	}
	
	private final EbrAdapterInfo getByEbrId(String ebrId, List<EbrAdapterInfo> devTrmsExist) {
		EbrAdapterInfo found = null;
		for(EbrAdapterInfo dt : devTrmsExist) {
			if(dt.getEbrAsId().equals(ebrId)) {
				found = dt;
				break;
			}
		}
		return found;
	}

}
