package com.fuyusakaiori.nep.im.service.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NepTomcatConfig {

    @Bean(name = "configurableServletWebServerFactory")
    public ConfigurableServletWebServerFactory getConfigurableServletWebServerFactory(){
        TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        serverFactory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\%"));
        return serverFactory;
    }

}
