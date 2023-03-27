package com.fuyusakaiori.nep.im.service.core.message.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.example.nep.im.common.entity.proto.message.NepChatConfirmAckMessage;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.fuyusakaiori.nep.im.service.core.message.service.NepChatP2PMessageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class NepChatP2PMessageReceiver {

    public static final String MESSAGE_TYPE = "messageType";

    @Autowired
    private NepChatP2PMessageService chatP2PMessageService;

    @RabbitListener(
            // 1. 绑定队列
            bindings = @QueueBinding(
                    // 1.1 指定队列名称
                    value = @Queue(value = NepRabbitMQConstant.GATEWAY_TO_SERVICE_CHAT_MESSAGE, durable = "true"),
                    // 1.2 指定交换机名称
                    exchange = @Exchange(value = NepRabbitMQConstant.GATEWAY_TO_SERVICE_CHAT_MESSAGE, durable = "true", type = ExchangeTypes.DIRECT),
                    // 1.3 指定路由 key
                    key = StrUtil.EMPTY
            ),
            // 2. 每次从队列中获取的消息数
            concurrency = "1"
    )
    public void onChatMessage(@Headers Map<String, Object> headers, @Payload Message message, Channel channel) throws IOException {
        // 1. 获取聊天消息
        String messageJson = new String(message.getBody(), StandardCharsets.UTF_8);
        if (StrUtil.isEmpty(messageJson)){
            log.error("NepChatP2PMessageReceiver onChatMessage: 接收到的消息为空");
            return;
        }
        // 2. 校验接收到的消息是否为空
        log.info("NepChatP2PMessageReceiver onChatMessage: 收到的消息 - message: {}", messageJson);
        // 3. 获取消息的 tag
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);

        try {
            // 4. 反序列化成协议对象
            JSONObject messageJsonObj = JSONUtil.parseObj(messageJson);
            // 5. 获取聊天消息的类型
            Integer messageType = messageJsonObj.getInt(MESSAGE_TYPE);
            // 6. 校验消息类型是否为空
            if (Objects.isNull(messageType)){
                log.error("NepChatP2PMessageReceiver onChatMessage: 接收到的消息类型为空 - message: {}", messageJson);
                return;
            }
            // 7. 根据聊天消息类型反序列化
            if (messageType == NepChatMessageType.P2P_MESSAGE.getMessageType()){
                // 7.1 处理单聊消息
                chatP2PMessageService.handleMessage(JSONUtil.toBean(messageJson, NepChatP2PMessage.class));
            }else if (messageType == NepChatMessageType.P2P_MESSAGE_RECEIVE_ACK.getMessageType()){
                // 7.2 消息接收者收到消息后返回给服务端相应的 ACK, 服务端再通知消息发送者, 从而体质该重发
                chatP2PMessageService.handleMessage(JSONUtil.toBean(messageJson, NepChatConfirmAckMessage.class));
            }else{
                log.error("NepChatP2PMessageReceiver onChatMessage: 暂时还不支持其他群聊消息类型的处理 - message: {}", messageJson);
                return;
            }
            // 9. 确认消息收到
            channel.basicAck(deliveryTag, false);
        } catch (Exception exception) {
            // 10. 消息处理出现异常
            log.error("NepChatP2PMessageReceiver onChatMessage: 消息处理出现异常 - message: {}", messageJson, exception);
            // 11. 回复 nack
            channel.basicNack(deliveryTag, false, false);
        }
    }

}
