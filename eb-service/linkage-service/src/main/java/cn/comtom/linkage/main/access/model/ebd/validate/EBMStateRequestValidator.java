package cn.comtom.linkage.main.access.model.ebd.validate;

import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateRequest;

public class EBMStateRequestValidator implements Validator {

	public String validateEntity(Object entity) {
        if(!EBMStateRequest.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBMStateRequest req = (EBMStateRequest) entity;
        if(null == req.getEBM()) {
        	return "所查询的应急广播消息不能为空";
        } else {
        	if(StringUtils.isEmpty(req.getEBM().getEBMID())) {
        		return "所查询的应急广播消息ID不能为空";
        	}
        }
        
		return null;
	}

}
