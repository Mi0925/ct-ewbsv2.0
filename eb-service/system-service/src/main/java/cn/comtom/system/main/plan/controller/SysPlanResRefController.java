package cn.comtom.system.main.plan.controller;


import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.plan.request.SysPlanResoRefAddRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import cn.comtom.system.main.plan.service.ISysPlanResRefService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/system/plan/res")
@Api(tags = "调度预案关联播出资源")
@Slf4j
public class SysPlanResRefController extends BaseController {

    @Autowired
    private ISysPlanResRefService sysPlanResRefService;


    @GetMapping("/getByPlanId")
    @ApiOperation(value = "根据预案ID查询关联的播出资源", notes = "根据预案ID查询关联的播出资源")
    public ApiListResponse<SysPlanResoRefInfo> getByPlanId(@RequestParam(name = "planId")  String planId) {
        if(log.isDebugEnabled()){
            log.debug("get  resources by planId planId=[{}]",planId);
        }
        if(StringUtils.isBlank(planId)){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        List<SysPlanResoRefInfo> sysPlanResoRefInfoList = sysPlanResRefService.getByPlanId(planId);
        return ApiListResponse.ok(sysPlanResoRefInfoList);
    }

}
