package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.ebm.SRC;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


/**
 * @author nobody
 * 应急广播消息播发状态查询处理
 */
@Service
public class EBMStateRequestService extends AbstractEMDService{

	@Autowired
	private ICommonService commonService;

	@Autowired
	private ICoreFeginService coreFeginService;

	@Autowired
	private MQMessageProducer mqMessageProducer;

	@Override
	public String serviceType() {
		return EBDType.EBMStateRequest.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
		// 必选
		String ebdId = ebd.getEBDID();
		// 必选
		SRC src = ebd.getSRC();
		// 必选资源ID
		String ebdSrcEbrId = src.getEBRID();
		String ebmId=ebd.getEBMStateRequest().getEBM().getEBMID();

		/*EbmStateRequest ebmStateRequest=new EbmStateRequest();
		ebmStateRequest.setEbmId(ebmId);
		ebmStateRequest.setRelatedEbdId(ebdId);
		ebmStateRequest.setRelatedEbrId(ebdSrcEbrId);
		coreFeginService.saveEbmStateRequest(ebmStateRequest);*/

		//采用MQ触发播发状态结果反馈
		EbmStateResponseMessage message=new EbmStateResponseMessage();
		message.setEbdId(ebdId);
		message.setEbmId(ebmId);




		mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue,JSON.toJSONString(message));

	}


}
