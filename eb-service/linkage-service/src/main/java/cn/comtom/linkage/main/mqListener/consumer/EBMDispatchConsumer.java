package cn.comtom.linkage.main.mqListener.consumer;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import cn.comtom.linkage.main.dispatch.service.IEbmDispatchService;
import cn.comtom.tools.constants.MqQueueConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:
 * @date: 2018/12/1 0001
 * @time: 上午 8:44
 * 消息分发--消费者
 * 消息正式分发
 */
@Component
@Slf4j
public class EBMDispatchConsumer {

    @Autowired
    private IEbmDispatchService ebmDispatchService;

    @RabbitListener(queuesToDeclare = @Queue(MqQueueConstant.EBM_DISPATCH_QUEUE))
    void messageProcess(Channel channel, Message message , String processMessage) throws IOException {
        try {
            Long beginTime = new Date().getTime();
            if(log.isDebugEnabled()){
                log.debug("######### EBM beforeDispatch begin. message=[{}]",processMessage);
            }
            if(StringUtils.isNotBlank(processMessage)){
            	log.info("【ebmDisPatch】~MQ消费者分发消息start, ebdId:{}", processMessage);
                ebmDispatchService.dispatch(processMessage);
            }else{
                log.error("##########   message process failed ! message is empty !");
            }

            Long endTime = new Date().getTime();
            if(log.isDebugEnabled()){
                log.debug("######### EBM beforeDispatch end.total time long=[{}] ms",endTime - beginTime);
            }
        } catch (Exception e) {
            log.error("消息正式分发出错：", e);
            //消费异常时，终止重复消费该消息。避免消息死循环的消费异常消息。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
