package com.fuyusakaiori.nep.im.service.util.mq.publish;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepRabbitMQConstant;
import com.example.nep.im.common.entity.message.NepServiceMessage;
import com.example.nep.im.common.entity.session.NepUserSessionInfo;
import com.fuyusakaiori.nep.im.service.util.mq.NepUserSessionTaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <h3>逻辑层向网关层发送消息</h3>
 */
@Slf4j
@Component
public class NepServiceToGateWayMessageProducer {

    @Autowired
    private NepUserSessionTaker sessionTaker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * <h3>发送消息</h3>
     */
    private void sendMessage(int messageType, NepUserSessionInfo session, Object dataSource){
        // 1. 封装成消息
        NepServiceMessage message = transferServiceMessage(messageType, session, dataSource);
        // 2. 发送消息
        try {
            rabbitTemplate.convertAndSend(NepRabbitMQConstant.MESSAGE_SERVICE_TO_GATEWAY, String.valueOf(session.getBrokerId()), message);
        } catch (Exception exception){
            log.error("NepUserSessionMessageProducer sendMessage: 消息发送失败", exception);
        }
    }

    private static NepServiceMessage transferServiceMessage(int messageType, NepUserSessionInfo session, Object dataSource) {
        return new NepServiceMessage()
                       .setTargetId(session.getUserId())
                       .setAppId(session.getAppId())
                       .setClientType(session.getClientType())
                       .setMessageType(messageType)
                       .setImei(session.getImei())
                       .setMessageBody(JSONUtil.toJsonStr(dataSource).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <h3>给用户的所有在线客户端都发送消息</h3>
     * <h4>执行逻辑: 逻辑层向网关层投递消息, 网关层负责将消息转发给客户端</h4>
     */
    public void sendMessage(int appId, int targetId, int messageType, Object dataSource){
        // 1. 查询目标用户的所有  session
        List<NepUserSessionInfo> userSessionList = sessionTaker.getUserSessionList(appId, targetId);
        // 2. 给每个用户都发送消息
        for (NepUserSessionInfo session : userSessionList) {
            sendMessage(messageType, session, dataSource);
        }
    }

    public void sendMessage(int appId, int targetId, int clientType, String imei, int messageType, Object dataSource){
        sendMessage(appId, targetId, clientType, imei, messageType, dataSource, true);
    }

    /**
     * <h3>给特定用户发送消息 或者 给用户的其他客户端发送消息 （不包含用户当前使用的客户端）</h3>
     */
    public void sendMessage(int appId, int targetId, int clientType, String imei, int messageType, Object dataSource, boolean isExceptSend){
        // 1. 如果是除外发送消息的话, 那么就会发送给用户的其他在线客户端, 当前使用的客户端是不会收到消息的 & 如果不是除外发送消息的话, 那么就直接发给特定的用户
        if (isExceptSend){
            // 2.1 获取所有客户端
            List<NepUserSessionInfo> sessionList = sessionTaker.getUserSessionList(appId, targetId);
            // 2.2 循环比较除去当前的客户端
            for (NepUserSessionInfo session : sessionList) {
                // 2.3 拼接客户端标识
                String currentClient = clientType + StrUtil.COLON + imei;
                String targetClient = session.getClientType() + StrUtil.COLON + session.getImei();
                // TODO 如果存在多个网关, 那么逻辑层就会投递消息到多个网关？消息会被重复处理吗？
                if (!currentClient.equals(targetClient)){
                    // 2.4 发送消息
                    sendMessage(messageType, session, dataSource);
                }
            }
        }else{
            // 3.1. 查询目标用户
            NepUserSessionInfo session = sessionTaker.getUserSession(appId, targetId, clientType, imei);
            // 3.2. 发送消息
            sendMessage(messageType, session, dataSource);
        }
    }




}
