package cn.comtom.core.main.access.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.access.entity.dbo.AccessTime;
import cn.comtom.core.main.access.service.IAccessTimeService;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.domain.core.access.info.AccessTimeInfo;
import cn.comtom.domain.core.access.request.AccessTimeAddRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core/access/strategy/time")
@Api(tags = "接入信息策略")
public class AccessTimeController extends BaseController {

    @Autowired
    private IAccessTimeService accessTimeService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询接入信息策略时间段信息", notes = "根据ID查询接入信息策略时间段信息")
    public ApiEntityResponse<AccessTimeInfo> getById(@PathVariable(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("enter accessStrategyTime query by  id=[{}]",id);
        }
        AccessTime accessTime = accessTimeService.selectById(id);
        if(accessTime == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        AccessTimeInfo info = new AccessTimeInfo();
        BeanUtils.copyProperties(accessTime,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存接入信息策略时间段信息", notes = "保存接入信息策略时间段信息")
    public ApiEntityResponse<AccessTimeInfo> save(@RequestBody @Valid AccessTimeAddRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter accessStrategyTime save request=[{}]",request);
        }
        AccessTime accessTime = new AccessTime();
        String id = UUIDGenerator.getUUID();
        BeanUtils.copyProperties(request,accessTime);
        accessTime.setTimeId(id);
        accessTimeService.save(accessTime);
        AccessTimeInfo info = new AccessTimeInfo();
        BeanUtils.copyProperties(accessTime,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/getByStrategyId")
    @ApiOperation(value = "根据信息ID查询策略时间段信息", notes = "根据信息ID查询策略时间段信息")
    public ApiListResponse<AccessTimeInfo> getByStrategyId(@RequestParam(name = "strategyId") String strategyId){
        if(log.isDebugEnabled()){
            log.debug("enter accessTimeInfo query by  strategyId=[{}]",strategyId);
        }
        List<AccessTimeInfo> accessTimeInfoList = accessTimeService.getByStrategyId(strategyId);
        if(accessTimeInfoList == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(accessTimeInfoList);
    }

}
