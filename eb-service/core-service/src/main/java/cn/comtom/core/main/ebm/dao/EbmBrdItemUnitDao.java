package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdItemUnit;
import cn.comtom.core.main.ebm.mapper.EbmBrdItemUnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EbmBrdItemUnitDao extends BaseDao<EbmBrdItemUnit,String> {

    @Autowired
    private EbmBrdItemUnitMapper mapper;


    @Override
    public CoreMapper<EbmBrdItemUnit, String> getMapper() {
        return mapper;
    }



}
