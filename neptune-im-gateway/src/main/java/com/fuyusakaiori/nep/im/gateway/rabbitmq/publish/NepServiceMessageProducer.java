package com.fuyusakaiori.nep.im.gateway.rabbitmq.publish;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.NepRabbitMQFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
public class NepServiceMessageProducer {

    public static void sendMessage(NepMessageBody messageBody){
        try {
            // 1. 确定要发送的管道名称
            String channelName = NepRabbitMQConstant.MESSAGE_GATEWAY_TO_SERVICE;
            // 2. 获取管道对象
            Channel channel = NepRabbitMQFactory.getChannel(channelName);
            // TODO 3. 序列化消息对象: 聊天消息采用 ChatP2PMessage 消息体
            byte[] dataSource = JSONUtil.toJsonStr(messageBody).getBytes(StandardCharsets.UTF_8);
            // 4. 发送消息
            channel.basicPublish(channelName, StrUtil.EMPTY, null, dataSource);
        } catch (IOException | TimeoutException exception) {
            log.error("NepMessageProducer sendMessage: 发送消息出现异常", exception);
        }

    }

}
