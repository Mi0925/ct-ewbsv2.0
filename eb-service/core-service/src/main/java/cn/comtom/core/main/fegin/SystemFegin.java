package cn.comtom.core.main.fegin;


import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.domain.system.dict.info.SysDictGroupInfo;
import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.domain.system.dict.request.SysDictAddRequest;
import cn.comtom.domain.system.dict.request.SysDictGroupAddRequest;
import cn.comtom.domain.system.dict.request.SysDictGroupUpdateRequest;
import cn.comtom.domain.system.dict.request.SysDictUpdateRequest;
import cn.comtom.domain.system.eventtype.info.EventtypeInfo;
import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.plan.request.SysPlanDelRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRoleAddRequest;
import cn.comtom.domain.system.role.request.SysRoleUpdateRequest;
import cn.comtom.domain.system.roleresource.info.SysRoleResourceInfo;
import cn.comtom.domain.system.roleresource.request.RoleResAddBatchRequest;
import cn.comtom.domain.system.sequence.info.SysSequenceInfo;
import cn.comtom.domain.system.sequence.request.SequenceUpdateRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamsUpdateRequest;
import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.SysUserUpdateRequest;
import cn.comtom.domain.system.sysuser.request.UserAddRequest;
import cn.comtom.domain.system.sysuserrole.request.SysUserRoleAddBatchRequest;
import cn.comtom.domain.system.usertoken.info.UserTokenInfo;
import cn.comtom.domain.system.usertoken.request.UserTokenAddRequest;
import cn.comtom.domain.system.usertoken.request.UserTokenUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@FeignClient(value = "system-service")
public interface SystemFegin {

    @RequestMapping(value = "/system/userToken/update",method = RequestMethod.PUT)
    public ApiResponse updateUserToken(@RequestBody UserTokenUpdateRequest request);

    @RequestMapping(value = "/system/userToken/{tokenId}",method = RequestMethod.GET)
    public ApiEntityResponse<UserTokenInfo> getUserTokenById(@PathVariable(name = "tokenId") String tokenId);

    @RequestMapping(value = "/system/userToken/getToken",method = RequestMethod.GET)
    public ApiEntityResponse<UserTokenInfo> getToken(@RequestParam(name = "userId") String userId, @RequestParam(name = "clientType") String clientType);

    @RequestMapping(value = "/system/userToken/getByAccessToken",method = RequestMethod.GET)
    public ApiEntityResponse<UserTokenInfo> getByAccessToken(@RequestParam(name = "accessToken") String accessToken);

    @RequestMapping(value = "/system/userToken/getByRefreshToken",method = RequestMethod.GET)
    public ApiEntityResponse<UserTokenInfo> getByRefreshToken(@RequestParam(name = "refreshToken") String refreshToken);

    @RequestMapping(value = "/system/userToken/delete/{tokenId}",method = RequestMethod.DELETE)
    public ApiResponse deleteUserTokenById(@PathVariable(name = "tokenId") String tokenId);

    @RequestMapping(value = "/system/userToken/save",method = RequestMethod.POST)
    public ApiEntityResponse<UserTokenInfo> saveUserToken(@RequestBody UserTokenAddRequest request);

    @RequestMapping(value = "/system/users/save",method = RequestMethod.POST)
    public ApiEntityResponse<SysUserInfo> saveUserInfo(@RequestBody UserAddRequest request);

    @RequestMapping(value = "/system/users/{userId}",method = RequestMethod.GET)
    public ApiEntityResponse<SysUserInfo> getUserInfoById(@PathVariable(name = "userId") String userId);

    @RequestMapping(value = "/system/users/getByAccount",method = RequestMethod.GET)
    public ApiEntityResponse<SysUserInfo> getUserInfoByAccount(@RequestParam(name = "account") String account);

    @RequestMapping(value = "/system/users/authorize",method = RequestMethod.GET)
    public ApiEntityResponse<SysUserInfo> login(@RequestParam(name = "account") String account, @RequestParam(name = "password") String password);

    @RequestMapping(value = "/system/users/update",method = RequestMethod.PUT)
    public ApiResponse updateUserInfo(@RequestBody SysUserUpdateRequest request);

    @RequestMapping(value = "/system/userRole/getUserRoleIds",method = RequestMethod.GET)
    public ApiListResponse<String> getUserRoleIds(@RequestParam(name = "userId") String userId);


    @RequestMapping(value = "/system/resource/getByIds",method = RequestMethod.GET)
    public ApiListResponse<SysResourceInfo> getSysResourceByIds(@RequestParam(name = "ids") List<String> ids);

    @RequestMapping(value = "/system/resource/getAllWithRecursion",method = RequestMethod.GET)
    public ApiListResponse<SysResourceInfo> getAllWithRecursion();

    @RequestMapping(value = "/system/roleRes/getByRoleIds",method = RequestMethod.GET)
    public ApiListResponse<SysRoleResourceInfo> getSysRoleResourceByRoleIds(@RequestParam(name = "roleIds") List<String> roleIds);

    @RequestMapping(value = "/system/roleRes/saveBatch",method = RequestMethod.POST)
    public ApiEntityResponse<Integer> saveSysRoleResourceBatch(@RequestBody RoleResAddBatchRequest request);

    @RequestMapping(value = "/system/param/getByKey",method = RequestMethod.GET)
    public ApiEntityResponse<SysParamsInfo> getByKey(@RequestParam(name = "paramKey") String paramKey);

    @RequestMapping(value = "/system/node/isWhite",method = RequestMethod.GET)
    public ApiResponse isWhite(@RequestParam(name = "ip") String ip);

    @RequestMapping(value = "/system/node/getByPlatformId",method = RequestMethod.GET)
    ApiEntityResponse<AccessNodeInfo> getAccessNodeByPlatformId(@RequestParam(name = "platformId") String platformId);

    @PostMapping(value = "/system/node/save")
    ApiEntityResponse<AccessNodeInfo> saveAccessNode(@RequestBody AccessNodeRequest request);

    @RequestMapping(value = "/system/seq/getValue",method = RequestMethod.GET)
    ApiEntityResponse<SysSequenceInfo> getValue(@RequestParam(name = "name") String name);

    @RequestMapping(value = "/system/seq/update",method = RequestMethod.PUT)
    public ApiResponse updateById(@RequestBody SequenceUpdateRequest request);

    @RequestMapping(value = "/system/plan/getMatchPlan",method = RequestMethod.GET)
    ApiListResponse<SysPlanMatchInfo> getMatchPlan(@RequestParam(value = "severity",required = false) String severity,
                                                   @RequestParam(value = "eventType",required = false) String eventType,
                                                   @RequestParam(value = "areaCodes",required = false) String areaCodes);

    @RequestMapping(value = "/system/plan/res/getByPlanId",method = RequestMethod.GET)
    ApiListResponse<SysPlanResoRefInfo> getPlanResRefByPlanId(@RequestParam(name = "planId") String planId);

    @RequestMapping(value = "/system/region/area/{areaCode}",method = RequestMethod.GET)
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaByAreaCode(@PathVariable(name = "areaCode") String areaCode);

    @RequestMapping(value = "/system/region/area/getByAreaCodes",method = RequestMethod.GET)
    public ApiListResponse<RegionAreaInfo> getRegionAreaByAreaCodes(@RequestParam(name = "areaCodes") List<String> areaCodes);

    @RequestMapping(value = "/system/log/operate/save",method = RequestMethod.POST)
    public ApiEntityResponse<SysOperateLogInfo> saveSysOperateLog(@RequestBody @Valid SysOperateLogAddRequest request);

    @GetMapping(value = "/system/resource/getByPage")
    ApiPageResponse<SysResourceInfo> getSysResourceByPage(@RequestParam Map<String, Object> paramMap);

    @GetMapping(value = "/system/resource/getSysResAndChildren")
    ApiPageResponse<SysResourceInfo> getSysResAndChildren(@RequestParam Map<String, Object> paramMap);

    @GetMapping(value = "/system/resource/getByPid")
    ApiListResponse<SysResourceInfo> getSysResourceByPid(@RequestParam(name = "pid") String pid);

    @GetMapping(value = "/system/resource/{id}")
    ApiEntityResponse<SysResourceInfo> getSysResourceById(@PathVariable(name = "id") String id);


    @GetMapping(value = "/system/dict/{dictId}")
    ApiEntityResponse<SysDictInfo> getSysDictInfoById(@PathVariable(name = "dictId") String dictId);

    @PostMapping(value = "/system/dict/save")
    ApiEntityResponse<SysDictInfo> saveSysDictInfo(@RequestBody SysDictAddRequest request);

    @PutMapping(value = "/system/dict/update")
    ApiResponse updateSysDictInfo(@RequestBody SysDictUpdateRequest request);

    @DeleteMapping(value = "/system/dict/delete/{dictId}")
    ApiResponse deleteSysDictInfo(@PathVariable(name = "dictId") String dictId);

    @GetMapping(value = "/system/dict/getByDictKey")
    ApiEntityResponse<SysDictInfo> getSysDictInfoByDictKey(@RequestParam(name = "dictKey") String dictKey);

    @GetMapping(value = "/system/dict/getByGroupCode")
    ApiListResponse<SysDictInfo> getSysDictInfoByGroupCode(@RequestParam(name = "dictGroupCode") String dictGroupCode);

    @GetMapping(value = "/system/dict/group/{dictGroupId}")
    ApiEntityResponse<SysDictGroupInfo> getSysDictGroupInfoById(@PathVariable(name = "dictGroupId") String dictGroupId);

    @PostMapping(value = "/system/dict/group/save")
    ApiEntityResponse<SysDictGroupInfo> saveSysDictGroupInfo(@RequestBody SysDictGroupAddRequest request);

    @PutMapping(value = "/system/dict/group/update")
    ApiResponse updateSysDictGroupInfo(@RequestBody SysDictGroupUpdateRequest request);

    @DeleteMapping(value = "/system/dict/group/delete/{dictGroupId}")
    ApiResponse deleteSysDictGroupInfo(@PathVariable(name = "dictGroupId") String dictGroupId);

    @RequestMapping(value = "/system/users",method = RequestMethod.GET)
    ApiEntityResponse<List<SysUserInfo>> findUserPage(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/system/param/page",method = RequestMethod.GET)
    ApiPageResponse<SysParamsInfo> findSysParamPage(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/system/role/page",method = RequestMethod.GET)
    ApiPageResponse<SysRoleInfo> findSysRolePage(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/system/plan/page",method = RequestMethod.GET)
    ApiPageResponse<SysPlanMatchInfo> findSysPlanMathPage(@RequestParam Map<String, Object> whereRequestMap);

    @RequestMapping(value = "/system/plan/delete",method = RequestMethod.DELETE)
    void deleteSysPlanMathById(@RequestBody SysPlanDelRequest request);

    @RequestMapping(value = "/system/plan/save",method = RequestMethod.POST)
    ApiEntityResponse<SysPlanMatchInfo> saveSysPlanMath(@RequestBody SysPlanMatchAddRequest request);

    @RequestMapping(value = "/system/plan/update",method = RequestMethod.PUT)
    ApiEntityResponse<SysPlanMatchInfo> updateSysPlanMath(@RequestBody SysPlanMatchUpdRequest request);

    @PostMapping(value = "/system/userRole/saveBatch")
    ApiResponse saveSysUserRoleBatch(@RequestBody @Valid SysUserRoleAddBatchRequest request);

    @DeleteMapping(value = "/system/userRole/deleteByUserId")
    ApiEntityResponse<Integer> deleteUserRoleByUserId(@RequestParam(name = "userId") String userId);

    @GetMapping(value = "/system/role/{roleId}")
    ApiEntityResponse<SysRoleInfo> getSysRoleInfoById(@PathVariable(name = "roleId") String roleId);

    @DeleteMapping(value = "/system/role/delete/{roleId}")
    ApiResponse deleteRoleInfoById(@PathVariable(name = "roleId") String roleId);

    @DeleteMapping(value = "/system/roleRes/deleteByRoleId")
    ApiEntityResponse<Integer> deleteRoleResourceByRoleId(@RequestParam(name = "roleId") String roleId);

    @DeleteMapping(value = "/system/roleRes/deleteByResourceId")
    ApiEntityResponse<Integer> deleteRoleResourceByResourceId(@RequestParam(name = "resourceId") String resourceId);

    @DeleteMapping(value = "/system/userRole/deleteByRoleId")
    ApiEntityResponse<Integer> deleteUserRoleByRoleId(@RequestParam(name = "roleId") String roleId);

    @PostMapping(value = "/system/role/save")
    ApiEntityResponse<SysRoleInfo> saveSysRoleInfo(@RequestBody @Valid SysRoleAddRequest request);

    @PutMapping(value = "/system/role/update")
    ApiResponse updateSysRoleInfo(@RequestBody @Valid SysRoleUpdateRequest request);

    @RequestMapping(value = "/system/plan/{planId}",method = RequestMethod.GET)
    ApiEntityResponse<SysPlanMatchInfo> getByPlanId(@PathVariable(name = "planId") String planId);

    @RequestMapping(value = "/system/region/area/getNextAreaCodeByParentCode",method = RequestMethod.GET)
    ApiEntityResponse<List<RegionAreaInfo>> getNextAreaCodeByParentCode(@RequestParam(name = "parentAreaCode") String parentAreaCode);

    @RequestMapping(value = "/system/event/type/getNextByParentCode",method = RequestMethod.GET)
    ApiEntityResponse<List<EventtypeInfo>> getEventNextByParentCode(@RequestParam(name = "parentCode") String parentCode);

    @GetMapping(value = "/system/param/{id}")
    ApiEntityResponse<SysParamsInfo> getSysParamsInfoById(@PathVariable(name = "id") String id);

    @PutMapping(value = "/system/param/update")
    ApiResponse updateSysParamsInfo(@RequestBody @Valid SysParamsUpdateRequest request);

    @DeleteMapping(value = "/system/node/delete/{ebrId}")
    ApiResponse deleteAccessNodeByEbrId(@PathVariable(name = "ebrId") String ebrId);

    @PutMapping(value = "/system/node/update")
    ApiResponse updateAccessNode(@RequestBody @Valid AccessNodeRequest request);

    @GetMapping(value = "/system/region/area/getNextAreaCodeByParentCode")
    ApiListResponse<RegionAreaInfo> getRegionAreaByParentAreaCode(@RequestParam(name = "parentAreaCode") String parentAreaCode);

    @GetMapping(value = "/system/region/area/getAreaTotal")
    ApiEntityResponse<Integer> getAreaTotal();

    @GetMapping(value = "/system/region/area/getNextReginCodeByParentCode")
    ApiEntityResponse<List<RegionAreaInfo>> getNextReginCodeByParentCode(@RequestParam(name = "parentAreaCode") String parentAreaCode);

    @PostMapping(value = "/system/region/area/sureInitArea")
    ApiResponse sureInitArea(@RequestBody String areaCode);

    @GetMapping(value = "/system/region/area/rootArea")
    ApiEntityResponse<RegionAreaInfo> getRootArea();

}
