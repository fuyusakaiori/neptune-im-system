package com.fuyusakaiori.nep.im.service.config;

import com.example.nep.im.common.constant.NepZookeeperConstant;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class NepZookeeperClientConfig {

    private String zkAddress;

    private int zkConnectTimeout;


    @Bean(name = "zookeeperClient")
    public CuratorFramework getZookeeperClient(){
        // 1. 配置 zookeeper 客户端
        CuratorFramework zookeeperClient = CuratorFrameworkFactory.builder()
                                                   .connectString(getZkAddress())
                                                   .connectionTimeoutMs(getZkConnectTimeout())
                                                   .retryPolicy(new ExponentialBackoffRetry(
                                                           NepZookeeperConstant.BASE_SLEEP_TIME_MS,
                                                           NepZookeeperConstant.MAX_RETRY_TIME))
                                                   .build();
        // 2. 启动 zookeeper 客户端
        zookeeperClient.start();
        // 3. 返回 zookeeper 客户端
        return zookeeperClient;
    }

}
