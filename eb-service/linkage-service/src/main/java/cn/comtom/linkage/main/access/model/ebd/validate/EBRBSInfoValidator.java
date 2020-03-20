package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.commom.Switch;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRBSInfo;

public class EBRBSInfoValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBRBSInfo.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBRBSInfo bsInfo = (EBRBSInfo) entity;
        if(null != bsInfo.getDataList() && bsInfo.getDataList().size() > 0) {
        	String ebrBsErrMsg = validEBRBS(bsInfo.getDataList());
        	if(null != ebrBsErrMsg) {
        		return ebrBsErrMsg;
        	}
        }
        
		return null;
	}

	private String validEBRBS(List<EBRBS> dataList) {
		String ebrbsErrMsg = null;
		for(EBRBS bs : dataList) {
			if(null == bs.getRptTime()) {
				ebrbsErrMsg = "数据操作（生成）时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(bs.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "数据操作（生成）时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(bs.getRptType())) {
				ebrbsErrMsg = "数据操作类型不能为空";
				break;
			}
			if(null != bs.getRelatedEBRPS() && StringUtils.isEmpty(bs.getRelatedEBRPS().getEBRID())) {
				ebrbsErrMsg = "关联应急广播平台ID不能为空";
				break;
			}
			if(null != bs.getRelatedEBRST() && StringUtils.isEmpty(bs.getRelatedEBRST().getEBRID())) {
				ebrbsErrMsg = "关联台站（前端）ID不能为空";
				break;
			}
			if(null != bs.getRelatedEBRAS() && StringUtils.isEmpty(bs.getRelatedEBRAS().getEBRID())) {
				ebrbsErrMsg = "关联消息接收设备ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getEBRID())) {
				ebrbsErrMsg = "播出系统 ID不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getEBRName())) {
				ebrbsErrMsg = "播出系统名称不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getLongitude())) {
				ebrbsErrMsg = "经度不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getLatitude())) {
				ebrbsErrMsg = "纬度不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getSquare())) {
				ebrbsErrMsg = "覆盖面积不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getAreaCode())) {
				ebrbsErrMsg = "播出系统所覆盖的行政区域不能为空";
				break;
			}
			if(null == bs.getPopulation()) {
				ebrbsErrMsg = "播出系统覆盖人口不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getLanguageCode())) {
				ebrbsErrMsg = "原播语种不能为空";
				break;
			}
			if(StringUtils.isEmpty(bs.getEquipRoom())) {
				ebrbsErrMsg = "机房名称不能为空";
				break;
			}
//			if(null != bs.getRadioParams()) {
//				if(StringUtils.isEmpty(bs.getRadioParams().getChannelName())) {
//					ebrbsErrMsg = "模拟广播（发射机）频道名称不能为空";
//					break;
//				}
//				if(null == bs.getRadioParams().getFreq()) {
//					ebrbsErrMsg = "模拟广播（发射机）频道频率不能为空";
//					break;
//				}
//				if(null == bs.getRadioParams().getPower()) {
//					ebrbsErrMsg = "模拟广播（发射机）发射功率不能为空";
//					break;
//				}
//				if(null == bs.getRadioParams().getBackup()) {
//					ebrbsErrMsg = "模拟广播（发射机）必须标识是否备机";
//					break;
//				}
//				if(null == bs.getRadioParams().getAutoSwitch()) {
//					ebrbsErrMsg = "模拟广播（发射机）必须标识是否自动切换";
//					break;
//				}
//				if(null == bs.getRadioParams().getRemoteControl()) {
//					ebrbsErrMsg = "模拟广播（发射机）必须标识能否遥控开机";
//					break;
//				}
//				if(null == bs.getRadioParams().getExperiment()) {
//					ebrbsErrMsg = "模拟广播（发射机）必须标识是实验还是覆盖发射";
//					break;
//				}
//			}
//			if(null != bs.gettVParams()) {
//				if(StringUtils.isEmpty(bs.gettVParams().getChannelName())) {
//					ebrbsErrMsg = "数字电视频道名称不能为空";
//					break;
//				}
//				if(null == bs.gettVParams().getFreq()) {
//					ebrbsErrMsg = "数字电视频道频率不能为空";
//					break;
//				}
//				if(StringUtils.isEmpty(bs.gettVParams().getProgramNum())) {
//					ebrbsErrMsg = "数字电视节目号不能为空";
//					break;
//				}
//				if(StringUtils.isEmpty(bs.gettVParams().getChannelName())) {
//					ebrbsErrMsg = "数字电视频道号不能为空";
//					break;
//				}
//			}
//			if(null != bs.getSchedule() && null != bs.getSchedule().getDataList() && bs.getSchedule().getDataList().size() > 0) {
//				ebrbsErrMsg = validSwitch(bs.getSchedule().getDataList());
//				if(null != ebrbsErrMsg) {
//					break;
//				}
//			}
		}
		
		return ebrbsErrMsg;
	}
	
	private String validSwitch(List<Switch> dataList) {
		String scheduleErrMsg = null;
		
		for(Switch swch : dataList) {
			if(null == swch.getWeekday()) {
				scheduleErrMsg = "开关机时间中星期数不能为空";
				break;
			}
			if(null == swch.getStartTime()) {
				scheduleErrMsg = "开机时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(swch.getStartTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()) 
						&& !ValidatorHelper.isDate(swch.getStartTime(), DateStyle.HH_MM_SS.getValue())) {
					scheduleErrMsg = "开机时间格式不符合要求YYYY-MM-DD HH:MI:SS或者HH:MI:SS";
					break;
				}
			}
			if(null == swch.getEndTime()) {
				scheduleErrMsg = "关机时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(swch.getEndTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())
						&& !ValidatorHelper.isDate(swch.getEndTime(), DateStyle.HH_MM_SS.getValue())) {
					scheduleErrMsg = "关机时间格式不符合要求YYYY-MM-DD HH:MI:SS或者HH:MI:SS";
					break;
				}
			}
		}
		
		return scheduleErrMsg;
	}

}
