package cn.comtom.linkage.main.ebr.service.impl;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.commons.SyncFlag;
import cn.comtom.linkage.main.access.constant.ODMRptType;
import cn.comtom.linkage.main.access.model.ebd.commom.Params;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;
import cn.comtom.linkage.main.ebr.service.IEbrSyncService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.linkage.main.mqListener.message.OMDRequestMessage;
import cn.comtom.tools.constants.MqQueueConstant;
import com.alibaba.fastjson.JSON;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:WJ
 * @date: 2019/3/5 0005
 * @time: 下午 3:21
 */
@Service
public class EbrSyncServiceImpl implements IEbrSyncService {

    @Autowired
    private MQMessageProducer mqMessageProducer;
    
    @Autowired
    IResoFeginService resoFeginService;
    
    /**
     * 平台信息同步
     */
    @Override
    public void psInfoReport() {
        //产生MQ，消费者上报信息数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRPSInfo.name());
        Params params=new Params();
        //首次上报全量上报
        PlatformWhereRequest platformWhereRequest = new PlatformWhereRequest();
        platformWhereRequest.setSyncFlag(SyncFlag.nosync.getValue());
        platformWhereRequest.setStatusSyncFlag(SyncFlag.nosync.getValue());
        List<EbrPlatformInfo> platformLists = resoFeginService.findPlatformListByWhere(platformWhereRequest);
        if(CollectionUtils.isNotEmpty(platformLists)) {
        	params.setRptType(ODMRptType.Full.name());//设置全同步
        }else {
        	params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        }
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

    /**
     * 平台状态同步
     */
    @Override
    public void psStateReport() {
        //产生MQ，消费者上报状态数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRPSState.name());
        Params params=new Params();
        //首次上报全量上报
        PlatformWhereRequest platformWhereRequest = new PlatformWhereRequest();
        platformWhereRequest.setSyncFlag(SyncFlag.nosync.getValue());
        platformWhereRequest.setStatusSyncFlag(SyncFlag.nosync.getValue());
        List<EbrPlatformInfo> platformLists = resoFeginService.findPlatformListByWhere(platformWhereRequest);
        if(CollectionUtils.isNotEmpty(platformLists)) {
        	params.setRptType(ODMRptType.Full.name());//设置增量同步，只同步未同步数据
        }else {
        	params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        }
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

    /**
     * 播出系统信息同步
     */
    @Override
    public void bsInfoReport() {
        //产生MQ，消费者上报信息数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRBSInfo.name());
        Params params=new Params();
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

    /**
     * 播出系统状态同步
     */
    @Override
    public void bsStateReport() {
        //产生MQ，消费者上报状态数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRBSState.name());
        Params params=new Params();
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

    /**
     * 终端信息同步
     */
    @Override
    public void dtInfoReport() {
        //产生MQ，消费者上报信息数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRDTInfo.name());
        Params params=new Params();
        //首次上报全量上报
        TerminalWhereRequest terminalWhereRequest = new TerminalWhereRequest();
        terminalWhereRequest.setSyncFlag(SyncFlag.sync.getValue());
        List<EbrTerminalInfo> terminalListss = resoFeginService.findTerminalListByWhere(terminalWhereRequest);
        if(CollectionUtils.isNotEmpty(terminalListss)) {
        	params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        }else {
        	params.setRptType(ODMRptType.Full.name());//设置增量同步，只同步未同步数据
        }
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

    /**
     * 终端状态同步
     */
    @Override
    public void dtStateReport() {
        //产生MQ，消费者上报状态数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRDTState.name());
        Params params=new Params();
        //首次上报全量上报
        TerminalWhereRequest terminalWhereRequest = new TerminalWhereRequest();
        terminalWhereRequest.setStatusSyncFlag(SyncFlag.sync.getValue());
        List<EbrTerminalInfo> terminalListss = resoFeginService.findTerminalListByWhere(terminalWhereRequest);
        if(CollectionUtils.isNotEmpty(terminalListss)) {
        	params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        }else {
        	params.setRptType(ODMRptType.Full.name());//设置增量同步，只同步未同步数据
        }
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

    @Override
    public void brdLogReport() {
        //产生MQ，消费者上报播发记录数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBMBrdLog.name());
        Params params=new Params();
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
    }

	/**
	 * 台站资源信息同步
	 */
	@Override
	public void stInfoReport() {
		//产生MQ，消费者上报台站资源信息数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRSTInfo.name());
        Params params=new Params();
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
	}

	/**
	 * 适配器资源信息同步
	 */
	@Override
	public void asInfoReport() {
		//产生MQ，消费者上报适配器资源信息数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRASInfo.name());
        Params params=new Params();
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
	}

	/**
	 * 适配器资源状态同步
	 */
	@Override
	public void asStateReport() {
		//产生MQ，消费者上报适配器资源状态数据
        OMDRequestMessage message=new OMDRequestMessage();
        OMDRequest omdRequest=new OMDRequest();
        omdRequest.setOMDType(EBDType.EBRASState.name());
        Params params=new Params();
        params.setRptType(ODMRptType.Incremental.name());//设置增量同步，只同步未同步数据
        omdRequest.setParams(params);
        message.setOmdRequest(omdRequest);
        mqMessageProducer.sendData(MqQueueConstant.OMDRequestQueue,JSON.toJSONString(message));
	}

}
