package com.fuyusakaiori.nep.im.service.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRedisConstant;
import com.example.nep.im.common.entity.session.NepUserSessionInfo;
import com.example.nep.im.common.enums.status.NepConnectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NepUserSessionTaker {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * <h3>获取用户在服务器上的所有会话</h3>
     */
    public List<NepUserSessionInfo> getUserSessionList(int appId, int userId){
        // 1. 拼接 key
        String sessionKey = appId + NepRedisConstant.USER_SESSION + userId;
        // 2. 查询 redis
        Map<Object, Object> sessionMap = redisTemplate.opsForHash().entries(sessionKey);
        // 3. 创建集合
        List<NepUserSessionInfo> sessionList = new ArrayList<>();
        // 4. 反序列化每个对象放入集合
        for (Object sessionJson : sessionMap.values()) {
            NepUserSessionInfo session = JSONUtil.toBean(String.valueOf(sessionJson), NepUserSessionInfo.class);
            // 5. 校验在线状态
            if (session.getConnectStatus() == NepConnectStatus.ONLINE.getStatus()){
                sessionList.add(session);
            }
        }
        return sessionList;
    }

    /**
     * <h3>获取用户在服务器上的特定会话</h3>
     */
    public NepUserSessionInfo getUserSession(int appId, int userId, int clientType, String imei){
        // 1. 拼接 key
        String sessionKey = appId + NepRedisConstant.USER_SESSION + userId;
        // 2. 拼接 hash key
        String sessionHashKey = clientType + StrUtil.COLON + imei;
        // 3. 查询 redis
        String sessionJson = String.valueOf(redisTemplate.opsForHash().get(sessionKey, sessionHashKey));
        // 4. 反序列化
        NepUserSessionInfo session = JSONUtil.toBean(sessionJson, NepUserSessionInfo.class);
        // 5. 校验在线状态
        if (session.getConnectStatus() == NepConnectStatus.OFFLINE.getStatus()){
            return null;
        }
        return session;
    }

}
