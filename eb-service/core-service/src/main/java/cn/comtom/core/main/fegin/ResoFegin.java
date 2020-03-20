package cn.comtom.core.main.fegin;


import ch.qos.logback.classic.spi.PlatformInfo;
import cn.comtom.domain.reso.ebr.info.*;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.domain.reso.file.info.FileInfo;
import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.*;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Create By wujiang on 2018/11/16
 */
@FeignClient(value = "reso-service")
public interface ResoFegin {

    @RequestMapping(value = "/res/originFile/save",method = RequestMethod.POST)
     ApiEntityResponse<OriginFileInfo> save(@RequestBody OriginFileAddRequest request);

    @RequestMapping(value = "/res/originFile/getByMd5",method = RequestMethod.GET)
     ApiEntityResponse<OriginFileInfo> getByMd5(@RequestParam(name = "md5Code") String md5Code);

    @RequestMapping(value = "/res/ebr/platform/{ebrId}",method = RequestMethod.GET)
     ApiEntityResponse<EbrPlatformInfo> getEbrPlatformById(@PathVariable(value = "ebrId") String ebrId);

    @RequestMapping(value = "/res/file/getByMd5",method = RequestMethod.GET)
     ApiEntityResponse<FileInfo> getFileByMd5(@RequestParam(name = "md5Code") String md5Code);

    @RequestMapping(value = "/res/file/{fileId}",method = RequestMethod.GET)
     ApiEntityResponse<FileInfo> getFileInfoByFileId(@PathVariable(name = "fileId") String fileId);

    @RequestMapping(value = "/res/vFileLib/page",method = RequestMethod.GET)
    ApiPageResponse<VFileLibInfo> getFileLibViewByPage(@RequestParam Map<String, Object> map);

    @RequestMapping(value = "/res/file/page",method = RequestMethod.GET)
    ApiPageResponse<FileInfo> getFileInfoByPage(@RequestParam Map<String, Object> map);

    @DeleteMapping(value = "/res/file/delete/{fileId}")
    ApiResponse deleteFileInfoByFileId(@PathVariable(name = "fileId") String fileId);

    @PostMapping(value = "/res/file/deleteBatch")
    ApiEntityResponse<Integer> deleteFileInfoBatch(@RequestBody List<String> fileId);

    @PutMapping(value = "/res/file/update")
    ApiResponse updateFileInfo(@RequestBody FileUpdateRequest request);

    @PutMapping(value = "/res/file/updateBatch")
    ApiEntityResponse<Integer> updateFileInfoBatch(@RequestBody List<FileUpdateRequest> requests);

    @PutMapping(value = "/res/fileLibrary/update")
    ApiResponse updateFileLibraryInfo(@RequestBody FileLibraryUpdateRequest request);

    @PutMapping(value = "/res/fileLibrary/updateBatch")
    ApiEntityResponse<Integer> updateFileLibraryInfoBatch(@RequestBody List<FileLibraryUpdateRequest> requests);

    @DeleteMapping(value = "/res/fileLibrary/delete/{libId}")
    ApiResponse deleteFileLibraryInfo(@PathVariable(name = "libId") String libId);

    @DeleteMapping(value = "/res/fileLibrary/deleteThisAndSubAll/{libId}")
    ApiEntityResponse<Integer> deleteFileLibraryAll(@PathVariable(name = "libId") String libId);

    @PostMapping(value = "/res/fileLibrary/deleteBatch")
    ApiEntityResponse<Integer> deleteFileLibraryInfoBatch(@RequestBody List<String> libIds);

    @GetMapping(value = "/res/fileLibrary/{libId}")
    ApiEntityResponse<FileLibraryInfo> getFileLibraryInfoByLibId(@PathVariable(name = "libId") String libId);

    @GetMapping(value = "/res/fileLibrary/page")
    ApiPageResponse<FileLibraryInfo> getFileLibraryInfoByPage(@RequestParam Map<String, Object> map);

    @RequestMapping(value = "/res/file/save",method = RequestMethod.POST)
     ApiEntityResponse<FileInfo> saveFile(@RequestBody FileAddRequest request);

    @RequestMapping(value = "/res/region/area/{areaCode}",method = RequestMethod.GET)
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaByAreaCode(@PathVariable(name = "areaCode") String areaCode);

    @RequestMapping(value = "/res/region/area/getByAreaCodes",method = RequestMethod.GET)
    public ApiListResponse<RegionAreaInfo> getRegionAreaByAreaCodes(@RequestParam(name = "areaCodes") List<String> areaCodes);

    @RequestMapping(value = "/res/ebr/broadcast/{bsEbrId}",method = RequestMethod.GET)
    ApiEntityResponse<EbrBroadcastInfo> getEbrBroadcastByBsEbrId(@PathVariable(name = "bsEbrId") String bsEbrId);


    @RequestMapping(value = "/res/ebr/platform/findPlatformListByWhere",method = RequestMethod.GET)
    ApiListResponse<EbrPlatformInfo> findPlatformListByWhere(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/res/ebr/platform/updatePlatform",method = RequestMethod.PUT)
    ApiResponse updatePlatform(@RequestBody PlatformUpdateRequest updateRequest);

    @RequestMapping(value = "/res/ebr/platform/updatePlatformBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updatePlatformBatch(@RequestBody List<PlatformUpdateRequest> updateRequestList);

    @RequestMapping(value = "/res/ebr/platform/savePlatformBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> savePlatformBatch(@RequestBody List<PlatformAddRequest> addRequestList);

    @RequestMapping(value = "/res/ebr/platform/updateEbrPlatformState",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrPlatformState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList);

    @RequestMapping(value = "/res/ebr/broadcast/updateEbrBroadcastState",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrBroadcastState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList);

    @RequestMapping(value = "/res/ebr/platform/savePlatformBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbrPlatformBatch(@RequestBody List<PlatformAddRequest> saveRequestList);

    @RequestMapping(value = "/res/ebr/platform/save",method = RequestMethod.POST)
    ApiEntityResponse<EbrPlatformInfo> saveEbrPlatform(@RequestBody PlatformAddRequest request);

    @RequestMapping(value = "/res/ebr/platform/updatePlatformBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbrPlatformBatch(@RequestBody List<PlatformUpdateRequest> updateRequestList);

    @RequestMapping(value = "/res/originFile/{fileId}",method = RequestMethod.GET)
    ApiEntityResponse<OriginFileInfo> getOriginFileById(@PathVariable(name = "fileId") String fileId);

    @GetMapping(value = "/res/ebr/info/{ebrId}")
    ApiEntityResponse<EbrInfo> getEbrCommonInfoById(@PathVariable(name = "ebrId") String ebrId);

    @GetMapping(value = "/res/ebr/view/list")
    ApiListResponse<EbrInfo> getEbrInfoViewList(@RequestParam Map<String, Object> map);

    @RequestMapping(value = "/res/ebr/platform/{ebrId}",method = RequestMethod.GET)
    ApiEntityResponse<PlatformInfo> findPlatfomById(@PathVariable(name = "ebrId") String ebrId);

    @GetMapping(value = "/res/ebr/platform/page")
    ApiPageResponse<EbrPlatformInfo> getEbrPlatformInfoByPage(@RequestParam Map<String, Object> map);

    @GetMapping(value = "/res/ebr/view/page")
    ApiPageResponse<EbrInfo> getEbrInfoViewByPage(@RequestParam Map<String, Object> map);

    @GetMapping(value = "/res/ebr/terminal/{ebrId}")
    ApiEntityResponse<EbrTerminalInfo> getEbrTerminalInfoById(@PathVariable(name = "ebrId") String ebrId);

    @GetMapping(value = "/res/ebr/broadcast/page")
    ApiPageResponse<EbrBroadcastInfo> getEbrBroadcastInfoByPage(@RequestParam Map<String, Object> map);

    @GetMapping(value = "/res/ebr/terminal/page")
    ApiPageResponse<EbrTerminalInfo> getEbrTerminalInfoByPage(@RequestParam Map<String, Object> map);

    @PutMapping(value = "/res/ebr/broadcast/update")
    ApiEntityResponse<Integer> updateEbrBroadcastInfo(@RequestBody EbrBroadcastUpdateRequest request);

    @PostMapping(value = "/res/ebr/broadcast/save")
    ApiEntityResponse<EbrBroadcastInfo> saveEbrBroadcastInfo(@RequestBody EbrBroadcastAddRequest request);

    @PostMapping(value = "/res/ebr/broadcast/saveBatch")
    ApiEntityResponse<Integer> saveEbrBroadcastInfoBatch(@RequestBody List<EbrBroadcastAddRequest> requests);

    @DeleteMapping(value = "/res/ebr/broadcast/delete/{ebrId}")
    ApiResponse deleteEbrBroadcastInfo(@PathVariable(name = "ebrId") String ebrId);

    @PutMapping(value = "/res/ebr/terminal/update")
    ApiEntityResponse<Integer> updateEbrTerminalInfo(@RequestBody EbrTerminalUpdateRequest request);

    @PostMapping(value = "/res/ebr/terminal/save")
    ApiEntityResponse<EbrTerminalInfo> saveEbrTerminalInfo(@RequestBody EbrTerminalAddRequest request);

    @DeleteMapping(value = "/res/ebr/terminal/delete/{ebrId}")
    ApiResponse deleteEbrTerminalInfo(@PathVariable(name = "ebrId") String ebrId);

    @PostMapping(value = "/res/fileLibrary/save")
    ApiEntityResponse<FileLibraryInfo> saveFileLibraryInfo(FileLibraryAddRequest request);
    @GetMapping(value = "/res/ebr/view/gis/page")
    ApiPageResponse<EbrGisInfo> getEbrGisByPage(@RequestParam Map<String, Object> map);

    @GetMapping(value = "/res/ebr/view/gis/getEbrGisStatusCount")
    ApiEntityResponse<List<EbrstatusCount>> getEbrGisStatusCount(@RequestParam Map<String, Object> whereRequestMap);

    @GetMapping(value = "/res/ebr/view/infoSrc/page")
    ApiPageResponse<EbrInfo> getInfoSrcByPage(@RequestParam Map<String, Object> map);

    @DeleteMapping(value = "/res/ebr/platform/delete/{ebrId}")
    ApiResponse deletePlatformInfo(@PathVariable(name = "ebrId") String ebrId);

    @GetMapping(value = "/res/ebr/id/creator/create")
    ApiEntityResponse<EbrIdCreatorInfo> getEbrId(@RequestParam Map<String, Object> map);
    
    @RequestMapping(value = "/res/ebr/adapter/{ebrId}",method = RequestMethod.GET)
    ApiEntityResponse<EbrAdapterInfo> getEbrAdapterByEbrId(@PathVariable(name = "ebrId") String ebrId);
    
    @RequestMapping(value = "/res/ebr/station/{ebrId}",method = RequestMethod.GET)
    ApiEntityResponse<EbrStationInfo> getEbrStationByEbrId(@PathVariable(name = "ebrId") String ebrId);
    
	@RequestMapping(value = "/res/ebr/channel/{ebrId}",method = RequestMethod.GET)
	ApiEntityResponse<String> getChannelByEbrId(@PathVariable(name = "ebrId") String ebrId);
}
