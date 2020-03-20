package cn.comtom.core.main.ebd.controller;


import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.core.main.ebd.entity.dbo.EbdResponse;
import cn.comtom.core.main.ebd.service.IEbdResponseService;
import cn.comtom.domain.core.ebd.info.EbdResponseInfo;
import cn.comtom.domain.core.ebd.request.EbdResponseAddRequest;
import cn.comtom.domain.core.ebd.request.EbdResponsePageRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/core/ebd/response")
@Api(tags = "业务数据包通用反馈")
@Slf4j
public class EbdResponseController extends BaseController {

    @Autowired
    private IEbdResponseService ebdResponseService;

    @GetMapping("/{ebdId}")
    @ApiOperation(value = "根据ID业务数据包通用反馈详情", notes = "根据ID业务数据包通用反馈详情")
    public ApiEntityResponse<EbdResponseInfo> detail(@PathVariable(name = "ebdId") String ebdId) {
        EbdResponse ebdResponse = ebdResponseService.selectById(ebdId);
        if(ebdResponse == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        EbdResponseInfo info = new EbdResponseInfo();
        BeanUtils.copyProperties(ebdResponse,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存业务数据包通用反馈信息", notes = "保存业务数据包通用反馈信息")
    public ApiEntityResponse<EbdResponseInfo> save(@RequestBody @Valid EbdResponseAddRequest request, BindingResult bindResult) {
        EbdResponse ebdResponse = new EbdResponse();
        BeanUtils.copyProperties(request,ebdResponse);
        ebdResponseService.save(ebdResponse);
        EbdResponseInfo info = new EbdResponseInfo();
        BeanUtils.copyProperties(ebdResponse,info);
        return ApiEntityResponse.ok(info);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询业务数据包通用反馈信息列表", notes = "分页查询业务数据包通用反馈信息列表")
    public ApiPageResponse<EbdResponseInfo> page(@Valid EbdResponsePageRequest request, BindingResult bindingResult) {
        startPage(request,EbdResponse.class);
        List<EbdResponseInfo> ebdResponseInfoList = ebdResponseService.list(request);
        return ApiPageResponse.ok(ebdResponseInfoList,page);
    }



}
