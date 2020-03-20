package cn.comtom.core.main.access.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.access.entity.dbo.AccessArea;
import cn.comtom.core.main.access.service.IAccessAreaService;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.domain.core.access.info.AccessAreaInfo;
import cn.comtom.domain.core.access.request.AccessAreaAddRequest;
import cn.comtom.domain.core.access.request.AccessAreaReq;
import cn.comtom.tools.response.*;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core/access/area")
@Api(tags = "信息接入区域")
public class AccessAreaController extends BaseController {

    @Autowired
    private IAccessAreaService accessAreaService;

    @GetMapping("/list")
    @ApiOperation(value = "查询信息区域编码列表", notes = "查询信息区域编码列表")
    public ApiPageResponse<AccessAreaInfo> list(@Valid AccessAreaReq req){
        if(log.isDebugEnabled()){
            log.debug("enter accessArea query list req=[{}]",req);
        }
        List<AccessAreaInfo> infoList = accessAreaService.list(req);

        return ApiPageResponse.ok(infoList);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存信息区域编码", notes = "保存信息区域编码")
    public ApiEntityResponse<AccessAreaInfo> save(@Valid AccessAreaAddRequest req){
        if(log.isDebugEnabled()){
            log.debug("enter accessArea save req=[{}]",req);
        }
        AccessArea accessArea = new AccessArea();
        BeanUtils.copyProperties(req,accessArea);
        accessArea.setId(UUIDGenerator.getUUID());
        accessAreaService.save(accessArea);
        AccessAreaInfo areaInfo = new AccessAreaInfo();
        BeanUtils.copyProperties(accessArea,areaInfo);
        return ApiEntityResponse.ok(areaInfo);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询信息区域编码", notes = "根据ID查询信息区域编码")
    public ApiEntityResponse<AccessAreaInfo> getById(@PathVariable(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("enter accessArea query by  id=[{}]",id);
        }
        AccessArea accessArea = accessAreaService.selectById(id);
        if(accessArea == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        AccessAreaInfo info = new AccessAreaInfo();
        BeanUtils.copyProperties(accessArea,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/getByInfoId")
    @ApiOperation(value = "根据信息ID查询区域数据", notes = "根据信息ID查询区域数据")
    public ApiListResponse<AccessAreaInfo> getByInfoId(@RequestParam(name = "infoId") String infoId){
        if(log.isDebugEnabled()){
            log.debug("enter accessFile query by  infoId=[{}]",infoId);
        }
        List<AccessAreaInfo> accessAreaInfoList = accessAreaService.getByInfoId(infoId);
        if(accessAreaInfoList == null){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.QUERY_NO_DATA);
        }
        return ApiListResponse.ok(accessAreaInfoList);
    }

}
