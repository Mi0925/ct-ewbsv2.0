package cn.comtom.system.main.region.service;

import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.region.entity.dbo.Region;

import java.util.List;

public interface IRegionService extends BaseService<Region,String> {

    List<Region> getNextAreaCodeByParentCode(String parentAreaCode);

    void sureInitArea(String areaCode);
}
