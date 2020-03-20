package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmRes;
import cn.comtom.core.main.ebm.service.IEbmResService;
import cn.comtom.domain.core.ebm.info.EbmResInfo;
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
@RequestMapping("/core/ebmRes")
@Api(tags = "消息关联资源信息管理")
@Slf4j
public class EbmResController extends BaseController {

    @Autowired
    private IEbmResService ebmResService;


    @PostMapping("/saveBatch")
    @ApiOperation(value = "批量保存消息关联资源信息", notes = "批量保存消息关联资源信息")
    public ApiEntityResponse<Integer> save(@RequestBody @Valid List<EbmResInfo> ebmResInfoList, BindingResult bindResult) {
        List<EbmRes> ebmBrdRecordList = Optional.ofNullable(ebmResInfoList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ebmResInfo -> {
                    EbmRes ebmRes = new EbmRes();
                    BeanUtils.copyProperties(ebmResInfo, ebmRes);
                    if(StringUtils.isBlank(ebmRes.getEbmResourceId())){
                        ebmRes.setEbmResourceId(UUIDGenerator.getUUID());
                    }
                    return ebmRes;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebmResService.saveList(ebmBrdRecordList));
    }



    @GetMapping("/getEbmResListByBrdItemId")
    @ApiOperation(value = "根据brdItemId获取消息关联资源信息列表", notes = "根据brdItemId获取消息关联资源信息列表")
    public ApiListResponse<EbmResInfo> getEbmResListByBrdItemId(@RequestParam(name = "brdItemId") String brdItemId){
        if(log.isDebugEnabled()){
            log.debug("get EbmResInfo by brdItemId. brdItemId=[{}]",JSON.toJSONString(brdItemId));
        }
        if(StringUtils.isBlank(brdItemId)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmResInfo> ebmResInfoList = ebmResService.getEbmResListByBrdItemId(brdItemId);
        if(ebmResInfoList == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(ebmResInfoList);

    }

}
