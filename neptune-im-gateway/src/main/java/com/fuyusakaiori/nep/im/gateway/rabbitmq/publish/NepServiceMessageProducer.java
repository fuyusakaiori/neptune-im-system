package com.fuyusakaiori.nep.im.gateway.rabbitmq.publish;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fuyusakaiori.nep.im.codec.proto.NepMessageBody;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.NepRabbitMQFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class NepServiceMessageProducer {

    public static void sendMessage(NepMessageBody messageBody){

        Channel channel = null;

        String channelName = "";

        try {
            channel = NepRabbitMQFactory.getChannel(channelName);
            // TODO 发送的消息需要序列化: 暂时采用 JSON
            channel.basicPublish(channelName, StrUtil.EMPTY, null, JSONUtil.toJsonStr(messageBody).getBytes());
        } catch (IOException | TimeoutException exception) {
            log.error("NepMessageProducer sendMessage: 发送消息出现异常", exception);
        }

    }

}
