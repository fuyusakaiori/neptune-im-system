package com.fuyusakaiori.nep.im.service.config;


import com.example.nep.im.common.enums.status.NepConsistentHashType;
import com.example.nep.im.common.enums.status.NepLoadBalanceType;
import com.fuyusakaiori.nep.im.service.route.INepLoadBalance;
import com.fuyusakaiori.nep.im.service.route.algorithm.hash.NepAbstractConsistentHash;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.Objects;


@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class NepApplicationConfig {

    //================== 系统配置 ==================

    private int loadBalanceType;

    private int consistentHashType;

    private String callBackUrl;

    //================== 回调配置 ==================

    private boolean editUserAfterCallBack;

    private boolean addFriendshipBeforeCallBack;

    private boolean addFriendshipAfterCallBack;

    private boolean editFriendshipRemarkAfterCallBack;

    private boolean releaseFriendshipAfterCallBack;

    private boolean releaseAllFriendshipAfterCallBack;

    private boolean addFriendInBlackListAfterCallBack;

    private boolean removeFriendInBlackListCallBack;

    //================== 消息配置 ==================

    private boolean sendMessageCheckFriendship;

    private boolean sendMessageCheckFriendshipBlack;

    @Bean(name = "loadBalance")
    public INepLoadBalance getLoadBalance() throws Exception {
        // 1. 获取路由算法
        NepLoadBalanceType loadBalanceType = NepLoadBalanceType.getLoadBalance(getLoadBalanceType());
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
            NepConsistentHashType consistentHashType = NepConsistentHashType.getConsistentHashType(getConsistentHashType());
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
