package cn.comtom.linkage.main.mqListener.consumer;

import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.service.impl.omd.impl.OMDInfoServiceManager;
import cn.comtom.linkage.main.access.untils.SendEbdUtil;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.mqListener.message.OMDRequestMessage;
import cn.comtom.tools.constants.MqQueueConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author:WJ
 * @date: 2018/12/4 0004
 * @time: 下午 3:20
 * MQ运维数据反馈--消费者
 */
@Component
public class OMDRequestConsumer  {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OMDInfoServiceManager omdInfoServiceManager;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private SendEbdUtil sendEbdUtil;

    @RabbitListener(queuesToDeclare = @Queue(MqQueueConstant.OMDRequestQueue))
    void receiveMessage(String message, Channel channel, Message message1) throws IOException {
        try {
            logger.info("OMDRequestQueue:"+message);
            OMDRequestMessage omdRequestMessage = JSONObject.toJavaObject(JSON.parseObject(message),OMDRequestMessage.class);
            String parentUrl = commonService.getParentPlatUrl();
            if(StringUtils.isNotBlank(parentUrl)){
                EBD ebdResponse=omdInfoServiceManager.dispatchService(omdRequestMessage.getEbdId(),omdRequestMessage.getOmdRequest());
                logger.debug("====运维请求数据上报=====ebdResponse=[{}]",JSON.toJSONString(ebdResponse));
                sendEbdUtil.sendEBD(ebdResponse,commonService.getParentPlatUrl());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //消费异常时，终止重复消费该消息。避免消息死循环的消费异常消息。
            channel.basicAck(message1.getMessageProperties().getDeliveryTag(), false);
        }

    }

}
