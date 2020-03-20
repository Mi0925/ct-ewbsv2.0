package cn.comtom.ebs.front.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.tools.response.ApiPageResponse;

public interface IEbrAdapterService {
	
	EbrAdapterInfo getById(String ebrId);

    ApiPageResponse<EbrAdapterInfo> getEbrAdapterInfoByPage(EbrAdapterPageRequest request);

    EbrAdapterInfo save(AdapterAddRequest request);

    Boolean update(AdapterUpdateRequest request);

    Boolean delete(String ebrId);
}
