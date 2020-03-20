package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmPlanRefDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmPlanRef;
import cn.comtom.core.main.ebm.service.IEbmPlanRefService;
import cn.comtom.domain.core.ebm.info.EbmPlanRefInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EbmPlanRefServiceImpl extends BaseServiceImpl<EbmPlanRef,String> implements IEbmPlanRefService {

    @Autowired
    private EbmPlanRefDao dao;

    @Override
    public BaseDao<EbmPlanRef, String> getDao() {
        return dao;
    }

    @Override
    public EbmPlanRefInfo getOneByEbmId(String ebmId) {
        EbmPlanRef ebmPlanRef = dao.getOneByEbmId(ebmId);
        return Optional.ofNullable(ebmPlanRef).map(ebmPlanRef1 ->{
            EbmPlanRefInfo ebmPlanRefInfo = new EbmPlanRefInfo();
            BeanUtils.copyProperties(ebmPlanRef1,ebmPlanRefInfo);
            return ebmPlanRefInfo;
        }).orElseGet(null);
    }
}
