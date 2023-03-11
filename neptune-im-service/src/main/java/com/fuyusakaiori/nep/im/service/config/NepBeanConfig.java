package com.fuyusakaiori.nep.im.service.config;

import com.example.nep.im.common.constant.NepZookeeperConstant;
import com.example.nep.im.common.enums.status.NepConsistentHashType;
import com.example.nep.im.common.enums.status.NepLoadBalanceType;
import com.fuyusakaiori.nep.im.service.route.INepLoadBalance;
import com.fuyusakaiori.nep.im.service.route.algorithm.hash.NepAbstractConsistentHash;
import com.fuyusakaiori.nep.im.service.route.algorithm.hash.NepConsistentHashLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.Objects;

@Configuration
public class NepBeanConfig {

    @Autowired
    private NepApplicationConfig applicationConfig;

    @Bean
    public CuratorFramework getZookeeperClient(){
        // 1. 配置 zookeeper 客户端
        CuratorFramework zookeeperClient = CuratorFrameworkFactory.builder()
                                         .connectString(applicationConfig.getZkAddress())
                                         .connectionTimeoutMs(applicationConfig.getZkConnectTimeout())
                                         .retryPolicy(new ExponentialBackoffRetry(
                                                 NepZookeeperConstant.BASE_SLEEP_TIME_MS,
                                                 NepZookeeperConstant.MAX_RETRY_TIME))
                                         .build();
        // 2. 启动 zookeeper 客户端
        zookeeperClient.start();
        // 3. 返回 zookeeper 客户端
        return zookeeperClient;
    }

    @Bean
    public INepLoadBalance getLoadBalance() throws Exception {
        // 1. 获取路由算法
        NepLoadBalanceType loadBalanceType = NepLoadBalanceType.getLoadBalance(applicationConfig.getLoadBalanceType());
        if (Objects.isNull(loadBalanceType)){
            throw new RuntimeException("NepBeanConfig getLoadBalance: 获取路由算法失败, 请检查配置是否有误");
        }
        // 2. 反射创建路由算法对象
        Class<?> clazz = Class.forName(loadBalanceType.getClazz());
        INepLoadBalance loadBalance = (INepLoadBalance) clazz.getConstructors()[0].newInstance();
        // 3. 一致性哈希算法需要单独设置
        if (loadBalanceType.getType() == NepLoadBalanceType.CONSISTENT_HASH.getType()){
            // 4. 获取字段
            Field consistentHash = clazz.getDeclaredField("consistentHash");
            // 5. 设置访问权限
            consistentHash.setAccessible(true);
            // 6. 获取采用的一致性哈希算法
            NepConsistentHashType consistentHashType = NepConsistentHashType.getConsistentHashType(applicationConfig.getConsistentHashType());
            if (Objects.isNull(consistentHashType)){
                throw new RuntimeException("NepBeanConfig getLoadBalance: 获取一致性哈希算法失败, 请检查配置是否有误");
            }
            NepAbstractConsistentHash consistentHashLoadBalance = (NepAbstractConsistentHash) Class.forName(consistentHashType.getClazz()).getConstructors()[0].newInstance();
            // 7. 设置新的值
            consistentHash.set(loadBalance, consistentHashLoadBalance);
        }
        return loadBalance;
    }

}
