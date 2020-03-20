package cn.comtom.ebs.front.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.tools.response.ApiPageResponse;

public interface IEbrStationService {
	
    EbrStationInfo getById(String ebrId);

    ApiPageResponse<EbrStationInfo> getEbrStationInfoByPage(EbrStationPageRequest request);

    EbrStationInfo save(StationAddRequest request);

    Boolean update(StationUpdateRequest request);

    Boolean delete(String ebrId);
}
