package cn.comtom.core.main.flow.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.flow.entity.dbo.DispatchFlow;
import cn.comtom.core.main.flow.service.IDispatchFlowService;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.flow.request.DispatchFlowAddRequest;
import cn.comtom.domain.core.flow.request.DispatchFlowUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/core/flow")
@Api(tags = "调度流程信息")
@Slf4j
public class DispatchFlowController extends BaseController {

    @Autowired
    private IDispatchFlowService dispatchFlowService;


    @PostMapping("/save")
    @ApiOperation(value = "保存调度流程信息", notes = "保存调度流程信息")
    public ApiEntityResponse<DispatchFlowInfo> save(@RequestBody @Valid DispatchFlowAddRequest request, BindingResult bindResult) {
        DispatchFlow dispatchFlow = new DispatchFlow();
        BeanUtils.copyProperties(request,dispatchFlow);
        dispatchFlow.setFlowId(UUIDGenerator.getUUID());
        if(dispatchFlow.getCreateTime() == null){
            dispatchFlow.setCreateTime(new Date());
        }
        dispatchFlowService.save(dispatchFlow);
        DispatchFlowInfo dispatchFlowInfo = new DispatchFlowInfo();
        BeanUtils.copyProperties(dispatchFlow,dispatchFlowInfo);
        return ApiEntityResponse.ok(dispatchFlowInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新调度流程信息", notes = "更新调度流程信息")
    public ApiResponse update(@RequestBody @Valid DispatchFlowUpdateRequest request, BindingResult bindResult) {
        DispatchFlow dispatchFlow = new DispatchFlow();
        BeanUtils.copyProperties(request,dispatchFlow);
        if(dispatchFlow.getUpdateTime() == null){
            dispatchFlow.setUpdateTime(new Date());
        }
        dispatchFlowService.update(dispatchFlow);
        return ApiResponse.ok();
    }

    @GetMapping("/{flowId}")
    @ApiOperation(value = "根据ID获取流程信息", notes = "根据ID获取流程信息")
    public ApiEntityResponse getById(@PathVariable(name = "flowId") String flowId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get dispatch flow info by flowId. flowId = [{}]",flowId);
        }
        DispatchFlow dispatchFlow =dispatchFlowService.selectById(flowId);
        DispatchFlowInfo dispatchFlowInfo = Optional.ofNullable(dispatchFlow).map(flow ->{
            DispatchFlowInfo flowInfo = new DispatchFlowInfo();
            BeanUtils.copyProperties(flow,flowInfo);
            return flowInfo;
        }).orElseGet(DispatchFlowInfo::new);
        return ApiEntityResponse.ok(dispatchFlowInfo);
    }



}
