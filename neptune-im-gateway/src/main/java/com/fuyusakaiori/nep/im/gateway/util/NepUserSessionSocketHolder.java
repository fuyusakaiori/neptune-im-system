package com.fuyusakaiori.nep.im.gateway.util;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>保存用户 Socket</h3>
 */
public class NepUserSessionSocketHolder {

    private static final Map<NepUserClient , NioSocketChannel> socketHolder = new ConcurrentHashMap<>();

    public static void put(int userId, int appId, int clientType, String imei, NioSocketChannel socketChannel){
        // 1. 根据协议携带的信息封装成相应的中间类
        NepUserClient userClient = transferUserClient(userId, appId, clientType, imei);
        // 2. 放入哈希表中
        socketHolder.put(userClient, socketChannel);
    }

    public static NioSocketChannel get(int userId, int appId, int clientType, String imei){
        // 1. 根据协议携带的信息封装成相应的中间类
        NepUserClient userClient = transferUserClient(userId, appId, clientType, imei);
        // 2. 获取相应的客户端中间类
        return socketHolder.get(userClient);
    }

    /**
     * <h3>获取用户在所有客户端中的会话连接</h3>
     */
    public static List<NioSocketChannel> get(int userId, int appId){
        List<NioSocketChannel> channelList = new ArrayList<>();
        for (NepUserClient userClient : socketHolder.keySet()) {
            if (userClient.getUserId() == userId && userClient.getAppId() == appId){
                channelList.add(socketHolder.get(userClient));
            }
        }
        return channelList;
    }

    public static void remove(int userId, int appId, int clientType, String imei){
        // 1. 根据协议携带的信息封装成相应的中间类
        NepUserClient userClient = transferUserClient(userId, appId, clientType, imei);
        // 2. 查询到对应的客户端信息然后移除
        socketHolder.remove(userClient);
    }

    private static NepUserClient transferUserClient(int userId, int appId, int clientType, String imei){
        return new NepUserClient()
                       .setUserId(userId)
                       .setAppId(appId)
                       .setClientType(clientType)
                       .setImei(imei);
    }

    /**
     * <h3>客户端信息</h3>
     */
    @Data
    @Accessors(chain = true)
    @ToString
    private static class NepUserClient{

        private int userId;

        private int appId;

        private int clientType;

        private String imei;

    }

}
