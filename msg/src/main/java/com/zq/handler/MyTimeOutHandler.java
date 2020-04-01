package com.zq.handler;

import com.zq.util.ChannelUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author ZQ
 * @Date 2019/11/28
 */
public class MyTimeOutHandler extends ReadTimeoutHandler {

    @Autowired
    private ChannelUtil channelUtil;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public MyTimeOutHandler(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    public MyTimeOutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        super.readTimedOut(ctx);
        log.info("{}...已经超时",ctx.channel().remoteAddress());
        channelUtil.remove(ctx.channel());
    }
}
