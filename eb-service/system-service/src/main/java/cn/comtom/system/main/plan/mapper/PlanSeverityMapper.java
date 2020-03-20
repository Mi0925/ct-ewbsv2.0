package cn.comtom.system.main.plan.mapper;

import cn.comtom.domain.system.plan.info.SysPlanSeverityInfo;
import cn.comtom.system.fw.BaseMapper;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanSeverity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PlanSeverityMapper extends SystemMapper<SysPlanSeverity,String>,BaseMapper<SysPlanSeverityInfo,String> {
}
