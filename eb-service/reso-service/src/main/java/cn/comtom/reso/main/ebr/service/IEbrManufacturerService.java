package cn.comtom.reso.main.ebr.service;

import java.util.List;

import cn.comtom.domain.reso.ebr.request.EbrManufactPageRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrManufacturer;

public interface IEbrManufacturerService extends BaseService<EbrManufacturer,String>{

	List<EbrManufacturer> getList(EbrManufactPageRequest request);

}
