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
 * @Date 2019/11/26
 * 在Netty中，frame是传输数据的载体，TextWebSocketFrame用于处理文本对象
 */
@Component
@ChannelHandler.Sharable
public class AddGroupTaskHandler extends SimpleChannelInboundHandler<Message> {
    @Autowired
    private ChannelUtil channelUtil;

    private Logger log = LoggerFactory.getLogger(this.getClass());

//    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /*@Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channels.add(channel);
        System.out.println(channel.remoteAddress()+":建立了链接");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channels.remove(channel);
        System.out.println(channel.remoteAddress()+":连接已关闭");
    }*/

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        //发送信息的channel
        Channel channel = channelHandlerContext.channel();
        //发送的信息
        /*String text = textWebSocketFrame.text();
        Channel channels = ChannelUtils.getByUserId(101);

        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(text, Message.class);*/
        String msgType = message.getMsgType();
        if("3".equals(msgType)){
            Map<String, Object> data = message.getData();
            Long groupId = Long.valueOf(String.valueOf(data.get("groupId")));
            String groupName = (String) data.get("groupName");
            String username = null;
            //发送给连接了的所有channel
            List<Channel> channelList = channelUtil.getGroupChannelsByGroupId(groupId);
            log.info("groupId:{},channelList长度{}",groupId,channelList.size());
            for (Channel channels:
                    channelList) {
                if (channels.equals(channel)){
                    username = channelUtil.getUsername(channel);
                    log.info("channel相同，发送消息给自己",channel.remoteAddress());
                    channels.writeAndFlush(new TextWebSocketFrame("你在任务组"+ groupName +"中新增了任务看板"));
                    continue;
                }
                log.info("channel不同，发送消息给{}",channel.remoteAddress());
                if (username!=null && !"".equals(username.trim())) {
                    channels.writeAndFlush(new TextWebSocketFrame("用户" + username + "在任务组"+ groupName +"中新增了任务看板"));
                }
            }
        }
        channelHandlerContext.fireChannelRead(message);
    }
}
