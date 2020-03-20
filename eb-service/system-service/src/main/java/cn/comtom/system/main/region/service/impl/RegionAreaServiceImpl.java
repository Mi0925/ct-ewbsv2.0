package cn.comtom.system.main.region.service.impl;

import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.region.dao.RegionAreaDao;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.system.main.region.service.IRegionAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionAreaServiceImpl extends BaseServiceImpl<RegionArea,String> implements IRegionAreaService {

    @Autowired
    private RegionAreaDao dao;

    @Override
    public BaseDao<RegionArea, String> getDao() {
        return dao;
    }

    @Override
    public List<RegionArea> getNextAreaCodeByParentCode(String parentAreaCode) {
        return dao.getNextAreaCodeByParentCode(parentAreaCode);
    }

    @Override
    public Integer getAreaTotal() {
        return dao.getAreaTotal();
    }

    @Override
    public RegionAreaInfo getRootArea() {
        List<RegionArea> regionAreaList = dao.getRootArea();
        RegionAreaInfo regionAreaInfo = new RegionAreaInfo();
        if(regionAreaList != null&&regionAreaList.size()!=0){
            RegionArea regionArea = regionAreaList.get(0);
            BeanUtils.copyProperties(regionArea,regionAreaInfo);
        }else{
            return null;
        }
        return regionAreaInfo;
    }

	@Override
	public RegionArea selectByAreaCode(String areaCode) {
		return dao.selectByAreaCode(areaCode);
	}

	@Override
	public List<RegionArea> selectByAreaCodes(List<String> areaCodes) {
		return dao.selectByAreaCodes(areaCodes);
	}
}
