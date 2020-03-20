package cn.comtom.ebs.front.main.role.service.impl;

import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRoleAddRequest;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.domain.system.role.request.SysRoleUpdateRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.role.service.ISysRoleService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private SystemFegin systemFegin;

    @Override
    public ApiPageResponse<SysRoleInfo> page(SysRolePageRequest request) {

        return systemFeginService.findSysRolePage(request);
    }

    @Override
    public ApiEntityResponse<SysRoleInfo> getRoleInfoById(String roleId) {
        return systemFegin.getSysRoleInfoById(roleId);
    }

    @Override
    public Boolean deleteRoleInfoById(String roleId) {
        return systemFeginService.deleteRoleInfoById(roleId);
    }

    @Override
    public ApiEntityResponse<SysRoleInfo> saveSysRoleInfo(SysRoleAddRequest request) {
        return systemFegin.saveSysRoleInfo(request);
    }

    @Override
    public ApiResponse updateSysRoleInfo(SysRoleUpdateRequest request) {
        return systemFegin.updateSysRoleInfo(request);
    }
}
