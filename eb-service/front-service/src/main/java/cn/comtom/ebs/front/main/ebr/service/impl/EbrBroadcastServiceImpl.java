package cn.comtom.ebs.front.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.ebr.service.IEbrBroadcastService;
import cn.comtom.tools.response.ApiPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbrBroadcastServiceImpl implements IEbrBroadcastService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public EbrBroadcastInfo getById(String ebrId) {
        return resoFeginService.getEbrBroadcastInfoById(ebrId);
    }

    @Override
    public ApiPageResponse<EbrBroadcastInfo> getEbrBroadcastInfoByPage(BroadcastPageRequest request) {
        return resoFeginService.getEbrBroadcastInfoByPage(request);
    }

    @Override
    public EbrBroadcastInfo save(EbrBroadcastAddRequest request) {
        return resoFeginService.saveEbrBroadcastInfo(request);
    }

    @Override
    public Boolean update(EbrBroadcastUpdateRequest request) {
        return resoFeginService.updateEbrBroadcastInfo(request);
    }

    @Override
    public Boolean delete(String ebrId) {
        return resoFeginService.deleteEbrBroadcastInfo(ebrId);
    }
}
