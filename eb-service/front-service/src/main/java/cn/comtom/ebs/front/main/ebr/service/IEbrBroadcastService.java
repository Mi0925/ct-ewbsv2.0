package cn.comtom.ebs.front.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.tools.response.ApiPageResponse;

public interface IEbrBroadcastService {
    EbrBroadcastInfo getById(String ebrId);

    ApiPageResponse<EbrBroadcastInfo> getEbrBroadcastInfoByPage(BroadcastPageRequest request);

    EbrBroadcastInfo save(EbrBroadcastAddRequest request);

    Boolean update(EbrBroadcastUpdateRequest request);

    Boolean delete(String ebrId);
}
