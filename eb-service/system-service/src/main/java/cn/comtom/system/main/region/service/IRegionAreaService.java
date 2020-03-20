package cn.comtom.system.main.region.service;

import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.region.entity.dbo.RegionArea;

import java.util.List;

public interface IRegionAreaService extends BaseService<RegionArea,String> {
    List<RegionArea> getNextAreaCodeByParentCode(String parentAreaCode);

    Integer getAreaTotal();

    RegionAreaInfo getRootArea();

    RegionArea selectByAreaCode(String areaCode);

	List<RegionArea> selectByAreaCodes(List<String> areaCodes);

}
