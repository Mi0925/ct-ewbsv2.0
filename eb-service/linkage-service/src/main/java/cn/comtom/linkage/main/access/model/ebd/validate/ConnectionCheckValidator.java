package cn.comtom.linkage.main.access.model.ebd.validate;


import cn.comtom.linkage.main.access.model.ebd.details.other.ConnectionCheck;
import cn.comtom.linkage.utils.DateStyle;

public class ConnectionCheckValidator implements Validator {

	public String validateEntity(Object entity) {
        if(!ConnectionCheck.class.equals(entity.getClass())) {
        	return null;
        }
        
        ConnectionCheck check = (ConnectionCheck) entity;
        if(null == check.getRptTime()) {
        	return "数据操作（生成）时间不能为空";
        } else {
        	if(!cn.comtom.linkage.main.access.model.ebd.validate.ValidatorHelper.isDate(check.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        		return "数据操作（生成）时间不符合要求 YYYY-MM-DD HH:MI:SS";
        	}
        }
        
		return null;
	}

}
