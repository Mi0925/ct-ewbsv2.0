package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmAuxiliary;
import cn.comtom.core.main.ebm.service.IEbmAuxiliaryService;
import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import cn.comtom.domain.core.ebm.request.EbmAuxiliaryAddBatchRequest;
import cn.comtom.domain.core.ebm.request.EbmAuxiliaryAddRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/core/ebm")
@Api(tags = "应急消息辅助数据管理")
@Slf4j
public class EbmAuxiliaryController extends BaseController {

    @Autowired
    private IEbmAuxiliaryService ebmAuxiliaryService;


    @PostMapping("/auxiliary/save")
    @ApiOperation(value = "保存应急广播消息辅助数据", notes = "保存应急广播消息辅助数据")
    public ApiEntityResponse<EbmAuxiliaryInfo> save(@RequestBody @Valid EbmAuxiliaryAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("save EbmAuxiliary req:[{}]",request);
        }
        EbmAuxiliary ebmAuxiliary = new EbmAuxiliary();
        BeanUtils.copyProperties(request,ebmAuxiliary);
        ebmAuxiliary.setAuxiliaryId(UUIDGenerator.getUUID());
        ebmAuxiliaryService.save(ebmAuxiliary);
        EbmAuxiliaryInfo info = new EbmAuxiliaryInfo();
        BeanUtils.copyProperties(ebmAuxiliary,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/auxiliary/saveBatch")
    @ApiOperation(value = "批量保存应急广播消息辅助数据", notes = "批量保存应急广播消息辅助数据")
    public ApiResponse save(@RequestBody @Valid EbmAuxiliaryAddBatchRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("save EbmAuxiliary Batch req : [{}]",request);
        }
        List<EbmAuxiliaryInfo> list = request.getAuxiliaryInfoList();
        List<EbmAuxiliary> ebmAuxiliaryList = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            list.forEach(ebmAuxiliaryInfo -> {
                EbmAuxiliary ebmAuxiliary = new EbmAuxiliary();
                BeanUtils.copyProperties(ebmAuxiliaryInfo,ebmAuxiliary);
                ebmAuxiliaryList.add(ebmAuxiliary);
            });
        }
        ebmAuxiliaryService.saveList(ebmAuxiliaryList);
        return ApiResponse.ok();
    }

    @GetMapping("/auxiliary/getByEbmId")
    @ApiOperation(value = "根据embId查询应急消息资源调度信息", notes = "根据embId查询应急消息资源调度信息")
    public ApiListResponse<EbmAuxiliaryInfo> getByEbmId(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("get EbmAuxiliaryInfo  list by ebmId . ebmId=[{}]",JSON.toJSONString(ebmId));
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbmAuxiliaryInfo> ebmAuxiliaryInfos = ebmAuxiliaryService.getEbmAuxiliaryInfoByEbmId(ebmId);
        if(ebmAuxiliaryInfos == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }

        return ApiListResponse.ok(ebmAuxiliaryInfos);
    }

}
