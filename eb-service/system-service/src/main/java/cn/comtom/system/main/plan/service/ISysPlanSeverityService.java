package cn.comtom.system.main.plan.service;

import cn.comtom.domain.system.plan.info.SysPlanAreaInfo;
import cn.comtom.domain.system.plan.info.SysPlanSeverityInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanSeverity;

import java.util.List;

public interface ISysPlanSeverityService extends BaseService<SysPlanSeverity,String> {

    List<SysPlanSeverityInfo> getByPlanId(String planId);

    public int deleteByPlanId(SysPlanSeverity severity);
}
