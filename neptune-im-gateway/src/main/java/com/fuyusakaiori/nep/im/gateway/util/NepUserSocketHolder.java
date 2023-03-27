package com.fuyusakaiori.nep.im.gateway.util;

import com.example.nep.im.common.entity.session.NepUserClientInfo;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>保存用户 Socket</h3>
 */
public class NepUserSocketHolder {

    private static final Map<NepUserClientInfo, NioSocketChannel> socketHolder = new ConcurrentHashMap<>();

    public static void put(int userId, int appId, int clientType, String imei, NioSocketChannel socketChannel){
        // 1. 根据协议携带的信息封装成相应的中间类
        NepUserClientInfo userClient = transferUserClient(userId, appId, clientType, imei);
        // 2. 放入哈希表中
        socketHolder.put(userClient, socketChannel);
    }

    public static NioSocketChannel get(int userId, int appId, int clientType, String imei){
        // 1. 根据协议携带的信息封装成相应的中间类
        NepUserClientInfo userClient = transferUserClient(userId, appId, clientType, imei);
        // 2. 获取相应的客户端中间类
        return socketHolder.get(userClient);
    }

    /**
     * <h3>获取用户在所有客户端中的会话连接</h3>
     */
    public static List<NioSocketChannel> get(int userId, int appId){
        List<NioSocketChannel> channelList = new ArrayList<>();
        for (NepUserClientInfo userClient : socketHolder.keySet()) {
            if (userClient.getUserId() == userId && userClient.getAppId() == appId){
                channelList.add(socketHolder.get(userClient));
            }
        }
        return channelList;
    }

    public static void remove(int userId, int appId, int clientType, String imei){
        // 1. 根据协议携带的信息封装成相应的中间类
        NepUserClientInfo userClient = transferUserClient(userId, appId, clientType, imei);
        // 2. 查询到对应的客户端信息然后移除
        socketHolder.remove(userClient);
    }

    private static NepUserClientInfo transferUserClient(int userId, int appId, int clientType, String imei){
        return new NepUserClientInfo().setUserId(userId).setAppId(appId)
                       .setClientType(clientType).setImei(imei);
    }

}
