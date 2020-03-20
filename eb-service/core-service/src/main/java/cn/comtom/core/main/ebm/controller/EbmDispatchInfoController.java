package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import cn.comtom.core.main.ebm.service.IEbmDispatchInfoService;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoAddRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoUpdateRequest;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
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
import java.util.List;

@RestController
@RequestMapping("/core/ebmDispatchInfo")
@Api(tags = "分发消息管理")
@Slf4j
public class EbmDispatchInfoController extends BaseController {

    @Autowired
    private IEbmDispatchInfoService ebmDispatchInfoService;


    @PostMapping("/save")
    @ApiOperation(value = "保存分发消息", notes = "保存分发消息")
    public ApiEntityResponse<EbmDispatchInfoInfo> save(@RequestBody @Valid EbmDispatchInfoAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("[!~]save ebmDispatchInfo  . request=[{}]",JSON.toJSONString(request));
        }
        EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
        BeanUtils.copyProperties(request,ebmDispatchInfo);
        ebmDispatchInfo.setInfoId(UUIDGenerator.getUUID());
        ebmDispatchInfo.setAuditResult(StringUtils.isBlank(request.getAuditResult()) ? StateDictEnum.AUDIT_STATUS_NOT_YET.getKey() : request.getAuditResult());
        ebmDispatchInfoService.save(ebmDispatchInfo);
        EbmDispatchInfoInfo info = new EbmDispatchInfoInfo();
        BeanUtils.copyProperties(ebmDispatchInfo,info);
        return ApiEntityResponse.ok(info);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新分发消息", notes = "更新分发消息")
    public ApiResponse update(@RequestBody @Valid EbmDispatchInfoUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("[!~] update ebmDispatchInfo info . request=[{}]",JSON.toJSONString(request));
        }
        EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
        BeanUtils.copyProperties(request,ebmDispatchInfo);
        ebmDispatchInfoService.update(ebmDispatchInfo);
        return ApiResponse.ok();
    }

    @GetMapping("/getByEbmId")
    @ApiOperation(value = "根据ebmId查询分发消息", notes = "根据ebmId查询分发消息")
    public ApiEntityResponse<EbmDispatchInfoInfo> getByEbmId(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebmDispatchInfo by ebmId . ebmId=[{}]",JSON.toJSONString(ebmId));
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmDispatchInfoInfo> ebmDispatchInfoInfos = ebmDispatchInfoService.getByEbmId(ebmId);
        if(ebmDispatchInfoInfos == null || ebmDispatchInfoInfos.isEmpty()){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(ebmDispatchInfoInfos.get(0));
    }



}
