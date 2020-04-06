package com.zq.handler;

import com.zq.pojo.Message;
import com.zq.util.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author ZQ
 * @Date 2020/3/31
 */
@Component
@ChannelHandler.Sharable
public class GroupUserHandler extends SimpleChannelInboundHandler<Message> {

    @Autowired
    private ChannelUtil channelUtil;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Channel channel = channelHandlerContext.channel();
        String msgType = message.getMsgType();
        if ("5".equals(msgType)){
            Map<String, Object> data = message.getData();
            String username = (String) data.get("username");
            String groupName = (String) data.get("groupName");
            Object adminUserId = data.get("adminUserId");
            Object delUserId = data.get("delUserId");
            if (username != null){
                log.info("点击一次添加！");
                Long groupId = Long.valueOf(String.valueOf(data.get("groupId")));
                //添加团队成员
                List<Channel> channels = channelUtil.getGroupChannelsByGroupId(groupId);
                //被通知人的channel
                Channel userChannel = channelUtil.getChannelByUsername(username);
                for (Channel channel2 :
                        channels) {
                    if ( channel.equals(channel2)){
                        //发送消息给自己
                        log.info("发给自己address：{}", channel2.remoteAddress());
                        channel2.writeAndFlush(new TextWebSocketFrame("你已将"+username+"添加到任务组"+groupName));
                    }else if (channel2.equals(userChannel)){
                        //发送给被添加用户
                        log.info("发给被添加人address：{}",channel2.remoteAddress());
                        channel2.writeAndFlush(new TextWebSocketFrame("你已被添加到任务组"+groupName));
                    }else {
                        //发送给组内成员
                        channel2.writeAndFlush(new TextWebSocketFrame(username+"已被添加到任务组"+groupName));
                    }
                }
            }else if (adminUserId!=null ){
                //成为管理员
                Long userId = Long.valueOf(String.valueOf(adminUserId));
                Channel userChannel = channelUtil.getByUserId(userId);
                String utilUsername = channelUtil.getUsername(userChannel);
                channel.writeAndFlush(new TextWebSocketFrame("你已将"+ utilUsername + "修改为任务组"+groupName+"的管理员"));
                userChannel.writeAndFlush(new TextWebSocketFrame("你已成为任务组"+groupName+"的管理员"));
            }else if (delUserId != null){
                //删除用户
                Long userId = Long.valueOf(String.valueOf(delUserId));
                Channel userChannel = channelUtil.getByUserId(userId);
                String utilUsername = channelUtil.getUsername(userChannel);
                channel.writeAndFlush(new TextWebSocketFrame("你已将"+ utilUsername + "移出任务组"+groupName));
                userChannel.writeAndFlush(new TextWebSocketFrame("你已被移出任务组"+groupName));
            }
        }
        channelHandlerContext.fireChannelRead(message);
    }
}
