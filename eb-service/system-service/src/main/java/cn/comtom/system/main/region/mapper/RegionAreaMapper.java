package cn.comtom.system.main.region.mapper;

import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RegionAreaMapper extends SystemMapper<RegionArea,String> {
    RegionAreaInfo getRootArea();
}
