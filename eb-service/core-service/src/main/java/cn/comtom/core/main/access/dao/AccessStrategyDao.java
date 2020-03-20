package cn.comtom.core.main.access.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessStrategy;
import cn.comtom.core.main.access.mapper.AccessStrategyMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class AccessStrategyDao extends BaseDao<AccessStrategy,String> {

    @Autowired
    private AccessStrategyMapper mapper;

    @Override
    public CoreMapper<AccessStrategy, String> getMapper() {
        return mapper;
    }

    public List<AccessStrategy> getByInfoId(String infoId) {
        Weekend<AccessStrategy> weekend = Weekend.of(AccessStrategy.class);
        WeekendCriteria<AccessStrategy,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(infoId)){
            weekendCriteria.andEqualTo(AccessStrategy::getInfoId,infoId);
        }
        return mapper.selectByExample(weekend);
    }
}
