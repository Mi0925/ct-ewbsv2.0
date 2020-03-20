package cn.comtom.linkage.main.access.model.ebd.validate;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRDTState;

public class EBRDTStateValidator implements Validator {
	public String validateEntity(Object entity) {
		if(!EBRDTState.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRDTState dtState = (EBRDTState) entity;
        if(null == dtState.getDataList()) {
        	return "设备状态条目不能为空";
        } else {
        	String ebrDtErrMsg = null;
        	for(EBRDT dt : dtState.getDataList()) {
        		if(null == dt.getRptTime()) {
        			ebrDtErrMsg = "数据操作（生成）时间不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.isDate(dt.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        				ebrDtErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        				break;
        			}
        		}
        		if(StringUtils.isEmpty(dt.getEBRID())) {
        			ebrDtErrMsg = "平台设备及终端ID不能为空";
        			break;
        		}
        		if(null == dt.getStateCode()) {
        			ebrDtErrMsg = "状态代码不能为空";
        			break;
        		} else {
        			if(!ValidatorHelper.validResStateCode(dt.getStateCode())) {
            			ebrDtErrMsg = "状态代码系统暂不支持";
            			break;
        			}
        		}
        		if(StringUtils.isEmpty(dt.getStateDesc())) {
        			ebrDtErrMsg = "平台设备及终端状态的详细描述不能为空";
        			break;
        		}
        	}
        	
        	if(null != ebrDtErrMsg) {
        		return ebrDtErrMsg;
        	}
        }
        
		return null;
	}

}
