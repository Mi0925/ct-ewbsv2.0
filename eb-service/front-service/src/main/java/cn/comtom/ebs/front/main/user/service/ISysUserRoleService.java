package cn.comtom.ebs.front.main.user.service;

import cn.comtom.domain.system.sysuserrole.request.SysUserRoleAddBatchRequest;

import java.util.Set;

public interface ISysUserRoleService {
    Set<String> getUserRoleIds(String userId);

    Boolean saveUserRoleRefBatch(SysUserRoleAddBatchRequest request);

    Boolean deleteUserRoleByUserId(String userId);

    Boolean deleteUserRoleByRoleId(String roleId);
}
