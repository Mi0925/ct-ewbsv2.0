package cn.comtom.linkage.main.access.service.impl;

/**
 * Create By wujiang on 2018/11/19
 */

import cn.comtom.linkage.commons.EBDRespResultEnum;
import cn.comtom.linkage.commons.EbmException;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.service.EMDservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nobody 应急数据包服务分发管理类
 */
@Service
public class EBMServiceManager implements BeanPostProcessor, DisposableBean, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, EMDservice> serviceMap = new HashMap<String, EMDservice>();

    /**
     * 分发服务
     *
     * @param ebd
     * @param resourceFiles
     */
    public void dispatchService(EBD ebd, String fileId, List<File> resourceFiles) {
        String serviceType = ebd.getEBDType();
        if (serviceMap.get(serviceType) == null) {
            throw new EbmException(EBDRespResultEnum.othererrors, serviceType + "没有对应的业务处理类");
        }
        serviceMap.get(serviceType).preservice(ebd, fileId, resourceFiles);//保存ebd
        serviceMap.get(serviceType).service(ebd, resourceFiles);//处理具体业务
    }

    /**
     * 处理服务结构
     *
     * @param ebd
     */
    public void dispatchAfterService(EBD ebd, String ebdName) {
        String serviceType = ebd.getEBDType();
        serviceMap.get(serviceType).afterservice(ebd, ebdName);
    }

    @Override
    public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {
        return arg0;
    }

    @Override
    public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
        if (arg0 instanceof EMDservice) {
            EMDservice emDservice = (EMDservice) arg0;
            serviceMap.put(emDservice.serviceType(), emDservice);
            logger.info("add service " + emDservice.serviceType());
        }
        return arg0;
    }

    @Override
    public void destroy() throws Exception {
        serviceMap.clear();
        serviceMap = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("init serviceMap:=" + serviceMap);
    }
}
