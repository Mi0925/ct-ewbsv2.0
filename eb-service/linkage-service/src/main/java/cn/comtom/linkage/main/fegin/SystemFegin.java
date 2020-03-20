package cn.comtom.linkage.main.fegin;


import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.domain.system.sequence.request.SequenceUpdateRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@FeignClient(value = "system-service")
public interface SystemFegin {

    @RequestMapping(value = "/system/param/getByKey",method = RequestMethod.GET)
    public ApiEntityResponse<SysParamsInfo> getByKey(@RequestParam(name = "paramKey") String paramKey);

    @RequestMapping(value = "/system/node/isWhite",method = RequestMethod.GET)
    public ApiResponse isWhite(@RequestParam(name = "ip") String ip);

    @RequestMapping(value = "/system/node/getByPlatformId",method = RequestMethod.GET)
    ApiEntityResponse<AccessNodeInfo> getByPlatformId(@RequestParam(name = "platformId") String platformId);

    @RequestMapping(value = "/system/seq/getValue",method = RequestMethod.GET)
    ApiEntityResponse<SysSequenceInfo> getValue(@RequestParam(name = "name") String name);

    @RequestMapping(value = "/system/seq/update",method = RequestMethod.PUT)
    public ApiResponse updateById(@RequestBody SequenceUpdateRequest request);

    @RequestMapping(value = "/system/plan/getMatchPlan",method = RequestMethod.GET)
    ApiListResponse<SysPlanMatchInfo> getMatchPlan(@RequestParam(value = "severity",required = false) String severity,
                                                   @RequestParam(value = "eventType",required = false) String eventType,
                                                   @RequestParam(value = "srcEbrId",required = false) String srcEbrId,
                                                   @RequestParam(value = "areaCodes",required = false) String areaCodes);

    @RequestMapping(value = "/system/plan/res/getByPlanId",method = RequestMethod.GET)
    ApiListResponse<SysPlanResoRefInfo> getPlanResRefByPlanId(@RequestParam(name = "planId") String planId);

    @RequestMapping(value = "/system/region/area/{areaCode}",method = RequestMethod.GET)
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaByAreaCode(@PathVariable(name = "areaCode") String areaCode);

    @RequestMapping(value = "/system/region/area/getByAreaCodes",method = RequestMethod.GET)
    public ApiListResponse<RegionAreaInfo> getRegionAreaByAreaCodes(@RequestParam(name = "areaCodes") List<String> areaCodes);

    @RequestMapping(value = "/system/log/operate/save",method = RequestMethod.POST)
    public ApiEntityResponse<SysOperateLogInfo> saveSysOperateLog(@RequestBody @Valid SysOperateLogAddRequest request);

}
