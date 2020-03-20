package cn.comtom.reso.main.ebr.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.service.IEbrChannelService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/res/ebr/channel")
@Api(tags = "资源发送消息通道")
@Slf4j
public class EbrChannelController extends BaseController {

    @Autowired
    private IEbrChannelService ebrChannelService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据EbrId查询channel", notes = "根据EbrId查询channel")
    public ApiEntityResponse<String> getChannelByEbrId(@PathVariable(name = "ebrId") String ebrId){
    	String channelId = ebrChannelService.getChannelByEbrId(ebrId);
    	if(StringUtils.isBlank(channelId)) {
    		return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
    	}else {
    		ApiEntityResponse ok = ApiEntityResponse.ok(channelId);
    		return ok.setData(channelId);
    	}
    }


}
