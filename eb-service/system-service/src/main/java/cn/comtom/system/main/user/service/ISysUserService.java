package cn.comtom.system.main.user.service;


import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.system.fw.BaseService;
import cn.comtom.system.main.user.entity.dbo.SysUser;
import cn.comtom.tools.response.ApiEntityResponse;

import java.util.List;

/**
 * 用户服务接口
 * @author guomao
 * @Date 2018-10-29 9:17
 */
public interface ISysUserService extends BaseService<SysUser,String> {

  List<SysUserInfo> pageList(UserPageRequest pageRequest);

  ApiEntityResponse<SysUserInfo> authorize(String account, String password);

  SysUserInfo getByAccount(String account);
}
