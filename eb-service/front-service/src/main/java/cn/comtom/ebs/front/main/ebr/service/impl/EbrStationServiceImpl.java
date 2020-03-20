package cn.comtom.ebs.front.main.ebr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.ebr.service.IEbrStationService;
import cn.comtom.tools.response.ApiPageResponse;

@Service
public class EbrStationServiceImpl implements IEbrStationService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public EbrStationInfo getById(String ebrId) {
        return resoFeginService.getEbrStationInfoById(ebrId);
    }

    @Override
    public ApiPageResponse<EbrStationInfo> getEbrStationInfoByPage(EbrStationPageRequest request) {
        return resoFeginService.getEbrStationInfoByPage(request);
    }

    @Override
    public EbrStationInfo save(StationAddRequest request) {
        return resoFeginService.saveEbrStationInfo(request);
    }

    @Override
    public Boolean update(StationUpdateRequest request) {
        return resoFeginService.updateEbrStationInfo(request);
    }

    @Override
    public Boolean delete(String ebrId) {
        return resoFeginService.deleteEbrStationInfo(ebrId);
    }
}
