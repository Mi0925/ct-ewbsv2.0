package cn.comtom.core.main.program.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.program.service.IProgramService;
import cn.comtom.domain.core.program.info.ProgramInfo;
import cn.comtom.domain.core.program.request.ProgramAddRequest;
import cn.comtom.domain.core.program.request.ProgramAuditRequest;
import cn.comtom.domain.core.program.request.ProgramPageRequest;
import cn.comtom.domain.core.program.request.ProgramUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/core/program")
@Api(tags = "节目信息")
@Slf4j
public class ProgramController extends BaseController {

    @Autowired
    private IProgramService programService;


    @PostMapping("/save")
    @ApiOperation(value = "保存节目信息", notes = "保存节目信息")
    public ApiEntityResponse<ProgramInfo> save(@RequestBody @Valid ProgramAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("save program info . request=[{}]",request);
        }
        ProgramInfo programInfo = programService.saveProgramInfo(request);
        return ApiEntityResponse.ok(programInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新节目信息", notes = "更新节目信息")
    public ApiResponse update(@RequestBody @Valid ProgramUpdateRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("update program info . request=[{}]",request);
        }
        programService.updateProgramInfo(request);
        return ApiResponse.ok();
    }

    @PutMapping("/audit")
    @ApiOperation(value = "审核节目信息", notes = "审核节目信息")
    public ApiResponse audit(@RequestBody @Valid ProgramAuditRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("audit program info . request=[{}]",request);
        }
        programService.auditProgramInfo(request);
        return ApiResponse.ok();
    }

    @GetMapping("/{programId}")
    @ApiOperation(value = "根据ID节目信息", notes = "根据ID节目信息")
    public ApiEntityResponse<ProgramInfo> getById(@PathVariable(name = "programId") String programId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get program info by programId. programId = [{}]",programId);
        }
        if(StringUtils.isBlank(programId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ProgramInfo programInfo = programService.getProgramInfoById(programId);
        return ApiEntityResponse.ok(programInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询节目信息", notes = "分页查询节目信息")
    public ApiPageResponse<ProgramInfo> getByPage(@ModelAttribute ProgramPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get program info by page. request = [{}]",request);
        }
        startPage(request);
        List<ProgramInfo> programInfoList = programService.getProgramInfoList(request);
        return ApiPageResponse.ok(programInfoList,page);
    }

    @DeleteMapping("/delete/{programId}")
    @ApiOperation(value = "删除节目信息", notes = "删除节目信息")
    public ApiResponse deleteById(@PathVariable(name = "programId") String programId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete program info . programId = [{}]",programId);
        }
        if(StringUtils.isBlank(programId)){
            return ApiResponseBuilder.buildError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        programService.deleteProgramInfoList(programId);
        return ApiResponse.ok();
    }
}
