package cn.comtom.system.main.plan.service.impl;

import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.plan.dao.SysPlanResRefDao;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import cn.comtom.system.main.plan.service.ISysPlanResRefService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysPlanResRefServiceImpl extends BaseServiceImpl<SysPlanResoRef,String> implements ISysPlanResRefService {

    @Autowired
    private SysPlanResRefDao dao;

    @Override
    public BaseDao<SysPlanResoRef, String> getDao() {
        return dao;
    }

    @Override
    public List<SysPlanResoRefInfo> getByPlanId(String planId) {
        List<SysPlanResoRef> sysPlanResoRefList = dao.getByPlanId(planId);
        return sysPlanResoRefList.stream().map(sysPlanResoRef -> {
            SysPlanResoRefInfo sysPlanResoRefInfo = new SysPlanResoRefInfo();
            BeanUtils.copyProperties(sysPlanResoRef,sysPlanResoRefInfo);
            return sysPlanResoRefInfo;
        }).collect(Collectors.toList());
    }


    public int deleteByPlanId(SysPlanResoRef ref){
          return  dao.delete(ref);
    }

}
