package cn.comtom.reso.main.ebr.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.reso.ebr.request.EbrManufactPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrManufacturer;
import cn.comtom.reso.main.ebr.mapper.EbrManufacturerMapper;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Repository
public class EbrManufacturerDao extends BaseDao<EbrManufacturer,String>{

	@Autowired
    private EbrManufacturerMapper mapper;
	
	@Override
	public ResoMapper<EbrManufacturer, String> getMapper() {
		
		return mapper;
	}

	public List<EbrManufacturer> getList(EbrManufactPageRequest request) {
		 Weekend<EbrManufacturer> weekend = Weekend.of(EbrManufacturer.class);
	     WeekendCriteria<EbrManufacturer,Object> weekendCriteria = weekend.weekendCriteria();
	     if(StringUtils.isNotBlank(request.getCompanyName())){
	         weekendCriteria.andLike(EbrManufacturer::getCompanyName,"%".concat(request.getCompanyName()).concat("%"));
	     }
		return mapper.selectByExample(weekend);
	}

}
