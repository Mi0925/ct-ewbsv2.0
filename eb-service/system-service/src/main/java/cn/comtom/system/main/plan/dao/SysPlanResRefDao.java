package cn.comtom.system.main.plan.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import cn.comtom.system.main.plan.mapper.PlanResRefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class SysPlanResRefDao extends BaseDao<SysPlanResoRef,String> {

    @Autowired
    private PlanResRefMapper mapper;


    @Override
    public SystemMapper<SysPlanResoRef, String> getMapper() {
        return mapper;
    }


    public List<SysPlanResoRef> getByPlanId(String planId) {
        Weekend<SysPlanResoRef> weekend = Weekend.of(SysPlanResoRef.class);
        WeekendCriteria<SysPlanResoRef,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(SysPlanResoRef::getPlanId,planId);
        return mapper.selectByExample(weekend);
    }
}
