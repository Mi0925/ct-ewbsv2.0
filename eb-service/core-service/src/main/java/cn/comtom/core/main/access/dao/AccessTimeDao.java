package cn.comtom.core.main.access.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessTime;
import cn.comtom.core.main.access.mapper.AccessTimeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class AccessTimeDao extends BaseDao<AccessTime,String> {

    @Autowired
    private AccessTimeMapper mapper;

    @Override
    public CoreMapper<AccessTime, String> getMapper() {
        return mapper;
    }

    public List<AccessTime> getByStrategyId(String strategyId) {
        Weekend<AccessTime> weekend = Weekend.of(AccessTime.class);
        WeekendCriteria<AccessTime,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(strategyId)){
            weekendCriteria.andEqualTo(AccessTime::getStrategyId,strategyId);
        }
        return mapper.selectByExample(weekend);
    }
}
