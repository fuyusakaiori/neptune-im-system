package com.fuyusakaiori.nep.im.gateway.config;


import lombok.Data;

@Data
public class NepServerBootStrapConfig {

    private NepServerConfig server;

    @Data
    public static class NepServerConfig{

        private Integer tcpServerPort;

        private Integer webSocketServerPort;

        private Integer bossThreadPoolSize;

        private Integer workerThreadPoolSize;

        private Long heartBeatTimeout;

        private NepRedisConfig redis;

        private NepRabbitMQ rabbitmq;
    }

    /**
     * <h3>Redis 配置</h3>
     */
    @Data
    public static class NepRedisConfig{

        private String mode;

        private Integer database;

        private String password;

        private Integer timeout;

        private Integer poolMinIdle;

        private Integer poolConnTimeout;

        private Integer poolSize;

        private NepRedisSingleConfig single;

    }

    /**
     * <h3>Redis 单机配置</h3>
     */
    @Data
    public static class NepRedisSingleConfig{

        private String address;

    }


    /**
     * <h3>RabbitMQ 配置</h3>
     */
    @Data
    public static class NepRabbitMQ {

        private String host;

        private Integer port;

        private String virtualHost;

        private String username;

        private String password;

    }

}
