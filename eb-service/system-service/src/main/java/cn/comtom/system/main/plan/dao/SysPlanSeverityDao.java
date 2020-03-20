package cn.comtom.system.main.plan.dao;

import cn.comtom.domain.system.plan.info.SysPlanSeverityInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanSeverity;
import cn.comtom.system.main.plan.mapper.PlanAreaMapper;
import cn.comtom.system.main.plan.mapper.PlanSeverityMapper;
import cn.comtom.tools.enums.SysDictCodeEnum;
import cn.comtom.tools.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class SysPlanSeverityDao extends BaseDao<SysPlanSeverity,String> {

    @Autowired
    private PlanSeverityMapper mapper;


    @Override
    public SystemMapper<SysPlanSeverity, String> getMapper() {
        return mapper;
    }


    public List<SysPlanSeverityInfo> getByPlanId(String planId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("severityDictGroupCode",SysDictCodeEnum.SEVERITY_DICT.getCode()); //关联数据字典查询
        map.put("planId",planId);
        return mapper.queryList(map);
    }
}
