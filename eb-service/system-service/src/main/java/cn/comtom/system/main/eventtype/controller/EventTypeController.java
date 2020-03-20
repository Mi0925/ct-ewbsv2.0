package cn.comtom.system.main.eventtype.controller;


import cn.comtom.domain.system.eventtype.info.EventtypeInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.eventtype.entity.dbo.EventType;
import cn.comtom.system.main.eventtype.service.IEventTypeService;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.tools.response.ApiEntityResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/system/event/type")
@Api(tags = "事件类型")
@Slf4j
public class EventTypeController extends BaseController {

    @Autowired
    private IEventTypeService eventTypeService;


    @GetMapping("/getNextByParentCode")
    @ApiOperation(value = "根据父级eventCode查询下级事件类型", notes = "根据父级eventCode查询下级事件类型")
    public ApiEntityResponse<List<EventtypeInfo>> getNextByParentCode(@RequestParam(name = "parentCode") String parentCode){
        List<EventType> eventTypeList = eventTypeService.getNextByParentCode(parentCode);
        if(eventTypeList == null){
            eventTypeList=new ArrayList<EventType>();
        }
        List<EventtypeInfo> eventTypeInfoList = new ArrayList<EventtypeInfo>();
        eventTypeList.forEach(eventType -> {
            EventtypeInfo info = new EventtypeInfo();
            BeanUtils.copyProperties(eventType,info);
            eventTypeInfoList.add(info);
        });
        return ApiEntityResponse.ok(eventTypeInfoList);
    }


}
