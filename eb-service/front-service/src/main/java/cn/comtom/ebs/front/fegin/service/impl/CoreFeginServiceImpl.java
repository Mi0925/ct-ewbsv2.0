package cn.comtom.ebs.front.fegin.service.impl;

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
import cn.comtom.ebs.front.fegin.CoreFegin;
import cn.comtom.ebs.front.fegin.service.FeginBaseService;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CoreFeginServiceImpl extends FeginBaseService implements ICoreFeginService {

    @Autowired
    private CoreFegin coreFegin;


    @Override
    public List<EbmDispatchInfo> getEbmDispatchByEbmId(String ebmId) {
        ApiListResponse<EbmDispatchInfo> response = coreFegin.getEbmDispatchByEbmId(ebmId);
        return getDataList(response);
    }

    @Override
    public List<EbmInfo> getDispatchEbm(String ebmState) {
        return getDataList(coreFegin.getDispatchEbm(ebmState));
    }

    @Override
    public List<EbmAuxiliaryInfo> getEbmAuxiliaryInfoEbmId(String ebmId) {
        return getDataList(coreFegin.getEbmAuxiliaryByEbmId(ebmId));
    }

    @Override
    public EbmInfo getEbmInfoById(String ebmId) {
        return getData(coreFegin.getEbmInfoByEbmId(ebmId));
    }

    @Override
    public List<EbdFilesInfo> getEbdFilesInfoByEbdId(String ebdId) {
        return getDataList(coreFegin.getEbdFilesInfoByEbdId(ebdId));
    }

    @Override
    public SchemeInfo getSchemeInfoById(String schemeId) {
        return getData(coreFegin.getSchemeInfoById(schemeId));
    }

    @Override
    public AccessRecordInfo saveRecord(AccessRecordAddRequest addRequest) {
        return getData(coreFegin.saveRecord(addRequest));
    }

    @Override
    public EbdInfo saveEbd(EbdAddRequest addRequest) {
        return getData(coreFegin.saveEbd(addRequest));
    }

    @Override
    public EbdResponseInfo saveEbdResponse(EbdResponseAddRequest addRequest) {
        return getData(coreFegin.saveEbdRespons(addRequest));
    }

    @Override
    public EbmInfo getEbmById(String ebmId) {
        return getData(coreFegin.getEbmInfoByEbmId(ebmId));
    }

    @Override
    public List<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(String ebmId) {
        return getDataList(coreFegin.getEbmBrdRecordListByEbmId(ebmId));
    }

    @Override
    public Boolean updateEbm(EbmUpdateRequest updateRequest) {
        return getBoolean(coreFegin.updateEbm(updateRequest));
    }

    @Override
    public List<EbmResInfo> getEbmResListByBrdItemId(String brdItemId) {
        return getDataList(coreFegin.getEbmResListByBrdItemId(brdItemId));
    }

    @Override
    public List<EbmResBsInfo> getEbmResBsByBrdItemId(String brdItemId) {
        return getDataList(coreFegin.getEbmResBsByBrdItemId(brdItemId));
    }

    @Override
    public List<SchemeEbrInfo> findSchemeEbrListBySchemeId(String schemeId) {
        return getDataList(coreFegin.getSchemeEbrListBySchemeId(schemeId));
    }

    @Override
    public EbmStateRequestInfo saveEbmStateRequest(EbmStateRequest ebmStateRequest) {
        return getData(coreFegin.saveEbmStateRequest(ebmStateRequest));
    }

    @Override
    public EbmBrdRecordInfo getEbmBrdByEbmIdAndResourceId(String ebmId, String ebdSrcEbrId) {
        return getData(coreFegin.getEbmBrdByEbmIdAndResourceId(ebmId,ebdSrcEbrId));
    }


    @Override
    public Boolean updateEbmBrdRecord(EbmBrdRecordUpdateRequest brdRecord) {
        return getBoolean(coreFegin.updateEbmBrdRecord(brdRecord));
    }

    @Override
    public Boolean updateSchemeInfo(SchemeUpdateRequest request) {
        return getBoolean(coreFegin.updateSchemeInfo(request));
    }

    @Override
    public void saveSchemeEbrBatch(SchemeEbrAddBatchRequest schemeEbrAddBatchRequest) {
        coreFegin.saveSchemeEbrBatch(schemeEbrAddBatchRequest);
    }

    @Override
    public void saveEbmDispatchBatch(EbmDispatchAddBatchRequest ebmDispatchAddBatchRequest) {
        coreFegin.saveEbmDispatchBatch(ebmDispatchAddBatchRequest);
    }

    @Override
    public SchemeInfo saveScheme(SchemeAddRequest schemeAddRequest) {
        return getData(coreFegin.saveScheme(schemeAddRequest));
    }

    @Override
    public AccessInfo saveAccessInfo(AccessInfoAddRequest request) {
        return getData(coreFegin.saveAccessInfo(request));
    }

    @Override
    public DispatchFlowInfo saveDispatchFlow(DispatchFlowAddRequest request) {
        return getData(coreFegin.saveDispatchFlow(request));
    }

    @Override
    public void saveEbmAuxiliaryBatch(EbmAuxiliaryAddBatchRequest request) {
        coreFegin.saveEbmAuxiliaryBatch(request);
    }

    @Override
    public void auditEbm(EbmAuditRequest request) {
        coreFegin.auditEbm(request);
    }

    @Override
    public EbmInfo saveEbm(EbmAddRequest ebmEntity) {
        return getData(coreFegin.saveEbm(ebmEntity));
    }

    @Override
    public void saveEbdFilesBatch(EbdFilesAddBatchRequest request) {
        coreFegin.saveEbdFilesBatch(request);
    }

    @Override
    public OmdRequestInfo saveOmdRequest(OmdRequestAddRequest omdRequest) {
        return getData(coreFegin.saveOmdRequest(omdRequest));
    }

    @Override
    public List<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(EbmBrdRecordWhereRequest whereRequest) {
        return getDataList(coreFegin.findEbmBrdRecordListByWhere(whereRequest));
    }

    @Override
    public void updateEbmDispatch(EbmDispatchUpdateRequest request) {
        coreFegin.updateEbmDispatch(request);
    }

    @Override
    public EbmDispatchInfo getEbmDispatchByEbdId(String ebdId) {
        return getData(coreFegin.getEbmDispatchByEbdId(ebdId));
    }

    @Override
    public Boolean updateEbd(EbdUpdateRequest updateRequest) {
        return getBoolean(coreFegin.updateEbd(updateRequest));
    }

    @Override
    public boolean updateDispatchFlow(DispatchFlowUpdateRequest request) {
        return getBoolean(coreFegin.updateDispatchFlow(request));
    }


    @Override
    public EbmBrdItemUnitInfo findEbmBrdItemUnitByBrdItemIdAndUnitId(String brdItemId, String unitId) {
        return getData(coreFegin.findEbmBrdItemUnitByBrdItemIdAndUnitId(brdItemId,unitId));
    }


    @Override
    public int saveEbmBrdRecordBatch(List<EbmBrdRecordInfo> addList) {
        return getData(coreFegin.saveEbmBrdRecordBatch(addList));
    }

    @Override
    public int updateEbmResBsBatch(List<EbmResBsInfo> updEbmResBsInfoList) {
        return getData(coreFegin.updateEbmResBsBatch(updEbmResBsInfoList));
    }

    @Override
    public int updEbmBrdRecordBatch(List<EbmBrdRecordInfo> addList) {
        return getData(coreFegin.updateEbmBrdRecordBatch(addList));
    }

    @Override
    public int saveEbmBrdItemUnitBatch(List<EbmBrdItemUnitInfo> ebmBrdItemUnits) {
        return getData(coreFegin.saveEbmBrdItemUnitBatch(ebmBrdItemUnits));
    }

    @Override
    public int saveEbmResBatch(List<EbmResInfo> ebmResList) {
        return getData(coreFegin.saveEbmResBatch(ebmResList));
    }

    @Override
    public int saveEbmResBsBatch(List<EbmResBsInfo> ebmResBsList) {
        return getData(coreFegin.saveEbmResBsBatch(ebmResBsList));
    }


    @Override
    public EbmResBsInfo getEbmResBsInfoByBrdItemIdAndBrdSysInfo(String brdItemId, String brdSysInfo) {
        return getData(coreFegin.getEbmResBsInfoByBrdItemIdAndBrdSysInfo(brdItemId,brdSysInfo));
    }

    @Override
    public EbdInfo getEbdInfoByEbdId(String ebdId) {
        return getData(coreFegin.getEbdInfoByEbdId(ebdId));
    }

    @Override
    public List<EbdInfo> getEbdInfoByEbmId(String ebmId) {
        return getDataList(coreFegin.getEbdInfoByEbmId(ebmId));
    }


    @Override
    public List<EbmDispatchInfo> getEbmDispatchByPage(EbmDispatchPageRequest request) {
        return getDataList(coreFegin.getEbmDispatchByPage(request));
    }

    @Override
    public EbmDispatchInfo getEbmDispatchByMatchedEbdId(String matchEbdId) {
        return getData(coreFegin.getEbmDispatchByMatchedEbdId(matchEbdId));
    }

    @Override
    public EbmBrdRecordInfo saveEbmBrdRecord(EbmBrdRecordRequest brdRecord) {
        return getData(coreFegin.saveEbmBrdRecord(brdRecord));
    }

    @Override
    public ApiPageResponse<AccessRecordInfo> findAccessRecordPage(AccessRecordPageRequest req) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(req);
        return coreFegin.findAccessRecordPage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<EbdInfo> findEbdPage(EbdPageRequest req) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(req);
        return coreFegin.findEbdPage(whereRequestMap);
    }

    @Override
    public ApiPageResponse<EbmInfo> getEbmInfoByPage(EbmPageRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return coreFegin.getEbmInfoByPage(whereRequestMap);
    }

    @Override
    public List<EbmStateBackInfo> findEbmStateBackListByEbmId(EbmStateBackQueryRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return getData(coreFegin.findEbmStateBackListByEbmId(whereRequestMap));
    }

    @Override
    public ApiPageResponse<SchemePageInfo> getSchemePageInfo(SchemeQueryRequest request) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(request);
        return coreFegin.getSchemePageInfo(whereRequestMap);
    }

    @Override
    public List<EbmBrdRecordInfo> getEbmBrdRecordInfoPage(EbmBrdRecordPageRequest req) {
        Map<String,Object> whereRequestMap = ReflectionUtils.convertBean2Map(req);
        return getData(coreFegin.getEbmBrdRecordInfoPage(whereRequestMap));
    }

    @Override
    public SchemeInfo getSchemeInfoByEbmId(String ebmId) {
        return getData(coreFegin.getSchemeInfoByEbmId(ebmId));
    }

    @Override
    public DispatchFlowInfo getDispatchFlowInfoById(String flowId) {
        return getData(coreFegin.getDispatchFlowInfoById(flowId));
    }

    @Override
    public EbmDispatchInfoInfo getEbmDispatchInfoInfoByEbmId(String ebmId) {
        return getData(coreFegin.getEbmDispatchInfoInfoByEbmId(ebmId));
    }

    @Override
    public List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId) {
        return getData(coreFegin.getEbmDispatchAndEbdFileByEbmId(ebmId));
    }

    @Override
    public EbmDispatchInfoInfo getEbmDispatchInfoByEbmId(String ebmId) {
        return getData(coreFegin.getEbmDispatchInfoByEbmId(ebmId));
    }

    @Override
    public EbmDispatchInfo updateEbmDispatchInfo(EbmDispatchInfoUpdateRequest updateRequest) {
        return getData(coreFegin.updateEbmDispatchInfo(updateRequest));
    }

    @Override
    public EbmDispatchInfoInfo saveEbmDispatchInfoInfo(EbmDispatchInfoAddRequest ebmDispatchInfoAddRequest) {
        return getData(coreFegin.saveEbmDispatchInfoInfo(ebmDispatchInfoAddRequest));
    }

    @Override
    public ProgramInfo saveProgramInfo(ProgramAddRequest request) {
        return getData(coreFegin.saveProgramInfo(request));
    }

    @Override
    public Boolean updateProgramInfo(ProgramUpdateRequest request) {
        return getBoolean(coreFegin.updateProgramInfo(request));
    }

    @Override
    public Boolean deleteProgramInfoByProgramId(String programId) {
        return getBoolean(coreFegin.deleteProgramInfoByProgramId(programId));
    }

    @Override
    public ProgramInfo getProgramInfoById(String programId) {
        return getData(coreFegin.getProgramInfoById(programId));
    }

    @Override
    public ApiPageResponse<ProgramInfo> getProgramInfoByPage(ProgramPageRequest request) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(request);
        return coreFegin.getProgramInfoByPage(map);
    }

    @Override
    public void auditProgram(ProgramAuditRequest request) {
        coreFegin.auditProgram(request);
    }

    @Override
    public ApiPageResponse<ProgramUnitInfo> getProgramUnitInfoByPage(ProgramUnitPageRequest pageRequest) {
        Map<String,Object> map = ReflectionUtils.convertBean2Map(pageRequest);
        return coreFegin.getProgramUnitInfoByPage(map);
    }

    @Override
    public ProgramUnitInfo saveProgramUnitInfo(ProgramUnitAddRequest request) {
        return getData(coreFegin.saveProgramUnitInfo(request));
    }

    @Override
    public Boolean updateProgramUnitInfo(ProgramUnitUpdateRequest request) {
        return getBoolean(coreFegin.updateProgramUnitInfo(request));
    }

    @Override
    public Boolean auditProgramUnitInfo(ProgramUnitAuditRequest request) {
        return getBoolean(coreFegin.auditProgramUnitInfo(request));
    }

    @Override
    public ProgramUnitInfo getProgramUnitInfoById(String id) {
        return getData(coreFegin.getProgramUnitInfoById(id));
    }

    @Override
    public List<EbmInfo> createEbmsByProgramUnits(EbmCreateRequest request) {
        return getDataList(coreFegin.createEbmsByProgramUnits(request));
    }

    @Override
    public Integer saveProgramUnitInfoBatch(List<ProgramUnitInfo> programUnitInfoList) {
        return getData(coreFegin.saveProgramUnitInfoBatch(programUnitInfoList));
    }

    @Override
    public Boolean updateProgramUnitInfoBatch(List<ProgramUnitUpdateRequest> requestList) {
        return getBoolean(coreFegin.updateProgramUnitInfoBatch(requestList));
    }

    @Override
    public SchemeInfo getSchemeInfoByProgramId(String programId) {
        return getData(coreFegin.getSchemeInfoByProgramId(programId));
    }

    @Override
    public Boolean updateEbmDispatchBatch(List<EbmDispatchUpdateRequest> request) {
        return getBoolean(coreFegin.updateEbmDispatchBatch(request));
    }

    @Override
    public Boolean cancelProgramUnit(List<String> ids) {
        return getBoolean(coreFegin.cancelProgramUnit(ids));
    }

	@Override
	public Boolean deleteSchemeEbr(String schemeId) {
		return getBoolean(coreFegin.deleteSchemeEbr(schemeId));
	}

	@Override
	public Boolean cancelEbmPlay(String ebmId) {
		return getBoolean(coreFegin.cancelEbmPlay(ebmId));
	}

	@Override
	public ApiPageResponse<QuartzInfo> getAllJobsByPage(QuartzPageRequest pageRequest) {
		Map<String,Object> map = ReflectionUtils.convertBean2Map(pageRequest);
        return coreFegin.getAllJobsByPage(map);
	}

    @Override
    public ApiEntityResponse<String> uploadFile(MultipartFile file) {
        return coreFegin.uploadFile(file);
    }
}
