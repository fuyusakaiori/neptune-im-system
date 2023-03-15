package com.fuyusakaiori.nep.im.gateway.redis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRedisConstant;
import com.example.nep.im.common.constant.NepUserConstant;
import com.example.nep.im.common.entity.session.NepUserClientInfo;
import com.example.nep.im.common.enums.message.NepSystemMessageType;
import com.example.nep.im.common.enums.status.NepClientType;
import com.example.nep.im.common.enums.status.NepMultiDeviceMode;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.example.nep.im.common.entity.proto.message.NepMultiLoginMessage;
import com.fuyusakaiori.nep.im.gateway.codec.serial.NepSerializer;
import com.fuyusakaiori.nep.im.gateway.util.NepUserSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class NepUserLoginMessageListener {

    private static int loginMode;


    private static void listenerMessage(){
        // 1. 获取 redisson 客户端
        RedissonClient redissonClient = NepRedisClient.getRedissonClient();
        // 2. 获取主题
        RTopic topic = redissonClient.getTopic(NepRedisConstant.USER_LOGIN_MESSAGE_QUEUE);
        // 3。设置监听器
        topic.addListener(String.class, (channel, message) -> {
            log.info("NepUserLoginMessageListener listenerMessage: 用户上线 - {}", message);
            // 1. 获取用户上线的信息
            NepUserClientInfo clientInfo = JSONUtil.toBean(message, NepUserClientInfo.class);
            // 2. 获取变量信息
            int userId = clientInfo.getUserId();
            int appId = clientInfo.getAppId();
            int clientType = clientInfo.getClientType();
            String imei = clientInfo.getImei();
            // 3. 获取用户在该服务器的会话连接
            List<NioSocketChannel> channelList = NepUserSocketHolder.get(userId, appId);
            // 4. 遍历用户在该服务器的所有连接, 根据登陆模式执行踢出逻辑
            for (NioSocketChannel socketChannel : channelList) {
                // 4.1 如果是单端登陆
                if (NepMultiDeviceMode.ONE.getMode() == loginMode){
                    oneDeviceLoginHandler(userId, appId, clientType, imei, socketChannel);
                }
                // 4.2 如果是双端登陆
                if (NepMultiDeviceMode.TWO.getMode() == loginMode){
                    twoDeviceLoginHandler(userId, appId, clientType, imei, socketChannel);
                }
                // 4.3 如果是三端登陆
                if (NepMultiDeviceMode.THREE.getMode() == loginMode){
                    threeDeviceLoginHandler(userId, appId, clientType, imei, socketChannel);
                }
            }
        });
    }

    /**
     * <h3>处理单端登陆的方法</h3>
     */
    private static void oneDeviceLoginHandler(int userId, int appId, int clientType, String imei, NioSocketChannel socketChannel) {
        // 1. 通过 channel 获取该用户在该服务器上的信息
        Integer channelUserId = (Integer) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).get();
        Integer channelClientType = (Integer) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).get();
        String channelImei = (String) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.IMEI)).get();
        // 2. 判断上线的客户端和当前服务器的客户端是否相同
        String loginClient = clientType + StrUtil.COLON + imei;
        String channelClient = channelClientType + StrUtil.COLON + channelImei;
        // 3. 如果不等, 那么就会给客户端发送消息, 让客户端自行结束连接
        if (!loginClient.equals(channelClient)){
            // 3.1 生成消息
            NepProtocol protocol = generateMessage(userId, appId, clientType, imei, channelUserId);
            // 3.2 发送消息
            socketChannel.writeAndFlush(protocol);
        }
    }

    /**
     * <h3>处理双端登陆的方法</h3>
     */
    private static void twoDeviceLoginHandler(int userId, int appId, int clientType, String imei, NioSocketChannel socketChannel) {
        // 1. 通过 channel 获取该用户在该服务器上的信息
        Integer channelUserId = (Integer) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).get();
        Integer channelClientType = (Integer) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).get();
        String channelImei = (String) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.IMEI)).get();
        // 2. 如果新增的客户端是 WEB 端, 那么不需要执行任何处理
        if (clientType == NepClientType.WEB.getType()){
            return;
        }
        // 3. 如果新增的客户端不是 WEB 端, 那么判断上线的客户端和当前服务器的客户端是否相同
        String loginClient = clientType + StrUtil.COLON + imei;
        String channelClient = channelClientType + StrUtil.COLON + channelImei;
        // 3. 如果不等, 那么就会给客户端发送消息, 让客户端自行结束连接
        if (!loginClient.equals(channelClient)){
            // 3.1 生成消息
            NepProtocol protocol = generateMessage(userId, appId, clientType, imei, channelUserId);
            // 3.2 发送消息
            socketChannel.writeAndFlush(protocol);
        }
    }

    /**
     * <h3>处理三端登陆的方法</h3>
     */
    private static void threeDeviceLoginHandler(int userId, int appId, int clientType, String imei, NioSocketChannel socketChannel) {
        // 1. 通过 channel 获取该用户在该服务器上的信息
        Integer channelUserId = (Integer) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.USER_ID)).get();
        Integer channelClientType = (Integer) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.CLIENT_TYPE)).get();
        String channelImei = (String) socketChannel.attr(AttributeKey.valueOf(NepUserConstant.IMEI)).get();
        // 2. 如果新增的客户端是 WEB 端, 那么不需要执行任何处理
        if (clientType == NepClientType.WEB.getType()){
            return;
        }
        boolean isSameClient = false;
        // 3. 如果是 PC 端, 那么只允许登陆 Windows 或者 Mac 并且仅限一台
        if ((clientType == NepClientType.WINDOWS.getType() || clientType == NepClientType.MAC.getType())
                    & (channelClientType == NepClientType.WINDOWS.getType() && channelClientType == NepClientType.MAC.getType())){
            isSameClient = true;
        }
        // 4. 如果是移动端, 那么只允许登陆 IOS 或者 Android 并且仅限一台
        if ((clientType == NepClientType.ANDROID.getType() || clientType == NepClientType.IOS.getType())
                    && (channelClientType == NepClientType.ANDROID.getType() || channelClientType == NepClientType.IOS.getType())){
            isSameClient = true;
        }
        // 5. 校验
        String loginClient = clientType + StrUtil.COLON + imei;
        String channelClient = channelClientType + StrUtil.COLON + channelImei;
        if (isSameClient && !loginClient.equals(channelClient)){
            NepProtocol protocol = generateMessage(userId, appId, clientType, imei, channelUserId);
            socketChannel.writeAndFlush(protocol);
        }

    }

    private static NepProtocol generateMessage(int userId, int appId, int clientType, String imei, Integer channelUserId) {
        // 1. 准备消息体
        NepMultiLoginMessage message = new NepMultiLoginMessage()
                                               .setFromId(userId)
                                               .setFromId(channelUserId);
        // 2. 准备消息头
        NepMessageHeader messageHeader = new NepMessageHeader()
                                                    .setVersion(NepProtocol.PROTOCOL_VERSION)
                                                    .setAppId(appId)
                                                    .setClientType(clientType)
                                                    .setMessageType(NepSystemMessageType.MUTUAL_LOGIN.getMessageType())
                                                    .setSerializeType(NepSerializer.jackson)
                                                    .setImeiLength(imei.getBytes(StandardCharsets.UTF_8).length)
                                                    .setImeiBody(imei);

        return new NepProtocol().setMessageHeader(messageHeader).setMessageBody(message);
    }


    public static void init(int loginMode){
        // 1. 初始化登陆模式的值
        NepUserLoginMessageListener.loginMode = loginMode;
        // 2. 开始监听消息
        listenerMessage();
    }


}
