package cn.comtom.signature.main.fegin.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.signature.main.fegin.SystemFegin;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemFeginService extends FeginBaseService {

    @Autowired
    private SystemFegin systemFegin;
    
    public String getByKey(String paramKey) {
        try {
			SysParamsInfo data = getData(systemFegin.getByKey(paramKey));
			if(data!=null && StringUtils.isNotBlank(data.getParamValue())) {
				return data.getParamValue();
			}
		} catch (Exception e) {
			log.error("system-service不可用，无法获取签名配置，使用默认配置-签名服务器签名");
			return null;
		}
        return null;
    }
}
