package cn.comtom.core.main.fegin.service.impl;

import cn.comtom.core.main.fegin.ResoFegin;
import cn.comtom.core.main.fegin.service.FeginBaseService;
import cn.comtom.core.main.fegin.service.IResoFeginService;
import cn.comtom.domain.reso.ebr.info.*;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.*;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
	public EbrAdapterInfo getEbrAdapterByEbrId(String ebrId) {
		return getData(resoFegin.getEbrAdapterByEbrId(ebrId));
	}

	@Override
	public EbrStationInfo getEbrStationByEbrId(String ebrId) {
		return getData(resoFegin.getEbrStationByEbrId(ebrId));
	}
	
	@Override
	public String getChannelByEbrId(String ebrId) {
		return getData(resoFegin.getChannelByEbrId(ebrId));
	}
}
