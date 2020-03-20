package cn.comtom.linkage.main.monitor.service;

import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;

import java.util.List;

public interface IMonitorService {
    Integer updateResourceState(List<EbrStateUpdateRequest> onLineList);

}
