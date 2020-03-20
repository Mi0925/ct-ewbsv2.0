package cn.comtom.core.main.statistics.service;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.EmergencyBrdCount;
import cn.comtom.core.main.statistics.domain.response.ResourceDistribution;
import cn.comtom.core.main.statistics.domain.response.EmergencyResOnlineCount;

import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/6
 * @desc
 */
public interface IEmergencyResourceService {

    List<EmergencyBrdCount> emergencyBrdCount(TimeFrameReq req);

    List<EmergencyResOnlineCount> emergencyResOnlineCount(TimeFrameReq req);

    List<ResourceDistribution> emergencyResDistribution();

}
