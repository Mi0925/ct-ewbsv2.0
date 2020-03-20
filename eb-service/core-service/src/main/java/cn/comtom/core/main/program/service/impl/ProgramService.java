package cn.comtom.core.main.program.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.program.dao.ProgramDao;
import cn.comtom.core.main.program.entity.dbo.Program;
import cn.comtom.core.main.program.service.IProgramService;
import cn.comtom.domain.core.program.info.*;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import cn.comtom.domain.core.program.request.ProgramUpdateRequest;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.utils.EntityUtil;
import cn.comtom.tools.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgramService extends BaseServiceImpl<Program,String> implements IProgramService {

    @Autowired
    private ProgramDao programDao;

    @Override
    public BaseDao<Program, String> getDao() {
        return programDao;
    }


    @Override
    public ProgramInfo saveProgramInfo(ProgramAddRequest request) {

        //保存节目基本信息
        Program program = new Program();
        BeanUtils.copyProperties(request,program);
        ProgramInfo programInfo = this.saveProgramBaseInfo(program);
        if(programInfo == null ){
            return null;
        }

        //保存节目内容
        if(!EntityUtil.isReallyEmpty(request.getProgramContent())){
            if(StringUtils.isBlank(request.getProgramContent().getProgramId())){
                request.getProgramContent().setId(UUIDGenerator.getUUID());
                request.getProgramContent().setProgramId(programInfo.getProgramId());
            }
            this.saveProgramContentInfo(request.getProgramContent());
            programInfo.setProgramContent(request.getProgramContent());
        }

        //保存节目关联文件信息
        List<ProgramFilesInfo> filesInfoList = request.getFilesList();
        filesInfoList = Optional.ofNullable(filesInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(programFilesInfo -> {
                    programFilesInfo.setId(UUIDGenerator.getUUID());
                    programFilesInfo.setProgramId(programInfo.getProgramId());
                }).collect(Collectors.toList());
        if(!filesInfoList.isEmpty()){
            this.saveProgramFileInfoList(filesInfoList);
        }
        programInfo.setFilesList(filesInfoList);

        //保存节目关联区域信息
        List<ProgramAreaInfo> programAreaInfoList = request.getAreaList();
        programAreaInfoList = Optional.ofNullable(programAreaInfoList).orElse(Collections.emptyList()).stream()
            .filter(Objects::nonNull)
            .peek(programAreaInfo -> {
                programAreaInfo.setId(UUIDGenerator.getUUID());
                programAreaInfo.setProgramId(programInfo.getProgramId());
            }).collect(Collectors.toList());
        if(!programAreaInfoList.isEmpty()){
            this.saveProgramAreaList(programAreaInfoList);
        }
        programInfo.setAreaList(programAreaInfoList);

        //保存节目策略信息
        ProgramStrategyInfo programStrategy = request.getProgramStrategy();
        if(!EntityUtil.isReallyEmpty(programStrategy)){
             programStrategy.setStrategyId(UUIDGenerator.getUUID());
             programStrategy.setProgramId(programInfo.getProgramId());
             this.saveProgramStrategyInfo(programStrategy);
             programInfo.setProgramStrategy(programStrategy);
        }

        return programInfo;
    }

    private void saveProgramStrategyInfo(ProgramStrategyInfo programStrategy) {
        programDao.saveProgramStrategyInfo(programStrategy);
        List<ProgramTimeInfo> programTimeInfoList = programStrategy.getTimeList();
        programTimeInfoList = Optional.ofNullable(programTimeInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(programTimeInfo -> {
                    programTimeInfo.setStrategyId(programStrategy.getStrategyId());
                    programTimeInfo.setTimeId(UUIDGenerator.getUUID());
                }).collect(Collectors.toList());
        programDao.saveProgramTimeInfoList(programTimeInfoList);
        programStrategy.setTimeList(programTimeInfoList);
    }

    private void saveProgramAreaList(List<ProgramAreaInfo> programAreaInfoList) {
        programDao.saveProgramAreaList(programAreaInfoList);
    }

    private void saveProgramFileInfoList(List<ProgramFilesInfo> filesList) {

        programDao.saveProgramFileInfoList(filesList);
    }

    private void saveProgramContentInfo(ProgramContentInfo programContentInfo) {
         programDao.saveProgramContentInfo(programContentInfo);
    }

    private ProgramInfo saveProgramBaseInfo(Program program){
        program.setProgramId(UUIDGenerator.getUUID());
        if(program.getAuditResult() == null){
            program.setAuditResult(Integer.valueOf(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey()));
        }
        if(program.getCreateTime() == null){
            program.setCreateTime(new Date());
        }
        if(program.getState() == null){
            program.setState(Integer.valueOf(StateDictEnum.PROGRAM_STATE_CREATE.getKey()));
        }
        programDao.save(program);
        ProgramInfo programInfo = new ProgramInfo();
        BeanUtils.copyProperties(program,programInfo);
        return programInfo;
    }

    @Override
    public Boolean updateProgramInfo(ProgramUpdateRequest request) {
        if(StringUtils.isBlank(request.getProgramId())){
            log.error("更新节目信息失败，节目编号不能为空！");
            return false;
        }
        String programId = request.getProgramId();
        Program program = new Program();
        BeanUtils.copyProperties(request,program);
        this.updateProgramBaseInfo(program);

        //更新节目区域信息，先删除后插入
        List<ProgramAreaInfo> programAreaInfoList = request.getAreaList();
        programAreaInfoList = Optional.ofNullable(programAreaInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(programAreaInfo -> {
                    programAreaInfo.setId(UUIDGenerator.getUUID());
                    programAreaInfo.setProgramId(programId);
                }).collect(Collectors.toList());
        if(!programAreaInfoList.isEmpty()){
            programDao.deleteProgramAreaByProgramId(programId);
            this.saveProgramAreaList(programAreaInfoList);
        }

        //更新节目关联文件信息，先删除后插入
        List<ProgramFilesInfo> programFilesInfoList = request.getFilesList();
        programFilesInfoList = Optional.ofNullable(programFilesInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(programFilesInfo -> {
                    programFilesInfo.setId(UUIDGenerator.getUUID());
                    programFilesInfo.setProgramId(programId);
                }).collect(Collectors.toList());
        if(!programFilesInfoList.isEmpty()){
            programDao.deleteProgramFileListByProgramId(programId);
            this.saveProgramFileInfoList(programFilesInfoList);
        }

        //更新节目内容
        ProgramContentInfo programContentInfo = request.getProgramContent();
        if(!EntityUtil.isReallyEmpty(programContentInfo)){
            if(StringUtils.isBlank(programContentInfo.getId())){
                programContentInfo.setId(UUIDGenerator.getUUID());
                programContentInfo.setProgramId(programId);
                programDao.saveProgramContentInfo(programContentInfo);
            }else{
                programDao.updateProgramContentInfo(programContentInfo);
            }
        }else{
            programDao.deleteProgramContentByProgramId(programId);
        }

        //更新节目策略信息
        ProgramStrategyInfo programStrategyInfo = request.getProgramStrategy();
        if(!EntityUtil.isReallyEmpty(programStrategyInfo)){
            this.updateProgramStrategyInfo(programStrategyInfo);
        }

        return true;
    }

    private void updateProgramStrategyInfo(ProgramStrategyInfo programStrategyInfo) {
        List<ProgramTimeInfo> timeList = programStrategyInfo.getTimeList();
        timeList = Optional.ofNullable(timeList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(programTimeInfo -> {
                    programTimeInfo.setTimeId(UUIDGenerator.getUUID());
                    programTimeInfo.setStrategyId(programStrategyInfo.getStrategyId());
                }).collect(Collectors.toList());
        if(!timeList.isEmpty()){
            programStrategyInfo.setTimeList(timeList);
            programDao.deleteProgramTimeByStrategyId(programStrategyInfo.getStrategyId());
            programDao.updateProgramStrategyInfo(programStrategyInfo);
            programDao.saveProgramTimeInfoList(timeList);
        }
    }

    private void updateProgramBaseInfo(Program program) {
        programDao.update(program);
    }

    @Override
    public ProgramInfo getProgramInfoById(String programId) {
        return programDao.getProgramInfoById(programId);
    }

    @Override
    public List<ProgramInfo> getProgramInfoList(ProgramPageRequest request) {
        return programDao.getProgramInfoList(request);
    }

    @Override
    public Boolean deleteProgramInfoList(String programId) {
        programDao.deleteProgramAreaByProgramId(programId);
        programDao.deleteProgramContentByProgramId(programId);
        programDao.deleteProgramFileListByProgramId(programId);
        ProgramStrategyInfo programStrategyInfo = programDao.getProgramStrategyByProgramId(programId);
        if(programStrategyInfo != null){
            programDao.deleteProgramTimeByStrategyId(programStrategyInfo.getStrategyId());
            programDao.deleteProgramStrategyByProgramId(programId);
        }
        programDao.deleteProgramBaseInfo(programId);
        return true;
    }

    @Override
    public void auditProgramInfo(ProgramAuditRequest request) {
        programDao.auditProgramInfo(request);
    }

    @Override
    public List<ProgramDecomposeInfo> findprogramDecomposeByDate(String date) {
        return programDao.findprogramDecomposeByDate(date);
    }
}
