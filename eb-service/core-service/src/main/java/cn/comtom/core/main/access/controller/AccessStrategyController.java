package cn.comtom.core.main.access.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.access.entity.dbo.AccessStrategy;
import cn.comtom.core.main.access.service.IAccessStrategyService;
import cn.comtom.core.main.access.service.IAccessTimeService;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.domain.core.access.info.AccessStrategyAll;
import cn.comtom.domain.core.access.info.AccessStrategyInfo;
import cn.comtom.domain.core.access.info.AccessTimeInfo;
import cn.comtom.domain.core.access.request.AccessStrategyAddRequest;
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
@RequestMapping("/core/access/strategy")
@Api(tags = "接入信息策略")
public class AccessStrategyController extends BaseController {

    @Autowired
    private IAccessStrategyService accessStrategyService;

    @Autowired
    private IAccessTimeService accessTimeService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询接入信息策略", notes = "根据ID查询接入信息策略")
    public ApiEntityResponse<AccessStrategyInfo> getById(@PathVariable(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("enter AccessStrategy query by  id=[{}]",id);
        }
        AccessStrategy accessStrategy = accessStrategyService.selectById(id);
        if(accessStrategy == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        AccessStrategyInfo info = new AccessStrategyInfo();
        BeanUtils.copyProperties(accessStrategy,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存接入信息策略", notes = "保存接入信息策略")
    public ApiEntityResponse<AccessStrategyInfo> save(@RequestBody @Valid AccessStrategyAddRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter accessStrategy save request=[{}]",request);
        }
        AccessStrategy accessStrategy = new AccessStrategy();
        String id = UUIDGenerator.getUUID();
        BeanUtils.copyProperties(request,accessStrategy);
        accessStrategy.setStrategyId(id);
        accessStrategyService.save(accessStrategy);
        AccessStrategyInfo info = new AccessStrategyInfo();
        BeanUtils.copyProperties(accessStrategy,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/getByInfoId")
    @ApiOperation(value = "根据信息ID查询策略信息", notes = "根据信息ID查询策略信息")
    public ApiListResponse<AccessStrategyInfo> getByInfoId(@RequestParam(name = "infoId") String infoId){
        if(log.isDebugEnabled()){
            log.debug("enter accessFile query by  infoId=[{}]",infoId);
        }
        List<AccessStrategyInfo> accessStrategyInfos = accessStrategyService.getByInfoId(infoId);
        if(accessStrategyInfos == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(accessStrategyInfos);
    }

    @GetMapping("/getInfoAll")
    @ApiOperation(value = "根据信息ID查询策略以及策略时间段", notes = "根据信息ID查询策略以及策略时间段")
    public ApiEntityResponse<AccessStrategyAll> getInfoAll(@RequestParam(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("enter accessStrategy query all info by  id=[{}]",id);
        }
        AccessStrategyAll accessStrategyAll = new AccessStrategyAll();
        AccessStrategy accessStrategy = accessStrategyService.selectById(id);
        if(accessStrategy == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        BeanUtils.copyProperties(accessStrategy,accessStrategyAll);
        List<AccessTimeInfo> accessTimeInfoList = accessTimeService.getByStrategyId(id);
        accessStrategyAll.setAccessTimeInfoList(accessTimeInfoList);
        return ApiEntityResponse.ok(accessStrategyAll);
    }

}
