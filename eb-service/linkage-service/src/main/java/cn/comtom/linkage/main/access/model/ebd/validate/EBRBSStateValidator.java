package cn.comtom.linkage.main.access.model.ebd.validate;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRBSState;

public class EBRBSStateValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRBSState.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRBSState bsState = (EBRBSState) entity;
        if(null == bsState.getDataList()) {
        	return "播出系统数据不能为空";
        } else {
        	String ebrBsErrMsg = null;
        	for(EBRBS bs : bsState.getDataList()) {
        		if(null == bs.getRptTime()) {
        			ebrBsErrMsg = "数据操作（生成）时间不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.isDate(bs.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        				ebrBsErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        				break;
        			}
        		}
        		if(StringUtils.isEmpty(bs.getEBRID())) {
        			ebrBsErrMsg = "播出系统ID不能为空";
        			break;
        		}
        		if(null == bs.getStateCode()) {
        			ebrBsErrMsg = "状态代码不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.validResStateCode(bs.getStateCode())) {
            			ebrBsErrMsg = "状态代码系统暂不支持";
            			break;
        			}
        		}
        		if(StringUtils.isEmpty(bs.getStateDesc())) {
        			ebrBsErrMsg = "播出系统状态的详细描述不能为空";
        			break;
        		}
        	}
        	
        	if(null != ebrBsErrMsg) {
        		return ebrBsErrMsg;
        	}
        }
        
		return null;
	}

}
