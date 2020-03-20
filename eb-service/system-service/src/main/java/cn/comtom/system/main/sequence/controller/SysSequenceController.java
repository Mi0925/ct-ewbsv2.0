package cn.comtom.system.main.sequence.controller;


import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.domain.system.sequence.request.SequenceUpdateRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.sequence.entity.dbo.SysSequence;
import cn.comtom.system.main.sequence.service.ISysSequenceService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/seq")
@Api(tags = "序列号管理接口")
@Slf4j
public class SysSequenceController extends BaseController {

    @Autowired
    private ISysSequenceService sequenceService;

    @GetMapping("/getValue")
    @ApiOperation(value = "获取序列号", notes = "获取序列号")
    public ApiEntityResponse<SysSequenceInfo> getValue(@RequestParam(name = "name") String name) {
        SysSequenceInfo sequenceInfo = sequenceService.getValue(name);
        if (sequenceInfo == null) {
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(sequenceInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "初始化序列号", notes = "初始化序列号")
    public ApiResponse updateById(@RequestBody @Valid SequenceUpdateRequest request, BindingResult result) {
        SysSequence sequence = new SysSequence();
        BeanUtils.copyProperties(request,sequence);
        sequenceService.update(sequence);
        return ApiResponse.ok();
    }



}
