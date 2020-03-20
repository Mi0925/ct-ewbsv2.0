package cn.comtom.ebs.front.main.user.service.impl;

import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.SysUserUpdateRequest;
import cn.comtom.domain.system.sysuser.request.UserAddRequest;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.user.service.ISysUserService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SystemFegin systemFegin;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public SysUserInfo getUserInfoById(String userId) {
        ApiEntityResponse<SysUserInfo> response = systemFegin.getUserInfoById(userId);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public ApiEntityResponse<SysUserInfo> login(String account, String password) {
        return systemFegin.login(account,password);
    }

    @Override
    public ApiPageResponse<SysUserInfo> page(UserPageRequest pageRequest) {
        return systemFeginService.findUserPage(pageRequest);
    }

    @Override
    public Boolean updateUserInfo(SysUserUpdateRequest request) {
        return systemFeginService.updateUserInfo(request);
    }

    @Override
    public SysUserInfo save(UserAddRequest request) {
        return systemFeginService.saveUserInfo(request);
    }

    @Override
    public SysUserInfo getByAccount(String account) {
        return systemFeginService.getUserInfoByAccount(account);
    }
}
