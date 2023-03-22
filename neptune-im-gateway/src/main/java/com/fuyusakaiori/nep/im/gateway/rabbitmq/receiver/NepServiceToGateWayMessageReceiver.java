package com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.message.NepServiceMessage;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.NepRabbitMQFactory;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver.process.NepBaseMessageHandler;
import com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver.process.NepMessageHandlerFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Slf4j
public class NepServiceToGateWayMessageReceiver {

    public static final String MESSAGE_TYPE = "messageType";
    public static final String MESSAGE_BODY = "messageBody";
    private static int brokerId;

    /**
     * 接收逻辑层向网关层投递的消息: 直接采用 NepProtocol
     */
    private static void receiverMessage(){
        try {
            // 1. 根据 channel 名称获取相应的 channel
            Channel channel = NepRabbitMQFactory.getChannel(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY + brokerId);
            // 2. 绑定消息队列 queue 到 channel
            channel.queueDeclare(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY + brokerId, true, false, false, null);
            // 3. 绑定交换机 exchange 到 channel
            channel.queueBind(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY + brokerId, NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY, String.valueOf(brokerId));
            // 4. 监听消息
            channel.basicConsume(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY + brokerId, false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        // 1. 将字节数据转换成相应的字符串
                        String messageJson = new String(body, StandardCharsets.UTF_8);
                        // 2. 将字符串转换为 JSON 对象
                        JSONObject messageJsonObj = JSONUtil.parseObj(messageJson);
                        // 3. 获取消息类型
                        Integer messageType = messageJsonObj.getInt(MESSAGE_TYPE);
                        // 4. 获取携带的消息体: 这里这么做的原因是因为直接反序列化, 没有办法反序列化抽象类, 并且没有标记实际类型, 所以只能单抽出来反序列化
                        NepMessageBody messageBody = messageJsonObj.get(MESSAGE_BODY, NepMessageBody.getMessageClass(messageType));
                        // 5. 这里需要忽略反序列中出现的错误, 直接反序列化成消息对象
                        NepServiceMessage message = JSONUtil.toBean(messageJson, NepServiceMessage.class, true);
                        // 6. 重新设置消息体的内容
                        message.setMessageBody(messageBody);
                        // 3. 校验消息是否为空
                        // 4. 获取处理器
                        NepBaseMessageHandler messageHandler = NepMessageHandlerFactory.getMessageHandler(messageType);
                        // 5. 处理消息
                        messageHandler.handle(message);
                        // 6. 确认消息
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (IOException exception) {
                        log.error("NepServiceToGateWayMessageReceiver receiverMessage: 消费消息出现异常", exception);
                        channel.basicNack(envelope.getDeliveryTag(), false, false);
                    }
                }
            });

        } catch (IOException | TimeoutException exception) {
            log.error("NepMessageReceiver receiverMessage: 监听消息出现异常", exception);
        }
    }

    public static void start(int brokerId){
        // 1. 初始化服务器标识
        NepServiceToGateWayMessageReceiver.brokerId = brokerId;
        // 2. 启动监听
        receiverMessage();
    }


}
