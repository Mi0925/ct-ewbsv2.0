package cn.comtom.core.main.scheme.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.service.IEbmPlanRefService;
import cn.comtom.core.main.ebm.service.IEbmService;
import cn.comtom.core.main.flow.service.IDispatchFlowService;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.core.main.scheme.service.ISchemeService;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeAddRequest;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;
import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.EntityUtil;
import cn.comtom.tools.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/core/scheme")
@Api(tags = "调度方案信息")
@Slf4j
public class SchemeController extends BaseController {

    @Autowired
    private ISchemeService schemeService;

    @Autowired
    private IEbmService ebmService;

    @Autowired
    private IDispatchFlowService dispatchFlowService;

    @Autowired
    private IEbmPlanRefService ebmPlanRefService;


    @PostMapping("/save")
    @ApiOperation(value = "保存方案信息", notes = "保存方案信息")
    public ApiEntityResponse<SchemeInfo> save(@RequestBody @Valid SchemeAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("save scheme. request=[{}]", JSON.toJSONString(request));
        }
        Scheme scheme = new Scheme();
        BeanUtils.copyProperties(request,scheme);
        scheme.setSchemeId(UUIDGenerator.getUUID());
        if(scheme.getCreateTime() == null){
            scheme.setCreateTime(new Date());
        }
        schemeService.save(scheme);
        SchemeInfo schemeInfo = new SchemeInfo();
        BeanUtils.copyProperties(scheme,schemeInfo);
        return ApiEntityResponse.ok(schemeInfo);
    }

    @GetMapping("/{schemeId}")
    @ApiOperation(value = "根据ID获取方案信息", notes = "根据ID获取方案信息")
    public ApiEntityResponse<SchemeInfo> getById(@PathVariable(name = "schemeId") String shemeId){
        if(log.isDebugEnabled()){
            log.debug("get schemeInfo by schemeId. schemeId=[{}]",shemeId);
        }
        Scheme scheme = schemeService.selectById(shemeId);
        if(scheme == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        SchemeInfo schemeInfo = new SchemeInfo();
        BeanUtils.copyProperties(scheme,schemeInfo);
        return ApiEntityResponse.ok(schemeInfo);
    }

    @GetMapping("/getByEbmId")
    @ApiOperation(value = "根据ID获取方案信息", notes = "根据ID获取方案信息")
    public ApiEntityResponse<SchemeInfo> getByEbmId(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("get schemeInfo by ebmId. ebmId=[{}]",ebmId);
        }
        return ApiEntityResponse.ok(schemeService.getSchemeInfoByEbmId(ebmId));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新方案信息", notes = "更新方案信息")
    public ApiResponse update(@RequestBody @Valid SchemeUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("update schemeInfo . request=[{}]",JSON.toJSONString(request));
        }
        Scheme scheme = new Scheme();
        BeanUtils.copyProperties(request,scheme);
        schemeService.update(scheme);
        return ApiResponse.ok();
    }


    @GetMapping("/getSchemePageInfo")
    @ApiOperation(value = "分页查询方案及其关联信息", notes= "分页查询方案及其关联信息")
    public ApiPageResponse<SchemePageInfo> getSchemePageInfo(@ModelAttribute SchemeQueryRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("get scheme and related Info by page. request=[{}]", request);
        }
        startPage(request);
        List<SchemePageInfo> schemePageInfoList = schemeService.list(request);
        return ApiPageResponse.ok(schemePageInfoList,page);
    }


    @GetMapping("/programId/{programId}")
    @ApiOperation(value = "分页查询方案及其关联信息", notes= "分页查询方案及其关联信息")
    public ApiEntityResponse<SchemeInfo> getSchemeInfoByProgramId(@PathVariable(name = "programId") String programId) {
        if (log.isDebugEnabled()) {
            log.debug("get scheme  Info by programId. programId=[{}]", programId);
        }
        SchemeInfo schemeInfo = schemeService.getSchemeInfoByProgramId(programId);
        if(EntityUtil.isReallyEmpty(schemeInfo)){
            return ApiEntityResponse.ok(null);
        }
        return ApiEntityResponse.ok(schemeInfo);
    }

}
