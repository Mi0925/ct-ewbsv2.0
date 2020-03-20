package cn.comtom.ebs.front.main.area.controller;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.area.service.IAreaService;
import cn.comtom.ebs.front.main.ebr.service.IPlatformService;
import cn.comtom.ebs.front.main.params.service.ISysParamsService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/safeRest/area")
@Api(tags = "区域管理", description = "区域管理")
public class AreaController extends AuthController {

    @Autowired
    private IAreaService areaService;

    @Autowired
    private ISysParamsService sysParamsService;

    @Autowired
    private IPlatformService platformService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @GetMapping(value = "/getByAreaCode")
    @ApiOperation(value = "根据areaCode查询区域信息", notes = "根据areaCode查询区域信息")
    public ApiEntityResponse<RegionAreaInfo> getRegionAreaInfoByCode(@RequestParam(name = "areaCode") String areaCode){
        if(log.isDebugEnabled()){
            log.debug("[!~] get region area Info by areaCode . areaCode=[{}]  ",areaCode);
        }
        RegionAreaInfo regionAreaInfo = areaService.getByAreaCode(areaCode);
        return ApiEntityResponse.ok(regionAreaInfo);
    }

    @GetMapping(value = "/getByParentAreaCode")
    @ApiOperation(value = "根据父级areaCode查询下级区域信息", notes = "根据父级areaCode查询下级区域信息")
    public ApiListResponse<RegionAreaInfo> getByParentAreaCode(@RequestParam(name = "parentAreaCode") String parentAreaCode){
        if(log.isDebugEnabled()){
            log.debug("[!~] get region area Info by ParentAreaCode . ParentAreaCode=[{}]  ",parentAreaCode);
        }

        //当parentAreaCode为0时，为顶层查询
        boolean isTop=false;
        if(parentAreaCode.equals("0")){
            SysParamsInfo sysParamsInfo=sysParamsService.getByKey(Constants.EBR_PLATFORM_ID);
            EbrPlatformInfo ebrPlatform = platformService.findById(sysParamsInfo.getParamValue());
            if(ebrPlatform!=null){
                parentAreaCode=ebrPlatform.getAreaCode();
                isTop=true;
            }else{
                log.error("没有查询到默认的平台区域信息");
            }
        }

        List<RegionAreaInfo> regionAreaInfoList = areaService.getByParentAreaCode(parentAreaCode);
        regionAreaInfoList = Optional.ofNullable(regionAreaInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .peek(regionAreaInfo -> {
                    if(!"5".equals(regionAreaInfo.getAreaLevel())){
                        regionAreaInfo.setParent(true);
                    }
                }).collect(Collectors.toList());
        if(isTop){
            RegionAreaInfo regionAreaInfoRoot = areaService.getByAreaCode(parentAreaCode);
            regionAreaInfoList.add(0,regionAreaInfoRoot);
        }
        return ApiListResponse.ok(regionAreaInfoList);
    }

    @GetMapping("/getNextAreaByCurPlatArea")
    @ApiOperation(value = "根据当前平台区域查询下级区域信息", notes = "根据当前平台区域查询下级区域信息")
    public ApiEntityResponse<List<RegionAreaInfo>> getNextAreaByCurPlatArea(){
        SysParamsInfo sysParamsInfo=sysParamsService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatform = platformService.findById(sysParamsInfo.getParamValue());
        List<RegionAreaInfo> regionAreaList = areaService.getNextAreaCodeByParentCode(ebrPlatform.getAreaCode());

        RegionAreaInfo top=new RegionAreaInfo();
        top.setAreaName("全区域");
        top.setAreaCode(ebrPlatform.getAreaCode());
        regionAreaList.add(0,top);

        return ApiEntityResponse.ok(regionAreaList);
    }


    @GetMapping("/getAreaTotal")
    @ApiOperation(value = "判断当前区域信息是否被初始化", notes = "判断当前区域信息是否被初始化")
    public ApiEntityResponse<Integer> getAreaTotal(){
        Integer total=areaService.getAreaTotal();
        return ApiEntityResponse.ok(total);
    }

    @GetMapping("/getNextReginCodeByParentCode")
    @ApiOperation(value = "根据父级areaCode查询下级区域信息(全国区域)", notes = "全国区域")
    public ApiEntityResponse<List<RegionAreaInfo>> getNextReginCodeByParentCode(@RequestParam(name = "parentAreaCode") String parentAreaCode){
        List<RegionAreaInfo> regionAreaInfoList = areaService.getNextReginCodeByParentCode(parentAreaCode);
        return ApiEntityResponse.ok(regionAreaInfoList);
    }

    @GetMapping("/sureInitArea")
    @ApiOperation(value = "初始化指定区域信息", notes = "初始化指定区域信息")
    public ApiResponse sureInitArea(@RequestParam(name="areaCode") String areaCode){
        boolean flag=areaService.sureInitArea(areaCode);
        if(flag){
            return ApiResponse.ok();
        }else{
            return ApiResponse.error(FrontErrorEnum.INIT_AREA_ERROR.getCode(),FrontErrorEnum.INIT_AREA_ERROR.getMsg());
        }
    }
    
	
	  @PutMapping("/updateOrSaveArea")
	  @ApiOperation(value = "更新或者保存区域", notes = "更新或者保存区域") 
	  public ApiResponse updateOrSaveArea(@RequestBody RegionAreaInfo regionArea){
		  areaService.updateOrSaveArea(regionArea);
		  return ApiEntityResponse.ok(); 
	  }
	 
    
    @GetMapping("/rootArea")
    @ApiOperation(value = "获取当前平台的根区域编码", notes = "获取当前平台的根区域编码")
    public ApiEntityResponse<RegionAreaInfo> getRootArea(){
        RegionAreaInfo areaInfo=areaService.getRootArea();
        return ApiEntityResponse.ok(areaInfo);
    }
    
    @DeleteMapping("/deleteAreaById/{id}")
    @ApiOperation(value = "删除区域编码", notes = "删除区域编码")
    public ApiResponse deleteAreaById(@PathVariable(name = "id") String id){
        areaService.deleteAreaById(id);
        return ApiEntityResponse.ok();
    }
    
}
