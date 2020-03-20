package cn.comtom.reso.main.ebr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.request.EbrManufactPageRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.BaseServiceImpl;
import cn.comtom.reso.main.ebr.dao.EbrManufacturerDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrManufacturer;
import cn.comtom.reso.main.ebr.service.IEbrManufacturerService;

@Service
public class EbrManufacturerServiceImpl  extends BaseServiceImpl<EbrManufacturer,String> implements IEbrManufacturerService{

	@Autowired
    private EbrManufacturerDao ebrManufacturerDao;
	
	@Override
	public BaseDao<EbrManufacturer, String> getDao() {
		
		return ebrManufacturerDao;
	}

	@Override
	public List<EbrManufacturer> getList(EbrManufactPageRequest request) {
		
		return ebrManufacturerDao.getList(request);
	}

}
