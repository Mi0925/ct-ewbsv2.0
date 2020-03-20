
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchPageRequest;
import cn.comtom.linkage.main.LinkageApplication;
import cn.comtom.linkage.main.dispatch.service.IEbmDispatchService;
import cn.comtom.linkage.main.dispatch.service.ITextInsertionService;
import cn.comtom.linkage.main.fegin.CoreFegin;
import cn.comtom.tools.response.ApiPageResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = LinkageApplication.class)
@RunWith(SpringRunner.class)
public class EbmTest {

    @Autowired
    private IEbmDispatchService ebmDispatchService;
    
    @Autowired
    private ITextInsertionService textInsertionService;

    @Autowired
    private CoreFegin coreFegin;

    @Test
    public void getEbdPack() {
        //ebmDispatchService.dispatch();
        EbmDispatchPageRequest request = new EbmDispatchPageRequest();
        request.setState("0");
        ApiPageResponse<EbmDispatchInfo> apiPageResponse = coreFegin.getEbmDispatchByPage(request);
        System.out.println("###############################"+apiPageResponse.getData());
    }

    @Test
    public void getByEbmIdAndResourceId(){
        coreFegin.getEbmBrdByEbmIdAndResourceId("111","111");
    }
    
    @Test
    public void textInsertionTest() {
    	textInsertionService.getChannelList();
    }
}
