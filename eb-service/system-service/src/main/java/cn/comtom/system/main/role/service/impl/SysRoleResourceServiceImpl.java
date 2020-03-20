package cn.comtom.system.main.role.service.impl;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.role.dao.SysRoleResourceDao;
import cn.comtom.system.main.role.entity.dbo.SysRoleResource;
import cn.comtom.system.main.role.service.ISysRoleResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务类
 * @author guomao
 * @Date 2018-10-29 9:30
 */
@Slf4j
@Service
public class SysRoleResourceServiceImpl extends BaseServiceImpl<SysRoleResource,String> implements ISysRoleResourceService {

    @Autowired
    private SysRoleResourceDao sysRoleResourceDao;


    @Override
    public BaseDao<SysRoleResource, String> getDao() {
        return sysRoleResourceDao;
    }


    @Override
    public List<SysRoleResource> getByRoleIds(List<String> roleIds) {
        return sysRoleResourceDao.getByRoleIds(roleIds);
    }
}
