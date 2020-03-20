package cn.comtom.linkage.main.access.model.ebd.validate;

import cn.comtom.linkage.main.access.model.ebd.details.other.EBDResponse;
import org.apache.commons.lang.StringUtils;


public class EBDResponseValidator implements Validator {
	
	public String validateEntity(Object entity) {
        if(!EBDResponse.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBDResponse ebdRes = (EBDResponse) entity;
        if(null == ebdRes.getResultCode()) {
        	return "执行结果代码不能为空";
        } else {
        	if(!cn.comtom.linkage.main.access.model.ebd.validate.ValidatorHelper.validResultCode(ebdRes.getResultCode())) {
        		return "执行结果代码系统暂不支持";
        	}
        }
        
        if(StringUtils.isEmpty(ebdRes.getResultDesc())) {
        	return "执行结果描述不能为空";
        }
        
		return null;
	}

}
