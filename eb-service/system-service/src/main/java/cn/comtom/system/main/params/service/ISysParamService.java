package cn.comtom.system.main.params.service;

import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.params.entity.dbo.SysParams;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;

import java.util.List;

public interface ISysParamService extends BaseService<SysParams,String> {

    List<SysParamsInfo> pageList(SysParamPageRequest request);

    SysParamsInfo getByKey(String paramKey);
}
