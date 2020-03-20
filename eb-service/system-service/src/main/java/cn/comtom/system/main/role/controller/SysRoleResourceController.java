package cn.comtom.system.main.role.controller;


import cn.comtom.domain.system.roleresource.info.SysRoleResourceInfo;
import cn.comtom.domain.system.roleresource.request.RoleResAddBatchRequest;
import cn.comtom.domain.system.roleresource.request.SysRoleResourceAddRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.role.entity.dbo.SysRoleResource;
import cn.comtom.system.main.role.service.ISysRoleResourceService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/roleRes")
@Api(tags = "角色资源绑定关系接口")
@Slf4j
public class SysRoleResourceController extends BaseController {

    @Autowired
    private ISysRoleResourceService sysRoleResourceService;


    @PostMapping("/save")
    @ApiOperation(value = "保存角色资源绑定关系", notes = "保存角色资源绑定关系")
    public ApiEntityResponse<SysRoleResourceInfo> save(@RequestBody SysRoleResourceAddRequest request, BindingResult result) {
        SysRoleResource sysRoleResource = new SysRoleResource();
        String resId = UUIDGenerator.getUUID();
        if(request != null){
            BeanUtils.copyProperties(request,sysRoleResource);
        }
        sysRoleResource.setId(resId);
        sysRoleResourceService.save(sysRoleResource);
        SysRoleResourceInfo sysRoleResourceInfo = new SysRoleResourceInfo();
        BeanUtils.copyProperties(sysRoleResource,sysRoleResourceInfo);
        return ApiEntityResponse.ok(sysRoleResourceInfo);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存角色资源绑定关系", notes = "批量保存角色资源绑定关系")
    public ApiEntityResponse<Integer> saveBatch(@RequestBody @Valid RoleResAddBatchRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~]save role resource batch . request=[{}]",JSON.toJSONString(request));
        }
        List<String> resoList = request.getResourceIdList();
        List<SysRoleResource> sysRoleResourceList = Optional.ofNullable(resoList).orElse(Collections.emptyList()).stream()
                .filter(resId -> StringUtils.isNotBlank(resId))
                .map(resId ->{
                    SysRoleResource sysRoleResource  = new SysRoleResource();
                    sysRoleResource.setRoleId(request.getRoleId());
                    sysRoleResource.setResourceId(resId);
                    sysRoleResource.setId(UUIDGenerator.getUUID());
                    return sysRoleResource;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(sysRoleResourceService.saveList(sysRoleResourceList));
    }

    @GetMapping("/getByRoleIds")
    @ApiOperation(value = "根据角色ID批量查询角色资源绑定关系", notes = "根据角色ID批量查询角色资源绑定关系")
    public ApiListResponse<SysRoleResourceInfo> getByRoleIds(@RequestParam(name = "roleIds") List<String> roleIds) {
        if(log.isDebugEnabled()){
            log.debug("[!~] get SysRoleResourceInfo list by roleIds .  roleIds=[{}]",roleIds);
        }
        if(roleIds ==  null || roleIds.isEmpty()){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        List<SysRoleResource> sysRoleResourceList = sysRoleResourceService.getByRoleIds(roleIds);
        if (sysRoleResourceList == null ) {
            return  ApiResponseBuilder.buildListError(SystemErrorEnum.QUERY_NO_DATA);
        }
        List<SysRoleResourceInfo> resultList = new ArrayList<>();
        sysRoleResourceList.forEach(sysRoleResource -> {
            SysRoleResourceInfo sysRoleResourceInfo = new SysRoleResourceInfo();
            BeanUtils.copyProperties(sysRoleResource,sysRoleResourceInfo);
            resultList.add(sysRoleResourceInfo);
        });
        return ApiListResponse.ok(resultList);
    }

    @DeleteMapping("/deleteByRoleId")
    @ApiOperation(value = "根据角色ID删除角色资源绑定关系", notes = "根据角色ID删除角色资源绑定关系")
    public ApiEntityResponse<Integer> deleteByRoleId(@RequestParam(name = "roleId") String roleId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete SysRoleResourceInfo by roleIds .  roleId=[{}]",roleId);
        }
        if(StringUtils.isBlank(roleId)){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysRoleResource sysRoleResource = new SysRoleResource();
        sysRoleResource.setRoleId(roleId);
        return ApiEntityResponse.ok(sysRoleResourceService.delete(sysRoleResource));
    }

    @DeleteMapping("/deleteByResourceId")
    @ApiOperation(value = "根据资源ID删除角色资源绑定关系", notes = "根据资源ID删除角色资源绑定关系")
    public ApiEntityResponse<Integer> deleteByResourceId(@RequestParam(name = "resourceId") String resourceId) {
        if(log.isDebugEnabled()){
            log.debug("[!~] delete SysRoleResourceInfo by resourceId .  resourceId=[{}]",resourceId);
        }
        if(StringUtils.isBlank(resourceId)){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysRoleResource sysRoleResource = new SysRoleResource();
        sysRoleResource.setResourceId(resourceId);
        return ApiEntityResponse.ok(sysRoleResourceService.delete(sysRoleResource));
    }



}
