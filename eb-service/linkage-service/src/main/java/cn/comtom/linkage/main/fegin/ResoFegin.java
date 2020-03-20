package cn.comtom.linkage.main.fegin;


import java.util.List;
import java.util.Map;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;

/**
 * Create By wujiang on 2018/11/16
 */
@FeignClient(value = "reso-service")
public interface ResoFegin {

    @RequestMapping(value = "/res/originFile/save",method = RequestMethod.POST)
     ApiEntityResponse<OriginFileInfo> save(@RequestBody OriginFileAddRequest request);

    @RequestMapping(value = "/res/originFile/getByMd5",method = RequestMethod.GET)
     ApiEntityResponse<OriginFileInfo> getByMd5(@RequestParam(name = "md5Code") String md5Code);

    @RequestMapping(value = "/res/originFile/{fileId}",method = RequestMethod.GET)
    ApiEntityResponse<OriginFileInfo> getOriginFileById(@PathVariable(name = "fileId") String fileId);

    @RequestMapping(value = "/res/ebr/platform/{ebrId}",method = RequestMethod.GET)
     ApiEntityResponse<EbrPlatformInfo> getEbrPlatformById(@PathVariable(value="ebrId") String ebrId);

    @RequestMapping(value = "/res/file/getByMd5",method = RequestMethod.GET)
     ApiEntityResponse<FileInfo> getFileByMd5(@RequestParam(name = "md5Code") String md5Code);

    @RequestMapping(value = "/res/file/{fileId}",method = RequestMethod.GET)
     ApiEntityResponse<FileInfo> getFileInfoByFileId(@PathVariable(name = "fileId") String fileId);

    @RequestMapping(value = "/res/file/save",method = RequestMethod.POST)
     ApiEntityResponse<FileInfo> saveFile(@RequestBody FileAddRequest request);

    @RequestMapping(value = "/res/region/area/{areaCode}",method = RequestMethod.GET)
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaByAreaCode(@PathVariable(name = "areaCode") String areaCode);

    @RequestMapping(value = "/res/region/area/getByAreaCodes",method = RequestMethod.GET)
    public ApiListResponse<RegionAreaInfo> getRegionAreaByAreaCodes(@RequestParam(name = "areaCodes") List<String> areaCodes);

    @RequestMapping(value = "/res/ebr/broadcast/{bsEbrId}",method = RequestMethod.GET)
    ApiEntityResponse<EbrBroadcastInfo> getEbrBroadcastByBsEbrId(@PathVariable(name = "bsEbrId") String bsEbrId);


    @RequestMapping(value = "/res/ebr/platform/findPlatformListByWhere",method = RequestMethod.GET)
    ApiListResponse<EbrPlatformInfo> findPlatformListByWhere(@RequestParam Map<String,Object> whereRequestMap);

    @RequestMapping(value = "/res/ebr/platform/updatePlatform",method = RequestMethod.PUT)
    ApiResponse updatePlatform(@RequestBody  PlatformUpdateRequest updateRequest);

    @RequestMapping(value = "/res/ebr/platform/updatePlatformBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updatePlatformBatch(@RequestBody  List<PlatformUpdateRequest> updateRequestList);

    @RequestMapping(value = "/res/ebr/platform/savePlatformBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> savePlatformBatch(@RequestBody  List<PlatformAddRequest> addRequestList);

    @RequestMapping(value = "/res/ebr/platform/updateEbrPlatformState",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrPlatformState(@RequestBody  List<EbrStateUpdateRequest> ebrStateUpdateRequestList);

    @RequestMapping(value = "/res/ebr/broadcast/updateEbrBroadcastState",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrBroadcastState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList);

    @RequestMapping(value = "/res/ebr/platform/savePlatformBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbrPlatformBatch(@RequestBody List<PlatformAddRequest> saveRequestList);

    @RequestMapping(value = "/res/ebr/platform/updatePlatformBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrPlatformBatch(@RequestBody List<PlatformUpdateRequest> updateRequestList);

    @GetMapping(value = "/res/ebr/info/{ebrId}")
    ApiEntityResponse<EbrInfo> getEbrCommonInfoById(@PathVariable(name = "ebrId") String ebrId);

    @RequestMapping(value = "/res/ebr/broadcast/saveBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbrBroadcastBath(@RequestBody List<EbrBroadcastAddRequest> addRequestList);

    @RequestMapping(value = "/res/ebr/broadcast/updateEbrBroadcastBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updEbrBroadcastBath(@RequestBody List<EbrBroadcastUpdateRequest> updateRequestList);

    @RequestMapping(value = "/res/ebr/broadcast/findBroadcastListByWhere",method = RequestMethod.GET)
    ApiListResponse<EbrBroadcastInfo> findBroadcastListByWhere(@RequestParam Map<String,Object> whereRequestMap);

    @RequestMapping(value = "/res/ebr/terminal/findListByTremIds",method = RequestMethod.POST)
    ApiEntityResponse<List<EbrTerminalInfo>> findListByTremIds(@RequestBody List<String> devTrmIds);

    @RequestMapping(value = "/res/ebr/terminal/addEbrTerminalBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> addEbrTerminalBatch(@RequestBody List<EbrTerminalAddRequest> addRequestList);

    @RequestMapping(value = "/res/ebr/terminal/updateTerminalBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrTerminalBatch(@RequestBody List<EbrTerminalUpdateRequest> updateRequestList);

    @RequestMapping(value = "/res/ebr/terminal/updateEbrTerminalState",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrTerminalState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList);

    @RequestMapping(value = "/res/ebr/terminal/findTerminalListByWhere",method = RequestMethod.GET)
    ApiListResponse<EbrTerminalInfo> findTerminalListByWhere(@RequestParam  Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/res/ebr/station/findStationListByWhere",method = RequestMethod.GET)
    ApiListResponse<EbrStationInfo> findStationListByWhere(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/res/ebr/adapter/findAdapterListByWhere",method = RequestMethod.GET)
    ApiListResponse<EbrAdapterInfo> findAdapterListByWhere(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/res/ebr/station/updateStationBatch",method = RequestMethod.PUT)
	ApiEntityResponse<Integer> updEbrStationBatch(@RequestBody List<StationUpdateRequest> updateRequestList);

	@RequestMapping(value = "/res/ebr/adapter/updateAdapterBatch",method = RequestMethod.PUT)
	ApiEntityResponse<Integer> updEbrAdapterBatch(@RequestBody List<AdapterUpdateRequest> updateRequestList);

	@RequestMapping(value = "/res/ebr/station/findListByStationIds",method = RequestMethod.POST)
	ApiEntityResponse<List<EbrStationInfo>> findListByStationIds(@RequestBody List<String> stationIds);

	@RequestMapping(value = "/res/ebr/station/addEbrStationBatch",method = RequestMethod.POST)
	ApiEntityResponse<Integer> addEbrStationBatch(@RequestBody List<StationAddRequest> addRequestList);

	@RequestMapping(value = "/res/ebr/adapter/findListByAdapterIds",method = RequestMethod.POST)
	ApiEntityResponse<List<EbrAdapterInfo>> findListByAdapterIds(@RequestBody List<String> adapterIds);

	@RequestMapping(value = "/res/ebr/adapter/addEbrAdapterBatch",method = RequestMethod.POST)
	ApiEntityResponse<Integer> addEbrAdapterBatch(@RequestBody List<AdapterAddRequest> addRequestList);

	@RequestMapping(value = "/res/ebr/adapter/updateEbrAdapterState",method = RequestMethod.PUT)
	ApiEntityResponse<Integer> updateEbrAdapterState(List<EbrStateUpdateRequest> updateRequestList);

	@RequestMapping(value = "/res/ebr/station/{stEbrId}",method = RequestMethod.GET)
	ApiEntityResponse<EbrStationInfo> getEbrStationInfoById(@PathVariable(name = "stEbrId") String stEbrId);

	@RequestMapping(value = "/res/ebr/adapter/{asEbrId}",method = RequestMethod.GET)
	ApiEntityResponse<EbrAdapterInfo> getEbrAdapterInfoById(@PathVariable(name = "asEbrId") String stEbrId);

	@RequestMapping(value = "/res/ebr/channel/{ebrId}",method = RequestMethod.GET)
	ApiEntityResponse<String> getChannelByEbrId(@PathVariable(name = "ebrId") String ebrId);

    @GetMapping(value = "/res/fileLibrary/{libId}")
    ApiEntityResponse<FileLibraryInfo> getFileLibraryInfoByLibId(@PathVariable(name = "libId") String libId);
    
    @RequestMapping(value = "/res/ebr/terminal/countTerminalByCondition", method = RequestMethod.POST)
	ApiEntityResponse<Integer> countTerminalByCondition(@RequestBody TerminalConditionRequest terminalConditionRequest);
}
