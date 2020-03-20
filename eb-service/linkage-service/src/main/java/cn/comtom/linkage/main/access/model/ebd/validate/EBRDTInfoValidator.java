package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRDT;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRDTInfo;

public class EBRDTInfoValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRDTInfo.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRDTInfo dtInfo = (EBRDTInfo) entity;
        if(null != dtInfo.getDataList() && dtInfo.getDataList().size() > 0) {
        	String ebrDtErrMsg = validEBRDT(dtInfo.getDataList());
        	if(null != ebrDtErrMsg) {
        		return ebrDtErrMsg;
        	}
        }
        
		return null;
	}

	private String validEBRDT(List<EBRDT> dataList) {
		String ebrDtErrMsg = null;
		for(EBRDT dt : dataList) {
			if(null == dt.getRptTime()) {
				ebrDtErrMsg = "数据操作（生成）时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(dt.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrDtErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(dt.getRptType())) {
				ebrDtErrMsg = "数据操作类型不能为空";
				break;
			}
			if(null != dt.getRelatedEBRPS() && StringUtils.isEmpty(dt.getRelatedEBRPS().getEBRID())) {
				ebrDtErrMsg = "关联应急广播平台ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(dt.getEBRID())) {
				ebrDtErrMsg = "设备ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(dt.getEBRName())) {
				ebrDtErrMsg = "设备名称不能为空";
				break;
			}
			if(StringUtils.isEmpty(dt.getLongitude())) {
				ebrDtErrMsg = "经度不能为空";
				break;
			}
			if(StringUtils.isEmpty(dt.getLatitude())) {
				ebrDtErrMsg = "纬度不能为空";
				break;
			}
		}
		return ebrDtErrMsg;
	}

}
