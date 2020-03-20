package cn.comtom.linkage.main.fegin.service;

import cn.comtom.domain.core.access.info.AccessInfo;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessInfoAddRequest;
import cn.comtom.domain.core.access.request.AccessRecordAddRequest;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.domain.core.ebd.info.EbdFilesInfo;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.info.EbdResponseInfo;
import cn.comtom.domain.core.ebd.request.EbdAddRequest;
import cn.comtom.domain.core.ebd.request.EbdFilesAddBatchRequest;
import cn.comtom.domain.core.ebd.request.EbdResponseAddRequest;
import cn.comtom.domain.core.ebd.request.EbdUpdateRequest;
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
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

public interface ICoreFeginService {

    public List<EbmDispatchInfo> getEbmDispatchByEbmId(String ebmId);

    public List<EbmInfo> getDispatchEbm(String ebmState);

    List<EbmAuxiliaryInfo> getEbmAuxiliaryInfoEbmId(String ebmId);

    EbmInfo getEbmInfoById(String ebmId);

    List<EbdFilesInfo> getEbdFilesInfoByEbdId(String ebdId);

    SchemeInfo getSchemeInfoById(String schemeId);

    SchemeInfo getSchemeInfoByEbmId(String ebmId);

    AccessRecordInfo saveRecord(AccessRecordAddRequest addRequest);

    EbdInfo saveEbd(EbdAddRequest addRequest);

    EbdResponseInfo saveEbdResponse(EbdResponseAddRequest addRequest);

    EbmInfo getEbmById(String ebmId);

    List<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(String ebmId);

    Boolean updateEbm(EbmUpdateRequest updateRequest);

    List<EbmResInfo> getEbmResListByBrdItemId(String brdItemId);

    List<EbmResBsInfo> getEbmResBsByBrdItemId(String brdItemId);

    List<SchemeEbrInfo> findSchemeEbrListBySchemeId(String schemeId);

  //  EbmStateRequestInfo saveEbmStateRequest(EbmStateRequest ebmStateRequest);

    EbmBrdRecordInfo getEbmBrdByEbmIdAndResourceId(String ebmId, String ebdSrcEbrId);

    Boolean updateEbmBrdRecord(EbmBrdRecordUpdateRequest brdRecord);

    void updateSchemeInfo(SchemeUpdateRequest request);

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

    EbmStateBackInfo saveEbmStateBack(EbmStateBackAddRequest addRequest);

    EbmPlanRefInfo getEbmPlanRefInfoByEbmId(String ebmId);

    EbmPlanRefInfo saveEbmPlanRefInfo(EbmPlanRefAddRequest request);

    EbmDispatchInfoInfo saveEbmDispatchInfoInfo(EbmDispatchInfoAddRequest request);

    EbmDispatchInfoInfo getEbmDispatchInfoInfoByEbmId(String ebmId);

    Boolean updateEbmDispatchInfoInfo(EbmDispatchInfoUpdateRequest request);

    DispatchFlowInfo getDispatchFlowById(String flowId);

    ProgramInfo getProgramInfoById(String programId);

	void callEbmNotice();

	void callEbrState();
	
	void callDVBNotice();

}
