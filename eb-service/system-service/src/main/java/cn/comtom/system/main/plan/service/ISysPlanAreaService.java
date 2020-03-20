package cn.comtom.system.main.plan.service;

import cn.comtom.domain.system.plan.info.SysPlanAreaInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;

import java.util.List;

public interface ISysPlanAreaService extends BaseService<SysPlanArea,String> {

    List<SysPlanAreaInfo> getByPlanId(String planId);

    public int deleteByPlanId(SysPlanArea area);
}
