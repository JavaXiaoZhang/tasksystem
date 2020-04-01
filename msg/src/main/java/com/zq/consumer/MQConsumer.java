package com.zq.consumer;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.feign.ITaskService;
import com.zq.util.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author ZQ
 * @Date 2020/4/1
 */
@Component
@RocketMQMessageListener(topic = "overtime", consumerGroup = "myconsumer")
public class MQConsumer implements RocketMQListener {

    private final static Logger log = LoggerFactory.getLogger(MQConsumer.class);

    @Autowired
    private ChannelUtil channelUtil;

    @Autowired
    private ITaskService taskService;

    @Override
    public void onMessage(Object o) {
        log.info("收到消息：{}",o);
        Long taskInfoId = Long.valueOf(String.valueOf(o));
        ResultBean resultBean = taskService.getUserListByTaskInfoId(taskInfoId);
        if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
            Map<String, Object> map = (Map<String, Object>) resultBean.getData();
            String taskInfoName = (String) map.get("taskInfoName");
            List<Long> userIdList = (List<Long>) map.get("userIdList");
            for (int i = 0; i < userIdList.size(); i++) {
                Channel channel = channelUtil.getByUserId(Long.valueOf(String.valueOf(userIdList.get(i))));
                if (channel!=null) {
                    channel.writeAndFlush(new TextWebSocketFrame("你的任务" + taskInfoName + "即将过期，请及时完成"));
                }
            }
        }
    }

}
