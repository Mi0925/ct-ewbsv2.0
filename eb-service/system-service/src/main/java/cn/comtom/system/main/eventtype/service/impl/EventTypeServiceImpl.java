package cn.comtom.system.main.eventtype.service.impl;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.eventtype.dao.EventTypeDao;
import cn.comtom.system.main.eventtype.entity.dbo.EventType;
import cn.comtom.system.main.eventtype.service.IEventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeServiceImpl extends BaseServiceImpl<EventType,String> implements IEventTypeService {

    @Autowired
    private EventTypeDao dao;

    @Override
    public BaseDao<EventType, String> getDao() {
        return dao;
    }

    @Override
    public List<EventType> getNextByParentCode(String parentCode) {
        return dao.getNextByParentCode(parentCode);
    }
}
