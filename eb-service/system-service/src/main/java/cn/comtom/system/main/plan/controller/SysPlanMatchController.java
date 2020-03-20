package cn.comtom.system.main.plan.controller;


import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.request.SysPlanAreaAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanDelRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.domain.system.plan.request.SysPlanSeverityAddRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.plan.entity.dbo.SysPlanMatch;
import cn.comtom.system.main.plan.service.ISysPlanMatchService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/system/plan")
@Api(tags = "调度预案信息")
@Slf4j
public class SysPlanMatchController extends BaseController {

    @Autowired
    private ISysPlanMatchService sysPlanMatchService;

    @GetMapping("/getMatchPlan")
    @ApiOperation(value = "获取匹配的预案", notes = "获取匹配的预案")
    public ApiListResponse<SysPlanMatchInfo> getMatchPlan(
            @RequestParam(value = "severity",required = false) String severity,
            @RequestParam(value = "eventType",required = false) String eventType,
            @RequestParam(value = "srcEbrId",required = false) String srcEbrId,
            @RequestParam(value = "areaCodes",required = false) String areaCodes) {
        if(log.isDebugEnabled()){
            log.debug("get match plans by severity and areaCodes. severity=[{}] ,areaCodes=[{}]",severity, JSON.toJSONString(areaCodes));
        }
        if(StringUtils.isNotBlank(srcEbrId)) {
        	areaCodes = null;
        }
        List<SysPlanMatchInfo> list = sysPlanMatchService.getMatchPlan(severity, eventType, srcEbrId, areaCodes);

        return ApiListResponse.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存调度预案", notes = "保存调度预案")
    public ApiEntityResponse<SysPlanMatchInfo> save(@RequestBody @Valid SysPlanMatchAddRequest request , BindingResult result) {

        //增加是否存在判断
        boolean flag=isExist(request.getSeverityList(),request.getEventType(), request.getSrcEbrId(), request.getAreaList());
        if(flag){
            return ApiEntityResponse.error(SystemErrorEnum.QUERY_IS_EXIST.getCode(),SystemErrorEnum.QUERY_IS_EXIST.getMsg());
        }

        SysPlanMatch sysPlanMatch=sysPlanMatchService.saveSysPlanMath(request);
        SysPlanMatchInfo sysPlanMatchInfo = new SysPlanMatchInfo();
        BeanUtils.copyProperties(sysPlanMatch,sysPlanMatchInfo);
        return ApiEntityResponse.ok(sysPlanMatchInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询预案信息", notes = "分页查询预案信息")
    public ApiPageResponse<SysPlanMatchInfo> page(@ModelAttribute @Valid SysPlanPageRequest pageRequest, BindingResult bResult) { ;
        startPage(pageRequest, SysPlanMatch.class);
        List<SysPlanMatchInfo> list = sysPlanMatchService.page(pageRequest);
        return ApiPageResponse.ok(list,page);
    }

    @ApiOperation(value = "删除预案信息", notes = "删除预案信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse delete(@RequestBody SysPlanDelRequest request) {
        sysPlanMatchService.delete(request.getPlanId());
        return ApiResponse.ok();
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改调度预案", notes = "修改调度预案")
    public ApiEntityResponse<SysPlanMatchInfo> update(@RequestBody @Valid SysPlanMatchUpdRequest request , BindingResult result) {

        //增加是否存在判断
        /*boolean flag=isExist(request.getSeverityList(),request.getEventType(),request.getAreaList());
        if(flag){
            return ApiEntityResponse.error(SystemErrorEnum.QUERY_IS_EXIST.getCode(),SystemErrorEnum.QUERY_IS_EXIST.getMsg());
        }*/

        SysPlanMatch sysPlanMatch=sysPlanMatchService.updateSysPlanMath(request);
        SysPlanMatchInfo sysPlanMatchInfo = new SysPlanMatchInfo();
        BeanUtils.copyProperties(sysPlanMatch,sysPlanMatchInfo);
        return ApiEntityResponse.ok(sysPlanMatchInfo);
    }

    /**
     * 判断预案数据是否已经存在
     * @param severityList
     * @param eventType
     * @param areaList
     * @return
     */
    private boolean isExist(List<SysPlanSeverityAddRequest> severityList, String eventType, String srcEbrId, List<SysPlanAreaAddRequest> areaList) {
        StringBuffer severitySb=new StringBuffer();
        for(SysPlanSeverityAddRequest addRequest:severityList){
            severitySb.append(addRequest.getSeverity()).append(",");
        }
        String severity=severitySb.toString();
        if(severity.length()>0){
            severity.substring(0,severity.length()-1);
        }

        StringBuffer areaSb=new StringBuffer();
        for(SysPlanAreaAddRequest addRequest:areaList){
            areaSb.append(addRequest.getAreaCode()).append(",");
        }
        String area=areaSb.toString();
        if(area.length()>0){
            area.substring(0,area.length()-1);
        }
        List<SysPlanMatchInfo> list = sysPlanMatchService.getMatchPlan(severity, eventType, srcEbrId, area);
        if(list.size()==0){
            return false;
        }
        return true;
    }

    @GetMapping("/{planId}")
    @ApiOperation(value = "根据planId预案详情", notes = "根据planId预案详情")
    public ApiEntityResponse<SysPlanMatchInfo> getByPlanId(@PathVariable(name = "planId") String planId){
        SysPlanMatchInfo info=sysPlanMatchService.getByPlanId(planId);
        return ApiEntityResponse.ok(info);
    }


}
