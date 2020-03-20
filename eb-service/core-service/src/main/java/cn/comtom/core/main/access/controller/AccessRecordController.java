package cn.comtom.core.main.access.controller;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.access.entity.dbo.AccessRecord;
import cn.comtom.core.main.access.service.IAccessRecordService;
import cn.comtom.core.main.constants.CoreErrorEnum;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordAddRequest;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.domain.core.access.request.AccessRecordUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core/access/record")
@Api(tags = "信息接入记录")
public class AccessRecordController extends BaseController {

    @Autowired
    private IAccessRecordService accessRecordService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询接入记录", notes = "分院查询接入记录")
    public ApiPageResponse<AccessRecordInfo> page(@Valid @ModelAttribute AccessRecordPageRequest req){
        startPage(req,AccessRecord.class);
        if(log.isDebugEnabled()){
            log.debug("enter accessArea query page req=[{}]",req);
        }
        List<AccessRecordInfo> infoList = accessRecordService.list(req);

        return ApiPageResponse.ok(infoList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询接入记录详情", notes = "根据ID查询接入记录详情")
    public ApiEntityResponse<AccessRecordInfo> getById(@PathVariable(name = "id") String id){
        if(log.isDebugEnabled()){
            log.debug("enter accessRecord query by  id=[{}]",id);
        }
        AccessRecord accessRecord = accessRecordService.selectById(id);
        if(accessRecord == null){
            return ApiResponseBuilder.buildEntityError(CoreErrorEnum.QUERY_NO_DATA);
        }
        AccessRecordInfo info = new AccessRecordInfo();
        BeanUtils.copyProperties(accessRecord,info);
        return ApiEntityResponse.ok(info);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存接入记录", notes = "保存接入记录")
    public ApiEntityResponse<AccessRecordInfo> save(@RequestBody @Valid AccessRecordAddRequest request,BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter accessRecord save request=[{}]",request);
        }
        AccessRecord accessRecord = new AccessRecord();
        String id = UUIDGenerator.getUUID();
        BeanUtils.copyProperties(request,accessRecord);
        accessRecord.setId(id);
        accessRecord.setReceiveTime(new Date());
        accessRecordService.save(accessRecord);
        AccessRecordInfo info = new AccessRecordInfo();
        BeanUtils.copyProperties(accessRecord,info);
        return ApiEntityResponse.ok(info);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新接入记录", notes = "更新接入记录")
    public ApiResponse update(@RequestBody @Valid AccessRecordUpdateRequest request, BindingResult result){
        if(log.isDebugEnabled()){
            log.debug("enter accessRecord update request=[{}]",request);
        }
        AccessRecord accessRecord = new AccessRecord();
        BeanUtils.copyProperties(request,accessRecord);
        accessRecordService.update(accessRecord);
        return ApiResponse.ok();
    }

}
