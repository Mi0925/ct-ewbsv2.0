package cn.comtom.system.main.plan.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PlanResRefMapper extends SystemMapper<SysPlanResoRef,String> {
}
