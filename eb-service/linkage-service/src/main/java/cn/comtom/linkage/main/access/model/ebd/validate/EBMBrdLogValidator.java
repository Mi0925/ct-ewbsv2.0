package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMBrdLog;
import cn.comtom.linkage.main.access.model.ebd.ebm.EBMBrdItem;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent;
import cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem;
import cn.comtom.linkage.main.access.model.ebd.ebm.Unit;

public class EBMBrdLogValidator implements Validator {

	public String validateEntity(Object entity) {
        if(!EBMBrdLog.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBMBrdLog brdLog = (EBMBrdLog) entity;
        if(null != brdLog.getParams()) {
        	if(null == brdLog.getParams().getRptStartTime()) {
        		return "数据起始时间不能为空";
        	} else {
        		if(!ValidatorHelper.isDate(brdLog.getParams().getRptStartTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        			return "数据起始时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        		}
        	}
        	if(null == brdLog.getParams().getRptEndTime()) {
        		return "数据结束时间不能为空";
        	} else {
        		if(!ValidatorHelper.isDate(brdLog.getParams().getRptEndTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        			return "数据结束时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        		}
        	}
        }
        
        if(null != brdLog.getDataList() && brdLog.getDataList().size() > 0) {
        	String ebmBrdItemErrMsg = validEBMBrdItem(brdLog.getDataList());
        	if(null != ebmBrdItemErrMsg) {
        		return ebmBrdItemErrMsg;
        	}
        }
        
        
		return null;
	}
	
	private String validEBMBrdItem(List<EBMBrdItem> dataList) {
		String ebmBrdItemErrMsg = null;
		for(EBMBrdItem brdItm : dataList) {
			if(null == brdItm.getEBM()) {
				ebmBrdItemErrMsg = "所播发的应急广播消息不能为空";
				break;
			} else {
				ebmBrdItemErrMsg = validEBM(brdItm.getEBM());
                if(null != ebmBrdItemErrMsg) {
                	break;
                }
			}
			if(null != brdItm.getUnitInfo() && null != brdItm.getUnitInfo().getDataList() && brdItm.getUnitInfo().getDataList().size() > 0) {
				ebmBrdItemErrMsg = validUnit(brdItm.getUnitInfo().getDataList());
                if(null != ebmBrdItemErrMsg) {
                	break;
                }
			}
			if(null == brdItm.getBrdStateCode()) {
				ebmBrdItemErrMsg = "播发状态代码不能为空";
				break;
			} else {
				if(!ValidatorHelper.validBroadcastState(brdItm.getBrdStateCode())) {
					ebmBrdItemErrMsg = "播发状态代码系统暂不支持";
					break;
				}
			}
			if(StringUtils.isEmpty(brdItm.getBrdStateDesc())) {
				ebmBrdItemErrMsg = "播发状态描述不能为空";
				break;
			}
			if(null != brdItm.getCoverage()) {
				if(null == brdItm.getCoverage().getCoverageRate()) {
					ebmBrdItemErrMsg= "实际覆盖率不能为空";
				}
				/*if(StringUtils.isEmpty(brdItm.getCoverage().getAreaCode())) {
					ebmBrdItemErrMsg= "实际覆盖区域编码不能为空";
				}*/
				if(StringUtils.isEmpty(brdItm.getCoverage().getResBrdStat())){
					ebmBrdItemErrMsg= "实际调用资源响应统计不能为空";
				}
			}
			if(null != brdItm.getResBrdInfo() && null != brdItm.getResBrdInfo().getDataList() && brdItm.getResBrdInfo().getDataList().size() > 0) {
				ebmBrdItemErrMsg = validResBrdItem(brdItm.getResBrdInfo().getDataList());
				if(null != ebmBrdItemErrMsg) {
					break;
				}
			}
		}
		
		return ebmBrdItemErrMsg;
	}

	private String validEBM(EBM ebm) {
		if(StringUtils.isEmpty(ebm.getEBMID())) {
			return "所播发的应急广播消息 ID不能为空";
		}
		if(null != ebm.getMsgBasicInfo()) {
            String errorMsg = validateMsgBasicInfo(ebm.getMsgBasicInfo());
            if(null != errorMsg) {
           	 return errorMsg;
            }
		}
        if(null == ebm.getMsgContent()) {
        	return "应急广播消息内容不能为空";
        } else {
            String errorMsg = validateMsgContent(ebm.getMsgContent());
            if(null != errorMsg) {
	           	return errorMsg;
            }
        }
		
		return null;
	}
	
	private String validateMsgBasicInfo(MsgBasicInfo msgBasicInfo) {
    	Integer msgType = msgBasicInfo.getMsgType();
    	if(null == msgType) {
    		return "消息类型不能为空";
    	} else {
        	if(!ValidatorHelper.validEBMBasicMsgType(msgType)) {
        		return "消息类型系统暂不支持";
        	}
    	}
    	
    	if(StringUtils.isEmpty(msgBasicInfo.getSenderName())) {
    		return "应急信息发布机构名称不能为空";
    	}
    	if(StringUtils.isEmpty(msgBasicInfo.getSenderCode())) {
    		return "应急信息发布机构编码不能为空"; 
    	}
    	if(null == msgBasicInfo.getSendTime()) {
    		return "应急广播消息发布时间不能为空";
    	} else {
    		if(!ValidatorHelper.isDate(msgBasicInfo.getSendTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
    			return "应急广播消息发布时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
    		}
    	}
    	if(StringUtils.isEmpty(msgBasicInfo.getEventType())) {
    		return "事件类型编码不能为空";
    	}
    	if(null == msgBasicInfo.getSeverity()) {
    		return "事件级别不能为空";
    	} else {
    		if(!ValidatorHelper.validSeverity(msgBasicInfo.getSeverity())) {
    			return "事件级别系统暂不支持";
    		}
    	}
    	if(null == msgBasicInfo.getStartTime()) {
    		return "播发开始时间不能为空";
    	} else {
    		if(!ValidatorHelper.isDate(msgBasicInfo.getStartTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
    			return "播发开始时间不符合要求 YYYY-MM-DD HH:MI:SS";
    		}
    	}
    	if(null == msgBasicInfo.getEndTime()) {
    		return "播发结束时间不能为空";
    	} else {
    		if(!ValidatorHelper.isDate(msgBasicInfo.getEndTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
    			return "播发结束时间不符合要求 YYYY-MM-DD HH:MI:SS";
    		}
    	}
    	
    	return null;
	}
	
	private String validateMsgContent(MsgContent msgContent) {
		if(StringUtils.isEmpty(msgContent.getLanguageCode())) {
			return "应急广播文本内容的语种代码不能为空";
		}
		if(StringUtils.isEmpty(msgContent.getMsgTitle())) {
			return "应急信息标题文本不能为空";
		}
		if(StringUtils.isEmpty(msgContent.getMsgDesc())) {
			return "应急信息内容文本不能为空";
		}
		if(StringUtils.isEmpty(msgContent.getAreaCode())) {
			return "应急广播消息的覆盖区域不能为空";
		}
		
		return null;
	}
	
	private String validUnit(List<Unit> dataList) {
		String unitErrMsg = null;
        for(Unit unt : dataList) {
        	if(null == unt.getEBRPS()) {
        		unitErrMsg = "播出部门所对应的应急广播平台信息不能为空";
        		break;
        	} else {
        		if(StringUtils.isEmpty(unt.getEBRPS().getEBRID())) {
        			unitErrMsg = "播出部门所对应的应急广播平台 ID不能为空";
        			break;
        		}
        	}
        	if(StringUtils.isEmpty(unt.getUnitId())) {
        		unitErrMsg = "播发部门ID不能为空";
        		break;
        	}
        	if(StringUtils.isEmpty(unt.getUnitName())) {
        		unitErrMsg = "播发部门名称不能为空";
        		break;
        	}
        	if(StringUtils.isEmpty(unt.getPersonID())) {
        		unitErrMsg = "播发人员ID不能为空";
        		break;
        	}
        	if(StringUtils.isEmpty(unt.getPersonName())) {
        		unitErrMsg = "播发人员姓名不能为空";
        		break;
        	}
        }
		return unitErrMsg;
	}
	
	private String validResBrdItem(List<ResBrdItem> dataList) {
		String resBrdItemErrMsg = null;
		for(ResBrdItem brdItm : dataList) {
			if(null != brdItm.getEBRPS() && StringUtils.isEmpty(brdItm.getEBRPS().getEBRID())) {
				resBrdItemErrMsg = "应急广播平台ID不能为空";
				break;
			}
			if(null != brdItm.getEBRST() && StringUtils.isEmpty(brdItm.getEBRST().getEBRID())) {
				resBrdItemErrMsg = "台站（前端）ID不能为空";
				break;
			}
			if(null != brdItm.getEBRAS() && StringUtils.isEmpty(brdItm.getEBRAS().getEBRID())) {
				resBrdItemErrMsg = "消息接收设备ID不能为空";
				break;
			}
			if(null == brdItm.getDataList() || brdItm.getDataList().size() < 1) {
				resBrdItemErrMsg = "调用播出系统播出情况不能为空";
				break;
			} else {
				resBrdItemErrMsg = validEBRBS(brdItm.getDataList());
				if(null != resBrdItemErrMsg) {
					break;
				}
			}
		}
		
		return resBrdItemErrMsg;
	}
	
	private String validEBRBS(List<EBRBS> dataList) {
		String ebrbsErrMsg = null;
		for(EBRBS bs : dataList) {
			if(null == bs.getRptTime()) {
				ebrbsErrMsg = "当前数据的生成时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(bs.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "当前数据的生成时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
//			if(StringUtils.isEmpty(bs.getBrdSysType())) {
//				ebrbsErrMsg = "播出系统类型不能为空";
//				break;
//			}
			if(StringUtils.isEmpty(bs.getBrdSysInfo())) {
				ebrbsErrMsg = "播出系统信息不能为空";
				break;   						
			}
			if(null == bs.getStartTime()) {
				ebrbsErrMsg = "播发起始时间不能为空";
				break;					
			} else {
				if(!ValidatorHelper.isDate(bs.getStartTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "播发起始时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(null == bs.getEndTime()) {
				ebrbsErrMsg = "播发结束时间不能为空";
				break;					
			} else {
				if(!ValidatorHelper.isDate(bs.getEndTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "播发结束时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(bs.getFileURL())) {
				ebrbsErrMsg = "播发录音文件地址不能为空";
				break;
			}
			if(null == bs.getBrdStateCode()) {
				ebrbsErrMsg = "播发状态代码不能为空";
				break;
			} else {
				if(!ValidatorHelper.validBroadcastState(bs.getBrdStateCode())) {
					ebrbsErrMsg = "播发状态代码系统暂不支持";
					break;
				}
			}
			if(StringUtils.isEmpty(bs.getBrdStateDesc())) {
				ebrbsErrMsg = "播发状态描述不能为空";
				break;
			}
		}
		
		return ebrbsErrMsg;
	}

}
