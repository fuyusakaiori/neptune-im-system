package com.fuyusakaiori.nep.im.service.core.message.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class NepChatP2PMessageReceiver {

    public static final String MESSAGE_TYPE = "messageType";

    @Autowired
    private NepChatP2PMessageService chatP2PMessageService;

    /**
     * TODO 后续可能需要重构
     */
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
    public void onChatMessage(@Headers Map<String, Object> headers, @Payload Message message, Channel channel){
        // 1. 获取聊天消息
        String messageJson = new String(message.getBody(), StandardCharsets.UTF_8);
        // 2. 反序列化成协议对象
        JSONObject messageJsonObj = JSONUtil.parseObj(messageJson);
        // 3. 获取聊天消息的类型
        int messageType = messageJsonObj.getInt(MESSAGE_TYPE);
        // 4. 根据聊天消息类型反序列化
        if (messageType == NepChatMessageType.SINGLE_MESSAGE.getMessageType()){
            // 4.1 处理单聊消息
            chatP2PMessageService.handleMessage(JSONUtil.toBean(messageJson, NepChatP2PMessage.class));
        }else if (messageType == NepChatMessageType.MESSAGE_RECEIVE_ACK.getMessageType()){

        }else if (messageType == NepChatMessageType.MESSAGE_READ.getMessageType()){

        }else if (messageType == NepChatMessageType.MESSAGE_RECALL.getMessageType()){

        }
    }

}
