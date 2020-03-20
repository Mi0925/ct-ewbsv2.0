package cn.comtom.ebs.front.main.eventtype.controller;


import cn.comtom.domain.system.eventtype.info.EventtypeInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.fw.BaseController;
import cn.comtom.ebs.front.main.eventtype.service.IEventTypeService;
import cn.comtom.tools.response.ApiEntityResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/safeRest/event/type")
@Api(tags = "事件类型")
@Slf4j
@AuthRest
public class EventTypeController extends AuthController {

    @Autowired
    private IEventTypeService eventTypeService;


    @GetMapping("/getNextByParentCode")
    @ApiOperation(value = "根据父级eventCode查询下级事件类型", notes = "根据父级eventCode查询下级事件类型")
    public ApiEntityResponse<List<EventtypeInfo>> getNextByParentCode(@RequestParam(name = "parentCode") String parentCode){
        List<EventtypeInfo> eventTypeList = eventTypeService.getNextByParentCode(parentCode);
        return ApiEntityResponse.ok(eventTypeList);
    }


}
