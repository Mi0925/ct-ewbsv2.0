package cn.comtom.ebs.front.main.core.contoller;

import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;
import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.core.service.ISchemeService;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/safeRest/scheme")
@Api(tags = "方案管理", description = "方案管理")
public class SchemeController extends AuthController {

    @Autowired
    private ICoreFeginService coreFeginService;
    @Autowired
    private IResoFeginService resoFeginService;
    @Autowired
    private ISystemFeginService systemFeginService;
    @Autowired
    private ISchemeService schemeService;


    @GetMapping("/page")
    @ApiOperation(value = "分页查询方案信息列表", notes = "分页查询方案信息列表")
    public ApiPageResponse<SchemePageInfo> getSchemePageInfo(@ModelAttribute SchemeQueryRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] get scheme info by page. request=[{}]",request);
        }
        if(StringUtils.isBlank(request.getSidx())) {
        	request.setOrder("desc");
        	request.setSidx("startTime");
        }
       return coreFeginService.getSchemePageInfo(request);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新方案信息", notes = "更新方案信息")
    public ApiResponse updateSchemeInfo(@RequestBody @Valid SchemeUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update scheme info by schemeId. request=[{}]",request);
        }
        coreFeginService.updateSchemeInfo(request);
        return ApiResponse.ok();
    }

    @PutMapping("/audit")
    @ApiOperation(value = "方案审核", notes = "方案审核")
    @RequiresPermissions(Permissions.CORE_SCHEME_AUDIT)
    public ApiResponse auditSchemeInfo(@RequestBody @Valid SchemeUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update scheme info by schemeId. request=[{}]",request);
        }

       Boolean success = schemeService.auditSchemeInfo(request);
       if(!success){
          ApiResponseBuilder.buildError(FrontErrorEnum.UPDATE_SCHEME_INFO_FAILED);
       }
        return ApiResponse.ok();
    }


}
