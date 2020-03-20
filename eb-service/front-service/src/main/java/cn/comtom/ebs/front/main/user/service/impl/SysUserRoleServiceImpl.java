package cn.comtom.ebs.front.main.user.service.impl;

import cn.comtom.domain.system.sysuserrole.request.SysUserRoleAddBatchRequest;
import cn.comtom.ebs.front.fegin.SystemFegin;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.user.service.ISysUserRoleService;
import cn.comtom.tools.response.ApiListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SysUserRoleServiceImpl implements ISysUserRoleService {

    @Autowired
    private SystemFegin systemFegin;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public Set<String> getUserRoleIds(String userId) {
        Set<String> resultSet = new HashSet<>();
        ApiListResponse<String> response = systemFegin.getUserRoleIds(userId);
        if(response.getSuccessful()){
            List<String> list = response.getData();
            resultSet.addAll(list);
            return  resultSet;
        }
        return null;
    }

    @Override
    public Boolean saveUserRoleRefBatch(SysUserRoleAddBatchRequest request) {
        return systemFeginService.saveUserRoleRefBatch(request);
    }

    @Override
    public Boolean deleteUserRoleByUserId(String userId) {
        return systemFeginService.deleteUserRoleByUserId(userId);
    }

    @Override
    public Boolean deleteUserRoleByRoleId(String roleId) {
        return systemFeginService.deleteUserRoleByRoleId(roleId);
    }
}
