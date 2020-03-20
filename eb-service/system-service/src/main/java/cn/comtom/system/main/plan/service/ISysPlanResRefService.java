package cn.comtom.system.main.plan.service;

import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;

import java.util.List;

public interface ISysPlanResRefService extends BaseService<SysPlanResoRef,String> {

    List<SysPlanResoRefInfo> getByPlanId(String planId);

    public int deleteByPlanId(SysPlanResoRef ref);
}
