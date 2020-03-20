package cn.comtom.core.main.ebm.service.impl;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.BaseServiceImpl;
import cn.comtom.core.main.ebm.dao.EbmBrdItemUnitDao;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdItemUnit;
import cn.comtom.core.main.ebm.service.IEbmBrdItemUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbmBrdItemUnitServiceImpl extends BaseServiceImpl<EbmBrdItemUnit,String> implements IEbmBrdItemUnitService {

    @Autowired
    private EbmBrdItemUnitDao dao;

    @Override
    public BaseDao<EbmBrdItemUnit, String> getDao() {
        return dao;
    }

}
