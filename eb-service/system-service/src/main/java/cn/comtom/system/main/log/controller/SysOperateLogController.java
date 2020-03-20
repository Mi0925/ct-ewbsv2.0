package cn.comtom.system.main.log.controller;


import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.log.entity.dbo.SysOperateLog;
import cn.comtom.system.main.log.service.ISysOperateLogService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/system/log/operate")
@Api(tags = "系统操作日志")
@Slf4j
public class SysOperateLogController extends BaseController {

    @Autowired
    private ISysOperateLogService sysOperateLogService;



    @PostMapping("/save")
    @ApiOperation(value = "保存系统操作日志", notes = "保存系统操作日志")
    public ApiEntityResponse<SysOperateLogInfo> save(@RequestBody @Valid SysOperateLogAddRequest request, BindingResult result) {
        SysOperateLog sysOperateLog = new SysOperateLog();
        sysOperateLog.setLogId(UUIDGenerator.getUUID());
        if(request != null){
            BeanUtils.copyProperties(request,sysOperateLog);
        }
        sysOperateLog.setLogTime(new Date());
        sysOperateLogService.save(sysOperateLog);
        SysOperateLogInfo info = new SysOperateLogInfo();
        BeanUtils.copyProperties(sysOperateLog,info);
        return ApiEntityResponse.ok(info);
    }





}
