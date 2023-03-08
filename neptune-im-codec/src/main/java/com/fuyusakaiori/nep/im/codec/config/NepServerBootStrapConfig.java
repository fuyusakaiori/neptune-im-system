package com.fuyusakaiori.nep.im.codec.config;


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
    }

}
