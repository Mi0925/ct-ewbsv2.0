package cn.comtom.system.main;

import cn.comtom.system.main.params.service.ISysParamService;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysParamTest {

    @Autowired
    private ISysParamService sysParamService;

    @Test
    public void getPage() {
        SysParamPageRequest req = new SysParamPageRequest();

        List<SysParamsInfo> list = sysParamService.pageList(req);
        System.out.println("=========sysuserlist : " +list);
    }
}
