package cn.comtom.system.main.region.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.region.entity.dbo.RegionArea;
import cn.comtom.system.main.region.mapper.RegionAreaMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class RegionAreaDao extends BaseDao<RegionArea,String> {

    @Autowired
    private RegionAreaMapper mapper;


    @Override
    public SystemMapper<RegionArea, String> getMapper() {
        return mapper;
    }

    public List<RegionArea> getNextAreaCodeByParentCode(String parentAreaCode) {
        Weekend<RegionArea> weekend = Weekend.of(RegionArea.class);
        WeekendCriteria<RegionArea, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotEmpty(parentAreaCode)){
            weekendCriteria.andEqualTo(RegionArea::getParentAreaCode,parentAreaCode);
        }
        return mapper.selectByExample(weekend);
    }

    public Integer getAreaTotal() {
        RegionArea regionArea=new RegionArea();
        return mapper.selectCount(regionArea);
    }

    public List<RegionArea> getRootArea() {
        Weekend<RegionArea> weekend = Weekend.of(RegionArea.class);
        return mapper.selectByExample(weekend);
    }

	public RegionArea selectByAreaCode(String areaCode) {
		RegionArea record = new RegionArea();
		record.setAreaCode(areaCode);
		return mapper.selectOne(record);
	}

	public List<RegionArea> selectByAreaCodes(List<String> areaCodes) {
		if(CollectionUtils.isEmpty(areaCodes)){
            return Lists.newArrayList();
        }
		Weekend<RegionArea> weekend = Weekend.of(RegionArea.class);
        WeekendCriteria<RegionArea, Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andIn(RegionArea::getAreaCode, areaCodes);
        return mapper.selectByExample(weekend);
	}
}
