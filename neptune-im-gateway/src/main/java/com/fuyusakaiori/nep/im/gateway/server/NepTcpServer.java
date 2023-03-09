package com.fuyusakaiori.nep.im.gateway.server;

import com.example.neptune.im.common.constant.NepHeartBeatConstant;
import com.fuyusakaiori.nep.im.codec.NepMessageDecoder;
import com.fuyusakaiori.nep.im.codec.NepMessageEncoder;
import com.fuyusakaiori.nep.im.gateway.config.NepServerBootStrapConfig;
import com.fuyusakaiori.nep.im.gateway.handler.NepHeartBeatHandler;
import com.fuyusakaiori.nep.im.gateway.handler.NepServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <h3>服务端类</h3>
 */
@Slf4j
public class NepTcpServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    private ServerBootstrap server;

    private NepServerBootStrapConfig.NepServerConfig serverConfig;

    public NepTcpServer(NepServerBootStrapConfig.NepServerConfig serverConfig) {
        // 0. 初始化配置类
        this.serverConfig = serverConfig;
        // 1. 准备服务端配置
        this.server = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(this.serverConfig.getBossThreadPoolSize());
        this.workGroup = new NioEventLoopGroup(this.serverConfig.getWorkerThreadPoolSize());
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
                        // TODO 黏包拆包还没有解决
                        // 1. 默认添加日志处理器
                        channel.pipeline().addLast("tcp-server-log", new LoggingHandler(LogLevel.INFO));
                        // 2. 添加编解码器
                        channel.pipeline().addLast("tcp-server-decode", new NepMessageDecoder());
                        channel.pipeline().addLast("tcp-server-encode", new NepMessageEncoder());
                        // 3. 添加发送心跳检测的处理器: 每隔 10 秒检测客户端是否存活
                        channel.pipeline().addLast("send-heart-beat-handler", new IdleStateHandler(NepHeartBeatConstant.READER_IDLE_TIME_SECONDS
                                , NepHeartBeatConstant.WRITER_IDLE_TIME_SECONDS, NepHeartBeatConstant.ALL_IDLE_TIME_SECONDS));
                        // 4. 添加心跳检测的处理器: 如果在规定时间内没有响应, 那么就会进入该处理器的逻辑
                        channel.pipeline().addLast("heart-beat-handler", new NepHeartBeatHandler(serverConfig.getHeartBeatTimeout()));
                        // 5. 业务逻辑处理器
                        channel.pipeline().addLast("server-handler", new NepServerHandler());
                    }
                });
    }

    public void start(){
        this.server.bind(this.serverConfig.getTcpServerPort());
    }
}
