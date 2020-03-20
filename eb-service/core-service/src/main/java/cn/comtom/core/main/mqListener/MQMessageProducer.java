package cn.comtom.core.main.mqListener;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQMessageProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendData(String queueName , String msg){
        amqpTemplate.convertAndSend(queueName, msg);
    }
}
