package cn.comtom.ebs.front.main.params.service;

import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import cn.comtom.domain.system.sysparam.request.SysParamsUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;

public interface ISysParamsService {

    ApiPageResponse<SysParamsInfo> page(SysParamPageRequest request);

    SysParamsInfo getByKey(String ebrPlatformId);

    ApiEntityResponse<SysParamsInfo> getSysParamsInfoById(String paramId);

    ApiResponse updateSysParamsInfo(SysParamsUpdateRequest request);

    ApiEntityResponse<SysParamsInfo> getSysParamsInfoByKey(String paramKey);
}
