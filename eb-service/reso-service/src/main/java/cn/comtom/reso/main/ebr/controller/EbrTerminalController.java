package cn.comtom.reso.main.ebr.controller;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrTerminal;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;
import cn.comtom.reso.main.ebr.service.IEbrTerminalService;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
import cn.comtom.tools.utils.EntityUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/res/ebr/terminal")
@Api(tags = "终端信息管理")
@Slf4j
public class EbrTerminalController extends BaseController {

    @Autowired
    private IEbrTerminalService ebrTerminalService;

    @Autowired
    private IEbrChannelService ebrChannelService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据EbrId查询终端资源信息", notes = "根据EbrId查询终端资源信息")
    public ApiEntityResponse<EbrTerminalInfo> getById(@PathVariable String ebrId) {
        EbrTerminal ebrTerminal = ebrTerminalService.selectById(ebrId);
        if (ebrTerminal == null) {
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
        }
        EbrTerminalInfo info = new EbrTerminalInfo();
        BeanUtils.copyProperties(ebrTerminal, info);
        info.setResChannel(ebrChannelService.getChannelByEbrId(ebrId));
        return ApiEntityResponse.ok(info);
    }


    @GetMapping("/page")
    @ApiOperation(value = "分页查询终端资源信息", notes = "分页查询终端资源信息")
    public ApiPageResponse<EbrTerminalInfo> getByPage(@ModelAttribute EbrTerminalPageRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("get ebr terminal info by page . request=[{}]", request);
        }
        startPage(request);
        List<EbrTerminalInfo> ebrTerminalInfoList = ebrTerminalService.getList(request);
        return ApiPageResponse.ok(ebrTerminalInfoList, page);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新终端信息", notes = "更新终端信息")
    public ApiEntityResponse<Integer> updateEbrTerminalInfo(@RequestBody @Valid EbrTerminalUpdateRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("update ebr terminal info by ebrId . request=[{}]  ", request);
        }
        EbrTerminal ebrTerminal = createUpdateEbrTerminal(request);
        return ApiEntityResponse.ok(ebrTerminalService.update(ebrTerminal));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增终端信息", notes = "新增终端信息")
    public ApiEntityResponse<EbrTerminalInfo> saveEbrTerminalInfo(@RequestBody @Valid EbrTerminalAddRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("save ebr terminal info . request=[{}]  ", request);
        }
        EbrTerminal ebrTerminal = createEbrTerminal(request);

        ebrTerminalService.save(ebrTerminal);
        EbrTerminalInfo ebrTerminalInfo = new EbrTerminalInfo();
        BeanUtils.copyProperties(ebrTerminal, ebrTerminalInfo);
        return ApiEntityResponse.ok(ebrTerminalInfo);
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除终端信息", notes = "删除终端信息")
    public ApiResponse delete(@PathVariable(name = "ebrId") String ebrId) {
        if (log.isDebugEnabled()) {
            log.debug("delete ebrTerminal info by ebrId . ebrId=[{}]  ", ebrId);
        }
        if (StringUtils.isBlank(ebrId)) {
            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ebrTerminalService.deleteById(ebrId);
        return ApiResponse.ok();
    }


    @PutMapping("/updateTerminalBatch")
    @ApiOperation(value = "批量更新终端信息", notes = "批量更新终端信息")
    public ApiEntityResponse<Integer> updateTerminalBatch(@RequestBody @Valid List<EbrTerminalUpdateRequest> updateRequestList) {
        Long count = Optional.ofNullable(updateRequestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> {
                    EbrTerminal ebrTerminal = createUpdateEbrTerminal(request);
                    if(StringUtils.isNotBlank(request.getSyncFlag()))
                        ebrTerminal.setSyncFlag(request.getSyncFlag());
                    if(StringUtils.isNotBlank(request.getStatusSyncFlag()))
                        ebrTerminal.setStatusSyncFlag(request.getStatusSyncFlag());
                    return ebrTerminalService.update(ebrTerminal);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    /**
     * 根据前端数据构造Terminal对象
     * @param request
     * @return
     */
    private EbrTerminal createEbrTerminal(EbrTerminalAddRequest request) {
        EbrTerminal ebrTerminal = new EbrTerminal();
        BeanUtils.copyProperties(request, ebrTerminal);
        //设置创建时间
        if (ebrTerminal.getCreateTime() == null) {
            ebrTerminal.setCreateTime(new Date());
        }
        if (ebrTerminal.getUpdateTime() == null) {
            ebrTerminal.setUpdateTime(new Date());
        }
        //如果同步状态为空设置同步状态未同步
        if (StringUtils.isBlank(ebrTerminal.getSyncFlag())) {
            ebrTerminal.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        //如果同步状态为空设置同步状态未同步
        if (StringUtils.isBlank(ebrTerminal.getStatusSyncFlag())) {
            ebrTerminal.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        //如果终端状态为空则设置终端状态为运行状态
        if (StringUtils.isBlank(ebrTerminal.getTerminalState())) {
            ebrTerminal.setTerminalState(StateDictEnum.EBR_RESOURCE_STATE_RUN.getKey());
        }
        return ebrTerminal;
    }

    /**
     * 根据前端数据构造修改的Terminal对象
     * @param request
     * @return
     */
    private EbrTerminal createUpdateEbrTerminal(EbrTerminalUpdateRequest request) {
        EbrTerminal ebrTerminal = new EbrTerminal();
        BeanUtils.copyProperties(request, ebrTerminal);
        //更新修改时间
        ebrTerminal.setUpdateTime(new Date());
        //终端修改后重新同步
        ebrTerminal.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        ebrTerminal.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        return ebrTerminal;
    }

    @PostMapping("/addEbrTerminalBatch")
    @ApiOperation(value = "批量新增终端信息", notes = "批量新增终端信息")
    public ApiEntityResponse<Integer> addEbrTerminalBatch(@RequestBody @Valid List<EbrTerminalAddRequest> addRequestList) {
        List<EbrTerminal> list = Optional.ofNullable(addRequestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> createEbrTerminal(request))
                .collect(Collectors.toList());
        return ApiEntityResponse.ok(ebrTerminalService.saveList(list));
    }

    @PutMapping("/updateEbrTerminalState")
    @ApiOperation(value = "批量更新终端信息状态", notes = "批量更新终端信息状态")
    public ApiEntityResponse<Integer> updateEbrTerminalState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        Long count = Optional.ofNullable(ebrStateUpdateRequestList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> {
                    EbrTerminal ebrTerminal = new EbrTerminal();
                    ebrTerminal.setTerminalEbrId(request.getEbrId());
                    if (request.getState() == null) {
                        ebrTerminal.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    } else {
                        ebrTerminal.setSyncFlag(request.getState().toString());
                    }
                    if (request.getStatusSyncFlag() == null) {
                        ebrTerminal.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    } else {
                        ebrTerminal.setStatusSyncFlag(request.getStatusSyncFlag().toString());
                    }
                    ebrTerminal.setUpdateTime(new Date());
                    return ebrTerminalService.update(ebrTerminal);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }


    @PostMapping(value = "/findListByTremIds")
    @ApiOperation(value = "根据终端ID集合查询终端信息集合", notes = "根据终端ID集合查询终端信息集合")
    public ApiEntityResponse<List<EbrTerminalInfo>> findListByTremIds(@RequestBody List<String> devTrmIds) {
        if (devTrmIds.size() == 0) {
            return ApiEntityResponse.ok(new ArrayList<EbrTerminalInfo>());
        }
        return ApiEntityResponse.ok(ebrTerminalService.findListByTremIds(devTrmIds));
    }

    @GetMapping("/findTerminalListByWhere")
    @ApiOperation(value = "条件终端信息", notes = "条件终端信息")
    public ApiListResponse<EbrTerminalInfo> findTerminalListByWhere(@ModelAttribute TerminalWhereRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~]find EbrTerminalInfo by condition . request = [{}]", JSON.toJSONString(request));
        }
        List<EbrTerminalInfo> ebrTerminalInfoList = ebrTerminalService.findTerminalListByWhere(request);
        return ApiListResponse.ok(ebrTerminalInfoList);
    }
    
    @PostMapping(value = "/countTerminalByCondition")
    @ApiOperation(value = "根据条件统计终端数量", notes = "根据条件统计终端数量")
    public ApiEntityResponse<Integer> countTerminalByCondition(@RequestBody TerminalConditionRequest terminalConditionRequest) {
        return ApiEntityResponse.ok(ebrTerminalService.countTerminalByCondition(terminalConditionRequest));
    }
}
