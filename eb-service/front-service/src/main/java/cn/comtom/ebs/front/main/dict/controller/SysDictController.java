package cn.comtom.ebs.front.main.dict.controller;

import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.dict.service.ISysDictService;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/safeRest/dict")
@Api(tags = "系统字典管理", description = "系统字典管理")
public class SysDictController extends AuthController {

    @Autowired
    private ISysDictService sysDictService;

    @GetMapping(value = "/getByGroupCode")
    @ApiOperation(value = "根据分组ID查询系统字典信息", notes = "根据分组ID查询系统字典信息")
    public ApiListResponse<SysDictInfo> getSysDictInfoByGroupId(@RequestParam(name = "dictGroupCode") String dictGroupCode){
        if(log.isDebugEnabled()){
            log.debug("[!~] get SysDictInfo by dictGroupCode. dictGroupCode=[{}]",dictGroupCode);
        }
        if(StringUtils.isBlank(dictGroupCode)){
            return ApiResponseBuilder.buildListError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        String[] codeArray = dictGroupCode.split(SymbolConstants.GENERAL_SEPARATOR);
        List<SysDictInfo> sysDictInfoList = new ArrayList<>() ;
        for(String code : codeArray){
            if(StringUtils.isNotBlank(code)){
                sysDictInfoList.addAll(sysDictService.getSysDictInfoByGroupCode(code));
            }
        }
        return ApiListResponse.ok(sysDictInfoList);
    }


    @GetMapping("/getStatusFromSysDict")
    @ApiOperation(value = "查询接入状态集合", notes = "查询接入状态集合（数据字典）")
    public ApiEntityResponse<List<SysDictInfo>> getStatusFromSysDict(){
        return ApiEntityResponse.ok(sysDictService.getSysDictInfoByGroupCode(SysDictCodeEnum.ACCESS_RECORD_STATUS.getCode()));
    }

    @GetMapping("/getSeverityFromSysDict")
    @ApiOperation(value = "查询事件类型集合", notes = "查询事件类型集合（数据字典）")
    public ApiEntityResponse<List<SysDictInfo>> getSeverityFromSysDict(){
        return ApiEntityResponse.ok(sysDictService.getSysDictInfoByGroupCode(SysDictCodeEnum.SEVERITY_DICT.getCode()));
    }

    @GetMapping("/getPlanFlowFromSysDict")
    @ApiOperation(value = "查询预案流程集合", notes = "查询预案流程集合（数据字典）")
    public ApiEntityResponse<List<SysDictInfo>> getPlanFlowFromSysDict(){
        return ApiEntityResponse.ok(sysDictService.getSysDictInfoByGroupCode(SysDictCodeEnum.PLAN_MATH_FLOW.getCode()));
    }


}
