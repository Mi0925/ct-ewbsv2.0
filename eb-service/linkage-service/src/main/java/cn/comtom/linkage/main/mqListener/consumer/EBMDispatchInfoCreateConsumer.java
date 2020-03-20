package cn.comtom.linkage.main.mqListener.consumer;

import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.dispatch.service.IEbmDispatchService;
import cn.comtom.tools.constants.MqQueueConstant;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author:
 * @date: 2018/12/1 0001
 * @time: 上午 8:44
 * 消息分发--消费者
 * 消息分发前处理逻辑
 */
@Component
@Slf4j
public class EBMDispatchInfoCreateConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IEbmDispatchService ebmDispatchService;

    @RabbitListener(queuesToDeclare = @Queue(MqQueueConstant.EBM_DISPATCH_INFO_CREATE_QUEUE))
    void messageProcess(Channel channel, Message message , String processMessage) throws IOException {
        try {
            Long beginTime = new Date().getTime();
            if(log.isDebugEnabled()){
                log.debug("######### EBM beforeDispatch info create begin. message=[{}]",processMessage);
            }
            if(StringUtils.isNotBlank(processMessage)){
                EBD ebd = JSON.toJavaObject(JSON.parseObject(processMessage),EBD.class);
                ebmDispatchService.beforeDispatch(ebd);
            }else{
                log.error("##########   message process failed ! message is empty !");
            }

            Long endTime = new Date().getTime();
            if(log.isDebugEnabled()){
                log.debug("######### EBM beforeDispatch info create end.total time long=[{}] ms",endTime - beginTime);
            }
        } catch (Exception e) {
            log.error("消息分发前处理逻辑消费出错：", e);
            //消费异常时，终止重复消费该消息。避免消息死循环的消费异常消息。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
