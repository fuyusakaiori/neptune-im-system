package com.fuyusakaiori.nep.im.service.config;


import lombok.Data;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    /**
     * <h3>定义 http 客户端连接池管理器</h3>
     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
        // 1. 实例化连接池管理器
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 2. 设置最大连接数
        httpClientConnectionManager.setMaxTotal(getMaxConn());
        // 3. 设置最大并发连接数
        httpClientConnectionManager.setDefaultMaxPerRoute(getMacConcurrentConn());
        // 4. 返回对象
        return httpClientConnectionManager;
    }

    /**
     * <h3>实例化 http 客户端连接池管理器</h3>
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier(value = "httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager){
        return HttpClientBuilder.create().setConnectionManager(httpClientConnectionManager);
    }


    /**
     * <h3>获取 http 客户端</h3>
     */
    @Bean(name = "httpCloseableHttpClient")
    public CloseableHttpClient getHttpCloseableHttpClient(@Qualifier(value = "httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }

    /**
     * <h3>设置 builder 信息</h3>
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder(){
        return RequestConfig.custom()
                       .setConnectionRequestTimeout(getConnectTimeout())
                       .setConnectionRequestTimeout(getConnRequestTimeout())
                       .setSocketTimeout(getSocketTimeout());
    }

    @Bean(name = "requestConfig")
    public RequestConfig getRequestConfig(@Qualifier(value = "builder") RequestConfig.Builder builder){
        return builder.build();
    }

}
