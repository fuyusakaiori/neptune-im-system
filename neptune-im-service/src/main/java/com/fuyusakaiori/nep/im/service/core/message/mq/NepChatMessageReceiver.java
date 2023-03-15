package com.fuyusakaiori.nep.im.service.core.message.mq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class NepChatMessageReceiver {

    public static final String MESSAGE_TYPE = "messageType";

    /**
     * TODO 后续可能需要重构
     */
    @RabbitListener(
            // 1. 绑定队列
            bindings = @QueueBinding(
                    // 1.1 指定队列名称
                    value = @Queue(value = NepRabbitMQConstant.MESSAGE_GATEWAY_TO_SERVICE, durable = "true"),
                    // 1.2 指定交换机名称
                    exchange = @Exchange(value = NepRabbitMQConstant.MESSAGE_GATEWAY_TO_SERVICE, durable = "true")
            ),
            // 2. 每次从队列中获取的消息数
            concurrency = "1"
    )
    public void onChatMessage(Message message, Channel channel){
        // 1. 获取聊天消息
        String messageJson = new String(message.getBody(), StandardCharsets.UTF_8);
        // 2. 反序列化成 jsonObject 对象
        JSONObject messageJsonObject = JSONUtil.parseObj(messageJson);
        // 3. 获取聊天消息的类型
        int messageType = (int) messageJsonObject.get(MESSAGE_TYPE);
        // 4. 根据聊天消息类型反序列化
        if (messageType == NepChatMessageType.MESSAGE_P2P.getMessageType()){
            // 4.1 反序列化消息
            NepChatP2PMessage chatMessage = (NepChatP2PMessage) JSONUtil.toBean(messageJson, NepMessageBody.getMessageClass(messageType));
            // 4.2 处理消息
        }else if (messageType == NepChatMessageType.MESSAGE_RECEIVE_ACK.getMessageType()){

        }else if (messageType == NepChatMessageType.MESSAGE_READ.getMessageType()){

        }else if (messageType == NepChatMessageType.MESSAGE_RECALL.getMessageType()){

        }
    }

}
