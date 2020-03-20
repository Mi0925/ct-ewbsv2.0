package cn.comtom.core.main.program.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.program.entity.dbo.Program;
import cn.comtom.domain.core.program.info.*;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProgramMapper extends CoreMapper<Program,String> {

    void saveProgramContentInfo(ProgramContentInfo programContentInfo);

    void saveProgramFileInfoList(List<ProgramFilesInfo> filesList);

    void saveProgramAreaList(List<ProgramAreaInfo> programAreaInfoList);

    void saveProgramTimeInfoList(List<ProgramTimeInfo> programTimeInfoList);

    void saveProgramStrategyInfo(ProgramStrategyInfo programStrategy);

    void deleteProgramAreaByProgramId(String programId);

    void deleteProgramFileListByProgramId(String programId);

    void updateProgramContentInfo(ProgramContentInfo programContentInfo);

    void deleteProgramContentByProgramId(String programId);

    void deleteProgramStrategyByProgramId(String programId);

    void deleteProgramTimeByStrategyId(String strategyId);

    ProgramInfo getProgramInfoById(String programId);

    List<ProgramInfo> getProgramInfoList(ProgramPageRequest request);

    ProgramStrategyInfo getProgramStrategyByProgramId(String programId);

    void deleteProgramBaseInfoById(String programId);

    void updateProgramStrategyInfo(ProgramStrategyInfo programStrategyInfo);

    void auditProgramInfo(ProgramAuditRequest request);

    List<ProgramDecomposeInfo> findprogramDecomposeByDate(String date);
}
