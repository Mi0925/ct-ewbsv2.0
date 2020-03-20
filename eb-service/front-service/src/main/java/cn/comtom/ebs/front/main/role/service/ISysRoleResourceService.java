package cn.comtom.ebs.front.main.role.service;

import cn.comtom.domain.system.roleresource.info.SysRoleResourceInfo;
import cn.comtom.domain.system.roleresource.request.RoleResAddBatchRequest;
import cn.comtom.tools.response.ApiListResponse;

import java.util.List;

public interface ISysRoleResourceService {

    List<SysRoleResourceInfo> getSysRoleResourceByRoleIds(List<String> roleIds);

    ApiListResponse<SysRoleResourceInfo> getRoleResourcesByRoleId(String roleId);

    Boolean deleteRoleResourceByRoleId(String roleId);

    Boolean deleteRoleResourceByResourceId(String resourceId);

   Boolean saveRoleResBatch(RoleResAddBatchRequest request);
}

