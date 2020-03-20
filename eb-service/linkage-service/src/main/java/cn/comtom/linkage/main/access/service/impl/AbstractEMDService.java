package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.linkage.commons.LinkageConstants;
import cn.comtom.linkage.main.access.constant.SendFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBDResponse;
import cn.comtom.linkage.main.access.service.EMDservice;
import cn.comtom.linkage.main.access.untils.SendEbdUtil;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

/**
 * @author nobody 处理接收数据包 保存数据库
 */
public abstract class AbstractEMDService  implements EMDservice {


	@Autowired
	private ICoreFeginService  coreFeginService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private SendEbdUtil sendEbdUtil;

	@Override
	public void preservice(EBD ebd, String fileId, List<File> resourceFiles) {
		recordEbd(ebd, fileId, SendFlag.receive, LinkageConstants.EBD_STATE_SEND_SUCCESS);
	}

	@Override
	public void afterservice(EBD ebd, String fileId) {
		recordEbd(ebd, fileId, SendFlag.send, LinkageConstants.EBD_STATE_SEND_SUCCESS);
		recordEbdResponse(ebd, ebd.getEBDResponse(), SendFlag.send);
	}

	/**
	 * 保存接收、发送据包记录
	 *
	 * @param ebd
	 * @param fileId
	 */
	protected void recordEbd(EBD ebd, String fileId, SendFlag sendFlag, Integer ebdState) {
		sendEbdUtil.recordEbd(ebd,fileId,sendFlag,ebdState);
	}

	/**
	 * 保存接收发送数据包响应数据
	 *
	 * @param ebd
	 * @param
	 */
	protected void recordEbdResponse(EBD ebd, EBDResponse eBDResponse, SendFlag sendFlag) {
		sendEbdUtil.recordEbdResponse(ebd,eBDResponse,sendFlag);
	}

	protected void sendEBD(EBD ebd, String requestURL) {
		sendEbdUtil.sendEBD(ebd,requestURL);
	}




}
