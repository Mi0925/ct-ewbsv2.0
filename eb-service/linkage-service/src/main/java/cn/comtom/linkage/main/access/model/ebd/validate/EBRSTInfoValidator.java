package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRST;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRSTInfo;

public class EBRSTInfoValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRSTInfo.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRSTInfo stInfo = (EBRSTInfo) entity;
        if(null != stInfo.getDataList() && stInfo.getDataList().size() > 0) {
        	String ebrStErrMsg = validEBRST(stInfo.getDataList());
        	if(null != ebrStErrMsg) {
        		return ebrStErrMsg;
        	}
        }
        
        return null;
	}

	private String validEBRST(List<EBRST> dataList) {
		String ebrStErrMsg = null;
		
		for(EBRST st : dataList) {
			if(null == st.getRptTime()) {
				ebrStErrMsg = "数据操作（生成）时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(st.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrStErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(st.getRptType())) {
				ebrStErrMsg = "数据操作类型不能为空";
				break;
			}
			if(null != st.getRelatedEBRPS() && StringUtils.isEmpty(st.getRelatedEBRPS().getEBRID())) {
				ebrStErrMsg = "关联应急广播平台ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getEBRID())) {
				ebrStErrMsg = "台站（前端）ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getEBRName())) {
				ebrStErrMsg = "台站（前端）名称不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getAddress())) {
				ebrStErrMsg = "台站地址不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getContact())) {
				ebrStErrMsg = "台站联系人不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getPhoneNumber())) {
				ebrStErrMsg = "台站联系电话不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getLongitude())) {
				ebrStErrMsg = "台站经度不能为空";
				break;
			}
			if(StringUtils.isEmpty(st.getLatitude())) {
				ebrStErrMsg = "台站纬度不能为空";
				break;
			}
		}
		
		return ebrStErrMsg;
	}

}
