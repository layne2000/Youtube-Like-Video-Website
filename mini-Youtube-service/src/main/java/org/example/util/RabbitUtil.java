package org.example.util;

import org.example.constant.MQConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RabbitUtil {
    RabbitTemplate rabbitTemplate;
    RabbitUtil(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendAsyncMsgToMQ(byte[] msg){
        rabbitTemplate.convertAndSend(MQConstant.QUEUE_LIVE_COMMENTS, msg);
    }
}
