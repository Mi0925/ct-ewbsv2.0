package cn.comtom.system.main.region.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.region.entity.dbo.Region;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RegionMapper extends SystemMapper<Region,String> {

    void sureInitArea(String areaCode);

}
