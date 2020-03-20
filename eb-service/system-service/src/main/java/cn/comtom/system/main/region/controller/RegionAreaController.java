package cn.comtom.system.main.region.controller;


import cn.comtom.domain.system.fw.CriterionRequest;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.region.entity.dbo.Region;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.system.main.region.service.IRegionAreaService;
import cn.comtom.system.main.region.service.IRegionService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/system/region")
@Api(tags = "行政区域信息管理")
@Slf4j
public class RegionAreaController extends BaseController {

    @Autowired
    private IRegionAreaService regionAreaService;

    @Autowired
    private IRegionService regionService;


    @GetMapping("/area/{areaCode}")
    @ApiOperation(value = "根据areaCode查询行政区域信息", notes = "根据areaCode查询行政区域信息")
    public ApiEntityResponse<RegionAreaInfo> getByAreaCode(@PathVariable(name = "areaCode") String areaCode){
        RegionArea regionArea = regionAreaService.selectByAreaCode(areaCode);//regionAreaService.selectById(areaCode);
        if(regionArea == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
        }
        RegionAreaInfo info = new RegionAreaInfo();
        BeanUtils.copyProperties(regionArea,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/area/getByAreaCodes")
    @ApiOperation(value = "根据areaCode查询行政区域信息", notes = "根据areaCode查询行政区域信息")
    public ApiListResponse<RegionAreaInfo> getByAreaCodes(@RequestParam(name = "areaCodes") List<String> areaCodes){
    	
        List<RegionArea> regionAreaList = regionAreaService.selectByAreaCodes(areaCodes);//regionAreaService.selectByIds(areaCodes);
        if(regionAreaList == null){
            return ApiResponseBuilder.buildListError(SystemErrorEnum.QUERY_NO_DATA);
        }
        List<RegionAreaInfo> regionAreaInfoList = new ArrayList<>();
        regionAreaList.forEach(regionArea -> {
            RegionAreaInfo regionAreaInfo = new RegionAreaInfo();
            BeanUtils.copyProperties(regionArea,regionAreaInfo);
            regionAreaInfoList.add(regionAreaInfo);
        });
        return ApiListResponse.ok(regionAreaInfoList);
    }


    @GetMapping("/area/getNextAreaCodeByParentCode")
    @ApiOperation(value = "根据父级areaCode查询下级区域信息", notes = "根据父级areaCode查询下级区域信息")
    public ApiEntityResponse<List<RegionAreaInfo>> getNextAreaCodeByParentCode(@RequestParam(name = "parentAreaCode") String parentAreaCode){
        List<RegionArea> regionAreaList = regionAreaService.getNextAreaCodeByParentCode(parentAreaCode);
        if(regionAreaList == null){
            regionAreaList=new ArrayList<RegionArea>();
        }
        List<RegionAreaInfo> regionAreaInfoList = new ArrayList<>();
        regionAreaList.forEach(regionArea -> {
            RegionAreaInfo regionAreaInfo = new RegionAreaInfo();
            BeanUtils.copyProperties(regionArea,regionAreaInfo);
            regionAreaInfoList.add(regionAreaInfo);
        });
        return ApiEntityResponse.ok(regionAreaInfoList);
    }

    @GetMapping("/area/getAreaTotal")
    @ApiOperation(value = "获取区域信息记录数", notes = "获取区域信息记录数")
    public ApiEntityResponse<Integer> getAreaTotal(){
        Integer total= regionAreaService.getAreaTotal();
        return ApiEntityResponse.ok(total);
    }

    @GetMapping("/area/rootArea")
    @ApiOperation(value = "获取区域信息记录数", notes = "获取区域信息记录数")
    public ApiEntityResponse<RegionAreaInfo> getRootArea(){
        CriterionRequest request = new CriterionRequest();
        request.setSidx("areaLevel");
        startPage(request);
        RegionAreaInfo areaInfo= regionAreaService.getRootArea();
        return ApiEntityResponse.ok(areaInfo);
    }


    @GetMapping("/area/getNextReginCodeByParentCode")
    @ApiOperation(value = "根据父级areaCode查询下级区域信息(全国区域)", notes = "全国区域")
    public ApiEntityResponse<List<RegionAreaInfo>> getNextReginCodeByParentCode(@RequestParam(name = "parentAreaCode") String parentAreaCode){
        List<Region> regionAreaList = regionService.getNextAreaCodeByParentCode(parentAreaCode);
        if(regionAreaList == null){
            regionAreaList=new ArrayList<Region>();
        }
        List<RegionAreaInfo> regionAreaInfoList = new ArrayList<>();
        regionAreaList.forEach(regionArea -> {
            RegionAreaInfo regionAreaInfo = new RegionAreaInfo();
            BeanUtils.copyProperties(regionArea,regionAreaInfo);
            regionAreaInfoList.add(regionAreaInfo);
        });
        return ApiEntityResponse.ok(regionAreaInfoList);
    }

    @PostMapping("/area/sureInitArea")
    @ApiOperation(value = "初始化指定区域信息", notes = "初始化指定区域信息")
    public ApiResponse sureInitArea(@RequestBody String areaCode){
        try {
            regionService.sureInitArea(areaCode);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(SystemErrorEnum.OPERATION_ERROR.getCode(),SystemErrorEnum.OPERATION_ERROR.getMsg());
        }
        return ApiResponse.ok();
    }

    
    @PutMapping("/area/updateOrSaveArea")
    @ApiOperation(value = "更新或者保存区域信息", notes = "更新或者保存区域信息")
    public ApiResponse updateOrSaveArea(@RequestBody RegionAreaInfo areaCode){
        try {
        	RegionArea regionArea = new RegionArea();
        	BeanUtils.copyProperties(areaCode, regionArea);
        	if(StringUtils.isBlank(areaCode.getId())) {
        		regionAreaService.save(regionArea);
        	}else {
        		RegionArea regionA = regionAreaService.selectById(areaCode.getId());
        		if(regionA!=null) {
        			regionA.setAreaCode(areaCode.getAreaCode());
        			regionA.setAreaName(areaCode.getAreaName());
        			regionAreaService.update(regionA);
        		}else {
        			regionAreaService.save(regionArea);
        		}
        	}
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(SystemErrorEnum.OPERATION_ERROR.getCode(),SystemErrorEnum.OPERATION_ERROR.getMsg());
        }
        return ApiResponse.ok();
    }
    
    @DeleteMapping("/area/deleteAreaById/{id}")
    @ApiOperation(value = "删除区域信息", notes = "删除区域信息")
    public ApiResponse deleteAreaById(@PathVariable(name="id") String id){
        try {
        	regionAreaService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(SystemErrorEnum.OPERATION_ERROR.getCode(),SystemErrorEnum.OPERATION_ERROR.getMsg());
        }
        return ApiResponse.ok();
    }
    
}
