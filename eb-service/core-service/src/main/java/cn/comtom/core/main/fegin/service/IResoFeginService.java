package cn.comtom.core.main.fegin.service;

import cn.comtom.domain.reso.ebr.info.*;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.*;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface IResoFeginService {


    EbrPlatformInfo getEbrPlatformById(String psEbrId);

    public EbrBroadcastInfo getEbrBroadcastInfoById(String bsEbrId);

    FileInfo getFileInfoById(String fileId);

    OriginFileInfo getByMd5(String md5Code);

    OriginFileInfo saveOriginFile(OriginFileAddRequest fileAddRequest);

    FileInfo getFileByMd5(String md5);

    FileInfo saveFile(FileAddRequest info);

    List<EbrPlatformInfo> findPlatformListByWhere(PlatformWhereRequest whereRequest);

    Boolean updatePlatform(PlatformUpdateRequest updateRequest);

    Integer updateEbrPlatformState(List<EbrStateUpdateRequest> onLineList);

    Integer updateEbrBroadcastState(List<EbrStateUpdateRequest> offLineList);

    Integer saveEbrPlatformBatch(List<PlatformAddRequest> saveRequestList);

    Integer updateEbrPlatformBatch(List<PlatformUpdateRequest> updateRequestList);

    OriginFileInfo getOriginFileById(String fileId);

    EbrInfo getEbrCommonInfoById(String ebrId);

    List<EbrInfo> getEbrInfoViewList(EbrQueryRequest request);


    EbrPlatformInfo findPlatfomById(String id);

    ApiPageResponse<EbrPlatformInfo> getEbrPlatformInfoByPage(EbrPlatformPageRequest pageRequest);

    ApiPageResponse<EbrInfo> getEbrInfoViewByPage(EbrPageRequest request);

    EbrTerminalInfo getEbrTerminalInfoById(String ebrId);

    ApiPageResponse<EbrBroadcastInfo> getEbrBroadcastInfoByPage(BroadcastPageRequest request);

    ApiPageResponse<EbrTerminalInfo> getEbrTerminalInfoByPage(EbrTerminalPageRequest request);

    EbrPlatformInfo saveEbrPlatform(PlatformAddRequest request);

    EbrBroadcastInfo saveEbrBroadcastInfo(EbrBroadcastAddRequest request);

    Boolean updateEbrBroadcastInfo(EbrBroadcastUpdateRequest request);

    EbrTerminalInfo saveEbrTerminalInfo(EbrTerminalAddRequest request);

    Boolean deleteEbrTerminalInfo(String ebrId);

    Boolean deleteEbrBroadcastInfo(String ebrId);

    Integer saveEbrBroadcastInfoBatch(List<EbrBroadcastAddRequest> requests);

    ApiPageResponse<EbrGisInfo> getEbrGisByPage(EbrGisPageRequest request);

    List<EbrstatusCount> getEbrGisStatusCount(EbrGisPageRequest request);
    ApiPageResponse<FileInfo> getFileInfoByPage(FilePageRequest request);

    Boolean deleteFileInfoByFileId(String fileId);

    Boolean updateFileInfo(FileUpdateRequest request);

    Boolean updateFileLibraryInfo(FileLibraryUpdateRequest request);

    Boolean deleteFileLibraryInfo(String libId);

    ApiPageResponse<FileLibraryInfo> getFileLibraryInfoByPage(FileLibraryPageRequest request);

    ApiPageResponse<VFileLibInfo> getFileLibViewByPage(VFileLibPageRequest request);

    FileLibraryInfo getFileLibraryInfoByLibId(String libId);

    Integer deleteFileLibraryInfoBatch(List<String> libIds);

    Integer updateFileLibraryInfoBatch(List<FileLibraryUpdateRequest> requests);

    Integer updateFileInfoBatch(List<FileUpdateRequest> requests);

    Integer deleteFileInfoBatch(List<String> fileId);

    Integer deleteFileLibraryAll(String libId);

    FileLibraryInfo saveFileLibraryInfo(FileLibraryAddRequest request);

    ApiPageResponse<EbrInfo> getInfoSrcByPage(EbrPageRequest request);

    Boolean deleteEbrPlatformInfo(String ebrId);

    String getEbrId(EbrIdCreatorInfo creatorInfo);
    
    EbrAdapterInfo getEbrAdapterByEbrId(String ebrId);
    
    EbrStationInfo getEbrStationByEbrId(String ebrId);
    
	String getChannelByEbrId(String ebrId);
}
