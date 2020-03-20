package cn.comtom.core.main.program.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.core.main.program.service.IProgramService;
import cn.comtom.core.main.program.service.IProgramUnitService;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.*;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/core/program/unit")
@Api(tags = "节目单关联")
@Slf4j
public class ProgramUnitController extends BaseController {

    @Autowired
    private IProgramUnitService programUnitService;

    @Autowired
    private IProgramService programService;

    @PostMapping("/save")
    @ApiOperation(value = "保存节目单信息", notes = "保存节目单信息")
    public ApiEntityResponse<ProgramUnitInfo> save(@RequestBody @Valid ProgramUnitAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] save program unit info . request=[{}]",request);
        }
        ProgramUnit programUnit = new ProgramUnit();
        BeanUtils.copyProperties(request,programUnit);
        programUnit.setId(UUIDGenerator.getUUID());
        programUnit.setState(StateDictEnum.PROGRAM_UNIT_STATE_CREATE.getKey());
        programUnit.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        programUnitService.save(programUnit);
        ProgramUnitInfo programUnitInfo = new ProgramUnitInfo();
        BeanUtils.copyProperties(programUnit,programUnitInfo);
        return ApiEntityResponse.ok(programUnitInfo);
    }


    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存节目单信息", notes = "批量保存节目单信息")
    public ApiEntityResponse<Integer> saveBatch(@RequestBody @Valid List<ProgramUnitInfo> programUnitInfoList, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] save program unit info batch . request=[{}]",programUnitInfoList);
        }
        List<ProgramUnit> programUnitList = Optional.ofNullable(programUnitInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(programUnitInfo -> {
                    ProgramUnit programUnit = new ProgramUnit();
                    BeanUtils.copyProperties(programUnitInfo,programUnit);
                    programUnit.setId(UUIDGenerator.getUUID());
                    programUnit.setState(StateDictEnum.PROGRAM_UNIT_STATE_CREATE.getKey());
                    programUnit.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
                    return programUnit;
                })
                .collect(Collectors.toList());
        return ApiEntityResponse.ok(programUnitService.saveList(programUnitList));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新节目单信息", notes = "更新节目单信息")
    public ApiResponse update(@RequestBody @Valid ProgramUnitUpdateRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("update program unit info . request=[{}]",request);
        }
        ProgramUnit programUnit = new ProgramUnit();
        BeanUtils.copyProperties(request,programUnit);
        programUnitService.update(programUnit);
        return ApiResponse.ok();
    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量更新节目单信息", notes = "批量更新节目单信息")
    public ApiResponse updateBatch(@RequestBody @Valid List<ProgramUnitUpdateRequest> requestList, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("update program unit info batch . requestList=[{}]",requestList);
        }
        Optional.ofNullable(requestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .forEach(programUnitUpdateRequest -> {
                    ProgramUnit programUnit = new ProgramUnit();
                    BeanUtils.copyProperties(programUnitUpdateRequest,programUnit);
                    programUnitService.update(programUnit);
                });
        return ApiResponse.ok();
    }

    @PutMapping("/audit")
    @ApiOperation(value = "批量审核节目单信息", notes = "批量审核节目单信息")
    public ApiResponse audit(@RequestBody @Valid ProgramUnitAuditRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("audit program info . request=[{}]",request);
        }
        Optional.ofNullable(request.getIds()).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .forEach(id ->{
                    ProgramUnit programUnit = new ProgramUnit();
                    BeanUtils.copyProperties(request,programUnit);
                    programUnit.setId(id);
                    programUnitService.update(programUnit);
                });
        return ApiResponse.ok();
    }

    @PostMapping("/ebm/create")
    @ApiOperation(value = "根据节目信息创建EBM消息", notes = "根据节目信息创建EBM消息")
    public ApiListResponse<EbmInfo> createEbms(@RequestBody @Valid EbmCreateRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("create ebm info by program units. request=[{}]", JSON.toJSONString(request));
        }
        List<EbmInfo> ebmInfoList = programUnitService.createEbms(request);
        return ApiListResponse.ok(ebmInfoList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID节目信息", notes = "根据ID节目信息")
    public ApiEntityResponse<ProgramUnitInfo> getById(@PathVariable(name = "id") String id) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get program unit info by id. id = [{}]",id);
        }
        if(StringUtils.isBlank(id)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ProgramUnit programUnit = programUnitService.selectById(id);
        ProgramUnitInfo programUnitInfo = new ProgramUnitInfo();
        BeanUtils.copyProperties(programUnit,programUnitInfo);
        ProgramInfo programInfo = programService.getProgramInfoById(programUnit.getProgramId());
        programUnitInfo.setProgramInfo(programInfo);
       /* Program program = programService.selectById(programUnit.getProgramId());
        if(program != null){
            ProgramInfo programInfo = new ProgramInfo();
            BeanUtils.copyProperties(program,programInfo);
            programUnitInfo.setProgramInfo(programInfo);
        }*/
        return ApiEntityResponse.ok(programUnitInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询节目信息", notes = "分页查询节目信息")
    public ApiPageResponse<ProgramUnitInfo> getByPage(@ModelAttribute ProgramUnitPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get program unit info by page. request = [{}]",request);
        }
        startPage(request);
        List<ProgramUnitInfo> programUnitInfoList = programUnitService.getProgramUnitInfoList(request);
        return ApiPageResponse.ok(programUnitInfoList,page);
    }



    @PostMapping("/cancel")
    @ApiOperation(value = "批量取消节目单播放", notes = "批量取消节目单播放")
    public ApiResponse cancel(@RequestBody @Valid List<String> ids, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] cancel program unit  . ids=[{}]",ids);
        }
        List<ProgramUnitInfo> programUnitInfoList = Optional.ofNullable(ids).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(id ->  this.getById(id).getData())
                .collect(Collectors.toList());
        programUnitService.cancel(programUnitInfoList);
        return ApiResponse.ok();
    }
}
