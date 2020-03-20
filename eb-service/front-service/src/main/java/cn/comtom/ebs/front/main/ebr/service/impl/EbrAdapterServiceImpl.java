package cn.comtom.ebs.front.main.ebr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.ebr.service.IEbrAdapterService;
import cn.comtom.tools.response.ApiPageResponse;

@Service
public class EbrAdapterServiceImpl implements IEbrAdapterService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public EbrAdapterInfo getById(String ebrId) {
        return resoFeginService.getEbrAdapterInfoById(ebrId);
    }

    @Override
    public ApiPageResponse<EbrAdapterInfo> getEbrAdapterInfoByPage(EbrAdapterPageRequest request) {
        return resoFeginService.getEbrAdapterInfoByPage(request);
    }

    @Override
    public EbrAdapterInfo save(AdapterAddRequest request) {
        return resoFeginService.saveEbrAdapterInfo(request);
    }

    @Override
    public Boolean update(AdapterUpdateRequest request) {
        return resoFeginService.updateEbrAdapterInfo(request);
    }

    @Override
    public Boolean delete(String ebrId) {
        return resoFeginService.deleteEbrAdapterInfo(ebrId);
    }
}
