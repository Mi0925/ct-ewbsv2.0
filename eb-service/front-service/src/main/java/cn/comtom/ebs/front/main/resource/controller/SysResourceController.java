package cn.comtom.ebs.front.main.resource.controller;

import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysresource.request.SysResourcePageRequest;
import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.oauth2.service.ShiroService;
import cn.comtom.ebs.front.main.resource.service.ISysResourceService;
import cn.comtom.ebs.front.main.role.service.ISysRoleResourceService;
import cn.comtom.ebs.front.main.user.service.ISysUserRoleService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/safeRest/sysRes")
@Api(tags = "系统资源管理", description = "系统资源管理")
public class SysResourceController extends AuthController {

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    ISysUserRoleService sysUserRoleService;

    @Autowired
    ISysRoleResourceService sysRoleResourceService;

    @Autowired
    private ShiroService shiroService;

    @GetMapping("/getByPage")
    @ApiOperation(value = "分页查询系统资源列表", notes = "分页查询系统资源列表")
    public ApiPageResponse<SysResourceInfo> getSysResourcesByPage(@Valid SysResourcePageRequest request){
        if(log.isDebugEnabled()){
            log.debug("query system resources by page . request=[{}]",request);
        }
        return sysResourceService.getSysResourceInfoListPage(request);
    }

    @GetMapping("/getByPid")
    @ApiOperation(value = "根据父ID查询系统资源列表", notes = "根据父ID查询系统资源列表")
    public ApiListResponse<SysResourceInfo> getByPid(@RequestParam(name = "pid") String pid){
        if(log.isDebugEnabled()){
            log.debug("query system resources by pid . pid=[{}]",pid);
        }
        List<SysResourceInfo> sysResourceInfoList = sysResourceService.getByPid(pid);
        return ApiListResponse.ok(sysResourceInfoList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询系统资源列表", notes = "根据ID查询系统资源列表")
    public ApiEntityResponse<SysResourceInfo> getById(@PathVariable(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("query system resources by id . id=[{}]",id);
        }
        SysResourceInfo sysResourceInfo = sysResourceService.getSysResourceById(id);
        return ApiEntityResponse.ok(sysResourceInfo);
    }

    @GetMapping("/getSysResAndChildren")
    @ApiOperation(value = "根据用户权限查询系统资源及其子资源列表", notes = "根据用户权限查询系统资源及其子资源列表")
    public ApiPageResponse<SysResourceInfo> getSysResAndChildren(@Valid SysResourcePageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] query system resources and children  . request=[{}]",request);
        }

        //获取登录用户的权限
        SysUserInfo userInfo = getUser();
        String userId = userInfo.getUserId();
        Set<String> permCodes = shiroService.getPermCodesByUserId(userId);
        if(log.isDebugEnabled()){
            log.debug("[!~] user permCodes is ： permCodes=[{}]",permCodes);
        }
        if(permCodes.isEmpty()){
            return ApiPageResponse.ok(new ArrayList<>());
        }
        //查询所有一级菜单列表
        ApiPageResponse<SysResourceInfo> response = sysResourceService.getSysResAndChildren(request);
        //获取所有一级菜单列表
        List<SysResourceInfo> sysResourceInfoList =  Optional.ofNullable(response).map(ApiPageResponse::getData).orElse(null);
        if(log.isDebugEnabled()){
            log.debug("[!~] all  the first level  system resource is ： [{}]",sysResourceInfoList);
        }

        sysResourceInfoList = Optional.ofNullable(sysResourceInfoList).orElse(Collections.emptyList()).stream()
                .filter(sysResourceInfo -> {
                    //过滤无权限的一级菜单
                    return StringUtils.isNotBlank(sysResourceInfo.getPerms())
                            && permCodes.contains(sysResourceInfo.getPerms());
                })
                .peek(sysResourceInfo -> {
                    //过滤无权限的二级菜单
                    List<SysResourceInfo> subList = sysResourceInfo.getSubList();
                    subList = Optional.ofNullable(subList).orElse(Collections.emptyList()).stream()
                            .filter(sysResourceInfo1 -> {
                                return StringUtils.isNotBlank(sysResourceInfo1.getPerms())
                                        && permCodes.contains(sysResourceInfo1.getPerms());
                            }).collect(Collectors.toList());
                    sysResourceInfo.setSubList(subList);
                })
                .collect(Collectors.toList());

        if(log.isDebugEnabled()){
            log.debug("[!~] user permitted system resource is ： [{}]",sysResourceInfoList);
        }
        response.setData(sysResourceInfoList);
        return response;
    }

    @GetMapping("/getAllWithRecursion")
    @ApiOperation(value = "递归查询所有系统资源", notes = "递归查询所有系统资源")
    public ApiListResponse<SysResourceInfo> getAllWithRecursion(){
        List<SysResourceInfo> sysResourceInfoList = sysResourceService.getAllWithRecursion();
        return ApiListResponse.ok(sysResourceInfoList);
    }

    @GetMapping("/infoSrc/page")
    @ApiOperation(value = "查询信息源列表", notes = "分页查询信息源列表")
    public ApiListResponse<SysResourceInfo> getInfoSrcByPage(@Valid SysResourcePageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] user permitted system resource is ： [{}]",request);
        }
        List<SysResourceInfo> sysResourceInfoList = sysResourceService.getAllWithRecursion();
        return ApiListResponse.ok(sysResourceInfoList);
    }

}
