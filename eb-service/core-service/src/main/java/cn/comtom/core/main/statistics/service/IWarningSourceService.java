package cn.comtom.core.main.statistics.service;

import cn.comtom.core.main.statistics.domain.request.TimeFrameReq;
import cn.comtom.core.main.statistics.domain.response.*;

import java.util.List;

/**
 * @author wangbingyan
 * @date 2020/1/3
 * @desc
 */
public interface IWarningSourceService {

    List<WarningAccess> warningAccessOrder(TimeFrameReq req);

    GlobalBroadcast warningStatistics(TimeFrameReq req);

    List<WarningSourceCount> warningSourceCount(TimeFrameReq req);

    List<TerminalDistribution> terminalDistribution(TimeFrameReq req);

    List<ResourceDistribution> terminalResources();
}
