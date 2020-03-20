package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmResBs;
import cn.comtom.core.main.ebm.service.IEbmResBsService;
import cn.comtom.domain.core.ebm.info.EbmResBsInfo;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/core/ebmResBs")
@Api(tags = "消息资源关联播出系统的记录")
@Slf4j
public class EbmResBsController extends BaseController {

    @Autowired
    private IEbmResBsService ebmResBsService;


    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存消息资源关联播出系统的记录", notes = "批量保存消息资源关联播出系统的记录")
    public ApiEntityResponse<Integer> saveBatch(@RequestBody @Valid List<EbmResBsInfo> ebmResBsInfoList, BindingResult bindResult) {
        List<EbmResBs> ebmResBsList = Optional.ofNullable(ebmResBsInfoList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ebmResBsInfo -> {
                    EbmResBs ebmResBs = new EbmResBs();
                    BeanUtils.copyProperties(ebmResBsInfo, ebmResBs);
                    if(StringUtils.isBlank(ebmResBs.getId())){
                        ebmResBs.setId(UUIDGenerator.getUUID());
                    }
                    return ebmResBs;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebmResBsService.saveList(ebmResBsList));
    }

    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量消息资源关联播出系统的记录", notes = "批量消息资源关联播出系统的记录")
    public ApiEntityResponse<Integer> updateBatch(@RequestBody @Valid List<EbmResBsInfo> ebmResBsInfoList, BindingResult bindResult) {
        Long count = Optional.ofNullable(ebmResBsInfoList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(ebmResBsInfo ->  StringUtils.isNotBlank(ebmResBsInfo.getId()))
                .map(ebmResBsInfo -> {
                    EbmResBs ebmResBs = new EbmResBs();
                    BeanUtils.copyProperties(ebmResBsInfo, ebmResBs);
                    return ebmResBsService.update(ebmResBs);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }



    @GetMapping("/getEbmResBsByBrdItemId")
    @ApiOperation(value = "根据brdItemId获取消息资源关联播出系统的记录列表", notes = "根据brdItemId获取消息资源关联播出系统的记录列表")
    public ApiListResponse<EbmResBsInfo> getEbmResBsByBrdItemId(@RequestParam(name = "brdItemId") String brdItemId){
        if(log.isDebugEnabled()){
            log.debug("get EbmResBsInfo by brdItemId. brdItemId=[{}]",JSON.toJSONString(brdItemId));
        }
        if(StringUtils.isBlank(brdItemId)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmResBsInfo> ebmResBsInfoList = ebmResBsService.getEbmResBsByResourceId(brdItemId);
        if(ebmResBsInfoList == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(ebmResBsInfoList);

    }

    @GetMapping("/getByBrdItemIdIdAndSysInfo")
    @ApiOperation(value = "根据brdItemId和brdSysInfo获取消息资源关联播出系统的记录列表", notes = "根据brdItemId和brdSysInfo获取消息资源关联播出系统的记录列表")
    public ApiEntityResponse<EbmResBsInfo> getByBrdItemIdIdAndSysInfo(@RequestParam(name = "brdItemId") String brdItemId,@RequestParam(name = "brdSysInfo") String brdSysInfo){
        if(log.isDebugEnabled()){
            log.debug("get EbmResBsInfo by brdItemId and brdSysInfo. brdItemId=[{}] ,brdSysInfo=[{}]",JSON.toJSONString(brdItemId),brdSysInfo);
        }
        if(StringUtils.isBlank(brdItemId) || StringUtils.isBlank(brdSysInfo)){
            return ApiEntityResponse.error(CoreErrorEnum.REQUIRED_PARAMS_EMPTY.getCode(),CoreErrorEnum.REQUIRED_PARAMS_EMPTY.getMsg());
        }
        List<EbmResBsInfo> ebmResBsInfoList = ebmResBsService.getByBrdItemIdIdAndSysInfo(brdItemId,brdSysInfo);
        if(ebmResBsInfoList.size() == 0){
            return ApiEntityResponse.error(CoreErrorEnum.QUERY_NO_DATA.getCode(),CoreErrorEnum.QUERY_NO_DATA.getMsg());
        }
        return ApiEntityResponse.ok(ebmResBsInfoList.get(0));

    }

}
