package cn.comtom.ebs.front.main.user.service;

import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.SysUserUpdateRequest;
import cn.comtom.domain.system.sysuser.request.UserAddRequest;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;

import java.util.List;

public interface ISysUserService {
    SysUserInfo getUserInfoById(String userId);

    ApiEntityResponse<SysUserInfo> login(String userName, String password);

    ApiPageResponse<SysUserInfo> page(UserPageRequest pageRequest);

    Boolean updateUserInfo(SysUserUpdateRequest request);

    SysUserInfo save(UserAddRequest request);

    SysUserInfo getByAccount(String account);
}
