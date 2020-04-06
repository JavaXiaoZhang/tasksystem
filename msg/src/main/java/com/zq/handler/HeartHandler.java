package com.zq.handler;

import com.zq.pojo.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZQ
 * @Date 2019/11/27
 */
@Component
@ChannelHandler.Sharable
public class HeartHandler extends SimpleChannelInboundHandler<Message> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        /* String text = msg.text();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(text, Message.class);*/
        if ("2".equals(message.getMsgType())){
            //log.info("{}连接正常标记",ctx.channel().remoteAddress());
        }
        channelHandlerContext.fireChannelRead(message);
    }
}
