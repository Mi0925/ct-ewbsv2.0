package cn.comtom.system.main.role.service;


import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.role.entity.dbo.SysRoleResource;

import java.util.List;

/**
 * 用户服务接口
 * @author guomao
 * @Date 2018-10-29 9:17
 */
public interface ISysRoleResourceService extends BaseService<SysRoleResource,String> {

    List<SysRoleResource> getByRoleIds(List<String> roleIds);
}
