package com.zq.netty;

import com.zq.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * @author ZQ
 * @Date 2020/3/29
 */
@Component
public class NettyServer implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AddGroupTaskHandler addGroupTaskHandler;
    @Autowired
    private AddChannelHandler addChannelHandler;
    @Autowired
    private DelGroupTaskHandler delGroupTaskHandler;
    @Autowired
    private GroupUserHandler groupUserHandler;
    @Autowired
    private HeartHandler heartHandler;

    @Value("${netty.server.port}")
    private int nettyPort;

    @Override
    public void run(String... args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //2.设置http编码器
                        pipeline.addLast(new HttpServerCodec());
                        //3.考虑到传输数据，设置支持大数据流
                        pipeline.addLast(new ChunkedWriteHandler());
                        //4.对HTTPMessage做聚合,设置支持传输的最大长度为1024*32 字节
                        pipeline.addLast(new HttpObjectAggregator(16 * 1024));
                        //设置跟http协议相关的处理器--end
                        //用于指定给客户端连接访问的路由 : /ws
                        //对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                        //处理用户连接请求：msgType：1
                        pipeline.addLast(addChannelHandler);
                        //处理用户心跳：msgType：2
                        pipeline.addLast(heartHandler);
                        //处理添加任务看板：msgType：3
                        pipeline.addLast(addGroupTaskHandler);
                        //处理删除任务看板：msgType：4
                        pipeline.addLast(delGroupTaskHandler);
                        //处理对组内用户的修改请求：msgType：5
                        pipeline.addLast(groupUserHandler);
                        //包装官方提供的超时类，实现我们移除的效果
                        pipeline.addLast(new MyTimeOutHandler(9, TimeUnit.SECONDS));


                        /*为了服务器端资源的合理利用，我们采用心跳包的机制来确保客户端是否存活
                          以责任链的模式来分开处理各种请求
                        */
                        //官方提供的超时处理器
                        //pipeline.addLast(new ReadTimeoutHandler(9, TimeUnit.SECONDS));
                    }
                });
        //7.开启监听
        serverBootstrap.bind(nettyPort).sync();
        log.info("netty启动完毕。。");
    }
}
