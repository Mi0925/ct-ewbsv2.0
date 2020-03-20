package cn.comtom.system.main.user.service;

import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.user.entity.dbo.SysUserRole;

import java.util.List;

public interface ISysUserRoleService extends BaseService<SysUserRole,String> {
    List<String> getUserRoleIds(String userId);

    Integer saveBatch(String userId, List<String> roleIds);
}
