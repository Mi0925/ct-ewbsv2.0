package cn.comtom.system.main.resource.controller;


import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysresource.request.SysResourceAddRequest;
import cn.comtom.domain.system.sysresource.request.SysResourcePageRequest;
import cn.comtom.domain.system.sysresource.request.SysResourceQueryRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.resource.entity.dbo.SysResource;
import cn.comtom.system.main.resource.service.ISysResourceService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/resource")
@Api(tags = "系统资源信息")
@Slf4j
public class SysResourceController extends BaseController {

    @Autowired
    private ISysResourceService sysResourceService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询系统资源", notes = "根据ID查询系统资源")
    public ApiEntityResponse<SysResourceInfo> getById(@PathVariable(name = "id") String id) {
        SysResource sysResource = sysResourceService.selectById(id);
        if(sysResource == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        SysResourceInfo sysResourceInfo = new SysResourceInfo();
        BeanUtils.copyProperties(sysResource,sysResourceInfo);
        return ApiEntityResponse.ok(sysResourceInfo);

    }

    @GetMapping("/getByIds")
    @ApiOperation(value = "根据ID批量查询系统资源", notes = "根据ID批量查询系统资源")
    public ApiListResponse<SysResourceInfo> getByIds(@RequestParam(name = "ids") List<String> ids) {
        List<SysResource> sysResourceList = sysResourceService.selectByIds(ids);
        log.info("getByIds.sysResourceList:{}",JSON.toJSONString(sysResourceList));
        if(sysResourceList == null){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.QUERY_NO_DATA);
        }
        List<SysResourceInfo> resultList = new ArrayList<>();
        sysResourceList.forEach(sysResource -> {
            SysResourceInfo sysResourceInfo = new SysResourceInfo();
            BeanUtils.copyProperties(sysResource,sysResourceInfo);
            resultList.add(sysResourceInfo);
        });
        log.info("getByIds.resultList:{}",JSON.toJSONString(resultList));
        return ApiListResponse.ok(resultList);

    }

    @PostMapping("/save")
    @ApiOperation(value = "保存系统资源信息", notes = "保存系统资源信息")
    public ApiEntityResponse<SysResourceInfo> save(@RequestBody SysResourceAddRequest request, BindingResult result) {
        SysResource sysResource = new SysResource();
        String resId = UUIDGenerator.getUUID();
        if(request != null){
            BeanUtils.copyProperties(request,sysResource);
        }
        sysResource.setId(resId);
        sysResourceService.save(sysResource);
        SysResourceInfo sysResourceInfo = new SysResourceInfo();
        BeanUtils.copyProperties(sysResource,sysResourceInfo);
        return ApiEntityResponse.ok(sysResourceInfo);
    }

    @GetMapping("/getByPage")
    @ApiOperation(value = "分页查询系统资源", notes = "分页查询系统资源")
    public ApiPageResponse<SysResourceInfo> getByPage(@ModelAttribute @Valid SysResourcePageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("query system resources by page. request=[{}]",request);
        }
        startPage(request);
        SysResourceQueryRequest sysResourceQueryRequest = new SysResourceQueryRequest();
        BeanUtils.copyProperties(request,sysResourceQueryRequest);
        List<SysResourceInfo> resultList = sysResourceService.list(sysResourceQueryRequest);
        return ApiPageResponse.ok(resultList,page);

    }

    @GetMapping("/getByPid")
    @ApiOperation(value = "根据父ID查询系统资源", notes = "根据父ID查询系统资源")
    public ApiListResponse<SysResourceInfo> getByPid(@RequestParam(name = "pid") String pid) {
        if(log.isDebugEnabled()){
            log.debug("query system resources by pid. pid=[{}]",pid);
        }
        List<SysResourceInfo> resultList = sysResourceService.getByPid(pid);
        return ApiListResponse.ok(resultList);

    }

    @GetMapping("/getSysResAndChildren")
    @ApiOperation(value = "查询系统资源及其子资源列表", notes = "查询系统资源及其子资源列表")
    public ApiListResponse<SysResourceInfo> getSysResAndChildren(@Valid @ModelAttribute SysResourcePageRequest request){
        startPage(request);
        SysResourceQueryRequest sysResourceQueryRequest = new SysResourceQueryRequest();
        BeanUtils.copyProperties(request,sysResourceQueryRequest);
        List<SysResourceInfo> sysResourceInfoList = sysResourceService.list(sysResourceQueryRequest);
        Optional.ofNullable(sysResourceInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
               // .filter(sysResourceInfo -> StringUtils.isNotBlank(sysResourceInfo.getPid()))
                .forEach(sysResourceInfo -> {
                    List<SysResourceInfo> subList = sysResourceService.getByPid(sysResourceInfo.getId());
                    //过滤掉空元素，和重复元素,并按照排序号进行排序
                    subList = Optional.ofNullable(subList).orElse(Collections.emptyList()).stream()
                            .filter(Objects::nonNull)
                            .distinct()
                            .sorted(Comparator.comparing(SysResourceInfo::getOrderNum))
                            .collect(Collectors.toList());
                    sysResourceInfo.setSubList(subList);
                });
        return ApiListResponse.ok(sysResourceInfoList);
    }

    @GetMapping("/getAllWithRecursion")
    @ApiOperation(value = "查询系统资源及其子资源列表", notes = "查询系统资源及其子资源列表")
    public ApiListResponse<SysResourceInfo> getAllWithRecursion(){
        if(log.isDebugEnabled()){
            log.debug("query system resources with recursion. ");
        }
        return sysResourceService.getAllWithRecursion();
    }



}
