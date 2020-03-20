package cn.comtom.ebs.front.main.plan.controller;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.request.SysPlanDelRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.plan.service.ISysPlanMathService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/safeRest/plan/math")
@Api(tags = "预案管理", description = "预案管理")
public class SysPlanMathController extends AuthController {

    @Autowired
    private ISysPlanMathService sysPlanMathService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询预案信息", notes = "分页查询预案信息")
    @RequiresPermissions(Permissions.SYS_PLAN_VIEW)
    public ApiPageResponse<SysPlanMatchInfo> page(@Valid SysPlanPageRequest pageRequest, BindingResult bResult) {
        return sysPlanMathService.page(pageRequest);
    }

    @ApiOperation(value = "删除预案信息", notes = "删除预案信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    @RequiresPermissions(Permissions.SYS_PLAN_DELETE)
    public ApiResponse delete(@RequestBody SysPlanDelRequest request) {
        sysPlanMathService.delete(request);
        return ApiResponse.ok();
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存预案", notes = "保存预案")
    @RequiresPermissions(Permissions.SYS_PLAN_ADD)
    public ApiEntityResponse<SysPlanMatchInfo> save(@Valid @RequestBody SysPlanMatchAddRequest request , BindingResult result) {
        SysPlanMatchInfo sysPlanMatchInfo=sysPlanMathService.save(request);
        if(sysPlanMatchInfo==null){
            return ApiEntityResponse.error(FrontErrorEnum.PLAN_MATH_IS_EXIST.getCode(),FrontErrorEnum.PLAN_MATH_IS_EXIST.getMsg());
        }
        return ApiEntityResponse.ok(sysPlanMatchInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改调度预案", notes = "修改调度预案")
    @RequiresPermissions(Permissions.SYS_PLAN_EDIT)
    public ApiEntityResponse<SysPlanMatchInfo> update(@RequestBody @Valid SysPlanMatchUpdRequest request , BindingResult result) {
        SysPlanMatchInfo sysPlanMatchInfo=sysPlanMathService.update(request);
        if(sysPlanMatchInfo==null){
            return ApiEntityResponse.error(FrontErrorEnum.PLAN_MATH_IS_EXIST.getCode(),FrontErrorEnum.PLAN_MATH_IS_EXIST.getMsg());
        }
        return ApiEntityResponse.ok(sysPlanMatchInfo);
    }

    @GetMapping("/{planId}")
    @ApiOperation(value = "根据planId预案详情", notes = "根据planId预案详情")
    @RequiresPermissions(Permissions.SYS_PLAN_VIEW)
    public ApiEntityResponse<SysPlanMatchInfo> getByPlanId(@PathVariable(name = "planId") String planId){
        SysPlanMatchInfo info=sysPlanMathService.getByPlanId(planId);
        return ApiEntityResponse.ok(info);
    }

}
