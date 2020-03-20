package cn.comtom.system.main.plan.service.impl;

import cn.comtom.domain.system.plan.info.SysPlanAreaInfo;
import cn.comtom.domain.system.plan.info.SysPlanSeverityInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.plan.dao.SysPlanAreaDao;
import cn.comtom.system.main.plan.dao.SysPlanSeverityDao;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanSeverity;
import cn.comtom.system.main.plan.service.ISysPlanAreaService;
import cn.comtom.system.main.plan.service.ISysPlanSeverityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysPlanSeverityImpl extends BaseServiceImpl<SysPlanSeverity,String> implements ISysPlanSeverityService {

    @Autowired
    private SysPlanSeverityDao dao;

    @Override
    public BaseDao<SysPlanSeverity, String> getDao() {
        return dao;
    }

    @Override
    public List<SysPlanSeverityInfo> getByPlanId(String planId) {
        List<SysPlanSeverityInfo> severityList = dao.getByPlanId(planId);
        return severityList;
    }

    public int deleteByPlanId(SysPlanSeverity severity){
          return  dao.delete(severity);
    }

}
