package cn.comtom.core.main.statistics.service;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.BroadcastDuration;
import cn.comtom.core.main.statistics.domain.response.BroadcastFrequency;
import cn.comtom.core.main.statistics.domain.response.GlobalBroadcast;

/**
 * @author wangbingyan
 * @date 2020/1/8
 * @desc 广播统计相关接口
 */
public interface IBroadcastStatisticsService {

    GlobalBroadcast embsStatistics(TimeFrameReq req);

    BroadcastFrequency broadcastFrequency(TimeFrameReq req);

    BroadcastDuration broadcastDuration(TimeFrameReq req);
}
