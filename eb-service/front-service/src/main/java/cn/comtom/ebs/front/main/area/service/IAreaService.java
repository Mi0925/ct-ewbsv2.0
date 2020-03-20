package cn.comtom.ebs.front.main.area.service;

import cn.comtom.domain.system.region.info.RegionAreaInfo;

import java.util.List;

public interface IAreaService {

    RegionAreaInfo getByAreaCode(String areaCode);

    List<RegionAreaInfo> getNextAreaCodeByParentCode(String curAreaCode);

    List<RegionAreaInfo> getByParentAreaCode(String parentAreaCode);

    Integer getAreaTotal();

    List<RegionAreaInfo> getNextReginCodeByParentCode(String parentAreaCode);

    boolean sureInitArea(String areaCode);

    RegionAreaInfo getRootArea();

	boolean updateOrSaveArea(RegionAreaInfo regionArea);

	boolean deleteAreaById(String id);

}
