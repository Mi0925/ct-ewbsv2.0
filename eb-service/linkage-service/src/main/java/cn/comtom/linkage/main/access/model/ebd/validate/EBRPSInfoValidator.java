package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRPS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRPSInfo;

public class EBRPSInfoValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRPSInfo.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRPSInfo psInfo = (EBRPSInfo) entity;
        if(null != psInfo.getDataList() && psInfo.getDataList().size() > 0) {
        	String ebrPsErrMsg = validEBRPS(psInfo.getDataList());
        	if(null != ebrPsErrMsg) {
        		return ebrPsErrMsg;
        	}
        }
        
        return null;
	}

	private String validEBRPS(List<EBRPS> dataList) {
		String ebrPsErrMsg = null;
		for(EBRPS ps : dataList) {
			if(null == ps.getRptTime()) {
				ebrPsErrMsg = "数据操作（生成）时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(ps.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrPsErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(ps.getRptType())) {
				ebrPsErrMsg = "数据操作类型不能为空";
				break;
			}
			if(null != ps.getRelatedEBRPS() && StringUtils.isEmpty(ps.getRelatedEBRPS().getEBRID())) {
				ebrPsErrMsg = "关联应急广播平台ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getEBRID())) {
				ebrPsErrMsg = "应急广播平台ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getEBRName())) {
				ebrPsErrMsg = "应急广播平台名称不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getAddress())) {
				ebrPsErrMsg = "应急广播平台地址不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getContact())) {
				ebrPsErrMsg = "应急广播平台联系人不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getPhoneNumber())) {
				ebrPsErrMsg = "应急广播平台联系电话不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getLongitude())) {
				ebrPsErrMsg = "应急广播平台经度不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getLatitude())) {
				ebrPsErrMsg = "应急广播平台纬度不能为空";
				break;
			}
			if(StringUtils.isEmpty(ps.getURL())) {
				ebrPsErrMsg = "应急广播平台网络地址不能为空";
				break;
			}
		}
		
		return ebrPsErrMsg;
	}

}
