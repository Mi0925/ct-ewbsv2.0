package cn.comtom.core.main.program.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.program.entity.dbo.Program;
import cn.comtom.domain.core.program.info.ProgramDecomposeInfo;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import cn.comtom.domain.core.program.request.ProgramUpdateRequest;

import java.util.List;

public interface IProgramService extends BaseService<Program,String> {

    ProgramInfo saveProgramInfo(ProgramAddRequest request);

    Boolean updateProgramInfo(ProgramUpdateRequest request);

    ProgramInfo getProgramInfoById(String programId);

    List<ProgramInfo> getProgramInfoList(ProgramPageRequest request);

    Boolean deleteProgramInfoList(String programId);

    void auditProgramInfo(ProgramAuditRequest request);

    List<ProgramDecomposeInfo> findprogramDecomposeByDate(String date);
}
