package cn.comtom.system.main;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.reso.main.ResoApplication;
import cn.comtom.reso.main.ebr.dao.EbrChannelDao;
import cn.comtom.reso.main.ebr.entity.dbo.EbrChannel;
import cn.comtom.reso.main.ebr.mapper.EbrChannelMapper;
import cn.comtom.reso.main.ebr.service.IEbrPlatformService;
import cn.comtom.tools.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = ResoApplication.class)
@RunWith(SpringRunner.class)
public class ebrPlatformTest {

    @Autowired
    private IEbrPlatformService ebrPlatformService;
    
    @Autowired
    private EbrChannelDao ebrChannelDao;

    @Test
    public void testQueryByCondition(){
        PlatformWhereRequest request = new PlatformWhereRequest();
        request.setSyncFlag(1);
        request.setRptStartTime("2018-10-01 00:00:00");
        request.setRptEndTime("2018-12-01 00:00:00");
        List<EbrPlatformInfo> platformListByWhere = ebrPlatformService.findPlatformListByWhere(request);
        System.out.println("========= : "+ JSON.toJSONString(platformListByWhere));
    }
    
    @Test
    public void testQueryEbrChannel(){
    	//String channelByEbrId = EbrChannelMapper.getChannelByEbrId("1");
        //System.out.println("========= : "+ channelByEbrId);
    	EbrChannel ebrChannel = new EbrChannel();
    	ebrChannel.setEbrId("2");
    	ebrChannel.setSendChannel("2");
    	ebrChannelDao.updateByEbrId(ebrChannel);
    }
}
