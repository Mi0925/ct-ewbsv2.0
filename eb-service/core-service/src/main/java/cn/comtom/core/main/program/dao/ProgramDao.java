package cn.comtom.core.main.program.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.program.entity.dbo.Program;
import cn.comtom.core.main.program.mapper.ProgramMapper;
import cn.comtom.domain.core.program.info.*;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgramDao extends BaseDao<Program,String> {

    @Autowired
    private ProgramMapper mapper;

    @Override
    public CoreMapper<Program, String> getMapper() {
        return mapper;
    }

    public void saveProgramContentInfo(ProgramContentInfo programContentInfo) {
        mapper.saveProgramContentInfo(programContentInfo);
    }

    public void saveProgramFileInfoList(List<ProgramFilesInfo> filesList) {
        mapper.saveProgramFileInfoList(filesList);
    }

    public void saveProgramAreaList(List<ProgramAreaInfo> programAreaInfoList) {
        mapper.saveProgramAreaList(programAreaInfoList);
    }

    public void saveProgramTimeInfoList(List<ProgramTimeInfo> programTimeInfoList) {
        mapper.saveProgramTimeInfoList(programTimeInfoList);
    }

    public void saveProgramStrategyInfo(ProgramStrategyInfo programStrategy) {
        mapper.saveProgramStrategyInfo(programStrategy);
    }

    public void deleteProgramAreaByProgramId(String programId) {
        mapper.deleteProgramAreaByProgramId(programId);
    }

    public void deleteProgramFileListByProgramId(String programId) {
        mapper.deleteProgramFileListByProgramId(programId);
    }

    public void updateProgramContentInfo(ProgramContentInfo programContentInfo) {
        mapper.updateProgramContentInfo(programContentInfo);
    }

    public void deleteProgramContentByProgramId(String programId) {
        mapper.deleteProgramContentByProgramId(programId);
    }

    public void updateProgramStrategyInfo(ProgramStrategyInfo programStrategyInfo) {
            mapper.updateProgramStrategyInfo(programStrategyInfo);
    }

    public void deleteProgramStrategyByProgramId(String programId) {
        mapper.deleteProgramStrategyByProgramId(programId);
    }

    public void deleteProgramTimeByStrategyId(String strategyId) {
        mapper.deleteProgramTimeByStrategyId(strategyId);
    }

    public ProgramInfo getProgramInfoById(String programId) {
        return mapper.getProgramInfoById(programId);
    }

    public List<ProgramInfo> getProgramInfoList(ProgramPageRequest request) {
        return mapper.getProgramInfoList(request);
    }

    public ProgramStrategyInfo getProgramStrategyByProgramId(String programId) {
        return mapper.getProgramStrategyByProgramId(programId);
    }

    public void deleteProgramBaseInfo(String programId) {
        mapper.deleteProgramBaseInfoById(programId);
    }

    public void auditProgramInfo(ProgramAuditRequest request) {
        mapper.auditProgramInfo(request);
    }

    public List<ProgramDecomposeInfo> findprogramDecomposeByDate(String date) {
       return mapper.findprogramDecomposeByDate(date);
    }
}
