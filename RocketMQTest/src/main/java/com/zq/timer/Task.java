package com.zq.timer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ZQ
 * @Date 2020/4/1
 */
@Component
public class Task {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Scheduled(cron = "0/10 * * * * ? ")
    public void timer(){
        rocketMQTemplate.convertAndSend("overtime", "hello world");
    }
}
