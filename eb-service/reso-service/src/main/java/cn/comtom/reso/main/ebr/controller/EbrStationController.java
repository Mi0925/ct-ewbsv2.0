package cn.comtom.reso.main.ebr.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;
import cn.comtom.reso.main.ebr.service.IEbrStationService;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.EntityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/res/ebr/station")
@Api(tags = "台站信息管理")
@Slf4j
public class EbrStationController extends BaseController {

    @Autowired
    private IEbrStationService ebrStationService;

    @Autowired
    private IEbrChannelService ebrChannelService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据EbrId查询台站资源信息", notes = "根据EbrId查询台站资源信息")
    public ApiEntityResponse<EbrStationInfo> getById(@PathVariable String ebrId){
        EbrStation ebrStation = ebrStationService.selectById(ebrId);
        if(ebrStation == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
        }
        EbrStationInfo info = new EbrStationInfo();
        BeanUtils.copyProperties(ebrStation,info);
        info.setResChannel(ebrChannelService.getChannelByEbrId(ebrId));
        return ApiEntityResponse.ok(info);
    }


    @GetMapping("/page")
    @ApiOperation(value = "分页查询终端资源信息", notes = "分页查询终端资源信息")
    public ApiPageResponse<EbrStationInfo> getByPage(@ModelAttribute EbrStationPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("get ebr station info by page . request=[{}]",request);
        }
        startPage(request);
        List<EbrStationInfo> ebrTerminalInfoList =  ebrStationService.getList(request);
        return ApiPageResponse.ok(ebrTerminalInfoList,page);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新台站信息", notes = "更新台站信息")
    public ApiEntityResponse<Integer> updateEbrStaionInfo(@RequestBody @Valid StationUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("update ebr station info by ebrId . request=[{}]  ",request);
        }
        EbrStation ebrStation = new EbrStation();
        BeanUtils.copyProperties(request,ebrStation);
        ebrStation.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());    //重新同步
        ebrStation.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());        //重新同步
        return ApiEntityResponse.ok(ebrStationService.update(ebrStation,request.getResChannel()));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增台站信息", notes = "新增台站信息")
    public ApiEntityResponse<EbrStationInfo> saveEbrStationInfo(@RequestBody @Valid StationAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("save ebr station info . request=[{}]  ",request);
        }
        EbrStation ebrStation = new EbrStation();
        BeanUtils.copyProperties(request,ebrStation);
        if(ebrStation.getCreateTime() == null){
        	ebrStation.setCreateTime(new Date());
        }
        if (ebrStation.getUpdateTime() == null) {
            ebrStation.setUpdateTime(new Date());
        }
        if(ebrStation.getSyncFlag() == null){
        	ebrStation.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        if(ebrStation.getStatusSyncFlag() == null){
        	ebrStation.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }

        ebrStationService.save(ebrStation,request.getResChannel());
        EbrStationInfo ebrStationInfo = new EbrStationInfo();
        BeanUtils.copyProperties(ebrStation,ebrStationInfo);
        return ApiEntityResponse.ok(ebrStationInfo);
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除台站信息", notes = "删除台站信息")
    public ApiResponse delete(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("delete station info by ebrId . ebrId=[{}]  ",ebrId);
        }
        if(StringUtils.isBlank(ebrId)){
            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ebrStationService.deleteById(ebrId);
        return ApiResponse.ok();
    }

    @GetMapping("/findStationListByWhere")
    @ApiOperation(value = "台站信息管理", notes = "台站信息管理")
    public ApiListResponse<EbrStationInfo> findStationListByWhere(@ModelAttribute StationWhereRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find EbrStationInfo by condition . request = [{}]",JSON.toJSONString(request));
        }
        List<EbrStationInfo> ebrTerminalInfoList = ebrStationService.findStationListByWhere(request);
        return ApiListResponse.ok(ebrTerminalInfoList);
    }


    @PutMapping("/updateStationBatch")
    @ApiOperation(value = "批量更新台站信息", notes = "批量更新台站信息")
    public ApiEntityResponse<Integer> updateEbrStationState(@RequestBody List<StationUpdateRequest> stationUpdateRequest){
        if(log.isDebugEnabled()){
            log.debug("[!~]update EbrStation state by ebrId . StationUpdateRequestList=[{}]  ",stationUpdateRequest);
        }
        Long count = Optional.ofNullable(stationUpdateRequest).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request ->{
                	EbrStation ebrStation = new EbrStation();
                    BeanUtils.copyProperties(request,ebrStation);
                    ebrStation.setEbrStId(request.getEbrStId());
                    ebrStation.setStationState(request.getStationState());
                    ebrStation.setUpdateTime(new Date());
                    ebrStation.setSyncFlag(request.getSyncFlag());
                    ebrStation.setStatusSyncFlag(request.getStatusSyncFlag());
                    return ebrStationService.update(ebrStation);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PostMapping(value = "/findListByStationIds")
    @ApiOperation(value = "根据台站ID集合查询信息集合", notes = "根据台站ID集合查询信息集合")
    public ApiEntityResponse<List<EbrStationInfo>> findListByStationIds(@RequestBody List<String> stationIds){
         if(stationIds.size()==0){
             return ApiEntityResponse.ok(new ArrayList<EbrStationInfo>());
         }
        return ApiEntityResponse.ok(ebrStationService.findListByStationIds(stationIds));
    }

    @PostMapping("/addEbrStationBatch")
    @ApiOperation(value = "批量新增适配器信息", notes = "批量新增适配器信息")
    public ApiEntityResponse<Integer>  addEbrStationBatch(@RequestBody @Valid List<StationAddRequest> addRequestList){
        List<EbrStation> list = Optional.ofNullable(addRequestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> {
                	EbrStation ebrStation = new EbrStation();
                    BeanUtils.copyProperties(request,ebrStation);
                    if(ebrStation.getCreateTime() == null){
                    	ebrStation.setCreateTime(new Date());
                    }
                    if (ebrStation.getUpdateTime() == null) {
                        ebrStation.setUpdateTime(new Date());
                    }
                    if(ebrStation.getSyncFlag() == null){
                    	ebrStation.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    }
                    if(ebrStation.getStatusSyncFlag() == null){
                    	ebrStation.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    }
                   return ebrStation;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebrStationService.saveList(list));
    }
}
