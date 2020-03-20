package cn.comtom.reso.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.BroadcastWhereRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;

import java.util.List;

public interface IEbrBroadcastService extends BaseService<EbrBroadcast,String> {

    List<EbrBroadcastInfo> getMatchBroadcast(String areaCode, String relatedPsEbrId);

    List<EbrBroadcastInfo> getList(BroadcastPageRequest request);

    List<EbrBroadcastInfo> findBroadcastListByWhere(BroadcastWhereRequest request);
    
	public int save(EbrBroadcast t,String channel);
	
	public int update(EbrBroadcast t,String channel);
}
