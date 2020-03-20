package cn.comtom.system.main.plan.service.impl;

import cn.comtom.domain.system.plan.info.SysPlanAreaInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.plan.dao.SysPlanAreaDao;
import cn.comtom.system.main.plan.dao.SysPlanResRefDao;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import cn.comtom.system.main.plan.service.ISysPlanAreaService;
import cn.comtom.system.main.plan.service.ISysPlanResRefService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysPlanAreaServiceImpl extends BaseServiceImpl<SysPlanArea,String> implements ISysPlanAreaService {

    @Autowired
    private SysPlanAreaDao dao;

    @Override
    public BaseDao<SysPlanArea, String> getDao() {
        return dao;
    }

    @Override
    public List<SysPlanAreaInfo> getByPlanId(String planId) {
        List<SysPlanArea> areaList = dao.getByPlanId(planId);
        return areaList.stream().map(sysPlanArea -> {
            SysPlanAreaInfo sysPlanAreaInfo = new SysPlanAreaInfo();
            BeanUtils.copyProperties(sysPlanArea,sysPlanAreaInfo);
            return sysPlanAreaInfo;
        }).collect(Collectors.toList());
    }


    public int deleteByPlanId(SysPlanArea area){
          return  dao.delete(area);
    }

}
