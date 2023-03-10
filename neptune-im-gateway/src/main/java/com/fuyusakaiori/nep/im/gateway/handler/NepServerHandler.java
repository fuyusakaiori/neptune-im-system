package com.fuyusakaiori.nep.im.gateway.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepHeartBeatConstant;
import com.example.nep.im.common.constant.NepRedisConstant;
import com.example.nep.im.common.constant.NepUserConstant;
import com.example.nep.im.common.entity.session.NepUserSession;
import com.example.nep.im.common.enums.message.NepSystemMessageType;
import com.example.nep.im.common.enums.status.NepConnectStatus;
import com.fuyusakaiori.nep.im.codec.proto.NepMessageBody;
import com.fuyusakaiori.nep.im.codec.proto.NepMessageHeader;
import com.fuyusakaiori.nep.im.codec.proto.NepProtocol;
import com.fuyusakaiori.nep.im.codec.proto.message.NepLoginMessage;
import com.fuyusakaiori.nep.im.gateway.redis.NepRedisClient;
import com.fuyusakaiori.nep.im.gateway.util.NepUserSessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class NepServerHandler extends SimpleChannelInboundHandler<NepProtocol> {

    private final int brokerId;

    public NepServerHandler(int brokerId){
        this.brokerId = brokerId;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, NepProtocol protocol) throws Exception {
        NepMessageHeader messageHeader = protocol.getMessageHeader();
        NepMessageBody messageBody = protocol.getMessageBody();
        if (messageHeader.getMessageType() == NepSystemMessageType.LOGIN.getMessageType()){
            loginMessageHandler(messageHeader, messageBody, context);
        }else if (messageHeader.getMessageType() == NepSystemMessageType.LOGOUT.getMessageType()){
            logoutMessageHandler(context);
        }else if (messageHeader.getMessageType() == NepSystemMessageType.PING.getMessageType()){
            // 设置上一次读写时间
            context.channel().attr(AttributeKey.valueOf(NepHeartBeatConstant.LAST_READ_TIME)).set(System.currentTimeMillis());
        }
    }

    private void loginMessageHandler(NepMessageHeader messageHeader, NepMessageBody messageBody, ChannelHandlerContext context) throws UnknownHostException {
        // 1. 强制转换为登陆消息
        NepLoginMessage message = (NepLoginMessage) messageBody;
        // 2. 用户相关信息保存在 channel -> 交给后续的处理器继续使用
        context.channel().attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).set(message.getUserId());
        context.channel().attr(AttributeKey.valueOf(NepUserConstant.APP_ID)).set(messageHeader.getAppId());
        context.channel().attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).set(messageHeader.getClientType());
        context.channel().attr(AttributeKey.valueOf(NepUserConstant.CLIENT_IMEI)).set(messageHeader.getClientType() + messageHeader.getImeiBody());
        context.channel().attr(AttributeKey.valueOf(NepUserConstant.IMEI)).set(messageHeader.getImeiBody());
        // 3. 保存用户的 channel
        NepUserSessionSocketHolder.put(message.getUserId(), messageHeader.getAppId(), messageHeader.getClientType(),
                messageHeader.getImeiBody(), (NioSocketChannel) context.channel());
        // 4. 保存用户的信息在 redis
        // 4.1 将消息转换为用户信息
        NepUserSession userSession = transferUserSession(messageHeader, message);
        // 4.2 获取客户端
        RedissonClient redissonClient = NepRedisClient.getRedissonClient();
        // 4.3 拼接 field
        String field = messageHeader.getAppId() + NepRedisConstant.USER_SESSION + message.getUserId();
        // 4.4 获取 map
        RMap<String, String> sessionMap = redissonClient.getMap(field);
        // 4.5 准备键值对并放入 map
        String key = messageHeader.getClientType() + StrUtil.COLON + messageHeader.getImeiBody();
        String value = JSONUtil.toJsonStr(userSession);
        sessionMap.put(key, value);
    }

    private void logoutMessageHandler(ChannelHandlerContext context){
        // 1. 直接从 channel 中获取用户的相关信息
        Integer userId = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).get();
        Integer appId = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.APP_ID)).get();
        Integer clientType = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).get();
        String imei = (String) context.channel().attr(AttributeKey.valueOf(NepUserConstant.IMEI)).get();
        // 2. 移除用户对应的 channel
        NepUserSessionSocketHolder.remove(userId, appId, clientType, imei);
        // 3. 移除用户保存在 redis 中的信息
        RedissonClient redissonClient = NepRedisClient.getRedissonClient();
        // 3.1 拼接 filed
        String field = appId + NepRedisConstant.USER_SESSION + userId;
        // 3.2 获取 map
        RMap<String, String> sessionMap = redissonClient.getMap(field);
        // 3.3 拼接 key
        String key = clientType + StrUtil.COLON + imei;
        // 3.4 移除 session
        sessionMap.remove(key);
    }


    /**
     * <h3>处理心跳检测</h3>
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        super.userEventTriggered(ctx, evt);
    }

    private NepUserSession transferUserSession(NepMessageHeader messageHeader, NepLoginMessage messageBody) throws UnknownHostException {
        return new NepUserSession()
                       .setUserId(messageBody.getUserId())
                       .setAppId(messageHeader.getAppId())
                       .setClientType(messageHeader.getClientType())
                       .setImei(messageHeader.getImeiBody())
                       .setConnectStatus(NepConnectStatus.ONLINE.getStatus())
                       .setBrokerId(brokerId)
                       .setBrokerHost(InetAddress.getLocalHost().getHostAddress());
    }
}
