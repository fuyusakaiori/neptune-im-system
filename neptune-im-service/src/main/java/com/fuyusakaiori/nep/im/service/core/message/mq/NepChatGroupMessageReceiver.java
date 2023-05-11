package com.fuyusakaiori.nep.im.service.core.message.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.proto.message.NepChatGroupMessage;
import com.example.nep.im.common.enums.message.NepChatGroupMessageType;
import com.fuyusakaiori.nep.im.service.core.message.service.NepChatGroupMessageService;
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
public class NepChatGroupMessageReceiver {

    public static final String MESSAGE_TYPE = "messageType";
    @Autowired
    private NepChatGroupMessageService chatGroupMessageService;

    @RabbitListener(
            // 1. 绑定队列和交换机
            bindings = @QueueBinding(
                    // 1.1 绑定网关投递消息的群聊队列
                    value = @Queue(value = NepRabbitMQConstant.GATEWAY_TO_SERVICE_GROUP_CHAT_MESSAGE, durable = "true"),
                    // 1.2 绑定向群聊队列转发消息的交换机
                    exchange = @Exchange(value = NepRabbitMQConstant.GATEWAY_TO_SERVICE_GROUP_CHAT_MESSAGE, durable = "true", type = ExchangeTypes.DIRECT),
                    // 1.3 绑定路由键
                    key = StrUtil.EMPTY
            ),
            concurrency = "1"
    )
    public void onChatMessage(@Headers Map<String, Object> headers, @Payload Message message, Channel channel) throws IOException {
        // 1. 获取发送的群聊消息
        String messageJson = new String(message.getBody(), StandardCharsets.UTF_8);
        // 2. 校验接收到的消息是否为空
        if (StrUtil.isEmpty(messageJson)){
            log.error("NepChatGroupMessageReceiver onChatMessage: 接收到的消息为空");
            return;
        }
        log.info("NepChatGroupMessageReceiver onChatMessage: 收到的消息 - message: {}", messageJson);
        // 3. 获取消息的 tag
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 4. 开始处理消息
        try {
            // 5. 消息转换为 JSON 对象
            JSONObject messageJsonObj = JSONUtil.parseObj(messageJson);
            // 6. 获取消息类型
            Integer messageType = messageJsonObj.getInt(MESSAGE_TYPE);
            // 7. 校验消息类型是否为空
            if (Objects.isNull(messageType)){
                log.error("NepChatGroupMessageReceiver onChatMessage: 接收到的消息类型为空 - message: {}", messageJson);
                return;
            }
            // 8. 根据消息类型继续处理
            log.info("messageType: {}, {}", messageType, NepChatGroupMessageType.GROUP_MESSAGE.getMessageType());
            if (messageType == NepChatGroupMessageType.GROUP_MESSAGE.getMessageType()) {
                // 8.1 处理群聊消息
                chatGroupMessageService.handleMessage(JSONUtil.toBean(messageJson, NepChatGroupMessage.class));
            }else{
                log.error("NepChatGroupMessageReceiver onChatMessage: 暂时还不支持其他群聊消息类型的处理 - message: {}", messageJson);
                return;
            }
            // 9. 确认消息收到
            channel.basicAck(deliveryTag, false);
        }catch (Exception exception){
            // 10. 消息处理出现异常
            log.error("NepChatGroupMessageReceiver onChatMessage: 消息处理出现异常 - message: {}", messageJson, exception);
            // 11. 回复 nack
            channel.basicNack(deliveryTag, false, false);
        }
    }

}
