package cn.comtom.ebs.front.main.plan.service.impl;

import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.request.SysPlanDelRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.plan.service.ISysPlanMathService;
import cn.comtom.tools.response.ApiPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysPlanMathServiceImpl implements ISysPlanMathService {

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public ApiPageResponse<SysPlanMatchInfo> page(SysPlanPageRequest pageRequest) {
        return systemFeginService.findSysPlanMathPage(pageRequest);
    }

    @Override
    public void delete(SysPlanDelRequest request) {
        systemFeginService.deleteSysPlanMathById(request);
    }

    @Override
    public SysPlanMatchInfo save(SysPlanMatchAddRequest request) {
        return systemFeginService.saveSysPlanMath(request);
    }

    @Override
    public SysPlanMatchInfo update(SysPlanMatchUpdRequest request) {
        return systemFeginService.updateSysPlanMath(request);
    }

    @Override
    public SysPlanMatchInfo getByPlanId(String planId) {
        return systemFeginService.getByPlanId(planId);
    }
}
