package com.fuyusakaiori.nep.im.gateway.rabbitmq;

import com.fuyusakaiori.nep.im.gateway.config.NepServerBootStrapConfig;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver.NepMessageReceiver;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class NepRabbitMQFactory {

    private static ConnectionFactory connectionFactory;

    private static final Map<String ,Channel> channelMap = new ConcurrentHashMap<>();

    public static void start(NepServerBootStrapConfig.NepServerConfig serverConfig){
        // 1. 配置 rabbitmq
        NepServerBootStrapConfig.NepRabbitMQConfig rabbitmqConfig = serverConfig.getRabbitmq();
        if (connectionFactory == null){
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(rabbitmqConfig.getHost());
            connectionFactory.setPort(rabbitmqConfig.getPort());
            connectionFactory.setVirtualHost(rabbitmqConfig.getVirtualHost());
            connectionFactory.setUsername(rabbitmqConfig.getUsername());
            connectionFactory.setPassword(rabbitmqConfig.getPassword());
        }
        // 2. 启动监听器
        NepMessageReceiver.start(serverConfig.getBrokerId());
    }

    public static Channel getChannel(String channelName) throws IOException, TimeoutException {
        if (!channelMap.containsKey(channelName)){
            Channel channel = getConnection().createChannel();
            channelMap.put(channelName, channel);
        }
        return channelMap.get(channelName);
    }

    /**
     * <h3>rabbitmq 创建的连接是单例的</h3>
     */
    private static Connection getConnection() throws IOException, TimeoutException {
        return connectionFactory.newConnection();
    }

}
