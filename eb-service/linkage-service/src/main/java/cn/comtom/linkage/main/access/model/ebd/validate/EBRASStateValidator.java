package cn.comtom.linkage.main.access.model.ebd.validate;


import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRASState;

public class EBRASStateValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRASState.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRASState asState = (EBRASState) entity;
        if(null == asState.getDataList()) {
        	return "消息接收设备条目不能为空";
        } else {
        	String ebrAsErrMsg = null;
        	for(EBRAS as : asState.getDataList()) {
        		if(null == as.getRptTime()) {
        			ebrAsErrMsg = "数据操作（生成）时间不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.isDate(as.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        				ebrAsErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        				break;
        			}
        		}
        		if(StringUtils.isEmpty(as.getEBRID())) {
        			ebrAsErrMsg = "消息接收设备ID不能为空";
        			break;
        		}
        		if(null == as.getStateCode()) {
        			ebrAsErrMsg = "状态代码不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.validResStateCode(as.getStateCode())) {
            			ebrAsErrMsg = "状态代码系统暂不支持";
            			break;
        			}
        		}
        		if(StringUtils.isEmpty(as.getStateDesc())) {
        			ebrAsErrMsg = "消息接收设备状态的详细描述不能为空";
        			break;
        		}
        	}
        	
        	if(null != ebrAsErrMsg) {
        		return ebrAsErrMsg;
        	}
        }
        
		return null;
	}

}
