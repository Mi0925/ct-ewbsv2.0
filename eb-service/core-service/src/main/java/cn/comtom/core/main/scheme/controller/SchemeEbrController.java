package cn.comtom.core.main.scheme.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.core.main.scheme.service.ISchemeEbrService;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.request.SchemeEbrAddBatchRequest;
import cn.comtom.domain.core.scheme.request.SchemeEbrAddRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/core/scheme/ebr")
@Api(tags = "调度方案关联资源信息")
@Slf4j
public class SchemeEbrController extends BaseController {

    @Autowired
    private ISchemeEbrService schemeEbrService;

    @PostMapping("/save")
    @ApiOperation(value = "保存调度方案关联资源信息", notes = "保存调度方案关联资源信息")
    public ApiEntityResponse<SchemeEbrInfo> save(@RequestBody @Valid SchemeEbrAddRequest request, BindingResult bindResult) {
        SchemeEbr schemeEbr = new SchemeEbr();
        BeanUtils.copyProperties(request,schemeEbr);
        schemeEbr.setId(UUIDGenerator.getUUID());
        schemeEbrService.save(schemeEbr);
        SchemeEbrInfo info = new SchemeEbrInfo();
        BeanUtils.copyProperties(schemeEbr,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存调度方案关联资源信息", notes = "批量保存调度方案关联资源信息")
    public ApiResponse saveBatch(@RequestBody @Valid SchemeEbrAddBatchRequest request, BindingResult bindResult) {
        List<SchemeEbrInfo> schemeEbrInfoList = request.getSchemeEbrInfoList();
        if(schemeEbrInfoList == null || schemeEbrInfoList.isEmpty()){
            return ApiResponseBuilder.buildError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<SchemeEbr> schemeEbrList = new ArrayList<>();
        schemeEbrInfoList.forEach(schemeEbrInfo -> {
            SchemeEbr schemeEbr = new SchemeEbr();
            BeanUtils.copyProperties(schemeEbrInfo,schemeEbr);
            schemeEbr.setId(UUIDGenerator.getUUID());
            schemeEbrList.add(schemeEbr);
        });
        schemeEbrService.saveList(schemeEbrList);
        return ApiResponse.ok();
    }

    @GetMapping("/getSchemeEbrListBySchemeId")
    @ApiOperation(value = "批量保存调度方案关联资源信息", notes = "批量保存调度方案关联资源信息")
    public ApiListResponse<SchemeEbrInfo> getSchemeEbrListBySchemeId(@RequestParam(value = "schemeId", required = false)  String schemeId) {
        if(StringUtils.isBlank(schemeId)){
        	
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<SchemeEbrInfo> schemeEbrList = schemeEbrService.getSchemeEbrListBySchemeId(schemeId);
        if(schemeEbrList == null){
            return null;
        }
        return ApiListResponse.ok(schemeEbrList);
    }

    @DeleteMapping("/delete/{schemeId}")
    @ApiOperation(value = "删除调度方案关联资源信息", notes = "删除调度方案关联资源信息")
    public ApiEntityResponse delete(@PathVariable("schemeId") String schemeId) {
        if(StringUtils.isBlank(schemeId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        Boolean res = schemeEbrService.deleteBySchemeId(schemeId);
        return ApiEntityResponse.ok(res);
    }



}
