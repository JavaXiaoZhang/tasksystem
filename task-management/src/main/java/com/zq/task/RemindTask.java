package com.zq.task;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author ZQ
 * @Date 2020/3/31
 */
@Component
public class RemindTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0/10 * * * * ? ")
    public void remindByDeadTime(){
        log.info("开始扫描....");
        String key = "deadtime";
        //获取到期时间小于一天的value
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String format1 = format.format(date);
        String format2 = format.format(new Date(System.currentTimeMillis()+24*60*60*1000));
        Set set = redisTemplate.opsForZSet().rangeByScore(key, Double.valueOf(format1), Double.valueOf(format2));
        if (set!=null && set.size()!=0){
            for (Object o : set) {
                //通过mq发送给msg模块
                rocketMQTemplate.convertAndSend("overtime",o);
                log.info(o+"将在一天内过期！");
            }
        }
        //redisTemplate.opsForZSet().rangeByScore(key,"-inf",Double.valueOf(format1));
    }
}
