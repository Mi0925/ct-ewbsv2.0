package cn.comtom.system.main.role.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.role.entity.dbo.SysRoleResource;
import cn.comtom.system.main.role.mapper.SysRoleResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysRoleResourceDao extends BaseDao<SysRoleResource,String> {

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    public SystemMapper<SysRoleResource, String> getMapper() {
        return sysRoleResourceMapper;
    }

    public List<SysRoleResource> getByRoleIds(List<String> roleIds) {
        Weekend<SysRoleResource> weekend = Weekend.of(SysRoleResource.class);
        WeekendCriteria<SysRoleResource,Object> weekendCriteria = weekend.weekendCriteria();
        if(roleIds != null && !roleIds.isEmpty()){
            weekendCriteria.andIn(SysRoleResource::getRoleId,roleIds);
        }
        return sysRoleResourceMapper.selectByExample(weekend);
    }
}
