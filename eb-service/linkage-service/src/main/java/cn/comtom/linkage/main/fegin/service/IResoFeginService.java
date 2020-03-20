package cn.comtom.linkage.main.fegin.service;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.request.FileAddRequest;
import cn.comtom.domain.reso.file.request.OriginFileAddRequest;
import cn.comtom.tools.response.ApiEntityResponse;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

public interface IResoFeginService {


    EbrPlatformInfo getEbrPlatformById(String psEbrId);

    public EbrBroadcastInfo getEbrBroadcastInfoById(String bsEbrId);

    FileInfo getFileInfoById(String fileId);

    OriginFileInfo getByMd5(String md5Code);

    OriginFileInfo getOriginFileById(String fileId);

    OriginFileInfo saveOriginFile(OriginFileAddRequest fileAddRequest);

    FileInfo getFileByMd5(String md5);

    FileInfo saveFile(FileAddRequest info);

    List<EbrPlatformInfo> findPlatformListByWhere(PlatformWhereRequest whereRequest);

    Boolean updatePlatform(PlatformUpdateRequest updateRequest);

    Integer updateEbrPlatformState(List<EbrStateUpdateRequest> onLineList);

    Integer updateEbrBroadcastState(List<EbrStateUpdateRequest> offLineList);

    Integer saveEbrPlatformBatch(List<PlatformAddRequest> saveRequestList);

    Integer updateEbrPlatformBatch(List<PlatformUpdateRequest> updateRequestList);

    EbrInfo getEbrCommonInfoById( String ebrId);

    Integer saveEbrBroadcastBath(List<EbrBroadcastAddRequest> addRequestList);

    Integer updEbrBroadcastBath(List<EbrBroadcastUpdateRequest> updateRequestList);

    List<EbrBroadcastInfo> findBroadcastListByWhere(PlatformWhereRequest whereRequest);

    List<EbrTerminalInfo> findListByTremIds(List<String> devTrmIds);

    Integer addEbrTerminalBatch(List<EbrTerminalAddRequest> addRequestList);

    Integer updateEbrTerminalBatch(List<EbrTerminalUpdateRequest> updateRequestList);

    Integer updateEbrTerminalState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList);

    List<EbrTerminalInfo> findTerminalListByWhere(TerminalWhereRequest whereRequest);

    List<EbrStationInfo> findStationListByWhere(StationWhereRequest whereRequest);

	List<EbrAdapterInfo> findAdapterListByWhere(AdapterWhereRequest whereRequest);

	Integer updEbrStationBatch(List<StationUpdateRequest> updateRequestList);

	Integer updEbrAdapterBatch(List<AdapterUpdateRequest> updateRequestList);

	List<EbrStationInfo> findListByStationIds(List<String> stationIds);

	Integer addEbrStationBatch(List<StationAddRequest> addRequestList);

	List<EbrAdapterInfo> findListByAdapterIds(List<String> adapterIds);

	Integer addEbrAdapterBatch(List<AdapterAddRequest> addRequestList);

	Integer updateEbrAdapterState(List<EbrStateUpdateRequest> updateRequestList);

	EbrStationInfo getEbrStationInfoById(String resoCode);

	EbrAdapterInfo getEbrAdapterInfoById(String resoCode);

	String getChannelByEbrId(String ebrId);

	FileLibraryInfo getFileLibraryInfoByLibId(String libId);

	Integer countTerminalByCondition(TerminalConditionRequest terminalConditionRequest);
}
