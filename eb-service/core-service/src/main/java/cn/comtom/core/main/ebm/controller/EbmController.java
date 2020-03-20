package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.service.IEbmService;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmAddRequest;
import cn.comtom.domain.core.ebm.request.EbmAuditRequest;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.domain.core.ebm.request.EbmUpdateRequest;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/core/ebm")
@Api(tags = "应急广播消息管理")
@Slf4j
public class EbmController extends BaseController {

    @Autowired
    private IEbmService ebmService;


    @PostMapping("/save")
    @ApiOperation(value = "保存应急广播消息", notes = "保存应急广播消息")
    public ApiEntityResponse<EbmInfo> save(@RequestBody @Valid EbmAddRequest request, BindingResult bindResult) {
        Ebm ebm = new Ebm();
        BeanUtils.copyProperties(request,ebm);
        if(StringUtils.isBlank(request.getEbmState())){
            ebm.setEbmState(StateDictEnum.EBM_STATE_INIT.getKey());
        }
        if(StringUtils.isBlank(request.getAuditResult())){
            ebm.setAuditResult(StateDictEnum.AUDIT_STATUS_NOT_YET.getKey());
        }
        ebmService.save(ebm);
        EbmInfo info = new EbmInfo();
        BeanUtils.copyProperties(ebm,info);
        return ApiEntityResponse.ok(info);
    }

    @PutMapping("/audit")
    @ApiOperation(value = "应急广播消息审核", notes = "应急广播消息审核")
    public ApiResponse audit(@RequestBody @Valid EbmAuditRequest request, BindingResult result){
        Ebm ebm = new Ebm();
        ebm.setEbmId(request.getEbmId());
        ebm.setAuditResult(request.getAuditResult());
        ebm.setAuditOpinion(request.getAuditOpinion());
        ebm.setAuditUser(request.getAuditUser());
        ebm.setAuditTime(new Date());
        ebmService.update(ebm);
        return ApiResponse.ok();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新应急广播消息", notes = "更新应急广播消息")
    public ApiResponse update(@RequestBody @Valid EbmUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("update ebm info . request=[{}]",JSON.toJSONString(request));
        }
        Ebm ebm = new Ebm();
        BeanUtils.copyProperties(request,ebm);
        ebmService.update(ebm);
        return ApiResponse.ok();
    }

    @GetMapping("/getDispatchEbm")
    @ApiOperation(value = "获取待分发的ebm列表", notes = "获取待分发的ebm列表")
    public ApiListResponse<EbmInfo> getDispatchEbm(@RequestParam(name = "ebmState") String ebmState){
        if(log.isDebugEnabled()){
            log.debug("get ebm list which ready to dispatchList. ebmState=[{}]",JSON.toJSONString(ebmState));
        }
        if(StringUtils.isBlank(ebmState)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmInfo> ebmInfos = ebmService.getDispatchEbm(ebmState);
        if(ebmInfos == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }

        return ApiListResponse.ok(ebmInfos);

    }

    @GetMapping("/{ebmId}")
    @ApiOperation(value = "根据ID获取ebm信息", notes = "根据ID获取ebm信息")
    public ApiEntityResponse<EbmInfo> getByEbmId(@PathVariable(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("get ebm by ebmId. ebmId=[{}]",JSON.toJSONString(ebmId));
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        Ebm ebm  = ebmService.selectById(ebmId);
        if(ebm == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        EbmInfo ebmInfo = new EbmInfo();
        BeanUtils.copyProperties(ebm,ebmInfo);
        return ApiEntityResponse.ok(ebmInfo);

    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询ebm信息", notes = "分页查询ebm信息")
    public ApiPageResponse<EbmInfo> getByPage(@ModelAttribute EbmPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebm by page. request=[{}]",JSON.toJSONString(request));
        }
        startPage(request);
        List<EbmInfo> resultList = ebmService.list(request);
        return  ApiPageResponse.ok(resultList,page);

    }

    
    @PostMapping("/cancelEbmPlay/{ebmId}")
    @ApiOperation(value = "根据ebmId取消播发", notes = "根据ebmId取消播发")
    public ApiResponse cancelEbmPlay(@PathVariable(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("cancelEbmPlay by ebmId. ebmId=[{}]", ebmId);
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        Boolean result  = ebmService.cancelEbmPlay(ebmId);
        if(!result){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(result);

    }
}
