package cn.comtom.system.main.resource.service;

import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysresource.request.SysResourceQueryRequest;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.resource.entity.dbo.SysResource;
import cn.comtom.tools.response.ApiListResponse;

import java.util.List;

public interface ISysResourceService extends BaseService<SysResource,String> {
    List<SysResourceInfo> list(SysResourceQueryRequest request);

    List<SysResourceInfo> getByPid(String pid);


    ApiListResponse<SysResourceInfo> getAllWithRecursion();

}
