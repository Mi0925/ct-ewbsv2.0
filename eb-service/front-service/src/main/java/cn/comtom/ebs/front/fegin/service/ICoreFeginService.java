package cn.comtom.ebs.front.fegin.service;

import cn.comtom.domain.core.access.info.AccessInfo;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessInfoAddRequest;
import cn.comtom.domain.core.access.request.AccessRecordAddRequest;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
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
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.*;
import cn.comtom.domain.core.quartz.QuartzInfo;
import cn.comtom.domain.core.quartz.QuartzPageRequest;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeAddRequest;
import cn.comtom.domain.core.scheme.request.SchemeEbrAddBatchRequest;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;
import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface ICoreFeginService {

    public List<EbmDispatchInfo> getEbmDispatchByEbmId(String ebmId);

    public List<EbmInfo> getDispatchEbm(String ebmState);

    List<EbmAuxiliaryInfo> getEbmAuxiliaryInfoEbmId(String ebmId);

    EbmInfo getEbmInfoById(String ebmId);

    List<EbdFilesInfo> getEbdFilesInfoByEbdId(String ebdId);

    SchemeInfo getSchemeInfoById(String schemeId);

    AccessRecordInfo saveRecord(AccessRecordAddRequest addRequest);

    EbdInfo saveEbd(EbdAddRequest addRequest);

    EbdResponseInfo saveEbdResponse(EbdResponseAddRequest addRequest);

    EbmInfo getEbmById(String ebmId);

    List<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(String ebmId);

    Boolean updateEbm(EbmUpdateRequest updateRequest);

    List<EbmResInfo> getEbmResListByBrdItemId(String brdItemId);

    List<EbmResBsInfo> getEbmResBsByBrdItemId(String brdItemId);

    List<SchemeEbrInfo> findSchemeEbrListBySchemeId(String schemeId);

    EbmStateRequestInfo saveEbmStateRequest(EbmStateRequest ebmStateRequest);

    EbmBrdRecordInfo getEbmBrdByEbmIdAndResourceId(String ebmId, String ebdSrcEbrId);

    Boolean updateEbmBrdRecord(EbmBrdRecordUpdateRequest brdRecord);

    Boolean updateSchemeInfo(SchemeUpdateRequest request);

    void saveSchemeEbrBatch(SchemeEbrAddBatchRequest schemeEbrAddBatchRequest);

    void saveEbmDispatchBatch(EbmDispatchAddBatchRequest ebmDispatchAddBatchRequest);

    SchemeInfo saveScheme(SchemeAddRequest schemeAddRequest);

    AccessInfo saveAccessInfo(AccessInfoAddRequest request);

    DispatchFlowInfo saveDispatchFlow(DispatchFlowAddRequest request);

    void saveEbmAuxiliaryBatch(EbmAuxiliaryAddBatchRequest request);

    void auditEbm(EbmAuditRequest request);

    EbmInfo saveEbm(EbmAddRequest ebmEntity);

    void saveEbdFilesBatch(EbdFilesAddBatchRequest request);

    void updateEbmDispatch(EbmDispatchUpdateRequest request);

    EbmDispatchInfo getEbmDispatchByEbdId(String ebdId);

    Boolean updateEbd(EbdUpdateRequest updateRequest);

    OmdRequestInfo saveOmdRequest(OmdRequestAddRequest omdRequest);

    List<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(EbmBrdRecordWhereRequest whereRequest);

    EbmBrdItemUnitInfo findEbmBrdItemUnitByBrdItemIdAndUnitId(String brdItemId, String unitId);

    int saveEbmBrdRecordBatch(List<EbmBrdRecordInfo> addList);

    int updEbmBrdRecordBatch(List<EbmBrdRecordInfo> addList);

    int saveEbmBrdItemUnitBatch(List<EbmBrdItemUnitInfo> ebmBrdItemUnits);

    int saveEbmResBatch(List<EbmResInfo> ebmResList);

    int saveEbmResBsBatch(List<EbmResBsInfo> ebmResBsList);

    int updateEbmResBsBatch(List<EbmResBsInfo> updEbmResBsInfoList);

    boolean updateDispatchFlow(DispatchFlowUpdateRequest request);

    EbmResBsInfo getEbmResBsInfoByBrdItemIdAndBrdSysInfo(String brdItemId, String brdSysInfo);

    EbdInfo getEbdInfoByEbdId(String ebdId);

    List<EbdInfo> getEbdInfoByEbmId(String ebmId);

    List<EbmDispatchInfo> getEbmDispatchByPage(EbmDispatchPageRequest request);

    EbmBrdRecordInfo saveEbmBrdRecord(EbmBrdRecordRequest brdRecord);

    EbmDispatchInfo getEbmDispatchByMatchedEbdId(String matchEbdId);

    ApiPageResponse<AccessRecordInfo> findAccessRecordPage(AccessRecordPageRequest req);

    ApiPageResponse<EbdInfo> findEbdPage(EbdPageRequest req);

    ApiPageResponse<EbmInfo> getEbmInfoByPage(EbmPageRequest request);

    List<EbmStateBackInfo> findEbmStateBackListByEbmId(EbmStateBackQueryRequest request);

    ApiPageResponse<SchemePageInfo> getSchemePageInfo(SchemeQueryRequest request);

    List<EbmBrdRecordInfo> getEbmBrdRecordInfoPage(EbmBrdRecordPageRequest req);

    SchemeInfo getSchemeInfoByEbmId(String ebmId);

    DispatchFlowInfo getDispatchFlowInfoById( String flowId);

    EbmDispatchInfoInfo getEbmDispatchInfoInfoByEbmId(String ebmId);


    List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId);

    EbmDispatchInfoInfo getEbmDispatchInfoByEbmId(String ebmId);

    EbmDispatchInfo updateEbmDispatchInfo(EbmDispatchInfoUpdateRequest updateRequest);

    EbmDispatchInfoInfo saveEbmDispatchInfoInfo(EbmDispatchInfoAddRequest ebmDispatchInfoAddRequest);

    ProgramInfo saveProgramInfo(ProgramAddRequest request);

    Boolean updateProgramInfo( ProgramUpdateRequest request);

    Boolean deleteProgramInfoByProgramId( String programId);

    ProgramInfo getProgramInfoById( String programId);

    ApiPageResponse<ProgramInfo> getProgramInfoByPage(ProgramPageRequest request);

    void auditProgram(ProgramAuditRequest request);

    ApiPageResponse<ProgramUnitInfo> getProgramUnitInfoByPage(ProgramUnitPageRequest pageRequest);

    ProgramUnitInfo saveProgramUnitInfo(ProgramUnitAddRequest request);

    Boolean updateProgramUnitInfo(ProgramUnitUpdateRequest request);

    Boolean auditProgramUnitInfo(ProgramUnitAuditRequest request);

    ProgramUnitInfo getProgramUnitInfoById(String id);

    List<EbmInfo> createEbmsByProgramUnits(EbmCreateRequest request);

    Integer saveProgramUnitInfoBatch(List<ProgramUnitInfo> programUnitInfoList);

    Boolean updateProgramUnitInfoBatch(List<ProgramUnitUpdateRequest> requestList);

    SchemeInfo getSchemeInfoByProgramId(String programId);

    Boolean updateEbmDispatchBatch(List<EbmDispatchUpdateRequest> request);

    Boolean cancelProgramUnit(List<String> ids);

    Boolean deleteSchemeEbr(String schemeId);

	Boolean cancelEbmPlay(String ebmId);

	ApiPageResponse<QuartzInfo> getAllJobsByPage(QuartzPageRequest request);

	ApiEntityResponse<String> uploadFile(MultipartFile file);
}
