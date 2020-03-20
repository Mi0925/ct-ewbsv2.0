package cn.comtom.system.main.eventtype.service;

import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.eventtype.entity.dbo.EventType;
import cn.comtom.system.main.region.entity.dbo.RegionArea;

import java.util.List;

public interface IEventTypeService extends BaseService<EventType,String> {

    List<EventType> getNextByParentCode(String parentCode);
}
