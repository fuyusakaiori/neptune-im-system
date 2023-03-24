package com.fuyusakaiori.nep.im.service.core.message.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.example.nep.im.common.entity.proto.message.NepChatAckMessage;
import com.example.nep.im.common.entity.proto.message.NepChatGroupMessage;
import com.example.nep.im.common.enums.message.NepChatGroupMessageType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NepChatGroupMessageService {

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private NepCheckSendMessageService checkSendMessageService;

    @Autowired
    private NepStoreSendMessageService storeMessageService;

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    /**
     * <h3>处理群聊消息</h3>
     */
    public void handleMessage(NepChatGroupMessage message) {
        // 1. 获取变量
        int appId = message.getAppId();
        int senderId = message.getSenderId();
        int groupId = message.getGroupId();
        // 2. 参数校验
        if (appId <= 0 || senderId <= 0 || groupId <= 0){
            log.error("NepChatGroupMessageService handleMessage: 群聊消息参数校验失败 - message: {}", message);
            return;
        }
        // 3. 发送消息的前置校验
        boolean isAllowSendGroupMessage = checkSendMessageService.checkChatGroupMessageSend(appId, senderId, groupId);
        if (!isAllowSendGroupMessage){
            log.error("NepChatGroupMessageService handleMessage: 群聊消息没有通过前置校验 - message: {}", message);
            return;
        }
        // 4. 持久化消息
        storeMessage(message);
        // 5. 响应 ACK 消息给客户端
        sendAckMessage(message);
        // 6. 同步消息给自己的所有客户端
        sendSyncMessage(message);
        // 7. 发送消息给所有群成员的所有客户端
        sendGroupMessage(message);
    }

    /**
     * <h3>持久化消息</h3>
     */
    private void storeMessage(NepChatGroupMessage message){
        storeMessageService.storeMessage(IdUtil.getSnowflakeNextId(), message);
    }

    /**
     * <h3>发送 ACK 消息给发送方</h3>
     */
    private void sendAckMessage(NepChatGroupMessage message){
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
    private void sendSyncMessage(NepChatGroupMessage message){
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                message.getMessageType(), message, true);
    }

    /**
     * <h3>发送消息给群中的所有群成员</h3>
     */
    private void sendGroupMessage(NepChatGroupMessage message){
        // 1. 查询群组中的所有成员
        List<NepGroupMember> groupMemberList = groupMemberMapper.queryAllGroupMember(message.getAppId(), message.getGroupId());
        // 2. 校验群成员是否为空
        if (CollectionUtil.isEmpty(groupMemberList)){
            log.error("NepChatGroupMessageService sendGroupMessage: 消息将要发送的群聊中没有任何成员 - message: {}", message);
            return;
        }
        // 3. 遍历成员集合
        for (NepGroupMember groupMember : groupMemberList) {
            if (!groupMember.getGroupMemberId().equals(message.getSenderId())){
                messageSender.sendMessage(message.getAppId(), groupMember.getGroupMemberId(),
                        message.getMessageType(), message);
            }
        }
    }


}
