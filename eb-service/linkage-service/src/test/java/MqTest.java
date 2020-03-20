import cn.comtom.linkage.main.LinkageApplication;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author:WJ
 * @date: 2018/12/1 0001
 * @time: 下午 2:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinkageApplication.class)
public class MqTest {

    @Autowired
    private MQMessageProducer mqMessageProducer;


    @Test
    public void testEBMStateResponseMQ(){
        System.out.println("================"+mqMessageProducer);

//        mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue,"222");

    }

}
