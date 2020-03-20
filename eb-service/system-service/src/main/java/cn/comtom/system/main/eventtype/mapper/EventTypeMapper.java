package cn.comtom.system.main.eventtype.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.eventtype.entity.dbo.EventType;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EventTypeMapper extends SystemMapper<EventType,String> {
}
