package cn.comtom.core.main.svs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import cn.comtom.core.main.svs.service.SVSService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Data
public class SVSServiceImpl implements  SVSService{

    @Value("${svs.service.ebmNoticeUrl}")
    private String ebmNoticeUrl;
    
    private static final String EBM_NOTICE = "ebmNotice";
    
    private static final String EBRSTATE_NOTICE = "ebrStateNotice";
    
    private static final String DVB_NOTICE = "flvNotice";
    
    private static RestTemplate restTemplate = new RestTemplate();
    
	private String doPost(String msgType) {
		
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();
        params.put("type", msgType);
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(ebmNoticeUrl, entity, String.class);
			log.info("【SVSService-{}】~消息推送大屏展示，resultJson:{}",msgType,result);
			return result;
		} catch (Exception e) {
			log.error("【SVSService-{}】~消息推送大屏展示，异常:{}",msgType,e.getStackTrace().toString());
		}
        
        return null;
	}
	
	@Override
	public void callEbmNotice() {
		doPost(EBM_NOTICE);
		
	}
	
	@Override
	public void callEbrState() {
		doPost(EBRSTATE_NOTICE);
	}

	@Override
	public void callDVBNotice() {
		doPost(DVB_NOTICE);
	}

}
