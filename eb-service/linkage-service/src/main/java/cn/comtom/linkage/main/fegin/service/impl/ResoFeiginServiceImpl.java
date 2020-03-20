package cn.comtom.linkage.main.fegin.service.impl;

import java.util.List;
import java.util.Map;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.domain.reso.ebr.request.TerminalConditionRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.linkage.main.fegin.ResoFegin;
import cn.comtom.linkage.main.fegin.service.FeginBaseService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResoFeiginServiceImpl extends FeginBaseService implements IResoFeginService {

    @Autowired
    private ResoFegin resoFegin;



    /**
     * 获取播出平台信息
     * @param psEbrId
     * @return
     */
    @Override
    public EbrPlatformInfo getEbrPlatformById(String psEbrId){
        return  getData(resoFegin.getEbrPlatformById(psEbrId));
    }

    /**
     * 获取播出系统信息
     * @param bsEbrId
     * @return
     */
    @Override
    public EbrBroadcastInfo getEbrBroadcastInfoById(String bsEbrId) {
        ApiEntityResponse<EbrBroadcastInfo> response = resoFegin.getEbrBroadcastByBsEbrId(bsEbrId);
        return  getData(response);
    }

    @Override
    public FileInfo getFileInfoById(String fileId) {
        return  getData(resoFegin.getFileInfoByFileId(fileId));
    }

    @Override
    public OriginFileInfo getByMd5(String md5Code) {
        return getData(resoFegin.getByMd5(md5Code));
    }

    @Override
    public OriginFileInfo getOriginFileById(String fileId) {
        return getData(resoFegin.getOriginFileById(fileId));
    }

    @Override
    public OriginFileInfo saveOriginFile(OriginFileAddRequest fileAddRequest) {
        return getData(resoFegin.save(fileAddRequest));
    }


    @Override
    public FileInfo getFileByMd5(String md5) {
        return getData(resoFegin.getFileByMd5(md5));
    }

    @Override
    public FileInfo saveFile(FileAddRequest info) {
        return getData(resoFegin.saveFile(info));
    }





    @Override
    public List<EbrPlatformInfo> findPlatformListByWhere(PlatformWhereRequest whereRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(whereRequest);
        return getDataList(resoFegin.findPlatformListByWhere(whereRequestMap));
    }

    @Override
    public Boolean updatePlatform(PlatformUpdateRequest updateRequest) {
        return getBoolean(resoFegin.updatePlatform(updateRequest));
    }

    @Override
    public Integer updateEbrPlatformState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        return getData(resoFegin.updateEbrPlatformState(ebrStateUpdateRequestList));
    }

    @Override
    public Integer updateEbrBroadcastState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        return getData(resoFegin.updateEbrBroadcastState(ebrStateUpdateRequestList));
    }

    @Override
    public Integer saveEbrPlatformBatch(List<PlatformAddRequest> saveRequestList) {
        return getData(resoFegin.saveEbrPlatformBatch(saveRequestList));
    }

    @Override
    public Integer updateEbrPlatformBatch(List<PlatformUpdateRequest> updateRequestList) {
        return getData(resoFegin.updateEbrPlatformBatch(updateRequestList));
    }

    @Override
    public EbrInfo getEbrCommonInfoById(String ebrId) {
        return getData(resoFegin.getEbrCommonInfoById(ebrId));
    }

    @Override
    public Integer saveEbrBroadcastBath(List<EbrBroadcastAddRequest> addRequestList) {
        return getData(resoFegin.saveEbrBroadcastBath(addRequestList));
    }

    @Override
    public Integer updEbrBroadcastBath(List<EbrBroadcastUpdateRequest> updateRequestList) {
        return getData(resoFegin.updEbrBroadcastBath(updateRequestList));
    }

    @Override
    public List<EbrBroadcastInfo> findBroadcastListByWhere(PlatformWhereRequest whereRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(whereRequest);
        return getDataList(resoFegin.findBroadcastListByWhere(whereRequestMap));
    }



    @Override
    public List<EbrTerminalInfo> findListByTremIds(List<String> devTrmIds) {
        return getData(resoFegin.findListByTremIds(devTrmIds));
    }

    @Override
    public Integer addEbrTerminalBatch(List<EbrTerminalAddRequest> addRequestList) {
        return getData(resoFegin.addEbrTerminalBatch(addRequestList));
    }

    @Override
    public Integer updateEbrTerminalBatch(List<EbrTerminalUpdateRequest> updateRequestList) {
        return getData(resoFegin.updateEbrTerminalBatch(updateRequestList));
    }

    @Override
    public Integer updateEbrTerminalState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        return getData(resoFegin.updateEbrTerminalState(ebrStateUpdateRequestList));
    }

    @Override
    public List<EbrTerminalInfo> findTerminalListByWhere(TerminalWhereRequest whereRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(whereRequest);
        return getDataList(resoFegin.findTerminalListByWhere(whereRequestMap));
    }

    @Override
    public List<EbrStationInfo> findStationListByWhere(StationWhereRequest whereRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(whereRequest);
        return getDataList(resoFegin.findStationListByWhere(whereRequestMap));
    }

	@Override
	public List<EbrAdapterInfo> findAdapterListByWhere(AdapterWhereRequest whereRequest) {
		Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(whereRequest);
        return getDataList(resoFegin.findAdapterListByWhere(whereRequestMap));
	}

	@Override
	public Integer updEbrStationBatch(List<StationUpdateRequest> updateRequestList) {
        return getData(resoFegin.updEbrStationBatch(updateRequestList));
    }

	@Override
	public Integer updEbrAdapterBatch(List<AdapterUpdateRequest> updateRequestList) {
		return getData(resoFegin.updEbrAdapterBatch(updateRequestList));
	}

	@Override
	public List<EbrStationInfo> findListByStationIds(List<String> stationIds) {
		return getData(resoFegin.findListByStationIds(stationIds));
	}

    @Override
    public Integer addEbrStationBatch(List<StationAddRequest> addRequestList) {
        return getData(resoFegin.addEbrStationBatch(addRequestList));
    }

	@Override
	public List<EbrAdapterInfo> findListByAdapterIds(List<String> adapterIds) {
		return getData(resoFegin.findListByAdapterIds(adapterIds));
	}

	@Override
	public Integer addEbrAdapterBatch(List<AdapterAddRequest> addRequestList) {
		return getData(resoFegin.addEbrAdapterBatch(addRequestList));
	}

	@Override
	public Integer updateEbrAdapterState(List<EbrStateUpdateRequest> updateRequestList) {
		 return getData(resoFegin.updateEbrAdapterState(updateRequestList));
	}

	@Override
	public EbrStationInfo getEbrStationInfoById(String ebrId) {
		return getData(resoFegin.getEbrStationInfoById(ebrId));
	}

	@Override
	public EbrAdapterInfo getEbrAdapterInfoById(String ebrId) {
		return getData(resoFegin.getEbrAdapterInfoById(ebrId));
	}

	@Override
	public String getChannelByEbrId(String ebrId) {
		return getData(resoFegin.getChannelByEbrId(ebrId));
	}

    @Override
    public FileLibraryInfo getFileLibraryInfoByLibId(String libId) {
        return getData(resoFegin.getFileLibraryInfoByLibId(libId));
    }

	@Override
	public Integer countTerminalByCondition(TerminalConditionRequest terminalConditionRequest) {
		return getData(resoFegin.countTerminalByCondition(terminalConditionRequest));
	}

}
