package cn.comtom.system.main.dict.controller;


import cn.comtom.domain.system.dict.info.SysDictGroupInfo;
import cn.comtom.domain.system.dict.request.SysDictGroupAddRequest;
import cn.comtom.domain.system.dict.request.SysDictGroupPageRequest;
import cn.comtom.domain.system.dict.request.SysDictGroupUpdateRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.dict.entity.dbo.SysDictGroup;
import cn.comtom.system.main.dict.service.ISysDictGroupService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/system/dict/group")
@Api(tags = "系统字典分组信息管理接口")
@Slf4j
public class SysDictGroupController extends BaseController {

    @Autowired
    private ISysDictGroupService sysDictGroupService;


    @PostMapping("/save")
    @ApiOperation(value = "保存系统字典分组信息", notes = "保存系统字典分组信息")
    public ApiEntityResponse<SysDictGroupInfo> save(@RequestBody @Valid SysDictGroupAddRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~] save system dict group .  request=[{}]",request);
        }
        SysDictGroupInfo sysDictGroupInfo = new SysDictGroupInfo();
        SysDictGroup sysDictGroup = new SysDictGroup();
        String dictGroupId = UUIDGenerator.getUUID();
        if(request != null){
            BeanUtils.copyProperties(request,sysDictGroup);
        }

        sysDictGroup.setDictGroupId(dictGroupId);
        sysDictGroupService.save(sysDictGroup);
        BeanUtils.copyProperties(sysDictGroup,sysDictGroupInfo);
        return ApiEntityResponse.ok(sysDictGroupInfo);
    }

    @GetMapping("/{dictGroupId}")
    @ApiOperation(value = "根据ID查询系统字典分组信息", notes = "根据ID查询系统字典分组信息")
    public ApiEntityResponse<SysDictGroupInfo> getById(@PathVariable(name = "dictGroupId") String dictGroupId) {
        if(log.isDebugEnabled()){
            log.debug("get system dict group info by dictGroupId   dictGroupId=[{}]",dictGroupId);
        }
        SysDictGroupInfo sysDictGroupInfo = new SysDictGroupInfo();
        SysDictGroup sysDictGroup = sysDictGroupService.selectById(dictGroupId);
        if(sysDictGroup == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        BeanUtils.copyProperties(sysDictGroup,sysDictGroupInfo);
        return ApiEntityResponse.ok(sysDictGroupInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询系统字典分组信息", notes = "分页查询系统字典分组信息")
    public ApiPageResponse<SysDictGroupInfo> getByPage(@ModelAttribute SysDictGroupPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("get system dict group info by page   request=[{}]",request);
        }
        startPage(request);
        SysDictGroupInfo sysDictGroupInfo = new SysDictGroupInfo();
        BeanUtils.copyProperties(request,sysDictGroupInfo);
        List<SysDictGroupInfo> resultList = sysDictGroupService.list(sysDictGroupInfo);
        return ApiPageResponse.ok(resultList,page);
    }


    @PutMapping("/update")
    @ApiOperation(value = "更新系统字典分组信息", notes = "更新系统字典分组信息")
    public ApiResponse update(@RequestBody @Valid SysDictGroupUpdateRequest request) {
        if(log.isDebugEnabled()){
            log.debug("update system dict group info .   request=[{}]",request);
        }
        SysDictGroup sysDictGroup = new SysDictGroup();
        BeanUtils.copyProperties(request,sysDictGroup);
        sysDictGroupService.update(sysDictGroup);
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{dictGroupId}")
    @ApiOperation(value = "删除系统字典分组信息", notes = "删除系统字典分组信息")
    public ApiResponse delete(@PathVariable(name = "dictGroupId")  String dictGroupId) {
        if(log.isDebugEnabled()){
            log.debug("delete system dict group info by dictGroupId .   dictGroupId=[{}]",dictGroupId);
        }
        sysDictGroupService.deleteById(dictGroupId);
        return ApiResponse.ok();
    }




}
