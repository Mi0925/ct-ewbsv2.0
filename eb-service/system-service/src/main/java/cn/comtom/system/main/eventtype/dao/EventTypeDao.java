package cn.comtom.system.main.eventtype.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.eventtype.entity.dbo.EventType;
import cn.comtom.system.main.eventtype.mapper.EventTypeMapper;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.system.main.region.mapper.RegionAreaMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class EventTypeDao extends BaseDao<EventType,String> {

    @Autowired
    private EventTypeMapper mapper;


    @Override
    public SystemMapper<EventType, String> getMapper() {
        return mapper;
    }

    public List<EventType> getNextByParentCode(String parentCode) {
        Weekend<EventType> weekend = Weekend.of(EventType.class);
        WeekendCriteria<EventType, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotEmpty(parentCode)){
            weekendCriteria.andEqualTo(EventType::getParentCode,parentCode);
        }
        return mapper.selectByExample(weekend);
    }
}
