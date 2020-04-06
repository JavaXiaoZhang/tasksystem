package com.zq.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author ZQ
 * @Date 2020/4/1
 */
@Component
@RocketMQMessageListener(topic = "overtime", consumerGroup = "myconsumer")
public class NewRocketmqConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("收到消息：" + message);
    }

}