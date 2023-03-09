package com.fuyusakaiori.nep.im.gateway.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.neptune.im.common.constant.NepHeartBeatConstant;
import com.example.neptune.im.common.constant.NepRedisConstant;
import com.example.neptune.im.common.constant.NepUserConstant;
import com.example.neptune.im.common.entity.session.NepUserSession;
import com.example.neptune.im.common.enums.status.NepConnectStatus;
import com.fuyusakaiori.nep.im.gateway.redis.NepRedisClient;
import com.fuyusakaiori.nep.im.gateway.util.NepUserSessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Objects;

@Slf4j
public class NepHeartBeatHandler extends ChannelInboundHandlerAdapter {

    private final Long heartBeatTimeout;

    public NepHeartBeatHandler(Long heartBeatTimeout) {
        this.heartBeatTimeout = heartBeatTimeout;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception {
        // 1. 判断是否为心跳检测事件
        if (event instanceof IdleStateEvent){
            // 2. 强制转换为心跳检测事件
            IdleStateEvent idleStateEvent = (IdleStateEvent) event;
            // 3. 判断事件状态
            if (idleStateEvent.state() == IdleState.READER_IDLE){
                log.info("NepHeartBeatHandler userEventTriggered: 读取数据超时");
            }else if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                log.info("NepHeartBeatHandler userEventTriggered: 写入数据超时");
            }else{
                Long currentTime = System.currentTimeMillis();
                Long lastReadTime = (Long) context.channel().attr(AttributeKey.valueOf(NepHeartBeatConstant.LAST_READ_TIME)).get();
                if (Objects.nonNull(lastReadTime) && currentTime - lastReadTime > heartBeatTimeout){
                    hearBeatHandler(context);
                }
            }
        }
    }

    private void hearBeatHandler(ChannelHandlerContext context){
        // 1. 直接从 channel 中获取用户的相关信息
        Integer userId = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).get();
        Integer appId = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.APP_ID)).get();
        Integer clientType = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).get();
        String imei = (String) context.channel().attr(AttributeKey.valueOf(NepUserConstant.IMEI)).get();
        // 2. 移除用户对应的 channel
        NepUserSessionSocketHolder.remove(userId, appId, clientType, imei);
        // 3. 移除用户保存在 redis 中的信息
        // 3.1 获取客户端
        RedissonClient redissonClient = NepRedisClient.getRedissonClient();
        // 3.2 拼接 filed
        String field = appId + NepRedisConstant.USER_SESSION + userId;
        // 3.3 获取 map
        RMap<String, String> sessionMap = redissonClient.getMap(field);
        // 3.4 拼接 key
        String key = clientType + StrUtil.COLON + imei;
        // 3.5 获取 session
        NepUserSession userSession = JSONUtil.toBean(sessionMap.get(key), NepUserSession.class);
        if (Objects.isNull(userSession)){
            throw new RuntimeException("NepHeartBeatHandler hearBeatHandler: 用户 Session 不存在");
        }
        // 3.6 设置为离线状态
        userSession.setConnectStatus(NepConnectStatus.OFFLINE.getStatus());
        // 3.7 写入 redis
        sessionMap.put(key, JSONUtil.toJsonStr(userSession));
    }
}
