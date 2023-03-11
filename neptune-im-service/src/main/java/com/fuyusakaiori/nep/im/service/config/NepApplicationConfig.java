package com.fuyusakaiori.nep.im.service.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class NepApplicationConfig {

    private int loadBalanceType;

    private int consistentHashType;

    private String zkAddress;

    private int zkConnectTimeout;

}
