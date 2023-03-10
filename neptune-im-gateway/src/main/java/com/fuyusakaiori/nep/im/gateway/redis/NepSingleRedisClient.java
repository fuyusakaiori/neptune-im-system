package com.fuyusakaiori.nep.im.gateway.redis;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.constant.NepRedisConstant;
import com.fuyusakaiori.nep.im.gateway.config.NepServerBootStrapConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * <h3>单机 Redis 配置</h3>
 */
class NepSingleRedisClient {

    public static RedissonClient getRedissonClient(NepServerBootStrapConfig.NepRedisConfig redisConfig){
        // 1. 准备配置类
        Config redissonConfig = new Config();
        // 2. 获取单机结点的地址
        String configAddress = redisConfig.getSingle().getAddress();
        // 3. 处理单机结点地址
        String actualAddress = configAddress.startsWith(NepRedisConstant.REDIS_ADDRESS_PREFIX)
                                       ? configAddress : NepRedisConstant.REDIS_ADDRESS_PREFIX + configAddress;
        // 4. 准备单机配置
        SingleServerConfig serverConfig = redissonConfig.useSingleServer()
                                                        .setAddress(actualAddress)
                                                        .setDatabase(redisConfig.getDatabase())
                                                        .setTimeout(redisConfig.getTimeout())
                                                        .setConnectionMinimumIdleSize(redisConfig.getPoolMinIdle())
                                                        .setConnectTimeout(redisConfig.getPoolConnTimeout())
                                                        .setConnectionPoolSize(redisConfig.getPoolSize());
        // 5. 校验密码
        if (StrUtil.isNotEmpty(redisConfig.getPassword())){
            serverConfig.setPassword(redisConfig.getPassword());
        }
        // 6. 设置键值对的编解码器
        redissonConfig.setCodec(new StringCodec());
        // 7. 创建客户端实例
        return Redisson.create(redissonConfig);
    }

}
