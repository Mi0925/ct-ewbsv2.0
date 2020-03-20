package cn.comtom.core.main.ebd.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebd.entity.dbo.Ebd;
import cn.comtom.core.main.ebd.service.IEbdService;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdAddRequest;
import cn.comtom.domain.core.ebd.request.EbdPageRequest;
import cn.comtom.domain.core.ebd.request.EbdUpdateRequest;
import cn.comtom.tools.response.*;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/core/ebd")
@Api(tags = "业务数据包信息")
@Slf4j
public class EbdController extends BaseController {

    @Autowired
    private IEbdService ebdService;


    @PostMapping("/save")
    @ApiOperation(value = "保存业务数据包信息", notes = "保存业务数据包信息")
    public ApiEntityResponse<EbdInfo> save(@RequestBody @Valid EbdAddRequest request, BindingResult bindResult) {
        Ebd ebd = new Ebd();
        BeanUtils.copyProperties(request,ebd);
        ebdService.save(ebd);
        EbdInfo info = new EbdInfo();
        BeanUtils.copyProperties(ebd,info);
        return ApiEntityResponse.ok(info);
    }


    @PutMapping("/update")
    @ApiOperation(value = "更新业务数据包信息", notes = "更新业务数据包信息")
    public ApiResponse update(@RequestBody @Valid EbdUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("update ebd info . request=[{}]",JSON.toJSONString(request));
        }
        Ebd ebd = new Ebd();
        BeanUtils.copyProperties(request,ebd);
        ebdService.update(ebd);
        return ApiResponse.ok();
    }

    @GetMapping("/{ebdId}")
    @ApiOperation(value = "根据ID获取ebd信息", notes = "根据ID获取ebd信息")
    public ApiEntityResponse<EbdInfo> getByEbdId(@PathVariable(name = "ebdId") String ebdId){
        if(log.isDebugEnabled()){
            log.debug("get ebd by ebdId. ebdId=[{}]",JSON.toJSONString(ebdId));
        }
        if(StringUtils.isBlank(ebdId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        Ebd ebd  = ebdService.selectById(ebdId);
        if(ebd == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        EbdInfo ebdInfo = new EbdInfo();
        BeanUtils.copyProperties(ebd,ebdInfo);
        return ApiEntityResponse.ok(ebdInfo);

    }

    @GetMapping("/getByEbmId")
    @ApiOperation(value = "根据ID获取ebd信息", notes = "根据ID获取ebd信息")
    public ApiListResponse<EbdInfo> getByEbmId(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("get ebd by ebmId. ebmId=[{}]",JSON.toJSONString(ebmId));
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildListError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        List<EbdInfo> ebdInfoList = ebdService.getByEbmId(ebmId);
        return ApiListResponse.ok(ebdInfoList);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询Ebd数据包", notes = "分页查询Ebd数据包")
    public ApiPageResponse<EbdInfo> page(@Valid @ModelAttribute EbdPageRequest req){
        startPage(req,Ebd.class);
        List<EbdInfo> infoList = ebdService.list(req);
        return ApiPageResponse.ok(infoList);
    }



}
