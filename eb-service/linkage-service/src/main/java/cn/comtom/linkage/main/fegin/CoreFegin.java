package cn.comtom.linkage.main.fegin;

import cn.comtom.domain.core.access.info.AccessFileInfo;
import cn.comtom.domain.core.access.info.AccessInfo;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.*;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.info.EbdResponseInfo;
import cn.comtom.domain.core.ebd.request.*;
import cn.comtom.domain.core.ebm.info.*;
import cn.comtom.domain.core.ebm.request.*;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.flow.request.DispatchFlowAddRequest;
import cn.comtom.domain.core.flow.request.DispatchFlowUpdateRequest;
import cn.comtom.domain.core.omd.info.OmdRequestInfo;
import cn.comtom.domain.core.omd.request.OmdRequestAddRequest;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.request.SchemeAddRequest;
import cn.comtom.domain.core.scheme.request.SchemeEbrAddBatchRequest;
import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Create By wujiang on 2018/11/17
 */
@FeignClient(value = "core-service")
public interface CoreFegin {

    @RequestMapping(value = "/core/access/record/save",method = RequestMethod.POST)
    ApiEntityResponse<AccessRecordInfo> saveRecord(@RequestBody AccessRecordAddRequest request);

    @RequestMapping(value = "/core/ebd/files/saveList",method = RequestMethod.POST)
    ApiResponse saveEbdFilesBatch(@RequestBody EbdFilesAddBatchRequest request);

    @RequestMapping(value = "/core/ebd/files/save",method = RequestMethod.POST)
    ApiEntityResponse<EbdFilesInfo> saveEbdFiles(@RequestBody EbdFilesAddRequest request);

    @RequestMapping(value = "/core/ebd/files/getByEbdId",method = RequestMethod.GET)
    ApiListResponse<EbdFilesInfo> getEbdFilesInfoByEbdId(@RequestParam(name = "ebdId") String ebdId);

    @RequestMapping(value = "/core/ebd/response/save",method = RequestMethod.POST)
    ApiEntityResponse<EbdResponseInfo> saveEbdRespons(@RequestBody EbdResponseAddRequest request);

    @RequestMapping(value = "/core/ebd/save",method = RequestMethod.POST)
    ApiEntityResponse<EbdInfo> saveEbd(@RequestBody EbdAddRequest request);

    @RequestMapping(value = "/core/ebd/update",method = RequestMethod.PUT)
    ApiResponse updateEbd(@RequestBody EbdUpdateRequest request);

    @RequestMapping(value = "/core/ebd/getByEbmId",method = RequestMethod.GET)
    ApiListResponse<EbdInfo> getEbdInfoByEbmId(@RequestParam(name = "ebmId") String ebmId);

    @RequestMapping(value = "/core/ebd/{ebdId}",method = RequestMethod.GET)
    ApiEntityResponse<EbdInfo> getEbdInfoByEbdId(@PathVariable(name = "ebdId") String ebdId);

    @RequestMapping(value = "/core/ebm/save",method = RequestMethod.POST)
    ApiEntityResponse<EbmInfo> saveEbm(@RequestBody EbmAddRequest request);

    @RequestMapping(value = "/core/ebm/{ebmId}",method = RequestMethod.GET)
    ApiEntityResponse<EbmInfo> getEbmInfoByEbmId(@PathVariable(name = "ebmId") String ebmId);

    @RequestMapping(value = "/core/ebm/audit",method = RequestMethod.PUT)
    ApiResponse auditEbm(@RequestBody EbmAuditRequest request);

    @RequestMapping(value = "/core/ebm/update",method = RequestMethod.PUT)
    ApiResponse updateEbm(@RequestBody EbmUpdateRequest request);

    @RequestMapping(value = "/core/ebm/getDispatchEbm",method = RequestMethod.GET)
    ApiListResponse<EbmInfo> getDispatchEbm(@RequestParam(name="ebmState") String ebmState);

    @RequestMapping(value = "/core/ebm/auxiliary/save",method = RequestMethod.POST)
    ApiEntityResponse<EbmAuxiliaryInfo> saveEbmAuxiliary(@RequestBody EbmAuxiliaryAddRequest request);

    @RequestMapping(value = "/core/ebm/auxiliary/saveBatch",method = RequestMethod.POST)
    ApiResponse saveEbmAuxiliaryBatch(@RequestBody EbmAuxiliaryAddBatchRequest request);

    @RequestMapping(value = "/core/ebm/auxiliary/getByEbmId",method = RequestMethod.GET)
    ApiListResponse<EbmAuxiliaryInfo> getEbmAuxiliaryByEbmId(@RequestParam(name = "ebmId") String ebmId);

    @RequestMapping(value = "/core/access/info/save",method = RequestMethod.POST)
    ApiEntityResponse<AccessInfo> saveAccessInfo(@RequestBody AccessInfoAddRequest request);

    @RequestMapping(value = "/core/access/info/audit",method = RequestMethod.PUT)
    ApiResponse auditAccess(@RequestBody AccessAuditRequest request);

    @RequestMapping(value = "/core/access/area/save",method = RequestMethod.POST)
    ApiEntityResponse<AccessInfo> saveAccessArea(@RequestBody AccessAreaAddRequest request);

    @RequestMapping(value = "/core/access/file/save",method = RequestMethod.POST)
    ApiEntityResponse<AccessFileInfo> saveAccessFile(@RequestBody @Valid AccessFileAddRequest request);

    @RequestMapping(value = "/core/flow/save",method = RequestMethod.POST)
    ApiEntityResponse<DispatchFlowInfo> saveDispatchFlow(@RequestBody @Valid DispatchFlowAddRequest request);

    @RequestMapping(value = "/core/flow/update",method = RequestMethod.PUT)
    ApiResponse updateDispatchFlow(@RequestBody @Valid DispatchFlowUpdateRequest request);

    @RequestMapping(value = "/core/flow/{flowId}",method = RequestMethod.GET)
    ApiEntityResponse<DispatchFlowInfo> getDispatchFlowById(@PathVariable(name = "flowId") String flowId);

    @RequestMapping(value = "/core/scheme/save",method = RequestMethod.POST)
    ApiEntityResponse<SchemeInfo> saveScheme(@RequestBody @Valid SchemeAddRequest request);

    @RequestMapping(value = "/core/scheme/{schemeId}",method = RequestMethod.GET)
    ApiEntityResponse<SchemeInfo> getSchemeInfoById(@PathVariable(name = "schemeId") String schemeId);

    @RequestMapping(value = "/core/scheme/getByEbmId",method = RequestMethod.GET)
    ApiEntityResponse<SchemeInfo> getSchemeInfoByEbmId(@RequestParam(name = "ebmId") String ebmId);

    @RequestMapping(value = "/core/scheme/update",method = RequestMethod.PUT)
    ApiResponse updateSchemeInfo(@RequestBody @Valid SchemeUpdateRequest request);

    @RequestMapping(value = "/core/scheme/ebr/saveBatch",method = RequestMethod.POST)
    ApiResponse saveSchemeEbrBatch(@RequestBody @Valid SchemeEbrAddBatchRequest request);

    @RequestMapping(value = "/core/ebm/dispatch/saveBatch",method = RequestMethod.POST)
    ApiResponse saveEbmDispatchBatch(@RequestBody @Valid EbmDispatchAddBatchRequest request);

    @RequestMapping(value = "/core/ebm/dispatch/getByEbmId",method = RequestMethod.GET)
    ApiListResponse<EbmDispatchInfo> getEbmDispatchByEbmId(@RequestParam(name = "ebmId") String ebmId);

    @RequestMapping(value = "/core/ebm/dispatch/getOneByEbdId",method = RequestMethod.GET)
    ApiEntityResponse<EbmDispatchInfo> getEbmDispatchByEbdId(@RequestParam(name = "ebdId") String ebdId);

    @RequestMapping(value = "/core/ebm/dispatch/getOneByMatchedEbdId",method = RequestMethod.GET)
    ApiEntityResponse<EbmDispatchInfo> getEbmDispatchByMatchedEbdId(@RequestParam(name = "matchedEbdId") String matchedEbdId);

    @RequestMapping(value = "/core/ebm/dispatch/page",method = RequestMethod.POST)
    ApiPageResponse<EbmDispatchInfo> getEbmDispatchByPage(@RequestBody EbmDispatchPageRequest request);

    @RequestMapping(value = "/core/ebm/dispatch/update",method = RequestMethod.PUT)
    ApiResponse updateEbmDispatch(@RequestBody EbmDispatchUpdateRequest request);

    @RequestMapping(value = "/core/ebmBrdRecord/getEbmBrdRecordListByEbmId",method = RequestMethod.GET)
    ApiListResponse<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(@RequestParam(name = "ebmId")  String ebmId);

    @RequestMapping(value = "/core/scheme/ebr/getSchemeEbrListBySchemeId",method = RequestMethod.GET)
    ApiListResponse<SchemeEbrInfo> getSchemeEbrListBySchemeId(@RequestParam(value = "schemeId") String schemeId);

    @RequestMapping(value = "/core/ebmRes/getEbmResListByBrdItemId",method = RequestMethod.GET)
    ApiListResponse<EbmResInfo> getEbmResListByBrdItemId(@RequestParam(name = "brdItemId") String brdItemId);

    @RequestMapping(value = "/core/ebmResBs/getEbmResBsByBrdItemId",method = RequestMethod.GET)
    ApiListResponse<EbmResBsInfo> getEbmResBsByBrdItemId(@RequestParam(name = "brdItemId") String brdItemId);

  /*  @RequestMapping(value = "/core/ebmStateRequest/saveEbmStateRequest",method = RequestMethod.POST)
    ApiEntityResponse<EbmStateRequestInfo> saveEbmStateRequest(@RequestBody EbmStateRequest ebmStateRequest);*/

    @RequestMapping(value = "/core/ebmBrdRecord/getEbmBrdByEbmIdAndResourceId",method = RequestMethod.GET)
    ApiEntityResponse<EbmBrdRecordInfo> getEbmBrdByEbmIdAndResourceId(@RequestParam(value = "ebmId",required=false) String ebmId,@RequestParam(value = "ebdSrcEbrId",required=false) String ebdSrcEbrId);

    @RequestMapping(value = "/core/ebmBrdRecord/update",method = RequestMethod.PUT)
    ApiResponse updateEbmBrdRecord(@RequestBody EbmBrdRecordUpdateRequest brdRecord);

    @RequestMapping(value = "/core/omdRequest/save",method = RequestMethod.POST)
    ApiEntityResponse<OmdRequestInfo> saveOmdRequest(@RequestBody OmdRequestAddRequest omdRequest);

    @RequestMapping(value = "/core/ebmBrdRecord/findEbmBrdRecordListByWhere",method = RequestMethod.POST)
    ApiListResponse<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(@RequestBody EbmBrdRecordWhereRequest request);

    @RequestMapping(value = "/core/ebmBrdItemUnit/findEbmBrdItemUnitByBrdItemIdAndUnitId",method = RequestMethod.GET)
    ApiEntityResponse<EbmBrdItemUnitInfo> findEbmBrdItemUnitByBrdItemIdAndUnitId(@RequestParam(name = "brdItemId") String brdItemId, @RequestParam(name = "unitId") String unitId);

    @RequestMapping(value = "/core/ebmBrd/itemUnit/saveBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbmBrdItemUnitBatch(@RequestBody List<EbmBrdItemUnitInfo> ebmBrdItemUnitInfoList);

    @RequestMapping(value = "/core/ebmBrdRecord/saveBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbmBrdRecordBatch(@RequestBody List<EbmBrdRecordInfo> ebmBrdRecordInfoList);

    @RequestMapping(value = "/core/ebmBrdRecord/updateBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbmBrdRecordBatch(@RequestBody List<EbmBrdRecordInfo> ebmBrdRecordInfoList);

    @RequestMapping(value = "/core/ebmResBs/updateBatch",method = RequestMethod.PUT)
    ApiEntityResponse<Integer> updateEbmResBsBatch(@RequestBody List<EbmResBsInfo> ebmResBsInfoList);

    @RequestMapping(value = "/core/ebmResBs/saveBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbmResBsBatch(@RequestBody List<EbmResBsInfo> ebmResBsInfoList);

    @RequestMapping(value = "/core/ebmRes/saveBatch",method = RequestMethod.POST)
    ApiEntityResponse<Integer> saveEbmResBatch(@RequestBody List<EbmResInfo> ebmResInfoList);

    @RequestMapping(value = "/core/ebmResBs/getByBrdItemIdIdAndSysInfo",method = RequestMethod.GET)
    ApiEntityResponse<EbmResBsInfo> getEbmResBsInfoByBrdItemIdAndBrdSysInfo(@RequestParam(name = "brdItemId") String brdItemId, @RequestParam(name = "brdSysInfo") String brdSysInfo);

    @RequestMapping(value = "/core/ebmBrdRecord/save",method = RequestMethod.POST)
    ApiEntityResponse<EbmBrdRecordInfo> saveEbmBrdRecord(@RequestBody EbmBrdRecordRequest brdRecord);

    @RequestMapping(value = "/core/access/record/page",method = RequestMethod.GET)
    ApiPageResponse<AccessRecordInfo> findAccessRecordPage(@RequestParam Map<String,Object> whereRequestMap);

    @RequestMapping(value = "/core/ebmStateBack/save",method = RequestMethod.POST)
    ApiEntityResponse<EbmStateBackInfo> saveEbmStateBack(@RequestBody EbmStateBackAddRequest addRequest);

    @RequestMapping(value = "/core/ebm/plan/getByEbmId",method = RequestMethod.GET)
    ApiEntityResponse<EbmPlanRefInfo> getEbmPlanRefInfoByEbmId(@RequestParam(name ="ebmId") String ebmId);

    @RequestMapping(value = "/core/ebm/plan/save",method = RequestMethod.POST)
    ApiEntityResponse<EbmPlanRefInfo> saveEbmPlanRefInfo(@RequestBody EbmPlanRefAddRequest request);

    @RequestMapping(value = "/core/ebmDispatchInfo/save",method = RequestMethod.POST)
    ApiEntityResponse<EbmDispatchInfoInfo> saveEbmDispatchInfoInfo(@RequestBody @Valid EbmDispatchInfoAddRequest request);

    @RequestMapping(value = "/core/ebmDispatchInfo/update",method = RequestMethod.PUT)
    ApiResponse updateEbmDispatchInfoInfo(@RequestBody @Valid EbmDispatchInfoUpdateRequest request);

    @GetMapping(value = "/core/ebmDispatchInfo/getByEbmId")
    ApiEntityResponse<EbmDispatchInfoInfo> getEbmDispatchInfoInfoByEbmId(@RequestParam(name = "ebmId") String ebmId);

    @GetMapping(value = "/core/program/{programId}")
    ApiEntityResponse<ProgramInfo> getProgramInfoById(@PathVariable(name = "programId") String programId);
    
    @GetMapping(value = "/core/svs/ebmNotice")
	void callEbmNotice();
    
    @GetMapping(value = "/core/svs/ebrNotice")
	void callEbrState();
    
    @GetMapping(value = "/core/svs/DVBNotice")
	void callDVBNotice();
    
}

