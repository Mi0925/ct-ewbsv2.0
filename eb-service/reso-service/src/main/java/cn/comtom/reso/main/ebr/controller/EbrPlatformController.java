package cn.comtom.reso.main.ebr.controller;


import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;
import cn.comtom.reso.main.ebr.service.IEbrPlatformService;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
import cn.comtom.tools.utils.EntityUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/res/ebr")
@Api(tags = "平台信息管理")
@Slf4j
public class EbrPlatformController extends BaseController {

    @Autowired
    private IEbrPlatformService ebrPlatformService;

    @Autowired
    private IEbrChannelService ebrChannelService;

    @GetMapping("/platform/{ebrId}")
    @ApiOperation(value = "根据EbrId查询平台信息", notes = "根据EbrId查询平台信息")
    public ApiEntityResponse<EbrPlatformInfo> getById(@PathVariable(name = "ebrId") String ebrId){
        EbrPlatform ebrPlatform = ebrPlatformService.selectById(ebrId);
        if(ebrPlatform == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
        }
        EbrPlatformInfo info = new EbrPlatformInfo();
        BeanUtils.copyProperties(ebrPlatform,info);
        info.setResChannel(ebrChannelService.getChannelByEbrId(ebrId));
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/platform/page")
    @ApiOperation(value = "根据EbrId查询平台信息", notes = "根据EbrId查询平台信息")
    public ApiPageResponse<EbrPlatformInfo> getByPage(@ModelAttribute EbrPlatformPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find PlatformList by page . request = [{}]",JSON.toJSONString(request));
        }
        startPage(request);
        List<EbrPlatformInfo> ebrPlatformInfoList = ebrPlatformService.list(request);
        return ApiPageResponse.ok(ebrPlatformInfoList,page);
    }

    @GetMapping("/platform/findPlatformListByWhere")
    @ApiOperation(value = "条件查询平台信息", notes = "条件查询平台信息")
    public ApiListResponse<EbrPlatformInfo> findPlatformListByWhere(@ModelAttribute PlatformWhereRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find PlatformList by condition . request = [{}]",JSON.toJSONString(request));
        }
//        if(EntityUtil.isReallyEmpty(request)){
//            return ApiResponseBuilder.buildListError(ResErrorEnum.PARAMS_NOT_ALL_EMPTY);
//        }
        List<EbrPlatformInfo> ebrPlatformInfoList = ebrPlatformService.findPlatformListByWhere(request);
        if(ebrPlatformInfoList == null){
            return ApiResponseBuilder.buildListError(ResErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(ebrPlatformInfoList);
    }

    @PutMapping("/platform/updateEbrPlatformState")
    @ApiOperation(value = "批量更新播出平台信息状态", notes = "批量更新播出平台信息状态")
    public ApiEntityResponse<Integer> updateEbrPlatformState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList){
        if(log.isDebugEnabled()){
            log.debug("[!~]update EbrPlatform state by ebrId . ebrStateUpdateRequestList=[{}]  ",ebrStateUpdateRequestList);
        }
        Long count = Optional.ofNullable(ebrStateUpdateRequestList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request ->{
                    EbrPlatform ebrPlatform = new EbrPlatform();
                    ebrPlatform.setPsEbrId(request.getEbrId());
                    ebrPlatform.setPsState(request.getState());
                    ebrPlatform.setUpdateTime(new Date());
                    ebrPlatform.setSyncFlag(request.getSyncFlag());
                    ebrPlatform.setStatusSyncFlag(request.getStatusSyncFlag());
                    return ebrPlatformService.update(ebrPlatform);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PutMapping("/platform/updatePlatform")
    @ApiOperation(value = "根据bsEbrId更新平台信息", notes = "根据bsEbrId更新平台信息")
    public ApiResponse updatePlatform(@RequestBody @Valid PlatformUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]update EbrPlatform  by ebrId . request=[{}]  ",request);
        }
        EbrPlatform ebrPlatform = new EbrPlatform();
        BeanUtils.copyProperties(request,ebrPlatform);
        ebrPlatform.setStatusSyncFlag(Integer.valueOf(StateDictEnum.SYNC_STATUS_NOT.getKey()));
        ebrPlatform.setSyncFlag(Integer.valueOf(StateDictEnum.SYNC_STATUS_NOT.getKey()));
        ebrPlatform.setUpdateTime(new Date());
        ebrPlatformService.update(ebrPlatform,request.getResChannel());
        return ApiResponse.ok();
    }

    @PutMapping("/platform/updatePlatformBatch")
    @ApiOperation(value = "批量更新平台信息", notes = "批量更新平台信息")
    public ApiEntityResponse<Integer>  updatePlatformBatch(@RequestBody @Valid List<PlatformUpdateRequest> platformUpdateRequestList){
        if(log.isDebugEnabled()){
            log.debug("[!~]update batch EbrPlatform  by ebrId . platformUpdateRequestList=[{}]  ",platformUpdateRequestList);
        }
        Long count = Optional.ofNullable(platformUpdateRequestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> {
                    EbrPlatform ebrPlatform = new EbrPlatform();
                    BeanUtils.copyProperties(request,ebrPlatform);
                    ebrPlatform.setStatusSyncFlag(request.getStatusSyncFlag());
                    ebrPlatform.setSyncFlag(request.getSyncFlag());
                    ebrPlatform.setUpdateTime(new Date());
                    return ebrPlatformService.update(ebrPlatform);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }


    @PostMapping("/platform/savePlatformBatch")
    @ApiOperation(value = "批量新增平台信息", notes = "批量新增平台信息")
    public ApiEntityResponse<Integer>  savePlatformBatch(@RequestBody @Valid List<PlatformAddRequest> platformAddRequestList){
        if(log.isDebugEnabled()){
            log.debug("[!~] update batch EbrPlatform  by ebrId . platformUpdateRequestList=[{}]  ",platformAddRequestList);
        }
        List<EbrPlatform> list = Optional.ofNullable(platformAddRequestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> {
                    EbrPlatform ebrPlatform = new EbrPlatform();
                    BeanUtils.copyProperties(request,ebrPlatform);
                    if(ebrPlatform.getCreateTime() == null){
                        ebrPlatform.setCreateTime(new Date());
                    }
                    if (ebrPlatform.getUpdateTime() == null) {
                        ebrPlatform.setCreateTime(new Date());
                    }
                    return ebrPlatform;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebrPlatformService.saveList(list));
    }

    @GetMapping("/info/{ebrId}")
    @ApiOperation(value = "根据ebrId查询ebr资源共有信息", notes = "根据ebrId查询ebr资源共有信息")
    public ApiEntityResponse<EbrInfo> getEbrInfoById(@PathVariable String ebrId){
        EbrInfo ebrInfo = ebrPlatformService.getEbrInfoById(ebrId);

        return ApiEntityResponse.ok(ebrInfo);
    }

    @PostMapping("/platform/save")
    @ApiOperation(value = "新增平台信息", notes = "新增平台信息")
    public ApiEntityResponse<EbrPlatformInfo>  savePlatform(@RequestBody @Valid PlatformAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save  EbrPlatform  . request=[{}]  ",request);
        }
        EbrPlatform ebrPlatform = new EbrPlatform();
        BeanUtils.copyProperties(request,ebrPlatform);
        if(ebrPlatform.getCreateTime() == null){
            ebrPlatform.setCreateTime(new Date());
        }
        if (ebrPlatform.getUpdateTime() == null) {
            ebrPlatform.setUpdateTime(new Date());
        }
        if(ebrPlatform.getSyncFlag() == null){
            ebrPlatform.setSyncFlag(Integer.valueOf(StateDictEnum.SYNC_STATUS_NOT.getKey()));
        }
        if(ebrPlatform.getStatusSyncFlag() == null){
            ebrPlatform.setStatusSyncFlag(Integer.valueOf(StateDictEnum.SYNC_STATUS_NOT.getKey()));
        }
        if(ebrPlatform.getPsState() == null){
            ebrPlatform.setPsState(Integer.valueOf(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey()));
        }
        if(request.getSquare()==null){
            request.setSquare(0.0);
        }
        if(request.getPopulation()==null){
            request.setPopulation(0.0);
        }
        ebrPlatformService.save(ebrPlatform,request.getResChannel());
        EbrPlatformInfo ebrPlatformInfo = new EbrPlatformInfo();
        BeanUtils.copyProperties(ebrPlatform,ebrPlatformInfo);
        return ApiEntityResponse.ok(ebrPlatformInfo);
    }

    @DeleteMapping("/platform/delete/{ebrId}")
    @ApiOperation(value = "删除平台信息", notes = "删除平台信息")
    public ApiResponse delete(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("delete EbrPlatform info by ebrId . ebrId=[{}]  ",ebrId);
        }
        if(StringUtils.isBlank(ebrId)){
            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ebrPlatformService.deleteById(ebrId);
        return ApiResponse.ok();
    }

    @GetMapping("/getFailureRate/{resType}")
    @ApiOperation(value = "失败率统计", notes = "失败率统计")
    public ApiEntityResponse<Double> getFailureRate(@PathVariable(name = "resType") String resType){
        if(log.isDebugEnabled()){
            log.debug("countFailureRate by resType . resType=[{}]  ",resType);
        }
        if(StringUtils.isBlank(resType)){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        Double rate = ebrPlatformService.countFailureRate(resType);
        return ApiEntityResponse.ok(rate);
    }

}
