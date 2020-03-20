package cn.comtom.ebs.front.main.area.service.impl;

import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.area.service.IAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AreaServiceImpl implements IAreaService {

    @Autowired
    private ISystemFeginService systemFeginService;


    @Override
    public RegionAreaInfo getByAreaCode(String areaCode) {
        return systemFeginService.getRegionAreaByAreaCode(areaCode);
    }

    @Override
    public List<RegionAreaInfo> getNextAreaCodeByParentCode(String parentCode) {
        return systemFeginService.getNextAreaCodeByParentCode(parentCode);
    }

    @Override
    public List<RegionAreaInfo> getByParentAreaCode(String parentAreaCode) {
        return systemFeginService.getRegionAreaByParentAreaCode(parentAreaCode);
    }

    @Override
    public Integer getAreaTotal() {
        return systemFeginService.getAreaTotal();
    }

    @Override
    public List<RegionAreaInfo> getNextReginCodeByParentCode(String parentAreaCode) {
        return systemFeginService.getNextReginCodeByParentCode(parentAreaCode);
    }

    @Override
    public boolean sureInitArea(String areaCode) {
        return systemFeginService.sureInitArea(areaCode);
    }

    @Override
    public RegionAreaInfo getRootArea() {
        return systemFeginService.getRootArea();
    }

	@Override
	public boolean updateOrSaveArea(RegionAreaInfo regionArea) {
		return systemFeginService.updateOrSaveArea(regionArea);
	}

	@Override
	public boolean deleteAreaById(String id) {
		
		return systemFeginService.deleteAreaById(id);
	}
}
