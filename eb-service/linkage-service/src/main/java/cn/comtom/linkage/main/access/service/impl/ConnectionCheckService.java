package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.ConnectionCheck;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.common.service.ICommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author nobody
 * 心跳检测处理
 */
@Service
@Slf4j
public class ConnectionCheckService extends AbstractEMDService{

	@Autowired
	private ICommonService commonService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public String serviceType() {
		return EBDType.ConnectionCheck.name();
	}

	
	@Override
	public void preservice(EBD ebd, String ebdName, List<File> resourceFiles) {
		//心跳频繁,不记录接收数据包
		if(log.isDebugEnabled()){
			log.debug("$=: ############# 心跳频繁,不记录接收数据包");
		}
	}
	
	@Override
	public void afterservice(EBD ebd, String ebdName) {
		//心跳频繁,不记录发送数据包
		if(log.isDebugEnabled()){
			log.debug("$=: @@@@@@@@@@@@@@ 心跳频繁,不记录发送数据包");
		}
	}
	
	@Override
	public void service(EBD ebd,List<File> resourceFiles) {
		SRC src=ebd.getSRC();
		//必选资源ID
		String ebrId=src.getEBRID();
		//心跳处理
		ConnectionCheck check=ebd.getConnectionCheck();
		String rptTime=check.getRptTime();
		String key = commonService.getConnectionCkeckKey(ebrId);
		if(log.isDebugEnabled()){
			log.debug("#############          接收到心跳包，解析并保存到redis中            ##############");
			log.debug("redisKey =[{}]  , rptTime = [{}] ",key,rptTime);

		}
		stringRedisTemplate.opsForValue().set(key,rptTime);
		if(log.isDebugEnabled()){
			log.debug("#############          解析并保存到redis中完成            ##############");
			log.debug("value=[{}] ",stringRedisTemplate.opsForValue().get(key));
		}
	}
}
