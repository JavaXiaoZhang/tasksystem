package com.zq.handler;

import com.zq.pojo.Message;
import com.zq.util.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author ZQ
 * @Date 2020/3/31
 */
@Component
@ChannelHandler.Sharable
public class DelGroupTaskHandler extends SimpleChannelInboundHandler<Message> {

    @Autowired
    private ChannelUtil channelUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        String msgType = message.getMsgType();
        if ("4".equals(msgType)){
            Map<String, Object> data = message.getData();
            Long groupId = Long.valueOf(String.valueOf(data.get("groupId")));
            String groupName = (String) data.get("groupName");
            List<Channel> channelList = channelUtil.getGroupChannelsByGroupId(groupId);
            for (Channel channel : channelList) {
                channel.writeAndFlush(new TextWebSocketFrame("任务组"+groupName+"已被删除"));
            }
        }

        channelHandlerContext.fireChannelRead(message);
    }
}
