package com.fuyusakaiori.nep.im.gateway.rabbitmq.publish;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.enums.message.NepChatGroupMessageType;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.example.nep.im.common.enums.message.NepMessageType;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.NepRabbitMQFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
public class NepGateWayToServiceMessageProducer {

    public static final int BEGIN_INDEX = 0;
    public static final int END_INDEX = 1;

    public static void sendMessage(NepMessageBody messageBody){
        try {
            // 1. 获取消息类型
            int messageType = Integer.parseInt(
                     String.valueOf(messageBody.getMessageType()).substring(BEGIN_INDEX, END_INDEX));
            // 2. 确定发送的管道名称
            String channelName = StrUtil.EMPTY;
            if (messageType == NepMessageType.P2P_MESSAGE.getMessageType()){
                channelName = NepRabbitMQConstant.GATEWAY_TO_SERVICE_CHAT_MESSAGE;
            }else if (messageType == NepMessageType.GROUP_MESSAGE.getMessageType()){
                channelName = NepRabbitMQConstant.GATEWAY_TO_SERVICE_GROUP_CHAT_MESSAGE;
            }
            // 3. 获取管道对象
            Channel channel = NepRabbitMQFactory.getChannel(channelName);
            // 4. 序列化消息对象: 暂时采用 JSON 序列化对象, 后续可能改成其他的
            byte[] dataSource = JSONUtil.toJsonStr(messageBody).getBytes(StandardCharsets.UTF_8);
            // 5. 发送消息
            channel.basicPublish(channelName, StrUtil.EMPTY, null, dataSource);
        } catch (IOException | TimeoutException exception) {
            log.error("NepGateWayToServiceMessageProducer sendMessage: 发送消息出现异常", exception);
        }

    }

}
