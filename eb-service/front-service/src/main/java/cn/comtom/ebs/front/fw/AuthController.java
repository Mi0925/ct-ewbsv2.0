package cn.comtom.ebs.front.fw;

import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.ebs.front.common.AuthRest;
import org.apache.shiro.SecurityUtils;

@AuthRest
public class AuthController extends BaseController {

    protected SysUserInfo getUser() {
        return (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
    }

    protected String getUserId() {
        SysUserInfo userInfo = getUser();
        if (userInfo == null) {
            return null;
        }
        return getUser().getUserId();
    }

    protected String getUserName() {
        SysUserInfo userInfo = getUser();
        if (userInfo == null) {
            return null;
        }
        return getUser().getUserName();
    }

    protected String getAccount() {
        SysUserInfo userInfo = getUser();
        if (userInfo == null) {
            return null;
        }
        return getUser().getAccount();
    }
}
