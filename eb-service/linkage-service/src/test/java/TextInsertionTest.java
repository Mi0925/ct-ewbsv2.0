import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import cn.comtom.linkage.main.LinkageApplication;
import cn.comtom.linkage.main.dispatch.service.impl.TextInsertionServiceImpl;

@SpringBootTest(classes = LinkageApplication.class)
@RunWith(SpringRunner.class)
public class TextInsertionTest {

	public static void main(String[] args) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("content", "dsfsfffffffff");
		params.put("play_number", 34343434);
		TextInsertionServiceImpl textInsertionService = new TextInsertionServiceImpl("http://192.168.111.103", "admin", "admin");
		boolean issueEBMessage = textInsertionService.issueEBMessage("", params);
		System.out.println(issueEBMessage);
		
		List<Map> ebMessage = textInsertionService.getEBMessage();
		System.out.println(JSON.toJSONString(ebMessage));
		
		List<Map> ebHistory = textInsertionService.getEBHistory(null);
		System.out.println(JSON.toJSONString(ebHistory));
	}
}
