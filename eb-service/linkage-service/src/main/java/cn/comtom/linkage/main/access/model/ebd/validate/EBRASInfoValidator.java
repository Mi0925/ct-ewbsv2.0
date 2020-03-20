package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRAS;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRASInfo;

public class EBRASInfoValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRASInfo.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRASInfo asInfo = (EBRASInfo) entity;
        if(null != asInfo.getDataList() && asInfo.getDataList().size() > 0) {
        	String ebrAsErrMsg = validEBRAS(asInfo.getDataList());
        	if(null != ebrAsErrMsg) {
        		return ebrAsErrMsg;
        	}
        }
        
		return null;
	}

	private String validEBRAS(List<EBRAS> dataList) {
		String ebrAsErrMsg = null;
		for(EBRAS as : dataList) {
			if(null == as.getRptTime()) {
				ebrAsErrMsg = "数据操作（生成）时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(as.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrAsErrMsg = "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(as.getRptType())) {
				ebrAsErrMsg = "数据操作类型不能为空";
				break;
			}
			if(StringUtils.isEmpty(as.getEBRID())) {
				ebrAsErrMsg = "消息接收设备ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(as.getEBRName())) {
				ebrAsErrMsg = "消息接收设备名称不能为空";
				break;
			}
			if(StringUtils.isEmpty(as.getLongitude())) {
				ebrAsErrMsg = "经度不能为空";
				break;
			}
			if(StringUtils.isEmpty(as.getLatitude())) {
				ebrAsErrMsg = "纬度不能为空";
				break;
			}
			if(StringUtils.isEmpty(as.getURL())) {
				ebrAsErrMsg = "消息设备设备的网络地址不能为空";
				break;
			}
			if(null != as.getRelatedEBRPS() && StringUtils.isEmpty(as.getRelatedEBRPS().getEBRID())) {
				ebrAsErrMsg = "关联应急广播平台ID不能为空";
				break;
			}
			if(null != as.getRelatedEBRST() && StringUtils.isEmpty(as.getRelatedEBRST().getEBRID())) {
				ebrAsErrMsg = "关联台站（前端）ID不能为空";
				break;
			}
		}
		
		return ebrAsErrMsg;
	}

}
