package cn.comtom.reso.main.ebr.controller;


import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.*;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;
import cn.comtom.reso.main.ebr.service.IEbrBroadcastService;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.*;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/res/ebr/broadcast")
@Api(tags = "播出系统信息")
@Slf4j
public class EbrBroadcastController extends BaseController {

    @Autowired
    private IEbrBroadcastService ebrBroadcastService;

    @Autowired
    private IEbrChannelService ebrChannelService;

    @GetMapping("/getMatchBroadcast")
    @ApiOperation(value = "根据区域编码和平台资源ID查询播出系统列表", notes = "根据区域编码和平台资源ID查询播出系统列表")
    public ApiListResponse<EbrBroadcastInfo> getMatchBroadcast(@RequestParam(name = "areaCode") String areaCode,@RequestParam(name = "relatedPsEbrId") String relatedPsEbrId){
       if(log.isDebugEnabled()){
           log.debug("get matching ebrBroadcast by areaCode and relatedPsEbrId . areaCode=[{}] , relatedPsEbrId=[{}] ",areaCode,relatedPsEbrId);
       }
       if(StringUtils.isBlank(areaCode) || StringUtils.isBlank(relatedPsEbrId)){
           return ApiResponseBuilder.buildListError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
       }
        List<EbrBroadcastInfo> ebrBroadcastInfoList = ebrBroadcastService.getMatchBroadcast(areaCode,relatedPsEbrId);
       if(ebrBroadcastInfoList == null){
           return ApiResponseBuilder.buildListError(ResErrorEnum.QUERY_NO_DATA);
       }

        return ApiListResponse.ok(ebrBroadcastInfoList);
    }

    @GetMapping("/{bsEbrId}")
    @ApiOperation(value = "根据bsEbrId查询播出系统信息", notes = "根据bsEbrId查询播出系统信息")
    public ApiEntityResponse<EbrBroadcastInfo> getByBsEbrId(@PathVariable(name = "bsEbrId") String bsEbrId){
        if(log.isDebugEnabled()){
            log.debug("get ebrBroadcast by bsEbrId . bsEbrId=[{}]  ",bsEbrId);
        }
        EbrBroadcast ebrBroadcast = ebrBroadcastService.selectById(bsEbrId);
        if(ebrBroadcast == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
        }
        EbrBroadcastInfo ebrBroadcastInfo = new EbrBroadcastInfo();
        BeanUtils.copyProperties(ebrBroadcast,ebrBroadcastInfo);
        ebrBroadcastInfo.setResChannel(ebrChannelService.getChannelByEbrId(bsEbrId));
        return ApiEntityResponse.ok(ebrBroadcastInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "根据bsEbrId查询播出系统信息", notes = "根据bsEbrId查询播出系统信息")
    public ApiPageResponse<EbrBroadcastInfo> getByBsEbrId(@ModelAttribute BroadcastPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("get ebrBroadcast by page . request=[{}]  ",request);
        }
        startPage(request);
        List<EbrBroadcastInfo> ebrBroadcastInfoList = ebrBroadcastService.getList(request);
        return ApiPageResponse.ok(ebrBroadcastInfoList,page);
    }


    @PutMapping("/updateEbrBroadcastState")
    @ApiOperation(value = "批量更新播出系统状态", notes = "批量更新播出系统状态")
    public ApiEntityResponse<Integer> updateEbrBroadcastState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList){
        if(log.isDebugEnabled()){
            log.debug("update ebrBroadcast state by ebrId . ebrStateUpdateRequestList=[{}]  ",ebrStateUpdateRequestList);
        }
        Long count = Optional.ofNullable(ebrStateUpdateRequestList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(request ->{
                    EbrBroadcast ebrBroadcast = new EbrBroadcast();
                    ebrBroadcast.setBsEbrId(request.getEbrId());
                    ebrBroadcast.setBsState(request.getState());
                    ebrBroadcast.setSyncFlag(String.valueOf(request.getSyncFlag()));
                    ebrBroadcast.setStatusSyncFlag(String.valueOf(request.getStatusSyncFlag()));
                    return ebrBroadcastService.update(ebrBroadcast);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PutMapping("/updateEbrBroadcastBatch")
    @ApiOperation(value = "批量更新播出系统信息", notes = "批量更新播出系统信息")
    public ApiEntityResponse<Integer> updateEbrBroadcastInfoBatch(@RequestBody List<EbrBroadcastUpdateRequest> requests){
        if(log.isDebugEnabled()){
            log.debug("update ebrBroadcast info by ebrId . requests=[{}]  ",requests);
        }
        Long count = Optional.ofNullable(requests).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(request ->{
                    EbrBroadcast ebrBroadcast = new EbrBroadcast();
                    BeanUtils.copyProperties(request,ebrBroadcast);
                    ebrBroadcast.setStatusSyncFlag(request.getStatusSyncFlag());    //重新同步
                    ebrBroadcast.setSyncFlag(request.getSyncFlag());        //重新同步
                    return ebrBroadcastService.update(ebrBroadcast);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新播出系统信息", notes = "更新播出系统信息")
    public ApiEntityResponse<Integer> updateEbrBroadcastInfo(@RequestBody EbrBroadcastUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("update ebrBroadcast info by ebrId . request=[{}]  ",request);
        }
        EbrBroadcast ebrBroadcast = new EbrBroadcast();
        BeanUtils.copyProperties(request,ebrBroadcast);
        ebrBroadcast.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());    //重新同步
        ebrBroadcast.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());        //重新同步
        return ApiEntityResponse.ok(ebrBroadcastService.update(ebrBroadcast,request.getResChannel()));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增播出系统信息", notes = "新增播出系统信息")
    public ApiEntityResponse<EbrBroadcastInfo> save(@RequestBody EbrBroadcastAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("save ebrBroadcast info  . request=[{}]  ",request);
        }
        EbrBroadcast ebrBroadcast = new EbrBroadcast();
        BeanUtils.copyProperties(request,ebrBroadcast);
        if(ebrBroadcast.getBsState() == null){
            ebrBroadcast.setBsState(Integer.valueOf(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey()));
        }
        if(StringUtils.isNotBlank(ebrBroadcast.getSyncFlag())){
            ebrBroadcast.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        if(StringUtils.isNotBlank(ebrBroadcast.getStatusSyncFlag())){
            ebrBroadcast.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        if(ebrBroadcast.getCreateTime() == null){
            ebrBroadcast.setCreateTime(new Date());
        }
        if (ebrBroadcast.getUpdateTime() == null) {
            ebrBroadcast.setUpdateTime(new Date());
        }
        if(ebrBroadcast.getBsState() == null){
            ebrBroadcast.setBsState(Integer.valueOf(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey()));
        }
        ebrBroadcastService.save(ebrBroadcast,request.getResChannel());
        EbrBroadcastInfo ebrBroadcastInfo = new EbrBroadcastInfo();
        BeanUtils.copyProperties(ebrBroadcast,ebrBroadcastInfo);
        return ApiEntityResponse.ok(ebrBroadcastInfo);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "新增播出系统信息", notes = "新增播出系统信息")
    public ApiEntityResponse<Integer> save(@RequestBody List<EbrBroadcastAddRequest> requests){
        if(log.isDebugEnabled()){
            log.debug("save ebrBroadcast info batch  . requests=[{}]  ",requests);
        }
        Long count = Optional.ofNullable(requests).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(request -> {
                    EbrBroadcast ebrBroadcast = new EbrBroadcast();
                    BeanUtils.copyProperties(request,ebrBroadcast);
                    if(ebrBroadcast.getBsState() == null){
                        ebrBroadcast.setBsState(Integer.valueOf(StateDictEnum.EBR_RESOURCE_STATE_STOP.getKey()));
                    }
                    if(StringUtils.isNotBlank(ebrBroadcast.getSyncFlag())){
                        ebrBroadcast.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    }
                    if(StringUtils.isNotBlank(ebrBroadcast.getStatusSyncFlag())){
                        ebrBroadcast.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    }
                    if(ebrBroadcast.getCreateTime() == null){
                        ebrBroadcast.setCreateTime(new Date());
                    }
                    if (ebrBroadcast.getUpdateTime() == null) {
                        ebrBroadcast.setUpdateTime(new Date());
                    }
                    ebrBroadcastService.save(ebrBroadcast);
                }).count();

        return ApiEntityResponse.ok(count.intValue());
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除播出系统信息", notes = "删除播出系统信息")
    public ApiResponse delete(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("delete ebrBroadcast info by ebrId . ebrId=[{}]  ",ebrId);
        }
        if(StringUtils.isBlank(ebrId)){
            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ebrBroadcastService.deleteById(ebrId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除播出系统信息", notes = "批量删除播出系统信息")
    public ApiEntityResponse<Integer> deleteBatch(@RequestBody List<String> ebrIds){
        if(log.isDebugEnabled()){
            log.debug("delete ebrBroadcast info batch by ebrIds . ebrIds=[{}]  ",ebrIds);
        }
        if(ebrIds == null || ebrIds.isEmpty()){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        return ApiEntityResponse.ok(ebrBroadcastService.deleteByIds(ebrIds));
    }


    @GetMapping("/findBroadcastListByWhere")
    @ApiOperation(value = "条件查询播出系统信息", notes = "条件查询播出系统信息")
    public ApiListResponse<EbrBroadcastInfo> findBroadcastListByWhere(@ModelAttribute BroadcastWhereRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find EbrBroadcastInfo by condition . request = [{}]",JSON.toJSONString(request));
        }
        List<EbrBroadcastInfo> ebrPlatformInfoList = ebrBroadcastService.findBroadcastListByWhere(request);
        return ApiListResponse.ok(ebrPlatformInfoList);
    }




}
