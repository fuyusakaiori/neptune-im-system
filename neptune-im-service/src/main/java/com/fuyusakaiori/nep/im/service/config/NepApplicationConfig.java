package com.fuyusakaiori.nep.im.service.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class NepApplicationConfig {

    //================== 系统配置 ==================

    private int loadBalanceType;

    private int consistentHashType;

    private String zkAddress;

    private int zkConnectTimeout;

    //================== 回调配置 ==================

    private boolean editUserAfterCallBack;

    private boolean addFriendshipBeforeCallBack;

    private boolean addFriendshipAfterCallBack;

    private boolean editFriendshipRemarkAfterCallBack;

    private boolean releaseFriendshipAfterCallBack;

    private boolean releaseAllFriendshipAfterCallBack;

    private boolean addFriendInBlackListAfterCallBack;

    private boolean removeFriendInBlackListCallBack;

}
