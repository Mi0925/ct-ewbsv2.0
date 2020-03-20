package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.core.main.ebm.service.IEbmDispatchService;
import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchAddBatchRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchAddRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchPageRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchUpdateRequest;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
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
import java.util.*;

@RestController
@RequestMapping("/core/ebm/dispatch")
@Api(tags = "应急消息资源调度信息管理")
@Slf4j
public class EbmDispatchController extends BaseController {

    @Autowired
    private IEbmDispatchService ebmDispatchService;


    @PostMapping("/save")
    @ApiOperation(value = "保存应急消息资源调度信息", notes = "保存应急消息资源调度信息")
    public ApiEntityResponse<EbmDispatchInfo> save(@RequestBody @Valid EbmDispatchAddRequest request, BindingResult bindResult) {
        EbmDispatch ebmDispatch = new EbmDispatch();
        BeanUtils.copyProperties(request,ebmDispatch);
        ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
        ebmDispatch.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
        ebmDispatch.setDispatchTime(new Date());
        ebmDispatchService.save(ebmDispatch);
        EbmDispatchInfo info = new EbmDispatchInfo();
        BeanUtils.copyProperties(ebmDispatch,info);
        return ApiEntityResponse.ok(info);
    }


    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存应急消息资源调度信息", notes = "批量保存应急消息资源调度信息")
    public ApiResponse saveBatch(@RequestBody @Valid EbmDispatchAddBatchRequest request, BindingResult bindResult) {
        List<EbmDispatchInfo> ebmDispatchInfoList = request.getEbmDispatchInfoList();
        if(ebmDispatchInfoList == null || ebmDispatchInfoList.isEmpty()){
            return ApiResponseBuilder.buildError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmDispatch> ebmDispatchList = new ArrayList<>();
        ebmDispatchInfoList.forEach(ebmDispatchInfo -> {
            EbmDispatch ebmDispatch = new EbmDispatch();
            BeanUtils.copyProperties(ebmDispatchInfo,ebmDispatch);
            ebmDispatch.setDispatchId(UUIDGenerator.getUUID());
            ebmDispatchList.add(ebmDispatch);
        });
        ebmDispatchService.saveList(ebmDispatchList);
        return ApiResponse.ok();
    }

    @GetMapping("/getByEbmId")
    @ApiOperation(value = "根据embId查询应急消息资源调度信息", notes = "根据embId查询应急消息资源调度信息")
    public ApiListResponse<EbmDispatchInfo> getByEbmId(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("get ebm dispatchList list by ebmId . ebmId=[{}]",JSON.toJSONString(ebmId));
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmDispatchInfo> ebmDispatchInfos = ebmDispatchService.getEbmDispatchByEbmId(ebmId);
        if(ebmDispatchInfos == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }

        return ApiListResponse.ok(ebmDispatchInfos);
    }

    @GetMapping("/getOneByEbdId")
    @ApiOperation(value = "根据embId查询应急消息资源调度信息", notes = "根据embId查询应急消息资源调度信息")
    public ApiEntityResponse<EbmDispatchInfo> getOneByEbdId(@RequestParam(name = "ebdId") String ebdId){
        if(log.isDebugEnabled()){
            log.debug("get ebm dispatch by ebdId . ebdId=[{}]",JSON.toJSONString(ebdId));
        }
        if(StringUtils.isBlank(ebdId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmDispatchInfo> ebmDispatchInfos = ebmDispatchService.getEbmDispatchByEbdId(ebdId);
        if(ebmDispatchInfos == null || ebmDispatchInfos.isEmpty()){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }

        return ApiEntityResponse.ok(ebmDispatchInfos.get(0));
    }

    @GetMapping("/getOneByMatchedEbdId")
    @ApiOperation(value = "根据matchedEbdId查询应急消息资源调度信息", notes = "根据matchedEbdId查询应急消息资源调度信息")
    public ApiEntityResponse<EbmDispatchInfo> getEbmDispatchByMatchedEbdId(@RequestParam(name = "matchedEbdId") String matchedEbdId){
        if(log.isDebugEnabled()){
            log.debug("get ebm dispatch by matchedEbdId . matchedEbdId=[{}]",JSON.toJSONString(matchedEbdId));
        }
        if(StringUtils.isBlank(matchedEbdId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmDispatchInfo> ebmDispatchInfos = ebmDispatchService.getEbmDispatchByMatchedEbdId(matchedEbdId);
        if(ebmDispatchInfos == null || ebmDispatchInfos.isEmpty()){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }

        return ApiEntityResponse.ok(ebmDispatchInfos.get(0));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新应急消息资源调度信息", notes = "更新应急消息资源调度信息")
    public ApiResponse update(@RequestBody @Valid EbmDispatchUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("update ebmDispatch info . request=[{}]",JSON.toJSONString(request));
        }
        EbmDispatch ebmDispatch = new EbmDispatch();
        BeanUtils.copyProperties(request,ebmDispatch);
        ebmDispatchService.update(ebmDispatch);
        return ApiResponse.ok();
    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量更新应急消息资源调度信息", notes = "批量更新应急消息资源调度信息")
    public ApiResponse updateBatch(@RequestBody @Valid List<EbmDispatchUpdateRequest> requestList, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("update ebmDispatch info batch . requestList=[{}]",JSON.toJSONString(requestList));
        }
        Optional.ofNullable(requestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .forEach(ebmDispatchUpdateRequest -> {
                    EbmDispatch ebmDispatch = new EbmDispatch();
                    BeanUtils.copyProperties(ebmDispatchUpdateRequest,ebmDispatch);
                    ebmDispatchService.update(ebmDispatch);
                });
        return ApiResponse.ok();
    }


    @PostMapping("/page")
    @ApiOperation(value = "查询需要分发的应急消息资源调度信息", notes = "查询需要分发的应急消息资源调度信息")
    public ApiPageResponse<EbmDispatchInfo> page(@RequestBody @Valid EbmDispatchPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("query ebm dispatch by page . req=[{}]",JSON.toJSONString(request));
        }
        startPage(request, EbmDispatch.class);
        List<EbmDispatchInfo> ebmDispatchInfos = ebmDispatchService.getEbmDispatchList(request);
        return ApiPageResponse.ok(ebmDispatchInfos,page);
    }


    @GetMapping("/getEbmDispatchAndEbdFileByEbmId")
    @ApiOperation(value = "根据embId查询应急消息资源调度信息(包含数据包文件信息)", notes = "根据embId查询应急消息资源调度信息(包含数据包文件信息)")
    public ApiEntityResponse<List<EbmDispatchAndEbdFileInfo>> getEbmDispatchByEbmId(@RequestParam(name = "ebmId",required = true) String ebmId){
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmDispatchAndEbdFileInfo> ebmDispatchInfos = ebmDispatchService.getEbmDispatchAndEbdFileByEbmId(ebmId);
        if(ebmDispatchInfos == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(ebmDispatchInfos);
    }


}
