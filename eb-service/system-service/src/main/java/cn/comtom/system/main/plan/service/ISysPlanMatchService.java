package cn.comtom.system.main.plan.service;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.plan.entity.dbo.SysPlanMatch;

import java.util.List;

public interface ISysPlanMatchService extends BaseService<SysPlanMatch,String> {

    List<SysPlanMatchInfo> getMatchPlan(String severity,String eventType, String srcEbrId, String areaCodes);

    List<SysPlanMatchInfo> page(SysPlanPageRequest pageRequest);

    void delete(String planId);

    SysPlanMatch saveSysPlanMath(SysPlanMatchAddRequest request);

    SysPlanMatch updateSysPlanMath(SysPlanMatchUpdRequest request);

    SysPlanMatchInfo getByPlanId(String planId);
}
