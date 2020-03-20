package cn.comtom.ebs.front.main.program.service;

import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.*;
import cn.comtom.ebs.front.main.program.model.ProgramTimeMatcher;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

public interface IProgramUnitService {
    ApiPageResponse<ProgramUnitInfo> page(ProgramUnitPageRequest pageRequest);

    ProgramUnitInfo getById(String unitId);

    Boolean update(ProgramUnitUpdateRequest request);

    Boolean updateBatch(List<ProgramUnitUpdateRequest> requestList);

    ProgramUnitInfo save(ProgramUnitAddRequest request);

    Integer saveBatch(List<ProgramUnitInfo> programUnitInfoList);

    Boolean audit(ProgramUnitAuditRequest request);

    List<EbmInfo> ebmCreate(EbmCreateRequest request);

    Boolean checkConflict(ProgramUnitInfo programUnitInfo, List<ProgramTimeMatcher> dateTimeList);

    Boolean cancel(List<String> ids);
}
