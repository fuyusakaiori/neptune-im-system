package com.fuyusakaiori.nep.im.gateway.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepHeartBeatConstant;
import com.example.nep.im.common.constant.NepRedisConstant;
import com.example.nep.im.common.constant.NepUserConstant;
import com.example.nep.im.common.entity.session.NepUserClientInfo;
import com.example.nep.im.common.entity.session.NepUserSessionInfo;
import com.example.nep.im.common.enums.message.NepSystemMessageType;
import com.example.nep.im.common.enums.status.NepConnectStatus;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.example.nep.im.common.entity.proto.message.NepLoginMessage;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.publish.NepGateWayToServiceMessageProducer;
import com.fuyusakaiori.nep.im.gateway.redis.NepRedisClient;
import com.fuyusakaiori.nep.im.gateway.util.NepUserSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class NepServerHandler extends SimpleChannelInboundHandler<NepProtocol> {

    private final int brokerId;

    public NepServerHandler(int brokerId){
        this.brokerId = brokerId;
    }

    /**
     * <h3>netty 服务器仅处理消息的分发逻辑, springboot 服务器处理用户和好友的管理逻辑</h3>
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, NepProtocol protocol) throws Exception {
        NepMessageHeader messageHeader = protocol.getMessageHeader();
        NepMessageBody messageBody = protocol.getMessageBody();
        // 1. 处理器处理用户登录事件
        if (messageHeader.getMessageType() == NepSystemMessageType.LOGIN.getMessageType()){
            // 1.1. 处理登陆请求
            loginMessageHandler(messageHeader, messageBody, context);
            // 1.2. 广播消息给其他服务器, 根据登陆模式踢出其他客户端
            sendLoginMessage(messageHeader, messageBody);
        }else if (messageHeader.getMessageType() == NepSystemMessageType.LOGOUT.getMessageType()){
            // 2. 处理器处理用户退出的事件
            logoutMessageHandler(context);
        }else if (messageHeader.getMessageType() == NepSystemMessageType.PING.getMessageType()){
            // 3. 处理器处理心跳检测的事件
            context.channel().attr(AttributeKey.valueOf(NepHeartBeatConstant.LAST_READ_TIME)).set(System.currentTimeMillis());
        }else{
            // 4. 处理器处理消息事件
            log.info("服务器端接收到消息: {}", messageBody);
            // 注: 这里直接投递协议对象
            NepGateWayToServiceMessageProducer.sendMessage(messageBody);
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
        NepUserSocketHolder.put(message.getUserId(), messageHeader.getAppId(), messageHeader.getClientType(),
                messageHeader.getImeiBody(), (NioSocketChannel) context.channel());
        // 4. 保存用户的信息在 redis
        // 4.1 将消息转换为用户信息
        NepUserSessionInfo userSession = transferUserSession(messageHeader, message);
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

    private void sendLoginMessage(NepMessageHeader messageHeader, NepMessageBody messageBody){
        NepLoginMessage message = (NepLoginMessage) messageBody;
        // 1. 获取 redisson 客户端
        RedissonClient redissonClient = NepRedisClient.getRedissonClient();
        // 2. 获取主题
        RTopic topic = redissonClient.getTopic(NepRedisConstant.USER_LOGIN_MESSAGE_QUEUE);
        // 3. 获取发送的消息
        NepUserClientInfo clientInfo = transferUserClientInfo(message.getUserId(), messageHeader.getAppId(), messageHeader.getClientType(), messageHeader.getImeiBody());
        // TODO 4. 发布消息: 暂时采用 JSON 序列化
        topic.publish(JSONUtil.toJsonStr(clientInfo));
    }

    private void logoutMessageHandler(ChannelHandlerContext context){
        // 1. 直接从 channel 中获取用户的相关信息
        Integer userId = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).get();
        Integer appId = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.APP_ID)).get();
        Integer clientType = (Integer) context.channel().attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).get();
        String imei = (String) context.channel().attr(AttributeKey.valueOf(NepUserConstant.IMEI)).get();
        // 2. 移除用户对应的 channel
        NepUserSocketHolder.remove(userId, appId, clientType, imei);
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

    private NepUserSessionInfo transferUserSession(NepMessageHeader messageHeader, NepLoginMessage messageBody) throws UnknownHostException {
        return new NepUserSessionInfo().setUserId(messageBody.getUserId()).setAppId(messageHeader.getAppId())
                       .setClientType(messageHeader.getClientType()).setImei(messageHeader.getImeiBody())
                       .setBrokerId(brokerId).setBrokerHost(InetAddress.getLocalHost().getHostAddress())
                       .setConnectStatus(NepConnectStatus.ONLINE.getStatus());
    }

    private NepUserClientInfo transferUserClientInfo(int userId, int appId, int clientType, String imei){
        return new NepUserClientInfo().setUserId(userId).setAppId(appId)
                       .setClientType(clientType).setImei(imei);
    }
}
