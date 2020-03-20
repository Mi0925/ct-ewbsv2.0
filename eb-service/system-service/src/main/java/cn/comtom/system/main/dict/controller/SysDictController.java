package cn.comtom.system.main.dict.controller;


import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.domain.system.dict.request.SysDictAddRequest;
import cn.comtom.domain.system.dict.request.SysDictPageRequest;
import cn.comtom.domain.system.dict.request.SysDictUpdateRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.dict.entity.dbo.SysDict;
import cn.comtom.system.main.dict.service.ISysDictService;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
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
@RequestMapping("/system/dict")
@Api(tags = "系统字典信息管理接口")
@Slf4j
public class SysDictController extends BaseController {

    @Autowired
    private ISysDictService sysDictService;


    @PostMapping("/save")
    @ApiOperation(value = "保存系统字典信息", notes = "保存系统字典信息")
    public ApiEntityResponse<SysDictInfo> save(@RequestBody @Valid SysDictAddRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~] save system dict .  request=[{}]",request);
        }
        SysDictInfo sysDictInfo = new SysDictInfo();
        SysDict sysDict = new SysDict();
        String dictId = UUIDGenerator.getUUID();
        if(request != null){
            BeanUtils.copyProperties(request,sysDict);
        }
        sysDict.setDictId(dictId);
        sysDictService.save(sysDict);
        BeanUtils.copyProperties(sysDict,sysDictInfo);
        return ApiEntityResponse.ok(sysDictInfo);
    }

    @GetMapping("/{dictId}")
    @ApiOperation(value = "根据ID查询系统字典信息", notes = "根据ID查询系统字典信息")
    public ApiEntityResponse<SysDictInfo> getById(@PathVariable(name = "dictId") String dictId) {
        if(log.isDebugEnabled()){
            log.debug("get system dict info by dictId   dictId=[{}]",dictId);
        }
        SysDictInfo sysDictInfo = new SysDictInfo();
        SysDict sysDict = sysDictService.selectById(dictId);
        if(sysDict == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        BeanUtils.copyProperties(sysDict,sysDictInfo);
        return ApiEntityResponse.ok(sysDictInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询系统字典信息", notes = "分页查询系统字典信息")
    public ApiPageResponse<SysDictInfo> getByPage(@ModelAttribute SysDictPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("get system dict info by page   request=[{}]",request);
        }
        startPage(request);
        SysDictInfo sysDictInfo = new SysDictInfo();
        BeanUtils.copyProperties(request,sysDictInfo);
        List<SysDictInfo> resultList = sysDictService.list(sysDictInfo);
        return ApiPageResponse.ok(resultList,page);
    }

    @GetMapping("/getByGroupCode")
    @ApiOperation(value = "根据分组编码查询系统字典信息", notes = "根据分组编码查询系统字典信息")
    public ApiListResponse<SysDictInfo> getByGroupCode(@RequestParam(name = "dictGroupCode") String dictGroupCode) {
        if(log.isDebugEnabled()){
            log.debug("get system dict info by dictGroupCode   dictGroupCode=[{}]",dictGroupCode);
        }
        if(StringUtils.isBlank(dictGroupCode)){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysDictInfo sysDictInfo = new SysDictInfo();
        sysDictInfo.setDictGroupCode(dictGroupCode);
        sysDictInfo.setDictStatus(StateDictEnum.DICT_STATUS_NORMAL.getKey());
        List<SysDictInfo> resultList = sysDictService.list(sysDictInfo);
        return ApiListResponse.ok(resultList);
    }

    @GetMapping("/getByDictKey")
    @ApiOperation(value = "根据key查询系统字典信息", notes = "根据key查询系统字典信息")
    public ApiEntityResponse<SysDictInfo> getByDictKey(@RequestParam(name = "dictKey") String dictKey) {
        if(log.isDebugEnabled()){
            log.debug("get system dict info by dictKey   dictKey=[{}]",dictKey);
        }
        if(StringUtils.isBlank(dictKey)){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        SysDictInfo sysDictInfo = new SysDictInfo();
        sysDictInfo.setDictKey(dictKey);
        List<SysDictInfo> resultList = sysDictService.list(sysDictInfo);
        if(resultList.isEmpty()){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(resultList.get(0));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新系统字典信息", notes = "更新系统字典信息")
    public ApiResponse update(@RequestBody @Valid SysDictUpdateRequest request) {
        if(log.isDebugEnabled()){
            log.debug("update system dict info .   request=[{}]",request);
        }
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(request,sysDict);
        sysDictService.update(sysDict);
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{dictId}")
    @ApiOperation(value = "删除系统字典信息", notes = "删除系统字典信息")
    public ApiResponse delete(@PathVariable(name = "dictId")  String dictId) {
        if(log.isDebugEnabled()){
            log.debug("delete system dict info by dictId .   dictId=[{}]",dictId);
        }
        sysDictService.deleteById(dictId);
        return ApiResponse.ok();
    }




}
