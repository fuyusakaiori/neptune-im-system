package com.fuyusakaiori.nep.im.gateway.redis;

import com.fuyusakaiori.nep.im.gateway.config.NepServerBootStrapConfig;
import org.redisson.api.RedissonClient;

/**
 * <h3>Redis 客户端封装类</h3>
 */
public class NepRedisClient {

    /**
     * <h3>redisson 客户端</h3>
     */
    private static RedissonClient redissonClient;


    public static void start(NepServerBootStrapConfig.NepServerConfig serverConfig){
        if (redissonClient == null){
            // 1. 初始化 redisson 客户端
            redissonClient =  NepSingleRedisClient.getRedissonClient(serverConfig.getRedis());
            // 2. 启动 redis 作为消息队列的能力
            NepUserLoginMessageListener.init(serverConfig.getLoginMode());
        }
    }

    public static RedissonClient getRedissonClient(){
        return redissonClient;
    }

}
