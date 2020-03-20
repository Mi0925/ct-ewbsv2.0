package cn.comtom.linkage.main.dispatch.service.impl;

import cn.comtom.linkage.main.dispatch.service.ITextInsertionService;
import cn.comtom.linkage.main.fegin.service.ISystemFeginService;
import cn.comtom.tools.constants.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TextInsertionServiceImpl implements ITextInsertionService , InitializingBean{

    @Value("${externalData.textInsert.baseUrl}")
    private  String baseUrl ;

    @Value("${externalData.textInsert.username}")
    private  String username ;

    @Value("${externalData.textInsert.password}")
    private  String password ;

    private RestTemplate restTemplate;

    @Autowired
    private ISystemFeginService systemFeginService;


    public TextInsertionServiceImpl() {
		super();
	}

	public TextInsertionServiceImpl(String baseUrl, String username, String password) {
		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
		restTemplate = new RestTemplate();
	}

	//1、登录
    private final static String LOGIN_PATH = "/EB/EBlogin.php";
    //2、登出
    private final static String EBLOGOUT_PATH = "/EB/EBlogout.php";
    //3、读取频道列表
    private final static String GETCHANNELLIST_PATH = "/EB/getChannelList.php";
    //4、读取字体列表
    private final static String GETFONTLIST_PATH = "/EB/getFontList.php";
    //5、添加应急广播字幕信息
    private final static String ISSUEEBMESSAGE_PATH = "/EB/issueEBMessage.php";
    //6、读取应急广播字幕信息播出记录
    private final static String GETEBHISTORY_PATH = "/EB/getEBHistory.php";
    //7、读取应急广播信息
    private final static String GETEBMESSAGE_PATH = "/EB/getEBMessage.php";
    //信任码
    private String creditCode;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(restTemplate==null) {
			restTemplate = new RestTemplate();
		}
	}

	/**
	 * 登陆
	 * @return
	 */
	private boolean login() {
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();
        params.put("username", username);
        params.put("password", DigestUtils.md5DigestAsHex(password.getBytes()));
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(baseUrl+LOGIN_PATH, entity, String.class);
			Map resultMap = JSON.parseObject(result, Map.class);
			log.info("【TextInsertion】~字幕插播登入接口，resultJson:{}",result);
			if(new Integer(0).equals(resultMap.get("code"))) {
				List<Map> listMap = JSONArray.parseArray(resultMap.get("data").toString(), Map.class);
				creditCode = (String) listMap.get(0).get("creditCode");
				log.info("【TextInsertion】~字幕插播登入接口，登陆成功！,creditCode:{}",creditCode);
				return true;
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~字幕插播登入接口，登陆失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        log.error("【TextInsertion】~字幕插播登入失败");
        return false;
	}
	/**
	 * 登出
	 * @return
	 */
	private boolean logout() {
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();
        params.put("creditCode", creditCode);
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
        	String result = restTemplate.postForObject(baseUrl+EBLOGOUT_PATH, entity, String.class);
			log.info("【TextInsertion】~字幕插播登出接口，resultJson:{}",JSON.toJSONString(result));
			Map resultMap = JSON.parseObject(result, Map.class);
			if(new Integer(0).equals(resultMap.get("code"))) {
				creditCode = null;
				log.info("【TextInsertion】~字幕插播登出接口，登出成功！,creditCode:{}",creditCode);
				return true;
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~字幕插播登出接口，登出失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        log.error("【TextInsertion】~字幕插播登出失败");
        creditCode = null;
        return false;
	}

	/**
	 * 获取频道列表
	 * @return
	 */
	public List<Map> getChannelList() {
		if(StringUtils.isBlank(creditCode)) {
			login();
        }
		List<Map> channelMaps = Lists.newArrayList();
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();

        params.put("creditCode", creditCode);
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(baseUrl+GETCHANNELLIST_PATH, entity, String.class);
			log.info("【TextInsertion】~获取频道列表接口，resultJson:{}",JSON.toJSONString(result));
			Map resultMap = JSON.parseObject(result, Map.class);
			if(new Integer(0).equals(resultMap.get("code"))) {
				log.info("【TextInsertion】~获取频道列表成功！,data:{}",resultMap.get("data"));
				channelMaps = JSONArray.parseArray(resultMap.get("data").toString(), Map.class);
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~获取频道列表失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        return channelMaps;
	}

	/**
	 * 4、读取字体列表
	 * @return
	 */
	public List<Map> getFontList() {
		if(StringUtils.isBlank(creditCode)) {
			login();
        }
		List<Map> fontMaps = Lists.newArrayList();
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();

        params.put("creditCode", creditCode);
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(baseUrl+GETFONTLIST_PATH, entity, String.class);
			Map resultMap = JSON.parseObject(result, Map.class);
			log.info("【TextInsertion】~获取字体列表接口，resultJson:{}",result);
			if(new Integer(0).equals(resultMap.get("code"))) {
				log.info("【TextInsertion】~获取字体列表成功！,data:{}",resultMap.get("data"));
				fontMaps = JSONArray.parseArray(resultMap.get("data").toString(), Map.class);
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~获取字体列表失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        return fontMaps;
	}

	/**
	 * 5、添加应急广播字幕信息
	 * @return
	 */
	public boolean issueEBMessage(String reqUrl, Map<String,Object> param) {
		String playTimes = systemFeginService.getByKey(Constants.TV_PROGRAM_PLAYTIMES);
		if(StringUtils.isBlank(playTimes)) {
			playTimes = "1";
		}
		if(StringUtils.isBlank(creditCode)) {
			login();
        }
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, Object> params = Maps.newHashMap();
        params.put("channel", "");//频道名称，空表示全部频道
        params.put("speed", 1);//0-5,0表示取消当前字幕播出
        params.put("start_x", 0);// 开始坐标x轴0-719
        params.put("start_y", 540);//开始坐标y轴0-575
        params.put("end_x", 719);//结束坐标x轴0-719
        params.put("end_y", 565);//结束坐标y轴0-575
        params.put("font_name", "缺省字体");//字体名称
        params.put("font_size", 16);//字体大小10-40
        params.put("font_color", "#ff0000");//字体颜色#000000到#ffffff
        params.put("font_bold", 0);//字体粗体
        params.put("font_italic", 0);//字体斜体
        params.put("shadow", 0);//阴影深度0 － 5
        params.put("shadow_color", "#000000");// 阴影颜色 值范围：#000000到#ffffff
        params.put("background_transparency", 0);//背景透明度值范围：0-100
        params.put("background_color", "#ffffff");//背景颜色值范围：#000000-#ffffff
        params.put("shrink", 0);//是否缩屏0/1
        params.put("interval", 3);//间隔时间1-65536（单位秒
        params.put("content", param.get("content"));//应急广播内容文本，utf8，最长500个汉字或2000个ASCII字符
        //params.put("picture_url", "http://www.baidu.com/");//图片url地址（http），可选
        //params.put("picture_transparency", 22);//图片透明度 0-100（有图片时才有此参数）
        //params.put("picture_start_x", 23);// 图片水平定位 有图片时才有此参数0-719
        //params.put("picture_start_y", 23);//图片垂直定位 有图片时才有此参数0-575
        //params.put("play_number", 0);//字幕滚动播出次数，0表示一直播，最大999
        params.put("creditCode", creditCode);
        params.put("play_number", param.get("play_number")==null?Integer.valueOf(playTimes):param.get("play_number"));
        String requestJson = JSON.toJSONString(params);
        log.info("【TextInsertion】~请求应急广播字幕信息接口开始，param:{},requestJson:{}",param,requestJson);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(baseUrl+ISSUEEBMESSAGE_PATH, entity, String.class);
			Map resultMap = JSON.parseObject(result, Map.class);
			log.info("【TextInsertion】~添加应急广播字幕信息接口，resultJson:{}",result);
			if(new Integer(0).equals(resultMap.get("code"))) {
				log.info("【TextInsertion】~添加应急广播字幕信息成功！,data:{}",resultMap.get("data"));
				return true;
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~添加应急广播字幕信息失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        logout();
        return false;
	}

	/**
	 * 6、读取应急广播字幕信息播出记录
	 * @return
	 */
	public List<Map> getEBHistory(Map<String,Object> param) {
		if(StringUtils.isBlank(creditCode)) {
			login();
        }
		List<Map> fontMaps = Lists.newArrayList();
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();

        params.put("creditCode", creditCode);
        params.put("channel", "");
        params.put("start_date", "20190515");
        params.put("end_date", "20190815");
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(baseUrl+GETEBHISTORY_PATH, entity, String.class);
			log.info("【TextInsertion】~获取应急广播字幕信息播出记录接口，resultJson:{}",JSON.toJSONString(result));
			Map resultMap = JSON.parseObject(result, Map.class);
			if(new Integer(0).equals(resultMap.get("code"))) {
				log.info("【TextInsertion】~获取应急广播字幕信息播出记录成功！,data:{}",resultMap.get("data"));
				fontMaps = JSONArray.parseArray(resultMap.get("data").toString(), Map.class);
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~获取应急广播字幕信息播出记录失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        return fontMaps;
	}

	/**
	 * 7、读取应急广播信息
	 * @return
	 */
	public List<Map> getEBMessage() {
		if(StringUtils.isBlank(creditCode)) {
			login();
        }
		List<Map> fontMaps = Lists.newArrayList();
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
        Map<String, String> params = Maps.newHashMap();

        params.put("creditCode", creditCode);
        params.put("channel", "");
        String requestJson = JSON.toJSONString(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        try {
			String result = restTemplate.postForObject(baseUrl+GETEBMESSAGE_PATH, entity, String.class);
			log.info("【TextInsertion】~读取应急广播信息接口，resultJson:{}",result);
			Map resultMap = JSON.parseObject(result, Map.class);
			if(new Integer(0).equals(resultMap.get("code"))) {
				log.info("【TextInsertion】~读取应急广播信息成功！,data:{}",resultMap.get("data"));
				fontMaps = JSONArray.parseArray(resultMap.get("data").toString(), Map.class);
			}
		} catch (RestClientException e) {
			log.error("【TextInsertion】~读取应急广播信息失败，异常:{}",e.getMessage());
			e.printStackTrace();
		}
        return fontMaps;
	}

}
