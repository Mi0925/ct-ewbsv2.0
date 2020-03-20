package cn.comtom.reso.main.ebr.service;

import java.util.List;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;

public interface IEbrStationService extends BaseService<EbrStation,String> {

    List<EbrStationInfo> findStationListByWhere(StationWhereRequest request);

	List<EbrStationInfo> findListByStationIds(List<String> stationIds);

	List<EbrStationInfo> getList(EbrStationPageRequest request);
	
	public int save(EbrStation t,String channel);
	
	public int update(EbrStation t,String channel);
}
