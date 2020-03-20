package cn.comtom.core.main.access.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.access.entity.dbo.AccessTime;
import cn.comtom.domain.core.access.info.AccessTimeInfo;

import java.util.List;

public interface IAccessTimeService extends BaseService<AccessTime,String> {
    List<AccessTimeInfo> getByStrategyId(String strategyId);
}
