package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.commons.EBMType;
import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.ebm.Auxiliary;
import cn.comtom.linkage.main.access.model.ebd.ebm.Dispatch;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent;

public class EBMValidator implements Validator {
	public String validateEntity(Object entity) {
		if (!EBM.class.equals(entity.getClass())) {
			return null;
		}

		EBM ebmEnty = (EBM) entity;
		if (StringUtils.isEmpty(ebmEnty.getEBMVersion())) {
			return "应急广播消息协议版本号EBMVersion不能为空";
		}
		if (StringUtils.isEmpty(ebmEnty.getEBMID())) {
			return "应急广播消息 ID不能为空";
		}
		MsgBasicInfo msgBasicInfo = ebmEnty.getMsgBasicInfo();
		if(null == msgBasicInfo) {
			return "应急广播消息基本信息不能为空";
		}else{
			String errorMsg = validateMsgBasicInfo(msgBasicInfo);
			if (null != errorMsg) {
				return errorMsg;
			}
		}
		/*
		 * 如果是取消播发则不再校验其它信息
		 */
		if(EBMType.cancel.getValue().equals(msgBasicInfo.getMsgType())) {
			return null;
		}
		if (null == ebmEnty.getMsgContent()) {
			return "应急广播消息内容不能为空";
		} else {
			String errorMsg = validateMsgContent(ebmEnty.getMsgContent());
			if (null != errorMsg) {
				return errorMsg;
			}
		}
		
		//TODO:分发校验

		/*
		 * if(null != ebmEnty.getDispatchList() &&
		 * ebmEnty.getDispatchList().size() > 0) { String errorMsg =
		 * validateDispatchList(ebmEnty.getDispatchList()); if(null != errorMsg)
		 * { return errorMsg; } }
		 */

		return null;
	}

	/**
	 * @param msgBasicInfo
	 * @return
	 */
	private String validateMsgBasicInfo(MsgBasicInfo msgBasicInfo) {
		Integer msgType = msgBasicInfo.getMsgType();
		if (null == msgType) {
			return "消息类型不能为空";
		} else {
			if (!ValidatorHelper.validEBMBasicMsgType(msgType)) {
				return "消息类型系统暂不支持";
			}
		}

		if (msgBasicInfo.getSenderName() == null) {
			return "应急信息发布机构名称不能为空";
		}
		if (StringUtils.isEmpty(msgBasicInfo.getSenderCode())) {
			return "应急信息发布机构编码不能为空";
		}
		if (null == msgBasicInfo.getSendTime()) {
			return "应急广播消息发布时间不能为空";
		} else {
			if (!ValidatorHelper.isDate(msgBasicInfo.getSendTime(),
					DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
				return "应急广播消息发布时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
			}
		}
		/*
		 * 取消广播时不需要校难后面的内容 
		 */
		if(!EBMType.cancel.getValue().equals(msgType)) {
			/*if (StringUtils.isEmpty(msgBasicInfo.getEventType())) {
				return "事件类型编码不能为空";
			}*/ //日常广播没有事件类型
			if (null == msgBasicInfo.getSeverity()) {
				return "事件级别不能为空";
			} else {
				if (!ValidatorHelper.validSeverity(msgBasicInfo.getSeverity())) {
					return "事件级别系统暂不支持";
				}
			}
			if (null == msgBasicInfo.getStartTime()) {
				return "播发开始时间不能为空";
			} else {
				if (!ValidatorHelper.isDate(msgBasicInfo.getStartTime(),
						DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					return "播发开始时间不符合要求 YYYY-MM-DD HH:MI:SS";
				}
			}
			if (null == msgBasicInfo.getEndTime()) {
				return "播发结束时间不能为空";
			} else {
				if (!ValidatorHelper.isDate(msgBasicInfo.getEndTime(),
						DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					return "播发结束时间不符合要求 YYYY-MM-DD HH:MI:SS";
				}
			}
		}
		return null;
	}

	private String validateMsgContent(MsgContent msgContent) {
		if (StringUtils.isEmpty(msgContent.getLanguageCode())) {
			return "应急广播文本内容的语种代码不能为空";
		}
		if (StringUtils.isEmpty(msgContent.getMsgTitle())) {
			return "应急信息标题文本不能为空";
		}
		if (StringUtils.isEmpty(msgContent.getAreaCode())) {
			return "应急广播消息的覆盖区域不能为空";
		}
		if (null != msgContent.getAuxiliaryList() && msgContent.getAuxiliaryList().size() > 0) {
			String auxiliaryErrorMsg = null;
			for (Auxiliary aux : msgContent.getAuxiliaryList()) {
				if (null == aux.getAuxiliaryType()) {
					auxiliaryErrorMsg = "辅助数据类型不能为空";
					break;
				}
				if (StringUtils.isEmpty(aux.getAuxiliaryDesc())) {
					auxiliaryErrorMsg = "辅助数据描述不能为空";
					break;
				}
			}

			if (null != auxiliaryErrorMsg) {
				return auxiliaryErrorMsg;
			}
		} else {
			if (StringUtils.isEmpty(msgContent.getMsgDesc())) {
				return "应急信息内容文本不能为空";
			}
		}

		return null;
	}

	private String validateDispatchList(List<Dispatch> dispatchList) {
		String dispatchListErrMsg = null;
		for (Dispatch dptch : dispatchList) {
			if (StringUtils.isEmpty(dptch.getLanguageCode())) {
				dispatchListErrMsg = "调度资源的语种代码不能为空";
				break;
			}
			if (null != dptch.getEBRPS() && StringUtils.isEmpty(dptch.getEBRPS().getEBRID())) {
				dispatchListErrMsg = "应急广播平台编号不能为空";
				break;
			}
			if (null != dptch.getEBRAS() && StringUtils.isEmpty(dptch.getEBRAS().getEBRID())) {
				dispatchListErrMsg = "消息接收设备编号不能为空";
				break;
			}
			if (null != dptch.getEBRBS()) {
//				if (StringUtils.isEmpty(dptch.getEBRBS().getBrdSysType())) {
//					dispatchListErrMsg = "播出系统类型不能为空";
//					break;
//				}
				if (StringUtils.isEmpty(dptch.getEBRBS().getBrdSysInfo())) {
					dispatchListErrMsg = "播出系统信息不能为空";
					break;
				}
			}
		}

		return dispatchListErrMsg;
	}
}
