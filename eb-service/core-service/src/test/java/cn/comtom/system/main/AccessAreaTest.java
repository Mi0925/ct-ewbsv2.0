package cn.comtom.system.main;

import cn.comtom.core.main.access.entity.dbo.AccessArea;
import cn.comtom.core.main.access.service.IAccessAreaService;
import cn.comtom.domain.core.access.info.AccessAreaInfo;
import cn.comtom.domain.core.access.request.AccessAreaReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccessAreaTest {

    @Autowired
    private IAccessAreaService accessAreaService;

    @Test
    public void getList(){
        AccessAreaReq req = new AccessAreaReq();
        req.setInfoId("11");
        List<AccessAreaInfo> list = accessAreaService.list(req);
        System.out.println("========resultList : "+list);
    }

    @Test
    public void getById(){
        AccessArea accessArea = (AccessArea) accessAreaService.selectById("1");
        System.out.println("========accessArea : "+accessArea);
    }
}
