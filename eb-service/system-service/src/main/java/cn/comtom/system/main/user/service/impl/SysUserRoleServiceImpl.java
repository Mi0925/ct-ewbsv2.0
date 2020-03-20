package cn.comtom.system.main.user.service.impl;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.role.dao.SysRoleDao;
import cn.comtom.system.main.user.dao.SysUserRoleDao;
import cn.comtom.system.main.user.entity.dbo.SysUserRole;
import cn.comtom.system.main.user.service.ISysUserRoleService;
import cn.comtom.tools.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole,String> implements ISysUserRoleService {

    @Autowired
    private SysUserRoleDao userRoleDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
    public BaseDao getDao() {
        return userRoleDao;
    }


    @Override
    public List<String> getUserRoleIds(String userId) {
        List<SysUserRole> sysUserRoleList = userRoleDao.getUserRoleIds(userId);
        return Optional.ofNullable(sysUserRoleList).orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(sysUserRole ->  sysRoleDao.selectById(sysUserRole.getRoleId()) != null)
                .map(sysUserRole ->  sysUserRole.getRoleId())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Integer saveBatch(String userId, List<String> roleIds) {
        if(StringUtils.isBlank(userId) || roleIds == null || roleIds.isEmpty()){
            return 0;
        }
        Set<String> roleIdSet = new HashSet<>();
        roleIdSet.addAll(roleIds);
        List<SysUserRole> list = new ArrayList<>();
        roleIdSet.forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRole.setId(UUIDGenerator.getUUID());
            list.add(sysUserRole);
        });
        return  userRoleDao.saveList(list);
    }
}
