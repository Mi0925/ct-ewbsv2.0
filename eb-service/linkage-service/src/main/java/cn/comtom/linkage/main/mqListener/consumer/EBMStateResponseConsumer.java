package cn.comtom.linkage.main.mqListener.consumer;

import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmStateBackAddRequest;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBM;
import cn.comtom.linkage.main.access.service.impl.EBMStateResponseService;
import cn.comtom.linkage.main.access.untils.SynchEbmPlayBroadcastState;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.linkage.main.fegin.service.IResoFeginService;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:WJ
 * @date: 2018/12/1 0001
 * @time: 上午 8:44
 * MQ播发状态反馈--消费者
 */
@Component
public class EBMStateResponseConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EBMStateResponseService ebmStateResponseService;

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private SynchEbmPlayBroadcastState synchEbmPlayBroadcastState;

    @Autowired
    private ICommonService commonService;

    @RabbitListener(queuesToDeclare = @Queue(MqQueueConstant.EBMStateResponseQueue))
    void receiveMessage(Channel channel, String receiveMessage, Message message) throws IOException {
        try {
            EbmStateResponseMessage ebmStateResponseMessage = JSONObject.toJavaObject(JSON.parseObject(receiveMessage),EbmStateResponseMessage.class);
            String ebmId=ebmStateResponseMessage.getEbmId();
            String ebmState = ebmStateResponseMessage.getBroadcastState();
            EbmInfo e = coreFeginService.getEbmById(ebmId);

            if(e.getSendFlag().equals(CommonDictEnum.FLAG_SEND.getKey())){
                ebmId=e.getRelatedEbmId(); //数据来源最新EBMID，需要换成原EBMID

                // TODO: 2019/2/25 0025 需要判断当前ebm下发的渠道是否都已经反馈，如果都反馈，则主动反馈给上级，否则不反馈给上级
                if(!synchEbmPlayBroadcastState.needToBack(ebmId)){
                    return;
                }

            }
            List<EbdInfo> relateEbdList = coreFeginService.getEbdInfoByEbmId(ebmId);
            if(CollectionUtils.isEmpty(relateEbdList)) {
            	logger.warn("relateEbd is empty!");
            	return;
            }
            EbdInfo relateEbd = relateEbdList.get(0);
            //根据ebm中的senderCode 去查询bc_ebr_platform具体信息
            EbmInfo ebm = coreFeginService.getEbmInfoById(ebmId);
//            String senderCode=ebm.getSenderCode();
            String senderCode = commonService.getParentPlatId();
            EbrPlatformInfo ebrPlatform = resoFeginService.getEbrPlatformById(senderCode);
            //未配置上级平台则取消反馈
            if (ebrPlatform == null) {
                return;
            }

            logger.info("EBMStateBackQueue:"+receiveMessage+"  反馈目标地址："+ebrPlatform.getPsUrl());
            ebmStateResponseService.sendResponse(ebmId, ebrPlatform.getPsUrl(), relateEbd.getEbdId(), ebrPlatform.getPsEbrId(), ebmState);
            //保存反馈记录
            EbmStateBackAddRequest addRequest=new EbmStateBackAddRequest();
            addRequest.setBackTime(new Date());
            addRequest.setEbmId(ebmId);
            addRequest.setBackType(ebmStateResponseMessage.getHandType());
            addRequest.setBackStatus(StringUtils.isNotBlank(ebmState)?ebmState:ebm.getBroadcastState().toString());
            coreFeginService.saveEbmStateBack(addRequest);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //消费异常时，终止重复消费该消息。避免消息死循环的消费异常消息。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
