package cn.comtom.linkage.main.dispatch.controller;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.fw.BaseController;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.ConnectionCheck;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.access.service.impl.EBMStateRequestService;
import cn.comtom.linkage.main.access.untils.HttpRequestUtil;
import cn.comtom.linkage.main.dispatch.service.IEbmDispatchService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.TarFileUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.enums.TypeDictEnum;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import cn.comtom.tools.utils.DateUtil;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 *   信息接入入口
 */
@RestController
@RequestMapping(value = "linkage")
@Slf4j
@Api(tags = "消息分发管理", description = "消息分发管理接口")
public class EbmDispatchController extends BaseController {

    @Autowired
    private IEbmDispatchService ebmDispatchService;

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private MQMessageProducer mqMessageProducer;
    
    @RequestMapping("/dispatch")
    @ApiOperation(value = "消息分发", notes = "消息分发")
    public void dispatch() {
        //查询待分发和分发失败的消息
        List<String> stateList = Arrays.asList(StateDictEnum.DISPATCH_STATE_READY.getKey(),StateDictEnum.DISPATCH_STATE_FAILED.getKey());
        //待分发消息
        List<EBD> ebdPackageList = ebmDispatchService.getEBDPackageList(stateList);
        //分发字幕插播消息
        ebmDispatchService.dispatchTextInsertionEBM(stateList);
        Optional.ofNullable(ebdPackageList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .forEach(ebd -> {
                   // ebmDispatchService.beforeDispatch(ebd);
                    //产生一条MQ消息，异步处理分发逻辑
                    mqMessageProducer.sendData(MqQueueConstant.EBM_DISPATCH_INFO_CREATE_QUEUE,JSON.toJSONString(ebd));
                });


    }
    
    @GetMapping("/ebmStateRequest/{ebmId}")
    @ApiOperation(value = "播发状态上报", notes = "播发状态上报")
    public void ebmStateRequest(@PathVariable(name = "ebmId") String ebmId) {
    	EbmStateResponseMessage message=new EbmStateResponseMessage();
		message.setEbdId(null);
		message.setEbmId(ebmId);
		mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue,JSON.toJSONString(message));

    }


}
