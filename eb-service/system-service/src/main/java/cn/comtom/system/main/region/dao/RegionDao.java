package cn.comtom.system.main.region.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.region.entity.dbo.Region;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.system.main.region.mapper.RegionAreaMapper;
import cn.comtom.system.main.region.mapper.RegionMapper;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class RegionDao extends BaseDao<Region,String> {

    @Autowired
    private RegionMapper mapper;


    @Override
    public SystemMapper<Region, String> getMapper() {
        return mapper;
    }

    public List<Region> getNextAreaCodeByParentCode(String parentAreaCode) {
        Weekend<Region> weekend = Weekend.of(Region.class);
        WeekendCriteria<Region, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotEmpty(parentAreaCode)){
            weekendCriteria.andEqualTo(Region::getParentAreaCode,parentAreaCode);
        }
        return mapper.selectByExample(weekend);
    }

    public void sureInitArea(String areaCode) {
        mapper.sureInitArea(areaCode);
    }
}
