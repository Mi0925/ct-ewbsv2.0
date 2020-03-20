package cn.comtom.ebs.front.main.role.service;

import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRoleAddRequest;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.domain.system.role.request.SysRoleUpdateRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;

public interface ISysRoleService {

    ApiPageResponse<SysRoleInfo> page(SysRolePageRequest request);

    ApiEntityResponse<SysRoleInfo> getRoleInfoById(String roleId);

    Boolean deleteRoleInfoById(String roleId);

    ApiEntityResponse<SysRoleInfo> saveSysRoleInfo(SysRoleAddRequest request);

    ApiResponse updateSysRoleInfo(SysRoleUpdateRequest request);
}
