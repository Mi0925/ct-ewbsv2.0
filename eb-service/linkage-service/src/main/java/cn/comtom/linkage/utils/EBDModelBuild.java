package cn.comtom.linkage.utils;

import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.info.*;
import cn.comtom.linkage.main.access.model.ebd.details.other.*;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRASState;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRBSState;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRDTState;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRPSState;
import cn.comtom.linkage.main.access.model.ebd.ebm.DEST;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;

import java.util.Date;

public class EBDModelBuild {

	/**
	 * 非心跳数据包
	 */
	public static final String EBDTYPE = "10";

	/**
	 * 心跳数据包
	 */
	public static final String EBD_HB_TYPE = "01";
	
	/**
	 * 上级平台ebrId
	 */
	public static String destEbrId;

	/**
	 * 生成EBD对象
	 * 
	 * @param eBRID
	 *            资源ID
	 * @param srcURL
	 *            资源对接地址
	 * @param ebdIndex
	 *            ebd数据包编号
	 * @param ebdType
	 *            数据包类型
	 * @param destEbrId
	 *            目标的资源ID
	 * @param relatedEbdId
	 *            关联数据包ID
	 * @return
	 */
	public static EBD buildEBD(String eBRID, String srcURL, String ebdIndex, String ebdType,
							   String destEbrId, String relatedEbdId) {
		EBD ebd = new EBD();
		ebd.setEBDVersion("1");
		ebd.setEBDType(ebdType);
		if (EBDType.ConnectionCheck.name().equals(ebdType)) {
			ebd.setEBDID(EBD_HB_TYPE + eBRID + ebdIndex);
		} else {
			ebd.setEBDID(EBDTYPE + eBRID + ebdIndex);
		}
		SRC sRC = new SRC();
		sRC.setEBRID(eBRID);
		sRC.setURL(srcURL);
		ebd.setSRC(sRC);
		if (destEbrId != null) {
			DEST dest = new DEST();
			dest.setEBRID(destEbrId);
			ebd.setDEST(dest);
		}
		if (relatedEbdId != null) {
			RelatedEBD relatedEBD = new RelatedEBD();
			relatedEBD.setEBDID(relatedEbdId);
			ebd.setRelatedEBD(relatedEBD);
		}
		ebd.setEBDTime(DateTimeUtil.dateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		return ebd;
	}

	/**
	 * 生成EBD对象
	 * 
	 * @param eBRID
	 *            资源ID
	 * @param srcURL
	 *            资源对接地址
	 * @param ebdIndex
	 *            ebd数据包编号
	 * @param ebdType
	 *            数据包类型
	 * @return
	 */
	public static EBD buildEBD(String eBRID, String srcURL, String ebdIndex, String ebdType) {
		return buildEBD(eBRID, srcURL, ebdIndex, ebdType, null, null);
	}


	/**
	 * 上级平台请求状态查询时返回relatedEbdId
	 * @param relatedEbdId
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param response
	 * @return
	 */
	public static EBD buildStateResponse(String relatedEbdId,String eBRID, String srcURL, String ebdIndex,String destEbrId,
										 EBMStateResponse response) {
		String ebdType = EBDType.EBMStateResponse.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBMStateResponse(response);
		return ebd;
	}

	/**
	 * 生成ebm状态查询的响应回复数据包
	 * 
	 * @param eBRID
	 *            资源ID
	 * @param srcURL
	 *            资源对接地址
	 * @param ebdIndex
	 *            ebd数据包编号
	 * @param response
	 * @return
	 */
	public static EBD buildStateResponse(String eBRID, String srcURL, String ebdIndex,
			EBMStateResponse response) {
		String ebdType = EBDType.EBMStateResponse.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType);
		ebd.setEBMStateResponse(response);
		return ebd;
	}

	/**
	 * 生成消息数据包
	 * 
	 * @param eBRID
	 *            资源ID
	 * @param srcURL
	 *            资源对接地址
	 * @param ebdIndex
	 *            ebd数据包编号
	 * @param ebm
	 * @return
	 */
	public static EBD buildEBM(String eBRID, String srcURL, String ebdIndex, EBM ebm) {
		String ebdType = EBDType.EBM.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType);
		ebd.setEBM(ebm);
		return ebd;
	}

	/**
	 * 生成通用反馈数据包
	 * 
	 * @param eBRID
	 *            资源ID
	 * @param srcURL
	 *            资源对接地址
	 * @param ebdIndex
	 *            ebd数据包编号
	 * @param ebdResponse
	 * @return
	 */
	public static EBD buildResponse(String eBRID, String srcURL, String ebdIndex,
			EBDResponse ebdResponse,String destEbrId) {
		String ebdType = EBDType.EBDResponse.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,null);
		ebd.setEBDResponse(ebdResponse);
		return ebd;
	}

	/**
	 * 创建平台信息数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param ebrpsInfo
	 * @return
	 */
	public static EBD buildEBRPSInfo(String eBRID, String srcURL, String ebdIndex,
									 EBRPSInfo ebrpsInfo, String relatedEbdId, String destEbrId) {
		String ebdType = EBDType.EBRPSInfo.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRPSInfo(ebrpsInfo);
		return ebd;
	}

	/**
	 * 创建台站数据包信息
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param ebrstInfo
	 * @return
	 */
	public static EBD buildEBRSTInfo(String eBRID, String srcURL, String ebdIndex,
									 EBRSTInfo ebrstInfo, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBRSTInfo.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRSTInfo(ebrstInfo);
		return ebd;
	}

	/**
	 * 创建接收设备数据包信息
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param ebrasInfo
	 * @return
	 */
	public static EBD buildEBRASInfo(String eBRID, String srcURL, String ebdIndex,
									 EBRASInfo ebrasInfo, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBRASInfo.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRASInfo(ebrasInfo);
		return ebd;
	}

	/**
	 * 创建播出系统信息数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param ebrbsInfo
	 * @return
	 */
	public static EBD buildEBRBSInfo(String eBRID, String srcURL, String ebdIndex,
									 EBRBSInfo ebrbsInfo, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBRBSInfo.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRBSInfo(ebrbsInfo);
		return ebd;
	}

	/**
	 * 创建设备及终端信息数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param ebrdtInfo
	 * @return
	 */
	public static EBD buildEBRDTInfo(String eBRID, String srcURL, String ebdIndex,
									 EBRDTInfo ebrdtInfo, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBRDTInfo.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRDTInfo(ebrdtInfo);
		return ebd;
	}

	/**
	 * 创建消息播发记录数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param eBMBrdLog
	 * @return
	 */
	public static EBD buildBrdLog(String eBRID, String srcURL, String ebdIndex, EBMBrdLog eBMBrdLog, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBMBrdLog.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBMBrdLog(eBMBrdLog);
		return ebd;
	}

	/**
	 * 创建平台状态数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param eBRPSState
	 * @return
	 */
	public static EBD buildPSState(String eBRID, String srcURL, String ebdIndex,
								   EBRPSState eBRPSState, String relatedEbdId, String destEbrId) {
		String ebdType = EBDType.EBRPSState.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRPSState(eBRPSState);
		return ebd;
	}

	/**
	 * 创建接收设备状态数据包包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param eBRASState
	 * @return
	 */
	public static EBD buildASState(String eBRID, String srcURL, String ebdIndex,
								   EBRASState eBRASState, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBRASState.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRASState(eBRASState);
		return ebd;
	}

	/**
	 * 创建播出设备状态数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param eBRBSState
	 * @return
	 */
	public static EBD buildBSState(String eBRID, String srcURL, String ebdIndex,
								   EBRBSState eBRBSState, String relatedEbdId,String destEbrId) {
		String ebdType = EBDType.EBRBSState.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRBSState(eBRBSState);
		return ebd;
	}

	/**
	 * 创建设备及终端的状态数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param eBRDTState
	 * @return
	 */
	public static EBD buildDTState(String eBRID, String srcURL, String ebdIndex,
								   EBRDTState eBRDTState, String relatedEbdId, String destEbrId) {
		String ebdType = EBDType.EBRDTState.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType,destEbrId,relatedEbdId);
		ebd.setEBRDTState(eBRDTState);
		return ebd;
	}

	/**
	 * 创建心跳数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param connectionCheck
	 * @return
	 */
	public static EBD buildConnectionCheck(String eBRID, String srcURL, String ebdIndex,
			ConnectionCheck connectionCheck) {
		String ebdType = EBDType.ConnectionCheck.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType);
		ebd.setConnectionCheck(connectionCheck);
		return ebd;
	}

	/**
	 * 创建请求消息状态数据包
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param ebmStateRequest
	 * @return
	 */
	public static EBD buildStateRequest(String eBRID, String srcURL, String ebdIndex,
			EBMStateRequest ebmStateRequest) {
		String ebdType = EBDType.EBMStateRequest.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType);
		ebd.setEBMStateRequest(ebmStateRequest);
		return ebd;
	}

	/**
	 * 创建运维数据请求
	 * 
	 * @param eBRID
	 * @param srcURL
	 * @param ebdIndex
	 * @param omdRequest
	 * @return
	 */
	public static EBD buildOMDRequest(String eBRID, String srcURL, String ebdIndex,
			OMDRequest omdRequest) {
		String ebdType = EBDType.OMDRequest.name();
		EBD ebd = buildEBD(eBRID, srcURL, ebdIndex, ebdType);
		ebd.setOMDRequest(omdRequest);
		return ebd;
	}
}
