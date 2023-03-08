package com.fuyusakaiori.nep.im.gateway.server;

import com.fuyusakaiori.nep.im.codec.config.NepServerBootStrapConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NepWebSocketServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    private ServerBootstrap server;

    private NepServerBootStrapConfig.NepServerConfig serverConfig;

    public NepWebSocketServer(NepServerBootStrapConfig.NepServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        // 1. 准备服务端配置
        this.server = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(this.serverConfig.getBossThreadPoolSize());
        this.workGroup = new NioEventLoopGroup(this.serverConfig.getBossThreadPoolSize());
        // 2. 初始化服务端配置
        this.server.group(this.bossGroup, this.workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 10240)  // 服务端连接队列容量
                .option(ChannelOption.SO_REUSEADDR, true) // 服务端是否允许重用端口号
                .childOption(ChannelOption.TCP_NODELAY, true)  // 服务端禁用 Nagle 算法
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 服务端保活机制（并非心跳检测）
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // 添加服务端处理器
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 0. 默认添加日志处理器
                        channel.pipeline().addLast("websocket-server-log", new LoggingHandler(LogLevel.INFO));
                        // 1. 添加编解码器: WebSocket 协议是基于 HTTP 协议封装的长连接协议 - 编解码器需要使用 HTTP 的编解码器
                        channel.pipeline().addLast("http-codec", new HttpServerCodec());
                        // 2. 添加大数据量处理器
                        channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                        // 3. 添加数据聚合处理器: HTTP 消息在传输的过程中可能因为某些原因被分成多个 - 数据聚合器就是为了将多个消息重新合并成一个 HTTP 请求
                        channel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                        // 4. 添加路由处理器: WebSocket协议指定客户端访问服务端的路由
                        channel.pipeline().addLast("websocket-protocol", new WebSocketServerProtocolHandler("/nep"));
                    }
                });
    }

    public void start(){
        this.server.bind(this.serverConfig.getWebSocketServerPort());
    }

}
