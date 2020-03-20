package cn.comtom.ebs.front.main.eventtype.service;


import cn.comtom.domain.system.eventtype.info.EventtypeInfo;

import java.util.List;

public interface IEventTypeService{

    List<EventtypeInfo> getNextByParentCode(String parentCode);
}
