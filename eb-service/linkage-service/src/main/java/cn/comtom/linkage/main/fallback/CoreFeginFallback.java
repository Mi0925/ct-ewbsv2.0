package cn.comtom.linkage.main.fallback;

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
import cn.comtom.linkage.main.fegin.CoreFegin;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.response.*;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Component
public class CoreFeginFallback implements CoreFegin {
    @Override
    public ApiEntityResponse<AccessRecordInfo> saveRecord(AccessRecordAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiResponse saveEbdFilesBatch(EbdFilesAddBatchRequest request) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<EbdFilesInfo> saveEbdFiles(EbdFilesAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiListResponse<EbdFilesInfo> getEbdFilesInfoByEbdId(String ebdId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmInfo> saveEbm(EbmAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<EbmInfo> getEbmInfoByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiResponse auditEbm(EbmAuditRequest request) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiResponse updateEbm(EbmUpdateRequest request) {
        return null;
    }

    @Override
    public ApiListResponse<EbmInfo> getDispatchEbm(String ebmState) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmAuxiliaryInfo> saveEbmAuxiliary(EbmAuxiliaryAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiResponse saveEbmAuxiliaryBatch(EbmAuxiliaryAddBatchRequest request) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiListResponse<EbmAuxiliaryInfo> getEbmAuxiliaryByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiEntityResponse<AccessInfo> saveAccessInfo(AccessInfoAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiResponse auditAccess(AccessAuditRequest request) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<AccessInfo> saveAccessArea(AccessAreaAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<AccessFileInfo> saveAccessFile(@Valid AccessFileAddRequest request) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<DispatchFlowInfo> saveDispatchFlow(@Valid DispatchFlowAddRequest request) {
        return null;
    }

    @Override
    public ApiResponse updateDispatchFlow(@Valid DispatchFlowUpdateRequest request) {
        return null;
    }

    @Override
    public ApiEntityResponse<DispatchFlowInfo> getDispatchFlowById(String flowId) {
        return null;
    }

    @Override
    public ApiEntityResponse<SchemeInfo> saveScheme(@Valid SchemeAddRequest request) {
        return null;
    }

    @Override
    public ApiEntityResponse<SchemeInfo> getSchemeInfoById(String schemeId) {
        return null;
    }

    @Override
    public ApiEntityResponse<SchemeInfo> getSchemeInfoByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiResponse updateSchemeInfo(@Valid SchemeUpdateRequest request) {
        return null;
    }

    @Override
    public ApiResponse saveSchemeEbrBatch(@Valid SchemeEbrAddBatchRequest request) {
        return null;
    }

    @Override
    public ApiResponse saveEbmDispatchBatch(@Valid EbmDispatchAddBatchRequest request) {
        return null;
    }

    @Override
    public ApiListResponse<EbmDispatchInfo> getEbmDispatchByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmDispatchInfo> getEbmDispatchByEbdId(String ebdId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmDispatchInfo> getEbmDispatchByMatchedEbdId(String matchedEbdId) {
        return null;
    }

    @Override
    public ApiPageResponse<EbmDispatchInfo> getEbmDispatchByPage(EbmDispatchPageRequest request) {
        return null;
    }

    @Override
    public ApiResponse updateEbmDispatch(EbmDispatchUpdateRequest request) {
        return null;
    }

    @Override
    public ApiListResponse<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiListResponse<SchemeEbrInfo> getSchemeEbrListBySchemeId(String schemeId) {
        return null;
    }

    @Override
    public ApiListResponse<EbmResInfo> getEbmResListByBrdItemId(String brdItemId) {
        return null;
    }

    @Override
    public ApiListResponse<EbmResBsInfo> getEbmResBsByBrdItemId(String brdItemId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbdResponseInfo> saveEbdRespons(EbdResponseAddRequest request) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbdInfo> saveEbd(EbdAddRequest request) {
        return null;
    }

   /* @Override
    public ApiEntityResponse<EbmStateRequestInfo> saveEbmStateRequest(EbmStateRequest ebmStateRequest) {
        return null;
    }*/

    @Override
    public ApiEntityResponse<EbmBrdRecordInfo> getEbmBrdByEbmIdAndResourceId(String ebmId, String ebdSrcEbrId) {
        return null;
    }
    @Override
    public ApiEntityResponse<OmdRequestInfo> saveOmdRequest(OmdRequestAddRequest omdRequest) {
        return null;
    }

    @Override
    public ApiListResponse<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(EbmBrdRecordWhereRequest whereRequest) {
        return null;
    }


    @Override
    public ApiResponse updateEbd(EbdUpdateRequest request) {
        return null;
    }

    @Override
    public ApiListResponse<EbdInfo> getEbdInfoByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbdInfo> getEbdInfoByEbdId(String ebdId) {
        return null;
    }


    @Override
    public ApiEntityResponse<EbmBrdItemUnitInfo> findEbmBrdItemUnitByBrdItemIdAndUnitId(String brdItemId, String unitId) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> saveEbmBrdItemUnitBatch(List<EbmBrdItemUnitInfo> ebmBrdItemUnitInfoList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> saveEbmBrdRecordBatch(List<EbmBrdRecordInfo> ebmBrdRecordInfoList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbmBrdRecordBatch(List<EbmBrdRecordInfo> ebmBrdRecordInfoList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> updateEbmResBsBatch(List<EbmResBsInfo> ebmResBsInfoList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> saveEbmResBsBatch(List<EbmResBsInfo> ebmResBsInfoList) {
        return null;
    }

    @Override
    public ApiEntityResponse<Integer> saveEbmResBatch(List<EbmResInfo> ebmResInfoList) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmResBsInfo> getEbmResBsInfoByBrdItemIdAndBrdSysInfo(String brdItemId, String brdSysInfo) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmBrdRecordInfo> saveEbmBrdRecord(EbmBrdRecordRequest brdRecord) {
        return null;
    }

    @Override
    public ApiResponse updateEbmBrdRecord(EbmBrdRecordUpdateRequest brdRecord) {
        return null;
    }

    @Override
    public ApiPageResponse<AccessRecordInfo> findAccessRecordPage(Map<String, Object> whereRequestMap) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmStateBackInfo> saveEbmStateBack(EbmStateBackAddRequest addRequest) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmPlanRefInfo> getEbmPlanRefInfoByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmPlanRefInfo> saveEbmPlanRefInfo(EbmPlanRefAddRequest request) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmDispatchInfoInfo> saveEbmDispatchInfoInfo(@Valid EbmDispatchInfoAddRequest request) {
        return null;
    }

    @Override
    public ApiResponse updateEbmDispatchInfoInfo(@Valid EbmDispatchInfoUpdateRequest request) {
        return null;
    }

    @Override
    public ApiEntityResponse<EbmDispatchInfoInfo> getEbmDispatchInfoInfoByEbmId(String ebmId) {
        return null;
    }

    @Override
    public ApiEntityResponse<ProgramInfo> getProgramInfoById(String programId) {
        return null;
    }

	@Override
	public void callEbmNotice() {
		
	}

	@Override
	public void callEbrState() {
		
	}

	@Override
	public void callDVBNotice() {
		
	}
}
