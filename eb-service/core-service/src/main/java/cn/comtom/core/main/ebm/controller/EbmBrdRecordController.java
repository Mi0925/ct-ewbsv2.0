package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdRecord;
import cn.comtom.core.main.ebm.service.IEbmBrdRecordService;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordUpdateRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordWhereRequest;
import cn.comtom.tools.response.*;
import cn.comtom.tools.utils.EntityUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/core/ebmBrdRecord")
@Api(tags = "播发记录管理")
@Slf4j
public class EbmBrdRecordController extends BaseController {

    @Autowired
    private IEbmBrdRecordService ebmBrdRecordService;

    @PostMapping("/save")
    @ApiOperation(value = "保存播发记录", notes = "保存播发记录")
    public ApiEntityResponse<EbmBrdRecordInfo> save(@RequestBody @Valid EbmBrdRecordRequest request, BindingResult bindResult) {
        EbmBrdRecord ebmBrdRecord = new EbmBrdRecord();
        BeanUtils.copyProperties(request,ebmBrdRecord);
        if(StringUtils.isBlank(request.getBrdItemId())){
            ebmBrdRecord.setBrdItemId(UUIDGenerator.getUUID());
        }
        ebmBrdRecordService.save(ebmBrdRecord);
        EbmBrdRecordInfo ebmBrdRecordInfo = new EbmBrdRecordInfo();
        BeanUtils.copyProperties(ebmBrdRecord,ebmBrdRecordInfo);
        return ApiEntityResponse.ok(ebmBrdRecordInfo);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存播发记录", notes = "批量保存播发记录")
    public ApiEntityResponse<Integer> saveBatch(@RequestBody @Valid List<EbmBrdRecordInfo> ebmBrdRecordInfoList, BindingResult bindResult) {
        List<EbmBrdRecord> ebmBrdRecordList = Optional.ofNullable(ebmBrdRecordInfoList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ebmBrdRecordInfo -> {
                    EbmBrdRecord ebmBrdRecord = new EbmBrdRecord();
                    BeanUtils.copyProperties(ebmBrdRecordInfo, ebmBrdRecord);
                    if(StringUtils.isBlank(ebmBrdRecord.getBrdItemId())){
                        ebmBrdRecord.setBrdItemId(UUIDGenerator.getUUID());
                    }
                    return ebmBrdRecord;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebmBrdRecordService.saveList(ebmBrdRecordList));
    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量更新播发记录", notes = "批量更新播发记录")
    public ApiEntityResponse<Integer> updateBatch(@RequestBody @Valid List<EbmBrdRecordInfo> ebmBrdRecordInfoList, BindingResult bindResult) {
        Long count = Optional.ofNullable(ebmBrdRecordInfoList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(ebmBrdRecordInfo ->  StringUtils.isNotBlank(ebmBrdRecordInfo.getBrdItemId()))
                .map(ebmBrdRecordInfo -> {
                    EbmBrdRecord ebmBrdRecord = new EbmBrdRecord();
                    BeanUtils.copyProperties(ebmBrdRecordInfo, ebmBrdRecord);
                    return ebmBrdRecordService.update(ebmBrdRecord);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新播发记录", notes = "更新播发记录")
    public ApiResponse update(@RequestBody @Valid EbmBrdRecordUpdateRequest request, BindingResult bindResult) {
        if (log.isDebugEnabled()) {
            log.debug("update EbmBrdRecord . request=[{}]", JSON.toJSONString(request));
        }
        EbmBrdRecord ebmBrdRecord = new EbmBrdRecord();
        BeanUtils.copyProperties(request,ebmBrdRecord);
        ebmBrdRecordService.update(ebmBrdRecord);
        return ApiResponse.ok();
    }


    @GetMapping("/getEbmBrdRecordListByEbmId")
    @ApiOperation(value = "根据ebmId获取播发记录列表", notes = "根据ebmId获取播发记录列表")
    public ApiListResponse<EbmBrdRecordInfo> getEbmBrdRecordListByEbmId(@RequestParam(name = "ebmId") String ebmId) {
        if (log.isDebugEnabled()) {
            log.debug("get EbmBrdRecordInfo by ebmId. ebmId=[{}]", JSON.toJSONString(ebmId));
        }
        if (StringUtils.isBlank(ebmId)) {
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmBrdRecordInfo> ebmResInfoList = ebmBrdRecordService.getEbmBrdRecordListByEbmId(ebmId);
        if (ebmResInfoList == null) {
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(ebmResInfoList);

    }

    @GetMapping("/getEbmBrdByEbmIdAndResourceId")
    @ApiOperation(value = "根据ebmId获取播发记录列表", notes = "根据ebmId获取播发记录列表")
    public ApiEntityResponse<EbmBrdRecordInfo> getEbmBrdByEbmIdAndResourceId(@RequestParam(value = "ebmId",required=false) String ebmId,@RequestParam(value = "ebdSrcEbrId",required=false) String ebdSrcEbrId) {
        if (log.isDebugEnabled()) {
            log.debug("get EbmBrdRecordInfo by ebmId and resourceId. ebmId=[{}],resourceId=[{}]", JSON.toJSONString(ebmId),ebdSrcEbrId);
        }
        if (StringUtils.isBlank(ebmId) || StringUtils.isBlank(ebdSrcEbrId)) {
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        EbmBrdRecordInfo ebmBrdRecordInfo = ebmBrdRecordService.getEbmBrdByEbmIdAndResourceId(ebmId,ebdSrcEbrId);
        if (ebmBrdRecordInfo == null) {
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiEntityResponse.ok(ebmBrdRecordInfo);

    }

    @PostMapping("/findEbmBrdRecordListByWhere")
    @ApiOperation(value = "根据条件获取播发记录列表", notes = "根据条件获取播发记录列表")
    public ApiListResponse<EbmBrdRecordInfo> findEbmBrdRecordListByWhere(@RequestBody EbmBrdRecordWhereRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("get EbmBrdRecordInfo by condition. request=[{}]", JSON.toJSONString(request));
        }
        if (EntityUtil.isReallyEmpty(request)) {
            return ApiResponseBuilder.buildListError(CoreErrorEnum.PARAMS_NOT_ALL_EMPTY);
        }

        List<EbmBrdRecordInfo> ebmResInfoList = ebmBrdRecordService.findEbmBrdRecordListByWhere(request);
        if (ebmResInfoList == null) {
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(ebmResInfoList);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询播发记录", notes = "分页查询播发记录")
    public ApiPageResponse<EbmBrdRecordInfo> page(@Valid @ModelAttribute EbmBrdRecordPageRequest req){
        startPage(req,EbmBrdRecord.class);
        List<EbmBrdRecordInfo> infoList = ebmBrdRecordService.page(req);
        return ApiPageResponse.ok(infoList);
    }


}
