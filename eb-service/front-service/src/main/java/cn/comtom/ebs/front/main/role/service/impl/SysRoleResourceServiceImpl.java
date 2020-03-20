package cn.comtom.ebs.front.main.role.service.impl;

import cn.comtom.domain.system.roleresource.info.SysRoleResourceInfo;
import cn.comtom.domain.system.roleresource.request.RoleResAddBatchRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.role.service.ISysRoleResourceService;
import cn.comtom.tools.response.ApiListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SysRoleResourceServiceImpl implements ISysRoleResourceService {

    @Autowired
    private SystemFegin systemFegin;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public List<SysRoleResourceInfo> getSysRoleResourceByRoleIds(List<String> roleIds) {
        ApiListResponse<SysRoleResourceInfo> response = systemFegin.getSysRoleResourceByRoleIds(roleIds);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public ApiListResponse<SysRoleResourceInfo> getRoleResourcesByRoleId(String roleId) {
        return systemFegin.getSysRoleResourceByRoleIds(Collections.singletonList(roleId));
    }

    @Override
    public Boolean deleteRoleResourceByRoleId(String roleId) {
        return systemFeginService.deleteRoleResourceByRoleId(roleId);
    }

    @Override
    public Boolean deleteRoleResourceByResourceId(String resourceId) {
        return systemFeginService.deleteRoleResourceByResourceId(resourceId);
    }

    @Override
    public Boolean saveRoleResBatch(RoleResAddBatchRequest request) {
        return systemFeginService.saveSysRoleResourceBatch(request);
    }


}
