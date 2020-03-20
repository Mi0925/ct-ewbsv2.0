package cn.comtom.system.main.role.service.impl;

import cn.comtom.domain.system.role.info.SysRoleInfo;
import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.role.dao.SysRoleDao;
import cn.comtom.system.main.role.entity.dbo.SysRole;
import cn.comtom.system.main.role.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务类
 * @author guomao
 * @Date 2018-10-29 9:30
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole,String> implements ISysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;


    @Override
    public BaseDao<SysRole, String> getDao() {
        return sysRoleDao;
    }

    @Override
    public List<SysRoleInfo> page(SysRolePageRequest request) {
        List<SysRole> sysRoleList = sysRoleDao.page(request);
        List<SysRoleInfo> infoList=new ArrayList<>();
        for (SysRole entity:sysRoleList) {
            SysRoleInfo info=new SysRoleInfo();
            BeanUtils.copyProperties(entity,info);
            infoList.add(info);
        }
        return infoList;
    }
}
