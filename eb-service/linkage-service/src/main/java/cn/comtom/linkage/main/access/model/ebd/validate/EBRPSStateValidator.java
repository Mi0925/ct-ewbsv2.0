package cn.comtom.linkage.main.access.model.ebd.validate;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRPSState;

public class EBRPSStateValidator implements Validator {
	public String validateEntity(Object entity) {
		if(!EBRPSState.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRPSState psState = (EBRPSState) entity;
        if(null == psState.getDataList()) {
        	return "应急广播平台条目不能为空";
        } else {
        	String ebrPsErrMsg = null;
        	for(EBRPS ps : psState.getDataList()) {
        		if(null == ps.getRptTime()) {
        			ebrPsErrMsg = "数据操作（生成）时间不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.isDate(ps.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        				ebrPsErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        				break;
        			}
        		}
        		if(StringUtils.isEmpty(ps.getEBRID())) {
        			ebrPsErrMsg = "应急广播平台ID不能为空";
        			break;
        		}
        		if(null == ps.getStateCode()) {
        			ebrPsErrMsg = "状态代码不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.validResStateCode(ps.getStateCode())) {
            			ebrPsErrMsg = "状态代码系统暂不支持";
            			break;
        			}
        		}
        		if(StringUtils.isEmpty(ps.getStateDesc())) {
        			ebrPsErrMsg = "应急广播平台状态的详细描述不能为空";
        			break;
        		}
        	}
        	
        	if(null != ebrPsErrMsg) {
        		return ebrPsErrMsg;
        	}
        }
        
		return null;
	}

}
