package cn.comtom.core.main.ebm.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebm.entity.dbo.EbmPlanRef;
import cn.comtom.core.main.ebm.service.IEbmPlanRefService;
import cn.comtom.domain.core.ebm.info.EbmPlanRefInfo;
import cn.comtom.domain.core.ebm.request.EbmPlanRefAddRequest;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
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

@RestController
@RequestMapping("/core/ebm/plan")
@Api(tags = "消息关联预案信息管理")
@Slf4j
public class EbmPlanRefController extends BaseController {

    @Autowired
    private IEbmPlanRefService ebmPlanRefService;


    @PostMapping("/save")
    @ApiOperation(value = "保存消息预案关联信息", notes = "保存消息预案关联信息")
    public ApiEntityResponse<EbmPlanRefInfo> save(@RequestBody @Valid EbmPlanRefAddRequest request, BindingResult bindResult) {
        if(log.isDebugEnabled()){
            log.debug("save EbmPlanRefInfo . request=[{}]",JSON.toJSONString(request));
        }
        EbmPlanRef ebmPlanRef = new EbmPlanRef();
        BeanUtils.copyProperties(request,ebmPlanRef);
        ebmPlanRef.setId(UUIDGenerator.getUUID());
        if(ebmPlanRef.getTargetAreas().lastIndexOf(SymbolConstants.GENERAL_SEPARATOR) != ebmPlanRef.getTargetAreas().length()-1){
            ebmPlanRef.setTargetAreas(ebmPlanRef.getTargetAreas()+SymbolConstants.GENERAL_SEPARATOR);
        }
        ebmPlanRefService.save(ebmPlanRef);
        EbmPlanRefInfo ebmPlanRefInfo = new EbmPlanRefInfo();
        BeanUtils.copyProperties(ebmPlanRef,ebmPlanRefInfo);
        return ApiEntityResponse.ok(ebmPlanRefInfo);
    }



    @GetMapping("/getByEbmId")
    @ApiOperation(value = "根据ebmId获取消息预案关联信息列表", notes = "根据ebmId获取消息预案关联信息列表")
    public ApiEntityResponse<EbmPlanRefInfo> getByEbmId(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("get EbmPlanRefInfo by ebmId. ebmId=[{}]",JSON.toJSONString(ebmId));
        }
        if(StringUtils.isBlank(ebmId)){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.REQUIRED_PARAMS_EMPTY);
        }
        EbmPlanRefInfo ebmPlanRefInfo = ebmPlanRefService.getOneByEbmId(ebmId);
        return ApiEntityResponse.ok(ebmPlanRefInfo);

    }

}
