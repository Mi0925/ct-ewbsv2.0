package cn.comtom.reso.main.ebr.controller;


import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrGisStatusCount;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.domain.reso.fw.CriterionRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.ebr.service.IVEbrGisService;
import cn.comtom.reso.main.ebr.service.IVEbrService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.utils.EntityUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/res/ebr/view")
@Api(tags = "系统资源视图信息")
@Slf4j
public class VEbrController extends BaseController {

    @Autowired
    private IVEbrService ebrService;

    @Autowired
    private IVEbrGisService ebrGisService;

    @GetMapping("/list")
    @ApiOperation(value = "查询系统资源信息视图列表", notes = "查询系统资源信息视图列表")
    public ApiListResponse<EbrInfo> list(@ModelAttribute EbrQueryRequest request){
       if(log.isDebugEnabled()){
           log.debug("get EbrInfo view by condition . request=[{}]  ",request);
       }
       if(EntityUtil.isReallyEmpty(request)){
           CriterionRequest criterionRequest = new CriterionRequest();
           criterionRequest.setLimit(Constants.MAX_SQL_ROWS);
           startPage(criterionRequest);
       }
        List<EbrInfo> ebrInfoList = ebrService.list(request);
        return ApiListResponse.ok(ebrInfoList);
    }

    @GetMapping("/page")
    @ApiOperation(value = "根据EbrId查询平台信息", notes = "根据EbrId查询平台信息")
    public ApiPageResponse<EbrInfo> getByPage(@ModelAttribute EbrPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find ebrInfos by page . request = [{}]",JSON.toJSONString(request));
        }
        startPage(request);
        EbrQueryRequest queryRequest = new EbrQueryRequest();
        BeanUtils.copyProperties(request,queryRequest);
        List<EbrInfo> list = ebrService.list(queryRequest);
        return ApiPageResponse.ok(list,page);
    }
    @GetMapping("/infoSrc/page")
    @ApiOperation(value = "根据EbrId查询平台信息", notes = "根据EbrId查询平台信息")
    public ApiPageResponse<EbrInfo> getInfoSrcByPage(@ModelAttribute EbrPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~]find infoSrc list by page . request = [{}]",JSON.toJSONString(request));
        }
        startPage(request);
        EbrQueryRequest queryRequest = new EbrQueryRequest();
        BeanUtils.copyProperties(request,queryRequest);
        List<EbrInfo> list = ebrService.listForInfoSrc(queryRequest);
        return ApiPageResponse.ok(list,page);
    }

    @GetMapping("/gis/page")
    @ApiOperation(value = "gis根据条件查询", notes = "gis根据条件查询")
    public ApiPageResponse<EbrGisInfo> getEbrGisByPage(@ModelAttribute EbrGisPageRequest request){
        startPage(request);
        List<EbrGisInfo> list = ebrGisService.getEbrGisByPage(request);
        return ApiPageResponse.ok(list,page);
    }

    @GetMapping("/gis/getEbrGisStatusCount")
    @ApiOperation(value = "gis展示状态统计", notes = "gis展示状态统计")
    public ApiEntityResponse<List<EbrstatusCount>> getEbrGisStatusCount(@ModelAttribute EbrGisPageRequest request){
        List<EbrstatusCount> list = ebrGisService.getEbrGisStatusCount(request);
        return ApiEntityResponse.ok(list);
    }



}
