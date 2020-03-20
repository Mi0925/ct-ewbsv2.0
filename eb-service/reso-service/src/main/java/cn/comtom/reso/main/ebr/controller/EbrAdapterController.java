package cn.comtom.reso.main.ebr.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.AdapterWhereRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;
import cn.comtom.reso.main.ebr.service.IEbrAdapterService;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;
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
@RequestMapping("/res/ebr/adapter")
@Api(tags = "应急广播适配器信息管理")
@Slf4j
public class EbrAdapterController extends BaseController {

    @Autowired
    private IEbrAdapterService ebrAdapterService;

    @Autowired
    private IEbrChannelService ebrChannelService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据EbrId查询适配器资源信息", notes = "根据EbrId查询适配器资源信息")
    public ApiEntityResponse<EbrAdapterInfo> getById(@PathVariable String ebrId){
        EbrAdapter ebrAdapter = ebrAdapterService.selectById(ebrId);
        if(ebrAdapter == null){
            return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
        }
        EbrAdapterInfo info = new EbrAdapterInfo();
        BeanUtils.copyProperties(ebrAdapter,info);
        info.setResChannel(ebrChannelService.getChannelByEbrId(ebrId));
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询适配器资源信息", notes = "分页查询适配器资源信息")
    public ApiPageResponse<EbrAdapterInfo> getByPage(@ModelAttribute EbrAdapterPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("get ebr adapter info by page . request=[{}]",request);
        }
        startPage(request);
        List<EbrAdapterInfo> ebrTerminalInfoList =  ebrAdapterService.getList(request);
        return ApiPageResponse.ok(ebrTerminalInfoList,page);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新适配器信息", notes = "更新适配器信息")
    public ApiEntityResponse<Integer> updateEbrAdapterInfo(@RequestBody @Valid AdapterUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("update ebr adapter info by ebrId . request=[{}]  ",request);
        }
        EbrAdapter ebrAdapter= new EbrAdapter();
        BeanUtils.copyProperties(request,ebrAdapter);
        ebrAdapter.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());    //重新同步
        ebrAdapter.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());        //重新同步
        return ApiEntityResponse.ok(ebrAdapterService.update(ebrAdapter,request.getResChannel()));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增适配器信息", notes = "新增适配器信息")
    public ApiEntityResponse<EbrAdapterInfo> saveEbrAdapterInfo(@RequestBody @Valid AdapterAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("save ebr adapter info . request=[{}]  ",request);
        }
        EbrAdapter ebrAdapter= new EbrAdapter();
        BeanUtils.copyProperties(request,ebrAdapter);
        if(ebrAdapter.getCreateTime() == null){
        	ebrAdapter.setCreateTime(new Date());
        }
        if(ebrAdapter.getSyncFlag() == null){
        	ebrAdapter.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        if(ebrAdapter.getStatusSyncFlag() == null){
        	ebrAdapter.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
        }
        if (ebrAdapter.getUpdateTime() == null) {
            ebrAdapter.setUpdateTime(new Date());
        }

        ebrAdapterService.save(ebrAdapter,request.getResChannel());
        EbrAdapterInfo ebrAdapterInfo = new EbrAdapterInfo();
        BeanUtils.copyProperties(ebrAdapter,ebrAdapterInfo);
        return ApiEntityResponse.ok(ebrAdapterInfo);
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除适配器信息", notes = "删除适配器信息")
    public ApiResponse delete(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("delete ebr adapter info by ebrId . ebrId=[{}]  ",ebrId);
        }
        if(StringUtils.isBlank(ebrId)){
            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        ebrAdapterService.deleteById(ebrId);
        return ApiResponse.ok();
    }

    @GetMapping("/findAdapterListByWhere")
    @ApiOperation(value = "应急广播适配器信息", notes = "应急广播适配器信息")
    public ApiListResponse<EbrAdapterInfo> findAdapterListByWhere(@ModelAttribute AdapterWhereRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find EbrAdapterInfo by condition . request = [{}]",JSON.toJSONString(request));
        }
        List<EbrAdapterInfo> ebrTerminalInfoList = ebrAdapterService.findAdapterListByWhere(request);
        return ApiListResponse.ok(ebrTerminalInfoList);
    }

    @PutMapping("/updateAdapterBatch")
    @ApiOperation(value = "批量更新适配器信息", notes = "批量更新适配器信息")
    public ApiEntityResponse<Integer> updateAdapterBatch(@RequestBody List<AdapterUpdateRequest> adapterUpdateRequest){
        if(log.isDebugEnabled()){
        }
        Long count = Optional.ofNullable(adapterUpdateRequest).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request ->{
                    EbrAdapter ebrAdapter = new EbrAdapter();
                    BeanUtils.copyProperties(request,ebrAdapter);
                    ebrAdapter.setEbrAsId(request.getEbrAsId());
                    ebrAdapter.setAdapterState(request.getAdapterState());
                    ebrAdapter.setUpdateTime(new Date());
                    ebrAdapter.setSyncFlag(request.getSyncFlag());
                    ebrAdapter.setStatusSyncFlag(request.getStatusSyncFlag());
                    return ebrAdapterService.update(ebrAdapter);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PutMapping("/updateEbrAdapterState")
    @ApiOperation(value = "批量更新适配器信息状态", notes = "批量更新适配器信息状态")
    public ApiEntityResponse<Integer> updateEbrAdapterState(@RequestBody List<EbrStateUpdateRequest> ebrStateUpdateRequestList){
        Long count = Optional.ofNullable(ebrStateUpdateRequestList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request ->{
                	EbrAdapter ebrAdapter = new EbrAdapter();
                	ebrAdapter.setEbrAsId(request.getEbrId());
                    if (request.getState() != null) {
                    	ebrAdapter.setAdapterState(request.getState()+"");
                    }
                    ebrAdapter.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    ebrAdapter.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    ebrAdapter.setUpdateTime(new Date());
                    return ebrAdapterService.update(ebrAdapter);
                }).count();
        return ApiEntityResponse.ok(count.intValue());
    }

    @PostMapping(value = "/findListByAdapterIds")
    @ApiOperation(value = "根据适配器ID集合查询信息集合", notes = "根据适配器ID集合查询信息集合")
    public ApiEntityResponse<List<EbrAdapterInfo>> findListByAdapterIds(@RequestBody List<String> adapterIds){
         if(adapterIds.size()==0){
             return ApiEntityResponse.ok(new ArrayList<EbrAdapterInfo>());
         }
        return ApiEntityResponse.ok(ebrAdapterService.findListByAdapterIds(adapterIds));
    }

    @PostMapping("/addEbrAdapterBatch")
    @ApiOperation(value = "批量新增适配器信息", notes = "批量新增适配器信息")
    public ApiEntityResponse<Integer>  addEbrAdapterBatch(@RequestBody @Valid List<AdapterAddRequest> addRequestList){
        List<EbrAdapter> list = Optional.ofNullable(addRequestList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(request -> !EntityUtil.isEmpty(request))
                .map(request -> {
                	EbrAdapter ebrAdapter = new EbrAdapter();
                    BeanUtils.copyProperties(request,ebrAdapter);
                    if(ebrAdapter.getCreateTime() == null){
                    	ebrAdapter.setCreateTime(new Date());
                    }
                    if (ebrAdapter.getUpdateTime() == null) {
                        ebrAdapter.setUpdateTime(new Date());
                    }
                    if(ebrAdapter.getSyncFlag() == null){
                    	ebrAdapter.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    }
                    if(ebrAdapter.getStatusSyncFlag() == null){
                    	ebrAdapter.setStatusSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());
                    }
                   return ebrAdapter;
                }).collect(Collectors.toList());
        return ApiEntityResponse.ok(ebrAdapterService.saveList(list));
    }
}
