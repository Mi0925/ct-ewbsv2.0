package cn.comtom.ebs.front.main.ebr.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrGisInitParame;
import cn.comtom.domain.reso.ebr.info.EbrGisStatusCount;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.dict.service.ISysDictService;
import cn.comtom.ebs.front.main.ebr.model.EbrTypeEntity;
import cn.comtom.ebs.front.main.ebr.service.IPlatformService;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.ebs.front.main.params.service.ISysParamsService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.ResTypeDictEnum;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.EntityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:38
 */

@Slf4j
@RestController
@RequestMapping("/safeRest")
@Api(tags = "系统资源信息视图", description = "系统资源信息视图")
@AuthRest
public class EbrController extends AuthController {

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private ISysParamsService sysParamsService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private IPlatformService platformService;


    @GetMapping("/ebrView/gis/page")
    @ApiOperation(value = "gis根据条件查询", notes = "gis根据条件查询")
    public ApiPageResponse<EbrGisInfo> getEbrGisByPage(@Valid EbrGisPageRequest request){
        ApiPageResponse<EbrGisInfo> page = resoFeginService.getEbrGisByPage(request);
        return page;
    }


    @GetMapping("/ebrView/gis/getEbrGisStatusCount")
    @ApiOperation(value = "gis展示状态统计", notes = "gis展示状态统计")
    public ApiEntityResponse<List<EbrGisStatusCount>> getEbrGisStatusCount(@Valid EbrGisPageRequest request){

        List<EbrGisStatusCount> gisList=new ArrayList<EbrGisStatusCount>();

        List<EbrstatusCount> list=resoFeginService.getEbrGisStatusCount(request);

        List<SysDictInfo> ebrTypeList = sysDictService.getSysDictInfoByGroupCode(SysDictCodeEnum.EBR_TYPE.getCode());

        //增加如果前台增加了判断条件是，不查询未匹配的资源类型和状态。
        if(request.getEbrType()!=null){
            for(int i=ebrTypeList.size()-1;i>=0;i--){
                    if(!ebrTypeList.get(i).getDictKey().equals(request.getEbrType())){
                        ebrTypeList.remove(i);
                    }
            }
        }

        List<SysDictInfo> ebrStatusList = sysDictService.getSysDictInfoByGroupCode(SysDictCodeEnum.RES_STATUS_DICT.getCode());
        if(request.getStatus()!=null){
            for(int j=ebrStatusList.size()-1;j>=0;j--){
                if(!ebrStatusList.get(j).getDictKey().equals(request.getStatus().toString())){
                    ebrStatusList.remove(j);
                }
            }
        }


        for(SysDictInfo dict:ebrTypeList){
            EbrGisStatusCount gisStatusCount=new EbrGisStatusCount();
            gisStatusCount.setEbrType(dict.getDictKey());
            gisStatusCount.setEbrTypeName(dict.getDictValue());
            gisStatusCount.setData(getStatusData(dict,list,ebrStatusList));
            gisList.add(gisStatusCount);
        }

        return ApiEntityResponse.ok(gisList);
    }

    private List<EbrstatusCount> getStatusData(SysDictInfo dict, List<EbrstatusCount> list, List<SysDictInfo> ebrStatusList) {
        List<EbrstatusCount>  counts=new ArrayList<EbrstatusCount>();
        for(SysDictInfo d:ebrStatusList )  {
            EbrstatusCount count=new EbrstatusCount();
            count.setTotal(0);
            count.setEbrType(d.getDictValue());
            count.setStatus(Integer.parseInt(d.getDictKey()));
            for(EbrstatusCount c:list){
                if(c.getEbrType().equals(dict.getDictKey())&&c.getStatus()==Integer.parseInt(d.getDictKey())){
                    count.setTotal(c.getTotal());
                    break;
                }
            }
            counts.add(count);
        }
        return counts;
    }


    @GetMapping("/ebrView/gis/initParame")
    @ApiOperation(value = "获取gis展示初始化参数", notes = "获取gis展示初始化参数")
    public ApiEntityResponse<EbrGisInitParame> initParame(){
        EbrGisInitParame parame=new EbrGisInitParame();

        SysParamsInfo mapZoom = sysParamsService.getByKey(Constants.MAP_ZOOM);
        SysParamsInfo longitude = sysParamsService.getByKey(Constants.MAP_LONGITUDE);
        SysParamsInfo latitude = sysParamsService.getByKey(Constants.MAP_LATITUDE);
        if(mapZoom!=null){
            parame.setZoom(Integer.parseInt(mapZoom.getParamValue()));
        }else{
            return ApiEntityResponse.error(FrontErrorEnum.MAP_INIT_NOT_EXIST.getCode(),FrontErrorEnum.MAP_INIT_NOT_EXIST.getMsg());
        }

        List<Double> lnt=new ArrayList<Double>();
        if(longitude!=null){
            lnt.add(Double.parseDouble(longitude.getParamValue()));
        }else{
            return ApiEntityResponse.error(FrontErrorEnum.MAP_INIT_NOT_EXIST.getCode(),FrontErrorEnum.MAP_INIT_NOT_EXIST.getMsg());
        }

        if(latitude!=null){
            lnt.add(Double.parseDouble(latitude.getParamValue()));
        }else{
            return ApiEntityResponse.error(FrontErrorEnum.MAP_INIT_NOT_EXIST.getCode(),FrontErrorEnum.MAP_INIT_NOT_EXIST.getMsg());
        }
        parame.setCenter(lnt);
        return ApiEntityResponse.ok(parame);
    }



    @GetMapping("/ebrView/list")
    @ApiOperation(value = "查询所有播出资源信息", notes = "查询所有播出资源信息")
    public ApiListResponse<EbrInfo> viewList(@Valid EbrQueryRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get ebr info view . request=[{}]", request);
        }
        List<EbrInfo> ebrInfoViewList = resoFeginService.getEbrInfoViewList(request);
        return ApiListResponse.ok(ebrInfoViewList);
    }

    @GetMapping("/ebrView/page")
    @ApiOperation(value = "分页查询播出资源信息", notes = "分页查询播出资源信息")
    public ApiPageResponse<EbrInfo> page(@Valid EbrPageRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get ebr info view by page . request=[{}]", request);
        }
        return  resoFeginService.getEbrInfoViewByPage(request);
    }

    @GetMapping("/ebrView/infoSrc/page")
    @ApiOperation(value = "分页查询信息源列表", notes = "分页查询信息源列表")
    @RequiresPermissions(Permissions.SYS_INFO_SRC_LIST)
    public ApiPageResponse<EbrInfo> getInfoSrcByPage(@Valid EbrPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] query infoSrc list by page ： request=[{}]",request);
        }

        //获取当前平台区域信息
        SysParamsInfo sysParamsInfo = sysParamsService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatform = platformService.findById(sysParamsInfo.getParamValue());


        String localAreaCode =(ebrPlatform==null?null:ebrPlatform.getAreaCode());


        if(StringUtils.isBlank(localAreaCode)){
            log.error("获取当前平台区域信息错误；");
            return ApiPageResponse.error(FrontErrorEnum.REQUIRED_PARAM_EMPTY.getCode(),FrontErrorEnum.REQUIRED_PARAM_EMPTY.getMsg());
        }
        request.setLocalAreaCode(localAreaCode);
        return resoFeginService.getInfoSrcByPage(request);
    }



    @GetMapping("/ebrView/fromEbrlist")
    @ApiOperation(value = "查询来源单位信息", notes = "查询来源单位信息")
    public ApiListResponse<EbrInfo> listChildren() {

        EbrQueryRequest request=new EbrQueryRequest();

        //查询本级平台信息
        SysParamsInfo sysParamsInfo = sysParamsService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo platform = Optional.ofNullable(sysParamsInfo).map(paramInfo -> {
            String localEbrId = paramInfo.getParamValue();
            return  Optional.ofNullable(localEbrId).map(ebrId -> resoFeginService.findPlatfomById(ebrId)).orElseGet(null);
        }).orElseGet(null);
        if(platform == null){
            return ApiResponseBuilder.buildListError(FrontErrorEnum.QUERY_LOCAL_EBR_NOT_EXISTS);
        }
        //获取本级平台的区域编码
        String ebrAreaCode = Optional.of(platform).map(EbrPlatformInfo::getAreaCode).orElseGet(String::new);

        //查询所有下一级区域编码
        List<RegionAreaInfo> regionAreaInfoList = systemFeginService.getNextAreaCodeByParentCode(ebrAreaCode);
        List<String> areaCodes = Optional.ofNullable(regionAreaInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(RegionAreaInfo::getAreaCode)
                .distinct()
                .collect(Collectors.toList());

        //查询父级区域编码
        SysParamsInfo parentSysParamsInfo = sysParamsService.getByKey(Constants.PARENT_PLATFORM_ID);
        EbrPlatformInfo parentPlatform = resoFeginService.findPlatfomById(parentSysParamsInfo.getParamValue());
        if(null != parentPlatform && null != parentPlatform.getAreaCode()){
            areaCodes.add(parentPlatform.getAreaCode());
        }

        //把本级区域的也加上
        areaCodes.add(ebrAreaCode);
        request.setAreaCodes(areaCodes);

        List<EbrInfo> ebrInfoViewList = resoFeginService.getEbrInfoViewList(request);
        //如果时应急平台系统，过滤掉本级平台，其他资源只取本级区域资源
        ebrInfoViewList = Optional.ofNullable(ebrInfoViewList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(ebrInfo -> {
                    if(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(ebrInfo.getEbrType())){
                        //平台
                        return !platform.getPsEbrId().equals(ebrInfo.getEbrId());
                    }
                    return ebrAreaCode.equals(ebrInfo.getAreaCode());
                })
                .collect(Collectors.toList());
        return ApiListResponse.ok(ebrInfoViewList);
    }


    @GetMapping("/ebrView/listChildren")
    @ApiOperation(value = "查询所有播出系统以及所有下级的平台信息", notes = "查询所有播出系统以及所有下级的平台信息")
    public ApiListResponse<EbrInfo> listChildren(@Valid EbrQueryRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get  ebr info children view . request=[{}]", request);
        }
        //查询本级平台信息
        SysParamsInfo sysParamsInfo = sysParamsService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo platform = Optional.ofNullable(sysParamsInfo).map(paramInfo -> {
                        String localEbrId = paramInfo.getParamValue();
                        return  Optional.ofNullable(localEbrId).map(ebrId -> resoFeginService.findPlatfomById(ebrId)).orElseGet(null);
                }).orElseGet(null);
        if(platform == null){
            return ApiResponseBuilder.buildListError(FrontErrorEnum.QUERY_LOCAL_EBR_NOT_EXISTS);
        }
        //获取本级平台的区域编码
        String ebrAreaCode = Optional.of(platform).map(EbrPlatformInfo::getAreaCode).orElseGet(String::new);

        //查询所有下一级区域编码
        List<RegionAreaInfo> regionAreaInfoList = systemFeginService.getNextAreaCodeByParentCode(ebrAreaCode);
        List<String> areaCodes = Optional.ofNullable(regionAreaInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(RegionAreaInfo::getAreaCode)
                .distinct()
                .collect(Collectors.toList());

        //查询父级区域编码
        SysParamsInfo parentSysParamsInfo = sysParamsService.getByKey(Constants.PARENT_PLATFORM_ID);
        EbrPlatformInfo parentPlatform = resoFeginService.findPlatfomById(parentSysParamsInfo.getParamValue());
        if(null != parentPlatform && null != parentPlatform.getAreaCode()){
            areaCodes.add(parentPlatform.getAreaCode());
        }
        //本级平台的也加上
        areaCodes.add(ebrAreaCode);
        if(request.getAreaCodes() == null || request.getAreaCodes().isEmpty()){
            request.setAreaCodes(areaCodes);
        }
        List<EbrInfo> ebrInfoViewList = resoFeginService.getEbrInfoViewList(request);
        ebrInfoViewList = Optional.ofNullable(ebrInfoViewList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(ebrInfo -> {
                    if(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(ebrInfo.getEbrType())){
                        //平台
                        return !platform.getPsEbrId().equals(ebrInfo.getEbrId());
                    }
                    return ebrAreaCode.equals(ebrInfo.getAreaCode());
                })
                .collect(Collectors.toList());
        return ApiListResponse.ok(ebrInfoViewList);
    }

    @GetMapping("/ebrView/listForType")
    @ApiOperation(value = "查询所有的平台信息,按照平台类型归类", notes = "查询所有的平台信息，按照平台类型归类")
    public ApiListResponse<EbrTypeEntity> listForType(@Valid EbrQueryRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get ebr info view . request=[{}]", request);
        }
        //查询本级平台信息
        SysParamsInfo sysParamsInfo = sysParamsService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo platform = Optional.ofNullable(sysParamsInfo).map(paramInfo -> {
            String localEbrId = paramInfo.getParamValue();
            return  Optional.ofNullable(localEbrId).map(ebrId -> resoFeginService.findPlatfomById(ebrId)).orElseGet(null);
        }).orElseGet(null);
        if(platform == null){
            return ApiResponseBuilder.buildListError(FrontErrorEnum.QUERY_LOCAL_EBR_NOT_EXISTS);
        }
        //获取本级平台的区域编码
        String ebrAreaCode = Optional.of(platform).map(EbrPlatformInfo::getAreaCode).orElseGet(String::new);

        //查询所有下一级区域编码
        List<RegionAreaInfo> regionAreaInfoList = systemFeginService.getNextAreaCodeByParentCode(ebrAreaCode);
        List<String> areaCodes = Optional.ofNullable(regionAreaInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(RegionAreaInfo::getAreaCode)
                .distinct()
                .collect(Collectors.toList());
        //本级平台的也加上
        areaCodes.add(ebrAreaCode);
        if(request.getAreaCodes() == null || request.getAreaCodes().isEmpty()){
            request.setAreaCodes(areaCodes);
        }

        //查询所有播出资源信息列表
        List<EbrInfo> ebrInfoViewList1 = resoFeginService.getEbrInfoViewList(request);
        List<EbrInfo> ebrInfoViewList = Optional.ofNullable(ebrInfoViewList1).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .filter(ebrInfo -> {
                    if(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(ebrInfo.getEbrType())){
                        //平台
                        return !ebrAreaCode.equals(ebrInfo.getAreaCode());
                    }
                    return ebrAreaCode.equals(ebrInfo.getAreaCode()) && !Constants.EBR_TYPE_PBULISH.equals(ebrInfo.getEbrType());
                })
                .collect(Collectors.toList());


        //遍历出所有播出资源的类型。
        List<EbrTypeEntity> ebrTypeEntityList = Optional.ofNullable(ebrInfoViewList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebrInfo -> {
                    EbrTypeEntity ebrTypeEntity = new EbrTypeEntity();
                    //ebrTypeEntity.setEbrTypeName(ebrInfo.getEbrTypeName());
                    ebrTypeEntity.setEbrType(ebrInfo.getEbrType());
                    ebrTypeEntity.setResType(ebrInfo.getResType());
                    ebrTypeEntity.setEbrTypeName(ResTypeDictEnum.getDescByKey(ebrInfo.getResType()));
                    return ebrTypeEntity;
                })
                .distinct()
                .filter(distinctByKey(EbrTypeEntity::getResType))
                .filter(ebrTypeEntity -> !EntityUtil.isReallyEmpty(ebrTypeEntity))
                .collect(Collectors.toList());
        log.info("listForType1:{}",JSON.toJSONString(ebrTypeEntityList));
        //将播出资源列表归类到对应的类型下
        ebrTypeEntityList = Optional.ofNullable(ebrTypeEntityList).orElse(Collections.emptyList()).stream()
                .peek(ebrTypeEntity -> {
                    List<EbrInfo> ebrInfoList = Optional.ofNullable(ebrInfoViewList).orElse(Collections.emptyList()).stream()
                            .filter(Objects::nonNull)
                            .filter(ebrInfo -> StringUtils.isNotBlank(ebrInfo.getResType()))
                            .filter(ebrInfo ->
                                    ebrTypeEntity.getResType().equals(ebrInfo.getResType())
                            ).collect(Collectors.toList());
                    ebrTypeEntity.setEbrInfoList(ebrInfoList);
                }).collect(Collectors.toList());
        log.info("listForType2:{}",JSON.toJSONString(ebrTypeEntityList));
        EbrPlatformInfo parentPlatformInfo = getParentPlatformInfo();
        if(parentPlatformInfo!=null) {
        	List<EbrTypeEntity> ebrTypelists = Lists.newArrayList();
        	EbrTypeEntity ebrTypeEntity =  new EbrTypeEntity();
            ebrTypeEntity.setResType(ResTypeDictEnum.SYS_SUB_RES_TYPE_PLATFORM.getKey());
            ebrTypeEntity.setEbrTypeName("上级平台");
            ebrTypeEntity.setEbrType(ResTypeDictEnum.SYS_SUB_RES_TYPE_PLATFORM.getKey());
	        EbrInfo ebrInfo = new EbrInfo();
	        ebrInfo.setEbrId(parentPlatformInfo.getPsEbrId());
	        ebrInfo.setEbrName(parentPlatformInfo.getPsEbrName());
	        ebrInfo.setEbrType(ResTypeDictEnum.SYS_SUB_RES_TYPE_PLATFORM.getKey());
	        ebrInfo.setEbrUrl(parentPlatformInfo.getPsUrl());
	        List<EbrInfo> parentsList = Lists.newArrayList();
	        parentsList.add(ebrInfo);
	        ebrTypeEntity.setEbrInfoList(parentsList);
	        ebrTypelists.add(ebrTypeEntity);
	        ebrTypeEntityList.addAll(ebrTypelists);
        }
        return ApiListResponse.ok(ebrTypeEntityList);
    }
    
    @GetMapping("/ebrView/listEbrListforStation")
    @ApiOperation(value = "查询所有的资源信息", notes = "查询所有的资源信息")
    public ApiPageResponse<EbrStationInfo> listEbrListforStation() {
    	StationWhereRequest whereRequest = new StationWhereRequest();
    	List<EbrStationInfo> listdata = resoFeginService.findStationListByWhere(whereRequest);
    	return ApiPageResponse.ok(listdata);
    }
    
    @GetMapping("/ebrView/checkEbrAlarmVal/{resType}")
    @ApiOperation(value = "检查资源的是否达到报警值", notes = "检查资源的是否达到报警值")
    public ApiResponse checkEbrAlarmVal(@PathVariable(name = "resType")String resType) {
    	SysParamsInfo byKey = sysParamsService.getByKey(Constants.EBR_ALARM_VALUE);
    	if(byKey==null || StringUtils.isBlank(byKey.getParamValue())) {
    	   return  ApiResponseBuilder.buildListError(FrontErrorEnum.QUERY_NO_DATA);
    	}
    	boolean result = true;
    	try {
			result = platformService.checkEbrAlarmVal(resType, byKey.getParamValue());
		} catch (ParseException e) {
			return  ApiResponseBuilder.buildListError(FrontErrorEnum.QUERY_NO_DATA);
		}
    	if(!result) {
    		return  ApiResponseBuilder.buildListError(FrontErrorEnum.QUERY_NO_DATA);
    	}
    	return ApiResponse.ok();
    }
    
	/*
	 * @GetMapping("/ebrView/listEbrListforPlatform")
	 * 
	 * @ApiOperation(value = "查询所有的资源信息", notes = "查询所有的资源信息") public
	 * ApiPageResponse<EbrStationInfo> listEbrListforPlatform() {
	 * StationWhereRequest whereRequest = new StationWhereRequest();
	 * resoFeginService.findPlatformListByWhere(whereRequest) return
	 * ApiPageResponse.ok(listdata); }
	 */
    
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    
    private EbrPlatformInfo getParentPlatformInfo() {
    	SysParamsInfo info = systemFeginService.getByKey(Constants.PARENT_PLATFORM_ID);
    	String parentPlatformId = info.getParamValue();
    	if(StringUtils.isBlank(parentPlatformId)||("NULL".equals(parentPlatformId))){
             return null;
        }
    	EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(parentPlatformId);
        return ebrPlatform;
    }
}
