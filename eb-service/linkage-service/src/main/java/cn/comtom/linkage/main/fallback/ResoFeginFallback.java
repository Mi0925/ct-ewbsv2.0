package cn.comtom.linkage.main.fallback;

import java.util.List;
import java.util.Map;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import org.springframework.stereotype.Component;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.domain.reso.ebr.request.TerminalConditionRequest;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.linkage.main.fegin.ResoFegin;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;

@Component
public class ResoFeginFallback implements ResoFegin {
    @Override
    public ApiEntityResponse<OriginFileInfo> save(OriginFileAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<OriginFileInfo> getByMd5(String md5Code) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<OriginFileInfo> getOriginFileById(String fileId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbrPlatformInfo> getEbrPlatformById(String EbrId) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<FileInfo> getFileByMd5(String md5Code) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<FileInfo> getFileInfoByFileId(String fileId) {
        return null;
    }

    @Override
    public ApiEntityResponse<FileInfo> saveFile(FileAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaByAreaCode(String areaCode) {
        return null;
    }

    @Override
    public ApiListResponse<RegionAreaInfo> getRegionAreaByAreaCodes(List<String> areaCodes) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbrBroadcastInfo> getEbrBroadcastByBsEbrId(String bsEbrId) {
        return null;
    }


    @Override
    public ApiResponse updatePlatform(PlatformUpdateRequest updateRequest) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updatePlatformBatch(List<PlatformUpdateRequest> updateRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> savePlatformBatch(List<PlatformAddRequest> addRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbrPlatformState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbrBroadcastState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        return null;
    }

    @Override
    public ApiListResponse<EbrPlatformInfo> findPlatformListByWhere(Map<String, Object> whereRequestMap) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> saveEbrPlatformBatch(List<PlatformAddRequest> saveRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbrPlatformBatch(List<PlatformUpdateRequest> updateRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbrInfo> getEbrCommonInfoById(String ebrId) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> saveEbrBroadcastBath(List<EbrBroadcastAddRequest> addRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updEbrBroadcastBath(List<EbrBroadcastUpdateRequest> updateRequestList) {
        return null;
    }

    @Override
    public ApiListResponse<EbrBroadcastInfo> findBroadcastListByWhere(Map<String, Object> whereRequestMap) {
        return null;
    }

    @Override
    public ApiEntityResponse<List<EbrTerminalInfo>> findListByTremIds(List<String> devTrmIds) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> addEbrTerminalBatch(List<EbrTerminalAddRequest> addRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbrTerminalBatch(List<EbrTerminalUpdateRequest> updateRequestList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbrTerminalState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        return null;
    }

    @Override
    public ApiListResponse findTerminalListByWhere(Map<String, Object> whereRequestMap) {
        return null;
    }

	@Override
	public ApiListResponse<EbrStationInfo> findStationListByWhere(Map<String, Object> whereRequestMap) {
		return null;
	}

	@Override
	public ApiListResponse<EbrAdapterInfo> findAdapterListByWhere(Map<String, Object> whereRequestMap) {
		return null;
	}

	@Override
	public ApiEntityResponse<Integer> updEbrStationBatch(List<StationUpdateRequest> updateRequestList) {
		return null;
	}

	@Override
	public ApiEntityResponse<Integer> updEbrAdapterBatch(List<AdapterUpdateRequest> updateRequestList) {
		return null;
	}

	@Override
	public ApiEntityResponse<List<EbrStationInfo>> findListByStationIds(List<String> stationIds) {
		return null;
	}

	@Override
	public ApiEntityResponse<Integer> addEbrStationBatch(List<StationAddRequest> addRequestList) {
		return null;
	}

	@Override
	public ApiEntityResponse<List<EbrAdapterInfo>> findListByAdapterIds(List<String> adapterIds) {
		return null;
	}

	@Override
	public ApiEntityResponse<Integer> addEbrAdapterBatch(List<AdapterAddRequest> addRequestList) {
		return null;
	}

	@Override
	public ApiEntityResponse<Integer> updateEbrAdapterState(List<EbrStateUpdateRequest> updateRequestList) {
		return null;
	}

	@Override
	public ApiEntityResponse<EbrStationInfo> getEbrStationInfoById(String stEbrId) {
		return null;
	}

	@Override
	public ApiEntityResponse<EbrAdapterInfo> getEbrAdapterInfoById(String stEbrId) {
		return null;
	}

	@Override
	public ApiEntityResponse<String> getChannelByEbrId(String ebrId) {
		return null;
	}

    @Override
    public ApiEntityResponse<FileLibraryInfo> getFileLibraryInfoByLibId(String libId) {
        return null;
    }

	@Override
	public ApiEntityResponse<Integer> countTerminalByCondition(TerminalConditionRequest terminalConditionRequest) {
		// TODO Auto-generated method stub
		return null;
	}
}
