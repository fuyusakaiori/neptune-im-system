package com.fuyusakaiori.nep.im.gateway.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <h3>服务端类</h3>
 */
@Slf4j
public class NepTcpServer {

    private EventLoopGroup boosGroup;
    private EventLoopGroup workGroup;

    private ServerBootstrap server;

    public NepTcpServer(int port) {
        // 1. 准备服务端配置
        server = new ServerBootstrap();
        boosGroup = new NioEventLoopGroup(2);
        workGroup = new NioEventLoopGroup(10);
        // 2. 初始化服务端配置
        server.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 10240)  // 服务端连接队列容量
                .option(ChannelOption.SO_REUSEADDR, true) // 服务端是否允许重用端口号
                .childOption(ChannelOption.TCP_NODELAY, true)  // 服务端禁用 Nagle 算法
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 服务端保活机制（并非心跳检测）
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // 添加服务端处理器
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 0. 默认添加日志处理器
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                    }
                });
        // 3. 启动服务端
        server.bind(port);
    }
}
