package cn.comtom.reso.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;

import java.util.List;
import java.util.Map;

public interface IEbrPlatformService extends BaseService<EbrPlatform,String> {
    List<EbrPlatformInfo> findPlatformListByWhere(PlatformWhereRequest request);

    EbrInfo getEbrInfoById(String ebrId);

    List<EbrPlatformInfo> list(EbrPlatformPageRequest request);
    
   	public int save(EbrPlatform t,String channel);
       
   	public int update(EbrPlatform t,String channel);
   	
   	public Double countFailureRate(String resType);
    
}
