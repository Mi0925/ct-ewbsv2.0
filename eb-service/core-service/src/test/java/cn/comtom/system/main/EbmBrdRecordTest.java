package cn.comtom.system.main;

import cn.comtom.core.main.CoreApplication;
import cn.comtom.core.main.ebm.service.IEbmBrdRecordService;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.tools.utils.UUIDGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class EbmBrdRecordTest {

    @Autowired
    private IEbmBrdRecordService ebmBrdRecordService;


    @Test
    public void saveBatch(){
        List<EbmBrdRecordInfo> ebmBrdRecordInfoList = new ArrayList<>();
        EbmBrdRecordInfo ebmBrdRecordInfo = new EbmBrdRecordInfo();
        ebmBrdRecordInfo.setBrdItemId(UUIDGenerator.getUUID());
        ebmBrdRecordInfo.setAreaCode("43010000");
        ebmBrdRecordInfo.setEbmId(UUIDGenerator.getUUID());
        ebmBrdRecordInfo.setResourceId(UUIDGenerator.getUUID());
        ebmBrdRecordInfoList.add(ebmBrdRecordInfo);
    }

    @Test
    public void getByEbmIdAndResourceId(){

    }
}
