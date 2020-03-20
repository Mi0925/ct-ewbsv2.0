package cn.comtom.linkage.main.access.model.ebd.validate;

import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;

public class OMDRequestValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!OMDRequest.class.equals(entity.getClass())) {
        	return null;
        }
        
        OMDRequest omdRequest = (OMDRequest) entity;
        if(StringUtils.isEmpty(omdRequest.getOMDType())) {
        	return "运维数据类型不能为空";
        } else {
        	if(!ValidatorHelper.validOMDType(omdRequest.getOMDType())) {
        		return "运维数据类型系统暂不支持";
        	}
        }
        
		return null;
	}
	
	

}
