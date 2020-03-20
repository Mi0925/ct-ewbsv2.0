package cn.comtom.system.main.user.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.user.entity.dbo.SysUserRole;
import cn.comtom.system.main.user.mapper.SysUserRoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysUserRoleDao extends BaseDao<SysUserRole,String> {

    @Autowired
    private SysUserRoleMapper userRoleMapper;


    @Override
    public SystemMapper<SysUserRole, String> getMapper() {
        return userRoleMapper;
    }


    public List<SysUserRole> getUserRoleIds(String userId) {
        Weekend<SysUserRole> weekend = Weekend.of(SysUserRole.class);
        WeekendCriteria<SysUserRole,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(userId)){
            weekendCriteria.andEqualTo(SysUserRole::getUserId,userId);
        }
        return userRoleMapper.selectByExample(weekend);
    }
}
