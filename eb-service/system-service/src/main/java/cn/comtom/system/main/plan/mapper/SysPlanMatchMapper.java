package cn.comtom.system.main.plan.mapper;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.system.fw.BaseMapper;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanMatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SysPlanMatchMapper extends SystemMapper<SysPlanMatch,String> ,BaseMapper<SysPlanMatchInfo,String> {
    List<SysPlanMatchInfo> getMatchedPlan(Map<String, Object> map);
}
