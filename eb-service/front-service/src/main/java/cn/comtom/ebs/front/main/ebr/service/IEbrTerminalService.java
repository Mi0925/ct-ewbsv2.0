package cn.comtom.ebs.front.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.tools.response.ApiPageResponse;

public interface IEbrTerminalService {
    EbrTerminalInfo getById(String ebrId);

    ApiPageResponse<EbrTerminalInfo> getByPage(EbrTerminalPageRequest request);
}
