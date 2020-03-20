package cn.comtom.ebs.front.main.eventtype.service.impl;

import cn.comtom.domain.system.eventtype.info.EventtypeInfo;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.eventtype.service.IEventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class EventTypeServiceImpl  implements IEventTypeService {

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public List<EventtypeInfo> getNextByParentCode(String parentCode) {
        return systemFeginService.getNextByParentCode(parentCode);
    }
}
