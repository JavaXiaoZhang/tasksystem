package com.zq.util;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import com.zq.feign.IUserService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map.Entry;

/**
 * @author ZQ
 * @Date 2019/11/27
 */

@Component
public class ChannelUtil {
    private Map<Long, Channel> channels = new ConcurrentHashMap<>();
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IUserService userService;

    /**
     * 添加
     */

    public void add(Long userId, Channel channel) {
        channels.put(userId, channel);
    }

    /**
     * 移除
     */

    public void remove(Long userId) {
        channels.remove(userId);
    }

    public void remove(Channel channel) {
        Set<Map.Entry<Long, Channel>> entries = channels.entrySet();
        for (Map.Entry<Long, Channel> entry : entries) {
            if (entry.getValue() == channel) {
                channels.remove(entry.getKey());
                return;
            }
        }
    }


    /**
     * 获取
     */
    public Channel getByUserId(Long userId) {
        return channels.get(userId);
    }

    public List<Channel> getGroupChannelsByGroupId(Long groupId) {
        ResultBean resultBean = userService.getGroupUserIds(groupId);
        log.info("调用完成：{}=={}",resultBean.getStatusCode(),resultBean.getData());
        List<Channel> channelList = new ArrayList<>();
        if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())) {
            List<Long> userIds = (List<Long>) resultBean.getData();
            log.info("userIds:{}",userIds.size());
            for (int i = 0; i < userIds.size(); i++) {
                Long userId = Long.valueOf(String.valueOf(userIds.get(i)));
                Channel channel = getByUserId(userId);
                if (channel!=null) {
                    channelList.add(channel);
                }
            }
        }

        return channelList;
    }

    public String getUsername(Channel channel) {
        Set<Entry<Long, Channel>> entries = channels.entrySet();
        for (Entry<Long, Channel> entry:
             entries) {
            if (entry.getValue().equals(channel)){
                log.info("找到相同的channel");
                ResultBean resultBean = userService.getUsername(entry.getKey());
                if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
                    return (String) resultBean.getData();
                }
                log.info("uername:{}",resultBean.getData());
                break;
            }
        }
        return "";
    }

    public Channel getChannelByUsername(String username) {
        ResultBean resultBean = userService.getUserId(username);
        if (ResultBeanConstant.OK.equals(resultBean.getStatusCode())){
            return getByUserId(Long.valueOf(String.valueOf(resultBean.getData())));
        }
        return null;
    }
}
