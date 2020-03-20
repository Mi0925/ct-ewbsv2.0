package cn.comtom.ebs.front.main.params.service.impl;

import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import cn.comtom.domain.system.sysparam.request.SysParamsUpdateRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.params.service.ISysParamsService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysParamServiceImpl implements ISysParamsService {

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private SystemFegin systemFegin;

    @Override
    public ApiPageResponse<SysParamsInfo> page(SysParamPageRequest request) {
        return systemFeginService.findSysParamPage(request);
    }

    @Override
    public SysParamsInfo getByKey(String paramKey) {
        return systemFeginService.getByKey(paramKey);
    }

    @Override
    public ApiEntityResponse<SysParamsInfo> getSysParamsInfoById(String paramId) {
        return systemFegin.getSysParamsInfoById(paramId);
    }

    @Override
    public ApiResponse updateSysParamsInfo(SysParamsUpdateRequest request) {
        return systemFegin.updateSysParamsInfo(request);
    }

    @Override
    public ApiEntityResponse<SysParamsInfo> getSysParamsInfoByKey(String paramKey) {
        return systemFegin.getByKey(paramKey);
    }
}
