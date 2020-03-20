package cn.comtom.linkage.main.fegin.service.impl;

import cn.comtom.linkage.main.fegin.SignatureFegin;
import cn.comtom.linkage.main.fegin.service.FeginBaseService;
import cn.comtom.linkage.main.fegin.service.ISignatureFeginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SignatureFeginServiceImpl extends FeginBaseService implements ISignatureFeginService{

    @Autowired
    private SignatureFegin signatureFegin;

    @Value("${signature.service.gurl}")
    private String gsignatureUrl;

    @Value("${signature.service.vurl}")
    private String vsignatureUrl;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

	@Override
	public Boolean verifySign(File xmlFile, String signValue) {
		ServiceInstance choose = loadBalancerClient.choose("SIGNATURE-SERVICE");
		RestTemplate rest = new RestTemplate();
		FileSystemResource resource = new FileSystemResource(xmlFile);
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("xmlFile", resource);
		param.add("signValue",signValue);
		HttpHeaders headers = new HttpHeaders();
		// 需求需要传参为form-data格式
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
		Map postForObject = rest.postForObject(choose.getUri()+vsignatureUrl, httpEntity, Map.class);
		return (Boolean)postForObject.get("successful");
	}

	@Override
	public String getSign(File xmlFile) {
		RestTemplate rest = new RestTemplate();
		ServiceInstance choose = loadBalancerClient.choose("SIGNATURE-SERVICE");
		FileSystemResource resource = new FileSystemResource(xmlFile);
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("xmlFile", resource);
		HttpHeaders headers = new HttpHeaders();
		// 需求需要传参为form-data格式
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
		Map postForObject = rest.postForObject(choose.getUri()+gsignatureUrl, httpEntity, Map.class);
		return (String) postForObject.get("data");
	}



}
