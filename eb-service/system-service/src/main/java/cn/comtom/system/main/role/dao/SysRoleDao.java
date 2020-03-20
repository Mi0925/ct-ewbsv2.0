package cn.comtom.system.main.role.dao;

import cn.comtom.domain.system.role.request.SysRolePageRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.role.entity.dbo.SysRole;
import cn.comtom.system.main.role.mapper.SysRoleMapper;
import cn.comtom.system.main.user.entity.dbo.SysUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysRoleDao extends BaseDao<SysRole,String> {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public SystemMapper<SysRole, String> getMapper() {
        return sysRoleMapper;
    }

    public List<SysRole> page(SysRolePageRequest request) {
        Weekend<SysRole> weekend = Weekend.of(SysRole.class);
        WeekendCriteria<SysRole, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotEmpty(request.getName())){
            weekendCriteria.andLike(SysRole::getName,"%".concat(request.getName()).concat("%"));
        }
        return sysRoleMapper.selectByExample(weekend);
    }
}
