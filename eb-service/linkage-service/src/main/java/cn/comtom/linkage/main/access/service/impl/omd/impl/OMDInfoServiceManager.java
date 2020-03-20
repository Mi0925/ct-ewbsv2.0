package cn.comtom.linkage.main.access.service.impl.omd.impl;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.service.impl.omd.OMDInfoService;


/**
 * Create By wujiang on 2018/12/04
 * 运维请求数据包服务分发管理类
 */
@Service
public class OMDInfoServiceManager implements BeanPostProcessor,DisposableBean,InitializingBean {
	
    private Logger logger = LoggerFactory.getLogger (getClass());
	
	private Map<String,OMDInfoService> serviceMap = new HashMap<String,OMDInfoService>();
	
	/**
	 * 分发服务
	 */
	public EBD dispatchService(String ebdId, OMDRequest omdRequest){
		String serviceType=omdRequest.getOMDType();
		return serviceMap.get(serviceType).service(ebdId,omdRequest);
	}

	
	@Override
	public Object postProcessAfterInitialization(Object arg0, String arg1)
			throws BeansException {
		return arg0;
	}

	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1)
			throws BeansException {
		if(arg0 instanceof OMDInfoService){
			OMDInfoService omdInfoService=(OMDInfoService) arg0;
			serviceMap.put(omdInfoService.OMDType(), omdInfoService);
			logger.info("add service "+omdInfoService.OMDType());
		}
		return arg0;
	}

	@Override
	public void destroy() throws Exception {
		serviceMap.clear();
		serviceMap=null;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("init serviceMap:="+serviceMap);
	}
	
}
