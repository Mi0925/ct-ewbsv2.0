package cn.comtom.system.main;

import cn.comtom.domain.system.sysuser.info.SysUserInfo;
import cn.comtom.system.main.user.service.ISysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserTest {

    @Autowired
    private ISysUserService userService;

    @Test
    public void getUserList() {
//        List<SysUserInfo> list = userService.pageList("江");
//        System.out.println("=========sysuserlist : " +list);
    }
}
