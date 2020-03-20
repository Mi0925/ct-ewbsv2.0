package cn.comtom.core.main.omd.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.omd.entity.dbo.OmdRequest;
import cn.comtom.core.main.omd.service.IOmdRequestService;
import cn.comtom.domain.core.omd.info.OmdRequestInfo;
import cn.comtom.domain.core.omd.request.OmdRequestAddRequest;
import cn.comtom.domain.core.omd.request.OmdRequestUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
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

@RestController
@RequestMapping("/core/omdRequest")
@Api(tags = "运维数据请求信息")
@Slf4j
public class OmdRequestController extends BaseController {

    @Autowired
    private IOmdRequestService omdRequestService;


    @PostMapping("/save")
    @ApiOperation(value = "保存运维数据请求信息", notes = "保存运维数据请求信息")
    public ApiEntityResponse<OmdRequestInfo> save(@RequestBody @Valid OmdRequestAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("save OmdRequestInfo. request=[{}]", JSON.toJSONString(request));
        }
        OmdRequest omdRequest = new OmdRequest();
        BeanUtils.copyProperties(request,omdRequest);
        omdRequest.setOmdRequestId(UUIDGenerator.getUUID());
        omdRequestService.save(omdRequest);
        OmdRequestInfo omdRequestInfo = new OmdRequestInfo();
        BeanUtils.copyProperties(omdRequest,omdRequestInfo);
        return ApiEntityResponse.ok(omdRequestInfo);
    }

    @GetMapping("/{omdRequestId}")
    @ApiOperation(value = "根据ID获取运维数据请求信息", notes = "根据ID获取运维数据请求信息")
    public ApiEntityResponse<OmdRequestInfo> getById(@PathVariable(name = "omdRequestId") String omdRequestId){
        if(log.isDebugEnabled()){
            log.debug("get OmdRequestInfo by omdRequestId. omdRequestId=[{}]",omdRequestId);
        }
        OmdRequest omdRequest = omdRequestService.selectById(omdRequestId);
        if(omdRequest == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        OmdRequestInfo omdRequestInfo = new OmdRequestInfo();
        BeanUtils.copyProperties(omdRequest,omdRequestInfo);
        return ApiEntityResponse.ok(omdRequestInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新运维数据请求信息", notes = "更新运维数据请求信息")
    public ApiResponse update(@RequestBody @Valid OmdRequestUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("update OmdRequestInfo . request=[{}]",JSON.toJSONString(request));
        }
        OmdRequest omdRequest = new OmdRequest();
        BeanUtils.copyProperties(request,omdRequest);
        omdRequestService.update(omdRequest);
        return ApiResponse.ok();
    }



}
