package cn.comtom.ebs.front.main.ebmDispatch.controller;

import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.ebmDispatch.service.IEbmDispatchService;
import cn.comtom.tools.response.ApiEntityResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/19 0019
 * @time: 上午 11:21
 */
@AuthRest
@RestController
@RequestMapping("/safeRest/ebm/dispatch")
@Api(tags = "应急消息资源调度信息管理")
public class EbmDispatchController extends AuthController {


    @Autowired
    private IEbmDispatchService ebmDispatchService;

    @GetMapping("/getEbmDispatchAndEbdFileByEbmId")
    @ApiOperation(value = "根据embId查询应急消息资源调度信息(包含数据包文件信息)", notes = "根据embId查询应急消息资源调度信息(包含数据包文件信息)")
    public ApiEntityResponse<List<EbmDispatchAndEbdFileInfo>> getEbmDispatchByEbmId(@RequestParam(name = "ebmId",required = true) String ebmId){
        List<EbmDispatchAndEbdFileInfo> ebmDispatchInfos = ebmDispatchService.getEbmDispatchAndEbdFileByEbmId(ebmId);
        return ApiEntityResponse.ok(ebmDispatchInfos);
    }


}
