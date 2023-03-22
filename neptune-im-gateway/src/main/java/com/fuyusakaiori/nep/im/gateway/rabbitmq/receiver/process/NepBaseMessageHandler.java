package com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver.process;

import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.entity.message.NepServiceMessage;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.codec.serial.NepSerializer;
import com.fuyusakaiori.nep.im.gateway.util.NepUserSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public abstract class NepBaseMessageHandler {

    public static final String TO_USER_ID = "toUserId";

    protected void beforeHandle(){

    }


    protected void afterHandle(){

    }

    /**
     * <h3>处理逻辑服务器投递的消息</h3>
     */
    public void handle(NepServiceMessage message){
        // 0. 前置处理
        beforeHandle();
        // 1. 获取消息目标的相关信息
        int appId = message.getTargetAppId();
        int clientType = message.getTargetClientType();
        int userId = message.getTargetId();
        String imei = message.getTargetImei();
        // 3. 获取消息目标的 channel
        NioSocketChannel channel = NepUserSocketHolder.get(userId, appId, clientType, imei);
        // 3. 校验获取的 channel 是否为空
        if (Objects.isNull(channel)){
            log.error("NepBaseHandler handle: 需要发送消息的目标对象不存在 - message: {}", message);
            return;
        }
        // 4. 发送给目标消息
        channel.writeAndFlush(getNepProtocol(message));
        // 5. 后置处理
        afterHandle();
    }

    private static NepProtocol getNepProtocol(NepServiceMessage message) {
        NepMessageHeader header = new NepMessageHeader().setVersion(NepProtocol.PROTOCOL_VERSION)
                                          .setSerializeType(NepSerializer.jackson).setAppId(message.getTargetAppId())
                                          .setMessageType(message.getMessageType()).setClientType(message.getTargetClientType())
                                          .setImeiLength(message.getTargetImei().getBytes(StandardCharsets.UTF_8).length).setImeiBody(message.getTargetImei());
        return new NepProtocol().setMessageHeader(header).setMessageBody(message.getMessageBody());
    }

}
