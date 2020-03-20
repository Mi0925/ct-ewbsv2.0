package cn.comtom.ebs.front.fegin.service.impl;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrManufacturerInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrManufactPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.FileLibraryAddRequest;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.domain.reso.file.request.FileLibraryUpdateRequest;
import cn.comtom.domain.reso.file.request.FilePageRequest;
import cn.comtom.domain.reso.file.request.FileUpdateRequest;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.ebs.front.fegin.ResoFegin;
import cn.comtom.ebs.front.fegin.service.FeginBaseService;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
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
    public OriginFileInfo getOriginFileById(String fileId) {
        return getData(resoFegin.getOriginFileById(fileId));
    }

    @Override
    public EbrInfo getEbrCommonInfoById(String ebrId) {
        return getData(resoFegin.getEbrCommonInfoById(ebrId));
    }

    @Override
    public List<EbrInfo> getEbrInfoViewList(EbrQueryRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return getDataList(resoFegin.getEbrInfoViewList(whereRequestMap));
    }

    @Override
    public EbrPlatformInfo findPlatfomById(String id) {
        return getData(resoFegin.getEbrPlatformById(id));
    }

    @Override
    public ApiPageResponse<EbrPlatformInfo> getEbrPlatformInfoByPage(EbrPlatformPageRequest pageRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(pageRequest);
        return resoFegin.getEbrPlatformInfoByPage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<EbrInfo> getEbrInfoViewByPage(EbrPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrInfoViewByPage(whereRequestMap);
    }

    @Override
    public EbrTerminalInfo getEbrTerminalInfoById(String ebrId) {
        return getData(resoFegin.getEbrTerminalInfoById(ebrId));
    }

    @Override
    public ApiPageResponse<EbrBroadcastInfo> getEbrBroadcastInfoByPage(BroadcastPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrBroadcastInfoByPage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<EbrTerminalInfo> getEbrTerminalInfoByPage(EbrTerminalPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrTerminalInfoByPage(whereRequestMap);
    }

    @Override
    public EbrPlatformInfo saveEbrPlatform(PlatformAddRequest request) {
        return getData(resoFegin.saveEbrPlatform(request));
    }

    @Override
    public EbrBroadcastInfo saveEbrBroadcastInfo(EbrBroadcastAddRequest request) {
        return getData(resoFegin.saveEbrBroadcastInfo(request));
    }

    @Override
    public Boolean updateEbrBroadcastInfo(EbrBroadcastUpdateRequest request) {
        return getBoolean(resoFegin.updateEbrBroadcastInfo(request));
    }

    @Override
    public EbrTerminalInfo saveEbrTerminalInfo(EbrTerminalAddRequest request) {
        return getData(resoFegin.saveEbrTerminalInfo(request));
    }

    @Override
    public Boolean deleteEbrTerminalInfo(String ebrId) {
        return getBoolean(resoFegin.deleteEbrTerminalInfo(ebrId));
    }

    @Override
    public Boolean deleteEbrBroadcastInfo(String ebrId) {
        return getBoolean(resoFegin.deleteEbrBroadcastInfo(ebrId));
    }

    @Override
    public Integer saveEbrBroadcastInfoBatch(List<EbrBroadcastAddRequest> requests) {
        return getData(resoFegin.saveEbrBroadcastInfoBatch(requests));
    }

    @Override
    public ApiPageResponse<EbrGisInfo> getEbrGisByPage(EbrGisPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrGisByPage(whereRequestMap);
    }

    @Override
    public List<EbrstatusCount> getEbrGisStatusCount(EbrGisPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return getData(resoFegin.getEbrGisStatusCount(whereRequestMap));
    }
    @Override
    public ApiPageResponse<FileInfo> getFileInfoByPage(FilePageRequest request) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getFileInfoByPage(map);
    }

    @Override
    public Boolean deleteFileInfoByFileId(String fileId) {
        return getBoolean(resoFegin.deleteFileInfoByFileId(fileId));
    }

    @Override
    public Boolean updateFileInfo(FileUpdateRequest request) {
        return getBoolean(resoFegin.updateFileInfo(request));
    }

    @Override
    public Boolean updateFileLibraryInfo(FileLibraryUpdateRequest request) {
        return getBoolean(resoFegin.updateFileLibraryInfo(request));
    }

    @Override
    public Boolean deleteFileLibraryInfo(String libId) {
        return getBoolean(resoFegin.deleteFileLibraryInfo(libId));
    }

    @Override
    public ApiPageResponse<FileLibraryInfo> getFileLibraryInfoByPage(FileLibraryPageRequest request) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getFileLibraryInfoByPage(map);
    }

    @Override
    public ApiPageResponse<VFileLibInfo> getFileLibViewByPage(VFileLibPageRequest request) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getFileLibViewByPage(map);
    }

    @Override
    public FileLibraryInfo getFileLibraryInfoByLibId(String libId) {
        return getData(resoFegin.getFileLibraryInfoByLibId(libId));
    }

    @Override
    public Integer deleteFileLibraryInfoBatch(List<String> libIds) {
        return getData(resoFegin.deleteFileLibraryInfoBatch(libIds));
    }

    @Override
    public Integer updateFileLibraryInfoBatch(List<FileLibraryUpdateRequest> requests) {
        return getData(resoFegin.updateFileLibraryInfoBatch(requests));
    }

    @Override
    public Integer updateFileInfoBatch(List<FileUpdateRequest> requests) {
        return getData(resoFegin.updateFileInfoBatch(requests));
    }

    @Override
    public Integer deleteFileInfoBatch(List<String> fileIds) {
        return getData(resoFegin.deleteFileInfoBatch(fileIds));
    }

    @Override
    public Integer deleteFileLibraryAll(String libId) {
        return getData(resoFegin.deleteFileLibraryAll(libId));
    }

    @Override
    public FileLibraryInfo saveFileLibraryInfo(FileLibraryAddRequest request) {
        return getData(resoFegin.saveFileLibraryInfo(request));
    }

    @Override
    public ApiPageResponse<EbrInfo> getInfoSrcByPage(EbrPageRequest request) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getInfoSrcByPage(map);
    }

    @Override
    public Boolean deleteEbrPlatformInfo(String ebrId) {
        return getBoolean(resoFegin.deletePlatformInfo(ebrId));
    }

    @Override
    public String getEbrId(EbrIdCreatorInfo creatorInfo) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(creatorInfo);
        EbrIdCreatorInfo ebrIdCreatorInfo = getData(resoFegin.getEbrId(map));
        return ebrIdCreatorInfo==null?null:ebrIdCreatorInfo.getEbrId();
    }

	@Override
	public EbrAdapterInfo getEbrAdapterInfoById(String ebrId) {
		return getData(resoFegin.getEbrAdapterInfoById(ebrId));
	}

	@Override
	public ApiPageResponse<EbrAdapterInfo> getEbrAdapterInfoByPage(EbrAdapterPageRequest request) {
		Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrAdapterInfoByPage(whereRequestMap);
	}

	@Override
	public EbrAdapterInfo saveEbrAdapterInfo(AdapterAddRequest request) {
		return getData(resoFegin.saveEbrAdapterInfo(request));
	}

	@Override
	public Boolean updateEbrAdapterInfo(AdapterUpdateRequest request) {
		return getBoolean(resoFegin.updateEbrAdapterInfo(request));
	}

	@Override
	public Boolean deleteEbrAdapterInfo(String ebrId) {
		return getBoolean(resoFegin.deleteEbrAdapterInfo(ebrId));
	}

	@Override
	public EbrStationInfo getEbrStationInfoById(String ebrId) {
		return getData(resoFegin.getEbrStationInfoById(ebrId));
	}

	@Override
	public ApiPageResponse<EbrStationInfo> getEbrStationInfoByPage(EbrStationPageRequest request) {
		Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrStationInfoByPage(whereRequestMap);
	}

	@Override
	public EbrStationInfo saveEbrStationInfo(StationAddRequest request) {
		return getData(resoFegin.saveEbrStationInfo(request));
	}

	@Override
	public Boolean updateEbrStationInfo(StationUpdateRequest request) {
		return getBoolean(resoFegin.updateEbrStationInfo(request));
	}

	@Override
	public Boolean deleteEbrStationInfo(String ebrId) {
		return getBoolean(resoFegin.deleteEbrStationInfo(ebrId));
	}
	
    @Override
    public List<EbrStationInfo> findStationListByWhere(StationWhereRequest whereRequest) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(whereRequest);
        return getDataList(resoFegin.findStationListByWhere(whereRequestMap));
    }

	@Override
	public EbrManufacturerInfo getEbrManufacturerById(String ebrId) {
		return getData(resoFegin.getEbrManufactInfoById(ebrId));
	}

	@Override
	public ApiPageResponse<EbrManufacturerInfo> getEbrManufacturerInfoByPage(@Valid EbrManufactPageRequest request) {
		Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return resoFegin.getEbrManufacturerInfoByPage(whereRequestMap);
	}

	@Override
	public EbrManufacturerInfo saveEbrManufacturerInfo(@Valid EbrManufacturerInfo request) {
		
		return getData(resoFegin.saveEbrManufacturerInfo(request));
	}

	@Override
	public Boolean updateEbrManufacturerInfo(@Valid EbrManufacturerInfo request) {
		return getBoolean(resoFegin.updateEbrManufacturerInfo(request));
	}

	@Override
	public Boolean deleteEbrManufacturerInfo(String ebrId) {
		
		return getBoolean(resoFegin.deleteEbrManufacturerInfo(ebrId));
	}

	@Override
	public Double getFailureRate(String resType) {
		return getData(resoFegin.getFailureRate(resType));
	}
}
