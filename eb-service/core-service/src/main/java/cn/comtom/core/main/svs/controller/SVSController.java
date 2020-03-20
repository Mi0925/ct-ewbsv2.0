package cn.comtom.core.main.svs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.svs.service.SVSService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/core/svs")
@Api(tags = "大屏展示webserver通知")
@Slf4j
public class SVSController extends BaseController {

    @Autowired
    private SVSService svsService;


    @GetMapping("/ebmNotice")
    @ApiOperation(value = "ebm接入通知", notes = "ebm接入通知")
    public ApiResponse callEbmNotice() {
        if(log.isDebugEnabled()){
            log.debug("svs.callEbmNotice");
        }
        svsService.callEbmNotice();
        return ApiEntityResponse.ok();
    }

    @GetMapping("/ebrNotice")
    @ApiOperation(value = "ebr状态更新通知", notes = "ebr状态更新通知")
    public ApiResponse callEbrState() {
        if(log.isDebugEnabled()){
            log.debug("svs.callEbrState");
        }
        svsService.callEbrState();
        return ApiEntityResponse.ok();
    }
    
    @GetMapping("/DVBNotice")
    @ApiOperation(value = "DVB返回流通知", notes = "DVB返回流通知")
    public ApiResponse callDVBNotice() {
        if(log.isDebugEnabled()){
            log.debug("svs.callDVBNotice");
        }
        svsService.callDVBNotice();
        return ApiEntityResponse.ok();
    }
    
}
