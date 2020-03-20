package cn.comtom.linkage.main.access.model.ebd;

import cn.comtom.linkage.main.access.model.ebd.details.info.EBRASInfo;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRBSInfo;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRDTInfo;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRPSInfo;
import cn.comtom.linkage.main.access.model.ebd.details.info.EBRSTInfo;
import cn.comtom.linkage.main.access.model.ebd.details.other.ConnectionCheck;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBDResponse;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMBrdLog;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateRequest;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateResponse;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRASState;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRBSState;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRDTState;
import cn.comtom.linkage.main.access.model.ebd.details.state.EBRPSState;
import cn.comtom.linkage.main.access.model.ebd.ebm.DEST;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;


/**
 * 应急广播业务数据
 * @author DTW
 */
public class EBD{
	
	/**
	 * 协议版本，目前为1.0
	 */
	private String EBDVersion="1.0";
	
	/**
	 * 应急广播数据包ID，数据包来源对象通过该数据包ID来唯一标识该数据包。
	 * 36位数字码，格式为类型码（2位数字码）+数据包来源对象ID（18位数字码）+顺序码（16位数字码）
	 * 其中：心跳检测的类型码为01，16位顺序码始终为0。
	 * 其他数据包的类型码为10，顺序码从0开始递增 
	 */
	private String EBDID;
	
	/**
	 * 业务数据类型，对应于业务数据详情内容元素。目前可为如下类型（可扩展）
	 * EBM EBMStateResponse EBMStateRequest OMDRequest EBRSTInfo 
	 * EBRASInfo EBRBSInfo EBRDTInfo EBMBrdLog EBRASState 
	 * EBRBSState EBRDTState ConnectionCheck EBDResponse
	 */
	private String EBDType;
	
	/**
	 * 数据包来源对象
	 */
	private SRC SRC;
	
	/**
	 * 数据包目标对象(可选)
	 */
	private DEST DEST;

	/**
	 * 数据包生成时间，格式为YYYY-MM-DD hh:mm:ss-
	 */
	private String EBDTime;
	
	/**
	 * 关联业务数据包(可选)
	 */
	private RelatedEBD relatedEBD;
	
	/**
	 * 应急广播消息
	 */
	private EBM EBM;
	
	/**
	 * 应急广播消息播发状态反馈
	 */
	private EBMStateResponse EBMStateResponse;
	
	/**
	 * 应急广播消息播发状态查询
	 */
	private EBMStateRequest EBMStateRequest;
	
	/**
	 * 运维数据请求
	 */
	private OMDRequest OMDRequest;
	
	/**
	 * 平台信息上报
	 */
	private EBRPSInfo EBRPSInfo;
	
	/**
	 * 台站上报信息
	 */
	private EBRSTInfo EBRSTInfo;
	
	/**
	 * 接收设备上报信息
	 */
	private EBRASInfo EBRASInfo;
	
	/**
	 * 播出系统上报信息
	 */
	private EBRBSInfo EBRBSInfo;
	
	/**
	 * 平台设备及终端上报信息
	 */
	private EBRDTInfo EBRDTInfo;
	
	/**
	 * 应急广播消息播发记录
	 */
	private EBMBrdLog EBMBrdLog;
	
	/**
	 * 应急广播平台状态
	 */
	private EBRPSState EBRPSState;
	
	/**
	 * 消息接收设备状态
	 */
	private EBRASState EBRASState;
	
	/**
	 * 播出系统状态
	 */
	private EBRBSState EBRBSState;
	
	/**
	 * 设备及终端状态
	 */
	private EBRDTState EBRDTState;
	
	/**
	 * 心跳检测
	 */
	private ConnectionCheck connectionCheck;
	
	/**
	 * 数据请求结果反馈
	 */
	private EBDResponse EBDResponse;

	/**
	 * @return the eBDVersion
	 */
	public String getEBDVersion() {
		return EBDVersion;
	}

	/**
	 * @param eBDVersion the eBDVersion to set
	 */
	public void setEBDVersion(String eBDVersion) {
		EBDVersion = eBDVersion;
	}

	/**
	 * @return the eBDID
	 */
	public String getEBDID() {
		return EBDID;
	}

	/**
	 * @param eBDID the eBDID to set
	 */
	public void setEBDID(String eBDID) {
		EBDID = eBDID;
	}

	/**
	 * @return the eBDType
	 */
	public String getEBDType() {
		return EBDType;
	}

	/**
	 * @param eBDType the eBDType to set
	 */
	public void setEBDType(String eBDType) {
		EBDType = eBDType;
	}

	/**
	 * @return the sRC
	 */
	public SRC getSRC() {
		return SRC;
	}

	/**
	 * @param sRC the sRC to set
	 */
	public void setSRC(SRC sRC) {
		SRC = sRC;
	}

	/**
	 * @return the dEST
	 */
	public DEST getDEST() {
		return DEST;
	}

	/**
	 * @param dEST the dEST to set
	 */
	public void setDEST(DEST dEST) {
		DEST = dEST;
	}

	/**
	 * @return the eBDTime
	 */
	public String getEBDTime() {
		return EBDTime;
	}

	/**
	 * @param eBDTime the eBDTime to set
	 */
	public void setEBDTime(String eBDTime) {
		EBDTime = eBDTime;
	}

	/**
	 * @return the 关联业务数据包(可选)
	 */
	public RelatedEBD getRelatedEBD() {
		return relatedEBD;
	}

	/**
	 * @param relatedEBD 关联业务数据包(可选) the relatedEBD to set
	 */
	public void setRelatedEBD(RelatedEBD relatedEBD) {
		this.relatedEBD = relatedEBD;
	}

	/**
	 * @return the eBM
	 */
	public EBM getEBM() {
		return EBM;
	}

	/**
	 * @param eBM the eBM to set
	 */
	public void setEBM(EBM eBM) {
		EBM = eBM;
	}

	/**
	 * @return the eBMStateResponse
	 */
	public EBMStateResponse getEBMStateResponse() {
		return EBMStateResponse;
	}

	/**
	 * @param eBMStateResponse the eBMStateResponse to set
	 */
	public void setEBMStateResponse(EBMStateResponse eBMStateResponse) {
		EBMStateResponse = eBMStateResponse;
	}

	/**
	 * @return the eBMStateRequest
	 */
	public EBMStateRequest getEBMStateRequest() {
		return EBMStateRequest;
	}

	/**
	 * @param eBMStateRequest the eBMStateRequest to set
	 */
	public void setEBMStateRequest(EBMStateRequest eBMStateRequest) {
		EBMStateRequest = eBMStateRequest;
	}

	/**
	 * @return the oMDRequest
	 */
	public OMDRequest getOMDRequest() {
		return OMDRequest;
	}

	/**
	 * @param oMDRequest the oMDRequest to set
	 */
	public void setOMDRequest(OMDRequest oMDRequest) {
		OMDRequest = oMDRequest;
	}

	/**
	 * @return the eBRPSInfo
	 */
	public EBRPSInfo getEBRPSInfo() {
		return EBRPSInfo;
	}

	/**
	 * @param eBRPSInfo the eBRPSInfo to set
	 */
	public void setEBRPSInfo(EBRPSInfo eBRPSInfo) {
		EBRPSInfo = eBRPSInfo;
	}

	/**
	 * @return the eBRSTInfo
	 */
	public EBRSTInfo getEBRSTInfo() {
		return EBRSTInfo;
	}

	/**
	 * @param eBRSTInfo the eBRSTInfo to set
	 */
	public void setEBRSTInfo(EBRSTInfo eBRSTInfo) {
		EBRSTInfo = eBRSTInfo;
	}

	/**
	 * @return the eBRASInfo
	 */
	public EBRASInfo getEBRASInfo() {
		return EBRASInfo;
	}

	/**
	 * @param eBRASInfo the eBRASInfo to set
	 */
	public void setEBRASInfo(EBRASInfo eBRASInfo) {
		EBRASInfo = eBRASInfo;
	}

	/**
	 * @return the eBRBSInfo
	 */
	public EBRBSInfo getEBRBSInfo() {
		return EBRBSInfo;
	}

	/**
	 * @param eBRBSInfo the eBRBSInfo to set
	 */
	public void setEBRBSInfo(EBRBSInfo eBRBSInfo) {
		EBRBSInfo = eBRBSInfo;
	}

	/**
	 * @return the eBRDTInfo
	 */
	public EBRDTInfo getEBRDTInfo() {
		return EBRDTInfo;
	}

	/**
	 * @param eBRDTInfo the eBRDTInfo to set
	 */
	public void setEBRDTInfo(EBRDTInfo eBRDTInfo) {
		EBRDTInfo = eBRDTInfo;
	}

	/**
	 * @return the eBMBrdLog
	 */
	public EBMBrdLog getEBMBrdLog() {
		return EBMBrdLog;
	}

	/**
	 * @param eBMBrdLog the eBMBrdLog to set
	 */
	public void setEBMBrdLog(EBMBrdLog eBMBrdLog) {
		EBMBrdLog = eBMBrdLog;
	}

	/**
	 * @return the eBRPSState
	 */
	public EBRPSState getEBRPSState() {
		return EBRPSState;
	}

	/**
	 * @param eBRPSState the eBRPSState to set
	 */
	public void setEBRPSState(EBRPSState eBRPSState) {
		EBRPSState = eBRPSState;
	}

	/**
	 * @return the eBRASState
	 */
	public EBRASState getEBRASState() {
		return EBRASState;
	}

	/**
	 * @param eBRASState the eBRASState to set
	 */
	public void setEBRASState(EBRASState eBRASState) {
		EBRASState = eBRASState;
	}

	/**
	 * @return the eBRBSState
	 */
	public EBRBSState getEBRBSState() {
		return EBRBSState;
	}

	/**
	 * @param eBRBSState the eBRBSState to set
	 */
	public void setEBRBSState(EBRBSState eBRBSState) {
		EBRBSState = eBRBSState;
	}

	/**
	 * @return the eBRDTState
	 */
	public EBRDTState getEBRDTState() {
		return EBRDTState;
	}

	/**
	 * @param eBRDTState the eBRDTState to set
	 */
	public void setEBRDTState(EBRDTState eBRDTState) {
		EBRDTState = eBRDTState;
	}

	/**
	 * @return the 心跳检测
	 */
	public ConnectionCheck getConnectionCheck() {
		return connectionCheck;
	}

	/**
	 * @param connectionCheck 心跳检测 the connectionCheck to set
	 */
	public void setConnectionCheck(ConnectionCheck connectionCheck) {
		this.connectionCheck = connectionCheck;
	}

	/**
	 * @return the eBDResponse
	 */
	public EBDResponse getEBDResponse() {
		return EBDResponse;
	}

	/**
	 * @param eBDResponse the eBDResponse to set
	 */
	public void setEBDResponse(EBDResponse eBDResponse) {
		EBDResponse = eBDResponse;
	}
}
