package com.fuyusakaiori.nep.im.service.core.message.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.example.nep.im.common.entity.proto.message.NepChatAckMessage;
import com.example.nep.im.common.entity.proto.message.NepChatGroupMessage;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.example.nep.im.common.enums.message.NepChatGroupMessageType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NepChatP2PMessageService {

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Autowired
    private NepCheckSendMessageService checkSendMessageService;

    @Autowired
    private NepStoreSendMessageService storeMessageService;

    /**
     * <h3>处理消息</h3>
     */
    public void handleMessage(NepChatP2PMessage message){
        // 1. 获取消息中的变量
        int appId = message.getAppId();
        int fromUserId = message.getSenderId();
        int toUserId = message.getReceiverId();
        // 2. 校验参数是否合理
        if (appId <= 0 || fromUserId <= 0 || toUserId <= 0){
            log.error("NepChatP2PMessageService handleMessage: 发送消息没有通过参数校验 - message: {}", message);
            return;
        }
        // 3. 发送消息的前置校验
        boolean isAllowSendMessage = checkSendMessageService.checkChatP2PMessageSend(appId, fromUserId, toUserId);
        if (!isAllowSendMessage){
            log.error("NepChatP2PMessageService handleMessage: 单聊消息没有通过校验 - message: {}", message);
            return;
        }
        // 4. 持久化消息
        storeMessage(message);
        // 5. 响应 ACK 消息给客户端
        sendAckMessage(message);
        // 6. 同步消息给自己的所有客户端
        sendSyncMessage(message);
        // 7. 发送消息给对方的所有客户端
        sendSingleMessage(message);
    }

    /**
     * <h3>持久化消息</h3>
     */
    private void storeMessage(NepChatP2PMessage message) {
        storeMessageService.storeMessage(IdUtil.getSnowflakeNextId(), message);
    }

    /**
     * <h3>发送 ACK 消息给发送方</h3>
     */
    private void sendAckMessage(NepChatP2PMessage message){
        // 1. 创建 ACK 消息
        NepChatAckMessage ackMessage = BeanUtil.copyProperties(message, NepChatAckMessage.class);
        // 2. 更新消息类型
        ackMessage.setMessageType(NepChatGroupMessageType.GROUP_MESSAGE_ACK.getMessageType());
        // 2. 发送消息
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                message.getMessageType(), message, false);
    }

    /**
     * <h3>发送同步消息给自己的所有客户端</h3>
     */
    private void sendSyncMessage(NepChatP2PMessage message){
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                message.getMessageType(), message, true);
    }

    /**
     * 发送单聊消息给对方
     */
    private void sendSingleMessage(NepChatP2PMessage message){
        messageSender.sendMessage(message.getAppId(), message.getReceiverId(),
                message.getMessageType(), message);
    }


}
