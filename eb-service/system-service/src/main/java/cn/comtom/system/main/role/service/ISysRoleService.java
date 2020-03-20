package cn.comtom.system.main.role.service;


import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.role.entity.dbo.SysRole;

import java.util.List;

/**
 * 用户服务接口
 * @author guomao
 * @Date 2018-10-29 9:17
 */
public interface ISysRoleService extends BaseService<SysRole,String> {

    List<SysRoleInfo> page(SysRolePageRequest request);
}
