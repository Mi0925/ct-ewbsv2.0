package cn.comtom.linkage.main.monitor.service.impl;

import cn.comtom.domain.reso.ebr.request.EbrStateUpdateRequest;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.main.monitor.service.IMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public Integer updateResourceState(List<EbrStateUpdateRequest> ebrStateUpdateRequestList) {
        Integer count  = resoFeginService.updateEbrPlatformState(ebrStateUpdateRequestList);
        count  += resoFeginService.updateEbrBroadcastState(ebrStateUpdateRequestList);
        count  += resoFeginService.updateEbrAdapterState(ebrStateUpdateRequestList);
        return count;
    }

}
