package cn.comtom.ebs.front.main.resource.service;

import cn.comtom.domain.system.sysresource.info.SysResourceInfo;
import cn.comtom.domain.system.sysresource.request.SysResourcePageRequest;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

public interface ISysResourceService {
    List<SysResourceInfo> getSysResourceByIds(List<String> resourceIds);

    ApiPageResponse<SysResourceInfo> getSysResourceInfoListPage(SysResourcePageRequest request);

    ApiPageResponse<SysResourceInfo> getSysResAndChildren(SysResourcePageRequest request);

    List<SysResourceInfo> getByPid(String pid);

    SysResourceInfo getSysResourceById(String id);

    List<SysResourceInfo> getAllWithRecursion();

}
