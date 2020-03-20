package cn.comtom.system.main.region.service.impl;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.region.dao.RegionAreaDao;
import cn.comtom.system.main.region.dao.RegionDao;
import cn.comtom.system.main.region.entity.dbo.Region;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.system.main.region.service.IRegionAreaService;
import cn.comtom.system.main.region.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region,String> implements IRegionService {

    @Autowired
    private RegionDao dao;

    @Override
    public BaseDao<Region, String> getDao() {
        return dao;
    }

    @Override
    public List<Region> getNextAreaCodeByParentCode(String parentAreaCode) {
        return dao.getNextAreaCodeByParentCode(parentAreaCode);
    }

    @Override
    public void sureInitArea(String areaCode) {
         dao.sureInitArea(areaCode);
    }
}
