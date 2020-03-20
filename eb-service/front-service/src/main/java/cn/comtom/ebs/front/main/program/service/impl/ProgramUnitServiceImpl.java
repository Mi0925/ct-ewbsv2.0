package cn.comtom.ebs.front.main.program.service.impl;

import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.*;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.main.program.model.ProgramTimeMatcher;
import cn.comtom.ebs.front.main.program.service.IProgramUnitService;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.DateUtil;
import cn.comtom.tools.utils.TimeMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgramUnitServiceImpl implements IProgramUnitService {

    @Autowired
    private ICoreFeginService coreFeginService;


    @Override
    public ApiPageResponse<ProgramUnitInfo> page(ProgramUnitPageRequest pageRequest) {
        return coreFeginService.getProgramUnitInfoByPage(pageRequest);
    }

    @Override
    public ProgramUnitInfo getById(String unitId) {
        return coreFeginService.getProgramUnitInfoById(unitId);
    }

    @Override
    public Boolean update(ProgramUnitUpdateRequest request) {
        return coreFeginService.updateProgramUnitInfo(request);
    }

    @Override
    public Boolean updateBatch(List<ProgramUnitUpdateRequest> requestList) {
        return coreFeginService.updateProgramUnitInfoBatch(requestList);
    }

    @Override
    public ProgramUnitInfo save(ProgramUnitAddRequest request) {
        return coreFeginService.saveProgramUnitInfo(request);
    }

    @Override
    public Integer saveBatch(List<ProgramUnitInfo> programUnitInfoList) {
        return coreFeginService.saveProgramUnitInfoBatch(programUnitInfoList);
    }

    @Override
    public Boolean audit(ProgramUnitAuditRequest request) {
        return coreFeginService.auditProgramUnitInfo(request);
    }

    @Override
    public List<EbmInfo> ebmCreate(EbmCreateRequest request) {
        return coreFeginService.createEbmsByProgramUnits(request);
    }

    @Override
    public Boolean checkConflict(ProgramUnitInfo programUnitInfo, List<ProgramTimeMatcher> dateTimeList) {
        String startTime = programUnitInfo.getStartTime();
        String endTime = programUnitInfo.getEndTime();
        if(startTime == null && endTime == null){
            return  false;
        }
        Date startDateTime = DateUtil.stringToDate(DateUtil.getDate() + SymbolConstants.BLANK_SPLIT + startTime);
        Date endDateTime = DateUtil.stringToDate(DateUtil.getDate() + SymbolConstants.BLANK_SPLIT + endTime);
        List<TimeMatcher> timeMatcherList = Optional.ofNullable(dateTimeList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(programTimeMatcher -> !programTimeMatcher.getProgramUnitId().equals(programUnitInfo.getId()))
                .map(programTimeMatcher -> {
                    TimeMatcher timeMatcher = new TimeMatcher();
                    timeMatcher.setStartTime(programTimeMatcher.getStartTime());
                    timeMatcher.setEndTime(programTimeMatcher.getEndTime());
                    return timeMatcher;
                }).collect(Collectors.toList());
        return DateUtil.checkConflict(startDateTime,endDateTime,timeMatcherList);
    }

    @Override
    public Boolean cancel(List<String> ids) {
        return coreFeginService.cancelProgramUnit(ids);
    }
}
