package cn.comtom.ebs.front.main.program.service;

import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import cn.comtom.domain.core.program.request.ProgramUpdateRequest;
import cn.comtom.tools.response.ApiPageResponse;

public interface IProgramService {
    ApiPageResponse<ProgramInfo> page(ProgramPageRequest pageRequest);

    ProgramInfo getById(String programId);

    Boolean update(ProgramUpdateRequest request);

    ProgramInfo save(ProgramAddRequest request);

    void delete(String programId);

    void audit(ProgramAuditRequest request);
}
