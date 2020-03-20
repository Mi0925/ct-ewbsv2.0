package cn.comtom.system.main.user.service.impl;

import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.domain.system.sysuser.request.UserPageRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.user.dao.SysUserDao;
import cn.comtom.system.main.user.entity.dbo.SysUser;
import cn.comtom.system.main.user.service.ISysUserService;
import cn.comtom.system.utils.PasswordUtils;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
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
public class SysUserServiceImpl extends BaseServiceImpl<SysUser,String> implements ISysUserService {

    @Autowired
    private SysUserDao userDao;


    @Override
    public BaseDao<SysUser, String> getDao() {
        return userDao;
    }

    @Override
    public List<SysUserInfo> pageList(UserPageRequest pageRequest) {
        List<SysUser> userList = userDao.pageList(pageRequest);
        List<SysUserInfo> infoList=new ArrayList<>();
        for (SysUser entity:userList) {
            SysUserInfo info=new SysUserInfo();
            BeanUtils.copyProperties(entity,info);
            infoList.add(info);
        }

        return infoList;
    }

    @Override
    public ApiEntityResponse<SysUserInfo> authorize(String account, String password) {
        SysUserInfo sysUserInfo = getByAccount(account);
        if(sysUserInfo == null){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.USER_ACCOUNT_NOT_EXISTS);
        }
        if(sysUserInfo.getDeleteFlag()){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.USER_ACCOUNT_DELETED);
        }
        if(StateDictEnum.USER_STATUS_DISABLE.getKey().equals(String.valueOf(sysUserInfo.getStatus()))){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.USER_ACCOUNT_DISABLED);
        }
        String salt = sysUserInfo.getSalt();
        if(!PasswordUtils.matchPassword(password,salt,sysUserInfo.getPassword())){
            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.USER_PASSWORD_INCORRECT);
        }
        return ApiEntityResponse.ok(sysUserInfo);
    }

    @Override
    public SysUserInfo getByAccount(String account) {
        //根据用户名查询用户信息，已删除用户除外
        SysUser sysUser = userDao.getByAccount(account);
        if(sysUser == null){
            return null;
        }
        SysUserInfo sysUserInfo = new SysUserInfo();
        BeanUtils.copyProperties(sysUser,sysUserInfo);
        return sysUserInfo;
    }


}
