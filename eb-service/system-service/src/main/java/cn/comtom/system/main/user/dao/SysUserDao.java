package cn.comtom.system.main.user.dao;

import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.user.entity.dbo.SysUser;
import cn.comtom.system.main.user.mapper.SysUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysUserDao extends BaseDao<SysUser,String> {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SystemMapper<SysUser, String> getMapper() {
        return userMapper;
    }
    public List<SysUser> pageList(UserPageRequest pageRequest) {
        Weekend<SysUser> weekend = Weekend.of(SysUser.class);
        WeekendCriteria<SysUser, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotEmpty(pageRequest.getUserName())){
            weekendCriteria.andLike(SysUser::getUserName,"%".concat(pageRequest.getUserName()).concat("%"));
        }
        if(StringUtils.isNotEmpty(pageRequest.getAccount())){
            weekendCriteria.andLike(SysUser::getAccount,"%".concat(pageRequest.getAccount()).concat("%"));
        }
        if(null!=pageRequest.getStatus()){
            weekendCriteria.andEqualTo(SysUser::getStatus,pageRequest.getStatus());
        }
        return userMapper.selectByExample(weekend);
    }


    public SysUser getByAccount(String account) {
        SysUser sysUser = new SysUser();
        sysUser.setAccount(account);
      //  sysUser.setDeleteFlag(false);
        return userMapper.selectOne(sysUser);
    }
}
