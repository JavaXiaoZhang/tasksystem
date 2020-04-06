package com.zq.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

/**
 * @author ZQ
 * @Date 2019/11/27
 */
@Component
@ChannelHandler.Sharable
public class AddChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChannelUtil channelUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = channelHandlerContext.channel();
        String text = textWebSocketFrame.text();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(text, Message.class);
        String msgType = message.getMsgType();
        if ("1".equals(msgType)) {
            Map<String, Object> data = message.getData();
            Long userId = Long.valueOf(String.valueOf(data.get("userId")));
            channelUtil.add( userId, channel);
            log.info("{}发来连接请求,用户id为{}", channelHandlerContext.channel().remoteAddress(),userId);
        }
        //交给下一个处理器处理
        channelHandlerContext.fireChannelRead(message);
    }
}
