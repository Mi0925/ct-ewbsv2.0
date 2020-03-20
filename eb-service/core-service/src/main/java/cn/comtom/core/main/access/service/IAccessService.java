package cn.comtom.core.main.access.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.access.entity.dbo.Access;
import cn.comtom.domain.core.access.info.AccessInfo;
import cn.comtom.domain.core.access.request.AccessInfoPageRequest;

import java.util.List;

public interface IAccessService extends BaseService<Access,String> {
    List<AccessInfo> list(AccessInfoPageRequest request);
}
