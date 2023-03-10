package com.fuyusakaiori.nep.im.gateway.zookeeper;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.constant.NepZookeeperConstant;
import com.fuyusakaiori.nep.im.gateway.config.NepServerBootStrapConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;


@Slf4j
public class NepZookeeperRegistry {

    private static CuratorFramework zookeeperClient;

    public static void start(String address, NepServerBootStrapConfig.NepServerConfig serverConfig) {
        // 1. 获取 zookeeper 配置
        NepServerBootStrapConfig.NepZookeeperConfig zookeeperConfig = serverConfig.getZookeeper();
        // 2. 创建 zookeeper 客户端
        zookeeperClient = CuratorFrameworkFactory.builder()
                                       .connectString(zookeeperConfig.getAddress())
                                       .connectionTimeoutMs(zookeeperConfig.getConnectTimeout())
                                       .retryPolicy(new ExponentialBackoffRetry(NepZookeeperConstant.BASE_SLEEP_TIME_MS, NepZookeeperConstant.MAX_RETRY_TIME))
                                       .build();
        // 3. 启动 zookeeper 客户端
        zookeeperClient.start();
        // 4. 注册相应的结点
        registerService(serverConfig.getTcpServerPort(), serverConfig.getWebSocketServerPort(), address);
    }

    private static void registerService(int tcpServerPort, int websocketServerPort, String address){
        try {
            // 注册每个服务器的结点
            String tcpServer = NepZookeeperConstant.IM_CORE_GATEWAY + NepZookeeperConstant.IM_CORE_GATEWAY_TCP + StrUtil.SLASH + address + StrUtil.COLON + tcpServerPort;
            if (zookeeperClient.checkExists().forPath(tcpServer) == null){
                zookeeperClient.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(tcpServer);
            }
            String websocketServer = NepZookeeperConstant.IM_CORE_GATEWAY + NepZookeeperConstant.IM_CORE_GATEWAY_WEB + StrUtil.SLASH + address + StrUtil.COLON + websocketServerPort;
            if (zookeeperClient.checkExists().forPath(websocketServer) == null){
                zookeeperClient.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(websocketServer);
            }
        } catch (Exception exception) {
            log.error("NepZookeeperRegistry registerService: 注册中心启动时注册结点失败", exception);
        }
    }


    public static CuratorFramework getZookeeperClient(){
        return zookeeperClient;
    }


}
