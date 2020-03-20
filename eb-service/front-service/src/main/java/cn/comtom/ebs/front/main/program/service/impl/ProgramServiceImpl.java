package cn.comtom.ebs.front.main.program.service.impl;

import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import cn.comtom.domain.core.program.request.ProgramUpdateRequest;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.program.service.IProgramService;
import cn.comtom.tools.response.ApiPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProgramServiceImpl implements IProgramService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Override
    public ApiPageResponse<ProgramInfo> page(ProgramPageRequest pageRequest) {
        return coreFeginService.getProgramInfoByPage(pageRequest);
    }

    @Override
    public ProgramInfo getById(String programId) {
        return coreFeginService.getProgramInfoById(programId);
    }

    @Override
    public Boolean update(ProgramUpdateRequest request) {
        return coreFeginService.updateProgramInfo(request);
    }

    @Override
    public ProgramInfo save(ProgramAddRequest request) {
        return coreFeginService.saveProgramInfo(request);
    }

    @Override
    public void delete(String programId) {
        coreFeginService.deleteProgramInfoByProgramId(programId);
    }

    @Override
    public void audit(ProgramAuditRequest request) {
        coreFeginService.auditProgram(request);
    }
}
