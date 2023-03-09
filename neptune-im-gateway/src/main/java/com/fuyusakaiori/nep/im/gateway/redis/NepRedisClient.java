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
            redissonClient =  NepSingleRedisClient.getRedissonClient(serverConfig.getRedis());
        }
    }

    public static RedissonClient getRedissonClient(){
        return redissonClient;
    }

}
