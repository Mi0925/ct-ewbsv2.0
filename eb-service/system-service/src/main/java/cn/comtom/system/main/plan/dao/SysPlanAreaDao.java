package cn.comtom.system.main.plan.dao;

import cn.comtom.domain.system.plan.info.SysPlanAreaInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import cn.comtom.system.main.plan.mapper.PlanAreaMapper;
import cn.comtom.system.main.plan.mapper.PlanResRefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class SysPlanAreaDao extends BaseDao<SysPlanArea,String> {

    @Autowired
    private PlanAreaMapper mapper;


    @Override
    public SystemMapper<SysPlanArea, String> getMapper() {
        return mapper;
    }


    public List<SysPlanArea> getByPlanId(String planId) {
        Weekend<SysPlanArea> weekend = Weekend.of(SysPlanArea.class);
        WeekendCriteria<SysPlanArea,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(SysPlanArea::getPlanId,planId);
        return mapper.selectByExample(weekend);
    }
}
