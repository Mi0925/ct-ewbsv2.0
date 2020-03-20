package cn.comtom.ebs.front.main.plan.service;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.request.SysPlanDelRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.tools.response.ApiPageResponse;

public interface ISysPlanMathService {

    ApiPageResponse<SysPlanMatchInfo> page(SysPlanPageRequest pageRequest);

    void delete(SysPlanDelRequest request);

    SysPlanMatchInfo save(SysPlanMatchAddRequest request);

    SysPlanMatchInfo update(SysPlanMatchUpdRequest request);

    SysPlanMatchInfo getByPlanId(String planId);
}
