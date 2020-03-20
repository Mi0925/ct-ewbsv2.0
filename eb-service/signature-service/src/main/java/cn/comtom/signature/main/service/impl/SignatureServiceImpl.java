package cn.comtom.signature.main.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import cn.comtom.domain.signature.req.CertApplyReq;
import cn.comtom.domain.signature.resp.CertResp;
import cn.comtom.signature.main.exception.SignException;
import cn.comtom.signature.main.exception.VerifySignException;
import cn.comtom.signature.main.fegin.service.SystemFeginService;
import cn.comtom.signature.main.service.ISignatureService;
import cn.comtom.tools.constants.Constants;
import cn.hutool.core.util.RuntimeUtil;
import cn.tass.exceptions.YJException;
import cn.tass.yingjgb.YingJGBCALLDLL;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignatureServiceImpl implements ISignatureService , InitializingBean, DisposableBean{


    @Value("${yingjgb.Signature.deviceType}")
    private  int deviceType;
    
    @Value("${yingjgb.Signature.dataType}")
    private  int dataType;
    
    @Value("${cert.apply.url}")
    private  String certApplyUrl;
    
    @Value("${cert.apply.cert}")
    private String platFormCert;
    
    @Autowired
    private SystemFeginService systemFeginService;
    
    private static final String STOP_SIGN_CMD = "ps -ef | grep signature-service-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}' |xargs kill -9";
    
    private static final String START_SIGN_CMD = "java -Xmx512m -Djava.security.egd=file:/dev/./urandom -jar /data/apps/jar/signature-service.jar --spring.profiles.active=prod --server.port=9007 --spring.config.location=/data/apps/config/signature/";
    
    /**
     * @return
     * true-使用签名服务器
     * false-使用签名密码器
     */
    private boolean isSignService () {
    	String sign_type = systemFeginService.getByKey(Constants.SIGNATURE_TYPE);
    	if(StringUtils.isNotBlank(sign_type) && sign_type.equals("0")) {
    		return false;
    	}
    	return true;
    }
    /**
     * 重试次数过后还是没有成功则重启签名服务
     * @param e
     * @return
     */
    @Recover
    public Object recover(RuntimeException e) {
    	if(e instanceof SignException) {
    		log.info("正在重启签名服务器!");
    		String execForStr = RuntimeUtil.execForStr(STOP_SIGN_CMD,START_SIGN_CMD);
    		log.info(execForStr);
    	}else if(e instanceof VerifySignException) {
    		return false;
    	}
        return null;
    }
    
    @Override
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 1 ))
    public String getSign(byte[] data) {
        String xmlSign = "";
        //synchronized (YingJGBCALLDLL.class) {
            // 打开密码机 , 0 ：代表SJJ1507密码机 , 1:代表SJJ1313密码器
            //openDevice(deviceType);
            //byte[] data = readFile(xmlFile);
            try {
                xmlSign = YingJGBCALLDLL.platformCalculateSignature(dataType, data);
                log.info("getSign  sign="+ xmlSign);
                xmlSign = DatatypeConverter.printHexBinary(DatatypeConverter.parseBase64Binary(xmlSign));
                log.info("xmlSign:"+xmlSign);
            } catch (YJException e) {
                log.error("getSign failed. ", e);
                throw new SignException("签名失败,正在重试,重试3次任然失败则重启签名服务！");
            }
            // 关闭密码机
            //closeDevice();
            return xmlSign;
        //}
    }
    
    @Override
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 1 ))
    public String getFileSign(byte[] data) {
        String xmlSign = "";
        //synchronized (YingJGBCALLDLL.class) {
            // 打开密码机 , 0 ：代表SJJ1507密码机 , 1:代表SJJ1313密码器
            //openDevice(deviceType);
            //byte[] data = readFile(xmlFile);
            try {
                xmlSign = YingJGBCALLDLL.platformCalculateSignature(dataType, data);
                log.info("getSign  sign="+ xmlSign);
                log.info("xmlSign:"+xmlSign);
            } catch (YJException e) {
                log.error("getFileSign failed. ", e);
                throw new SignException("签名失败,正在重试,重试3次任然失败则重启签名服务！");
            }
            // 关闭密码机
            //closeDevice();
            return xmlSign;
        //}
    }
    
    /**
     * 验证签名
     *
     * @return boolean
     * @throws YJException 
     */
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 1 ))
    public  Boolean verifySign(byte[] data, String signValue) throws YJException {
        try {
			boolean result = false;
			//synchronized (YingJGBCALLDLL.class) {
			// 打开密码机
			//openDevice(deviceType);
			//byte[] data = readFile(xmlFile);
			// 验证数据签名
			signValue = DatatypeConverter.printBase64Binary(DatatypeConverter.parseHexBinary(signValue));
			result = YingJGBCALLDLL.platformVerifySignature(dataType, data, signValue);
			log.info(" 验证签名结果  :{}" ,result);
			// 关闭密码机
			//closeDevice();
			 return result;
			//}
		} catch (Exception e) {
			 log.error("verifySign  failed. ", e);
			 throw new VerifySignException("解签失败,正在重试,重试3次任然失败则重启签名服务！");
		}
    }
    
    @Override
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 1 ))
    public  Boolean verifyFileSign(byte[] data, String signValue) throws YJException {
        try {
			boolean result = false;
			//synchronized (YingJGBCALLDLL.class) {
			// 打开密码机
			//openDevice(deviceType);
			//byte[] data = readFile(xmlFile);
			// 验证数据签名
			result = YingJGBCALLDLL.platformVerifySignature(dataType, data, signValue);
			log.info(" 验证签名结果  :{}" ,result);
			// 关闭密码机
			//closeDevice();
			return result;
			//}
		} catch (Exception e) {
			log.error("verifyFileSign  failed. ", e);
			 throw new VerifySignException("解签失败,正在重试,重试3次任然失败则重启签名服务！");
		}
    }
    /**
     * 打开密码机
     *
     * @param flag
     */
    private static void openDevice(int flag) {
        try {
            // 打开sjj1507密码机
            synchronized (YingJGBCALLDLL.class) {
                YingJGBCALLDLL.openDevice(flag);
            }
        } catch (Exception e) {
            log.error("Signature helper close device failed.", e);
        }
    }

    /**
     * 关闭密码机
     */
    private static void closeDevice() {
        try {
            // 关闭sjj1507密码机
            YingJGBCALLDLL.closeDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	@Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 1 ))
	public CertResp certApply(CertApplyReq certApplyReq) {
		CertResp certResp = new CertResp();
		String listStr = null;
		List<String> paraseCert = null;
		try {
			certApplyReq.getSourceCertNos().add(platFormCert);
			StringBuffer reqUrl = new StringBuffer(certApplyUrl);
			reqUrl.append("?path=");
			for (String sourceCertNo : certApplyReq.getSourceCertNos()) {
				reqUrl.append(sourceCertNo).append(".");
			}
			reqUrl.deleteCharAt(reqUrl.length() - 1);
			reqUrl.append("&opt=").append("reg");
			reqUrl.append("&SMSN=").append(certApplyReq.getDestCertNo());
			
			RestTemplate restTemplate = new RestTemplate();
			String message = restTemplate.getForObject(reqUrl.toString(), String.class);
			paraseCert = paraseCert(message);
			log.info("向安全代理获取信任列表结束，params:{},result:{}",JSON.toJSONString(certApplyReq),message);
			if(StringUtils.isEmpty(message)) {
				return certResp;
			}
			listStr = paraseToMap(message).get("LIST");
			if(StringUtils.isBlank(listStr)) {
				return certResp;
			}
		} catch (RestClientException e) {
			log.error("向安全代理获取信任列表异常，params:{},result:{}",JSON.toJSONString(certApplyReq));
			e.printStackTrace();
			throw new SignException("获取信任列表失败,正在重试,重试3次任然失败则重启签名服务！");
		}
		
		String data = DatatypeConverter.printHexBinary(DatatypeConverter.parseBase64Binary(listStr));
		certResp.setCertauth(data);
		certResp.setCerth(paraseCert);
		return certResp;
	}
	
	private List<String> paraseCert(String message) {
		int indexOf = message.indexOf(",<");
		String certStr = message.substring(indexOf+1);
		List<String> tagContent = getTagContent(certStr,"CertCtx");
		return tagContent;
	}
	  /**
     * @param source 要匹配的源文本 
     * @param element 标签名称 
     * @return  内容集合
     */
    public static List<String> getTagContent(String source, String element) {
        List<String> result = new ArrayList<String>();
        String reg = "<" + element + ">" + "(.+?)</" + element + ">";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }
    
	private  Map<String,String> paraseToMap(String message){
		Map<String, String> resultMap = Maps.newHashMap();
		String[] split = message.split(",");
		if(split==null||split.length<2) {
			return resultMap;
		}
		for(int x=0;x<split.length;x++) {
			if(split[x].split("=").length>1)
			  resultMap.put(split[x].split("=")[0], split[x].split("=")[1]);
		}
		return resultMap;
	}


	@Override
	public Boolean importCerth(byte[] certh) throws YJException {
		
		return YingJGBCALLDLL.platformImportTrustedCert(certh);
	}

	@Override
	public Boolean importCertauth(byte[] certauth) throws YJException {
		
		return YingJGBCALLDLL.platformImportTrustedCertSnChain(certauth);
	}
	

	@Override
	public void destroy() throws Exception {
		if(isSignService()) {
			closeDevice();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(isSignService()) {
			openDevice(deviceType);
		}
	}
}
