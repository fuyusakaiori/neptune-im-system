package com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver;

import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.constant.NepRabbitMQConstant;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.NepRabbitMQFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
public class NepMessageReceiver {

    private static void receiverMessage(){
        Channel channel = null;
        try {
            // 1. 根据 channel 名称获取相应的 channel
            channel = NepRabbitMQFactory.getChannel(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY);
            // 2. 绑定消息队列 queue 到 channel
            channel.queueDeclare(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY, true, false, false, null);
            // 3. 绑定交换机 exchange 到 channel
            channel.queueBind(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY, NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY, StrUtil.EMPTY);
            // 4. 监听消息
            channel.basicConsume(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY, false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    log.info("接收到的消息: {}", new String(body, StandardCharsets.UTF_8));
                }
            });

        } catch (IOException | TimeoutException exception) {
            log.error("NepMessageReceiver receiverMessage: 监听消息出现异常", exception);
        }
    }

    public static void start(){
        receiverMessage();
    }


}
