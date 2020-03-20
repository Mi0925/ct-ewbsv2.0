package cn.comtom.ebs.front.main.program.controller;

import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchUpdateRequest;
import cn.comtom.domain.core.ebm.request.EbmUpdateRequest;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.*;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.program.model.ProgramTimeMatcher;
import cn.comtom.ebs.front.main.program.service.IProgramUnitService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.DateUtil;
import cn.comtom.tools.utils.EntityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/safeRest/program/unit")
@Api(tags = "节目单管理", description = "节目单管理")
@Slf4j
public class ProgramUnitController extends AuthController {

    @Autowired
    private IProgramUnitService programUnitService;

    @Autowired
    private ICoreFeginService coreFeginService;


    @GetMapping("/list")
    @ApiOperation(value = "分页查询节目单信息", notes = "分页查询节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_LIST)
    public ApiPageResponse<ProgramUnitInfo> page(@ModelAttribute ProgramUnitPageRequest pageRequest, BindingResult bResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get program unit info by page. request=[{}]",pageRequest);
        }
        //默认查询当天的节目单
        if(StringUtils.isBlank(pageRequest.getPlayDate())
                && StringUtils.isBlank(pageRequest.getMonth())
                && StringUtils.isBlank(pageRequest.getYear())){
            pageRequest.setPlayDate(DateUtil.getDate());
        }
     	pageRequest.setOrder("asc");
    	pageRequest.setSidx("auditResult");
        //pageRequest.setLimit(Constants.MAX_SQL_ROWS);
        ApiPageResponse<ProgramUnitInfo> response = programUnitService.page(pageRequest);
        List<ProgramTimeMatcher> dateTimeList = Optional.ofNullable(response.getData()).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(programUnitInfo -> {
                    String startTime = programUnitInfo.getStartTime();
                    String endTime = programUnitInfo.getEndTime();
                    Date startDateTime = DateUtil.stringToDate(DateUtil.getDate() + SymbolConstants.BLANK_SPLIT + startTime);
                    Date endDateTime = DateUtil.stringToDate(DateUtil.getDate() + SymbolConstants.BLANK_SPLIT + endTime);
                    ProgramTimeMatcher timeMatcher = new ProgramTimeMatcher();
                    timeMatcher.setEndTime(endDateTime);
                    timeMatcher.setStartTime(startDateTime);
                    timeMatcher.setProgramUnitId(programUnitInfo.getId());
                    return timeMatcher;
                })
                .collect(Collectors.toList());
        //按开始时间排序
        List<ProgramUnitInfo> programUnitInfoList = Optional.ofNullable(response.getData()).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(programUnitInfo -> {
                	ProgramUnitInfo proUinfo = (ProgramUnitInfo)programUnitInfo;
                    String dateTimeStr = DateUtil.format(proUinfo.getPlayDate()) + SymbolConstants.BLANK_SPLIT + proUinfo.getStartTime();
                    return DateUtil.stringToDate(dateTimeStr);
                }).reversed())
                .peek(programUnitInfo -> {
                    String startTime = programUnitInfo.getStartTime();
                    String endTime = programUnitInfo.getEndTime();
                    Date startDateTime = DateUtil.stringToDate(DateUtil.format(programUnitInfo.getPlayDate()) + SymbolConstants.BLANK_SPLIT + startTime);
                    Date endDateTime = DateUtil.stringToDate(DateUtil.format(programUnitInfo.getPlayDate()) + SymbolConstants.BLANK_SPLIT + endTime);
                    Date curDate = new Date();
                    //计算该节目当前是否正在播放
                    if(StateDictEnum.AUDIT_STATUS_PASS.getKey().equals(programUnitInfo.getAuditResult())
                            && StateDictEnum.PROGRAM_UNIT_STATE_DONE.getKey().equals(programUnitInfo.getState())){
                        if(startDateTime.before(curDate)  && curDate.before(endDateTime)){
                            programUnitInfo.setPlayFlag(CommonDictEnum.PROGRAM_PLAY_FLAG_ACTING.getKey());
                        }else if(curDate.after(endDateTime)){
                            programUnitInfo.setPlayFlag(CommonDictEnum.PROGRAM_PLAY_FLAG_DONE.getKey());
                        }else if(curDate.before(startDateTime)){
                            programUnitInfo.setPlayFlag(CommonDictEnum.PROGRAM_PLAY_FLAG_READY.getKey());
                        }
                    }
                    //节目单状态为取消，设置播放状态为播放取消
                    if(StateDictEnum.PROGRAM_UNIT_STATE_CANCEL.getKey().equals(programUnitInfo.getState())){
                        programUnitInfo.setPlayFlag(CommonDictEnum.PROGRAM_PLAY_FLAG_CANCEL.getKey());
                    }
                    programUnitInfo.setConflicting(programUnitService.checkConflict(programUnitInfo,dateTimeList));
                })
                .collect(Collectors.toList()) ;
        response.setData(programUnitInfoList);
        return response;
    }



    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询节目单信息", notes = "根据ID查询节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_LIST)
    public ApiEntityResponse<ProgramUnitInfo> getById(@PathVariable(name = "id") String id) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get program unit info by id. id=[{}]",id);
        }
        ProgramUnitInfo programInfo = programUnitService.getById(id);
        return ApiEntityResponse.ok(programInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新节目单信息", notes = "更新节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_EDIT)
    public ApiResponse update(@RequestBody @Valid ProgramUnitUpdateRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update program unit info . request=[{}]",request);
        }
        programUnitService.update(request);
        ProgramUnitAuditRequest auditRequest = new ProgramUnitAuditRequest();
        auditRequest.setIds(Arrays.asList(request.getId()));
        auditRequest.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        auditRequest.setAuditOpinion("修改之后待审核");
        auditRequest.setAuditTime(new Date());
        programUnitService.audit(auditRequest);
        return ApiResponse.ok();
    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量更新节目单信息", notes = "批量更新节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_EDIT)
    public ApiResponse updateBatch(@RequestBody @Valid List<ProgramUnitUpdateRequest> programUnitUpdateRequestList) {
        if(log.isDebugEnabled()){
            log.debug("[!~] update program unit info batch . ProgramUnitUpdateRequestList=[{}]",programUnitUpdateRequestList);
        }
        programUnitService.updateBatch(programUnitUpdateRequestList);
        return ApiResponse.ok();
    }

    @PutMapping("/audit")
    @ApiOperation(value = "审核节目单信息", notes = "审核节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_AUDIT)
    public ApiResponse audit(@RequestBody @Valid ProgramUnitAuditRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] audit program unit info . request=[{}]",request);
        }
        if(StateDictEnum.AUDIT_STATUS_PASS.getKey().equals(request.getAuditResult())){
            //如果是审核通过，则生成EBM消息
            EbmCreateRequest ebmCreateRequest = new EbmCreateRequest();
            ebmCreateRequest.setUnitIds(request.getIds());
            programUnitService.ebmCreate(ebmCreateRequest);
        }
        programUnitService.audit(request);

        return ApiResponse.ok();
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消节目单播放", notes = "取消节目单播放")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_DELETE)
    public ApiResponse cancel(@RequestBody @Valid List<String> ids) {
        if(log.isDebugEnabled()){
            log.debug("[!~] cancel program unit  . ids=[{}]",ids);
        }
        programUnitService.cancel(ids);

        return ApiResponse.ok();
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增节目单信息", notes = "新增节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_ADD)
    public ApiEntityResponse<ProgramUnitInfo> save(@RequestBody @Valid ProgramUnitAddRequest request) {
        if(log.isDebugEnabled()){
            log.debug("[!~] add program unit info . request=[{}]",request);
        }
        ProgramUnitInfo programInfo = programUnitService.save(request);
        return ApiEntityResponse.ok(programInfo);
    }

    @DeleteMapping("/delete/{unitId}")
    @ApiOperation(value = "删除节目单信息", notes = "删除节目单信息")
    @RequiresPermissions(Permissions.CORE_PROGRAM_UNIT_DELETE)
    public ApiResponse delete(@PathVariable(name = "unitId") String unitId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete program unit info by unitId . unitId=[{}]",unitId);
        }
        ProgramUnitInfo programUnitInfo = programUnitService.getById(unitId);
        if(programUnitInfo == null){
            return ApiResponseBuilder.buildError(FrontErrorEnum.QUERY_NO_DATA);
        }
        //删除的节目单已经审核通过
        //节目单生成的EBM信息已存在，则更新ebm的状态
        SchemeInfo schemeInfo = coreFeginService.getSchemeInfoByProgramId(programUnitInfo.getProgramId());
        if(!EntityUtil.isReallyEmpty(schemeInfo)){
            EbmInfo ebmInfo = coreFeginService.getEbmInfoById(schemeInfo.getEbmId());

            //设置EBM信息状态为取消
            EbmUpdateRequest ebmUpdateRequest = new EbmUpdateRequest();
            ebmUpdateRequest.setEbmState(StateDictEnum.EBM_STATE_CANCEL.getKey());
            ebmUpdateRequest.setEbmId(ebmInfo.getEbmId());
            coreFeginService.updateEbm(ebmUpdateRequest);

            //设置EBM调度分发消息状态为取消
            List<EbmDispatchInfo> ebmDispatchInfoList = coreFeginService.getEbmDispatchByEbmId(ebmInfo.getEbmId());
            List<EbmDispatchUpdateRequest> ebmDispatchUpdateRequestList =  Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
                    .filter(Objects::nonNull)
                    .map(ebmDispatchInfo -> {
                        EbmDispatchUpdateRequest request = new EbmDispatchUpdateRequest();
                        request.setDispatchId(ebmDispatchInfo.getDispatchId());
                        request.setState(StateDictEnum.DISPATCH_STATE_CANCEL.getKey());
                        return request;
                    }).collect(Collectors.toList());
            coreFeginService.updateEbmDispatchBatch(ebmDispatchUpdateRequestList);

        }

        if(StateDictEnum.AUDIT_STATUS_PASS.getKey().equals(programUnitInfo.getAuditResult())){
            //已审核通过的消息进行删除，先设置状态为取消
            ProgramUnitUpdateRequest programUnitUpdateRequest = new ProgramUnitUpdateRequest();
            programUnitUpdateRequest.setId(programUnitInfo.getId());
            programUnitUpdateRequest.setState(StateDictEnum.PROGRAM_UNIT_STATE_CANCEL.getKey());
            programUnitService.update(programUnitUpdateRequest);

            //审核状态设置为未审核。
            ProgramUnitAuditRequest programUnitAuditRequest = new ProgramUnitAuditRequest();
            programUnitAuditRequest.setIds(Arrays.asList(programUnitInfo.getId()));
            programUnitAuditRequest.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
            programUnitService.audit(programUnitAuditRequest);
        }else{
            //未审核或者审核不通过的消息进行删除，设置状态为删除
            ProgramUnitUpdateRequest programUnitUpdateRequest = new ProgramUnitUpdateRequest();
            programUnitUpdateRequest.setId(programUnitInfo.getId());
            programUnitUpdateRequest.setState(StateDictEnum.PROGRAM_UNIT_STATE_DELETE.getKey());
            programUnitService.update(programUnitUpdateRequest);
        }



        return ApiResponse.ok();
    }


}
