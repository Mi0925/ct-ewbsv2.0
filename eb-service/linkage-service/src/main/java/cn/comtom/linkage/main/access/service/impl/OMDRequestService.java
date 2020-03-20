package cn.comtom.linkage.main.access.service.impl;

import cn.comtom.domain.core.omd.request.OmdRequestAddRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.linkage.main.mqListener.message.OMDRequestMessage;
import cn.comtom.linkage.utils.DateStyle;
import cn.comtom.linkage.utils.DateTimeUtil;
import cn.comtom.tools.constants.MqQueueConstant;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * wj
 * 运维数据请求处理类
 */
@Service
public class OMDRequestService extends AbstractEMDService{


	@Autowired
	private ICoreFeginService coreFeginService;

	@Autowired
	private MQMessageProducer mqMessageProducer;

	@Override
	public String serviceType() {
		return EBDType.OMDRequest.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {

		String relatedEbdId=ebd.getEBDID();
		String relatedEbrId=ebd.getSRC().getEBRID();
		OMDRequest omdRequestData=ebd.getOMDRequest();
		String omdType=omdRequestData.getOMDType();

		Date rptStartTime=null;
		Date rptEndTime=null;
		String rptType=null;

		Params params=omdRequestData.getParams();
		if(params!=null){
			String startTimeStr=params.getRptStartTime();
			String endTimeStr=params.getRptEndTime();
			rptType=omdRequestData.getParams().getRptType();
			if(StringUtils.isNotEmpty(startTimeStr)){
				rptStartTime=DateTimeUtil.stringToDate(startTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
			}
			if(StringUtils.isNotEmpty(endTimeStr)){
				rptEndTime=DateTimeUtil.stringToDate(endTimeStr, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
			}
		}
		OmdRequestAddRequest omdRequest=new OmdRequestAddRequest();
		omdRequest.setOmdType(omdType);
		omdRequest.setRelatedEbdId(relatedEbdId);
		omdRequest.setRelatedEbrId(relatedEbrId);
		omdRequest.setRptEndTime(rptEndTime);
		omdRequest.setRptStartTime(rptStartTime);
		omdRequest.setRptType(rptType);

		//保存运维请求数据
		coreFeginService.saveOmdRequest(omdRequest);

		//MQ生产运维请求消息
		OMDRequestMessage message=new OMDRequestMessage();
		message.setEbdId(relatedEbdId);
		message.setOmdRequest(ebd.getOMDRequest());
		mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));

	}
	
}
