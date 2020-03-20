package cn.comtom.ebs.front.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.ebr.service.IEbrTerminalService;
import cn.comtom.tools.response.ApiPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbrTerminalServiceImpl implements IEbrTerminalService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public EbrTerminalInfo getById(String ebrId) {
        return resoFeginService.getEbrTerminalInfoById(ebrId);
    }

    @Override
    public ApiPageResponse<EbrTerminalInfo> getByPage(EbrTerminalPageRequest request) {
        return resoFeginService.getEbrTerminalInfoByPage(request);
    }
}
