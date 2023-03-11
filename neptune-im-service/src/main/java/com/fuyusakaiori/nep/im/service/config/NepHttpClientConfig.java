package com.fuyusakaiori.nep.im.service.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "http-client")
public class NepHttpClientConfig {

    /**
     * <h3>http 客户端最大连接数</h3>
     */
    private int maxConn;

    /**
     * <h3>http 客户端最大并发连接数</h3>
     */
    private int macConcurrentConn;

    /**
     * <h3>创建 http 客户端连接的超时时间</h3>
     */
    private int connectTimeout;

    /**
     * <h3>获取 http 客户端连接的超时时间</h3>
     */
    private int connRequestTimeout;

    /**
     * <h3>http 连接传输数据的超时时间</h3>
     */
    private int socketTimeout;

    /**
     * <h3>提交数据时检查连接是否可用</h3>
     */
    private boolean staleConnCheckEnabled;


}
