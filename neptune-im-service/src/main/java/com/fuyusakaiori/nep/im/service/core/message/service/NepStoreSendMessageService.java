package com.fuyusakaiori.nep.im.service.core.message.service;

import cn.hutool.core.util.IdUtil;
import com.example.nep.im.common.entity.proto.message.NepChatGroupMessage;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatGroupMessageHeader;
import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatMessageBody;
import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatP2PMessageHeader;
import com.fuyusakaiori.nep.im.service.core.message.mapper.INepChatGroupMessageHeaderMapper;
import com.fuyusakaiori.nep.im.service.core.message.mapper.INepChatMessageBodyMapper;
import com.fuyusakaiori.nep.im.service.core.message.mapper.INepChatMessageHeaderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class NepStoreSendMessageService {

    @Autowired
    private INepChatMessageHeaderMapper chatMessageHeaderMapper;

    @Autowired
    private INepChatGroupMessageHeaderMapper chatGroupMessageHeaderMapper;

    @Autowired
    private INepChatMessageBodyMapper chatMessageBodyMapper;


    /**
     * <h3>存储单聊消息</h3>
     */
    @Transactional
    public void storeMessage(NepChatP2PMessage message){
        // 1. 根据消息生成消息头
        List<NepChatP2PMessageHeader> messageHeaderList = generateMessageHeader(message);
        // 2. 根据内容生成消息体
        NepChatMessageBody messageBody = generateMessageBody(message);
        // 3. 持久化保存消息头
        int isStoreMessageHeader = chatMessageHeaderMapper.storeChatMessageHeader(message.getAppId(), messageHeaderList);
        if (isStoreMessageHeader <= 0){
            log.error("NepStoreSendMessageService storeMessage: 持久化单聊消息头失败 - message: {}", message);
            return;
        }
        // 4. 持久胡保存消息体
        int isStoreMessageBody = chatMessageBodyMapper.storeChatMessageBody(message.getAppId(), messageBody);
        if (isStoreMessageBody <= 0){
            log.error("NepStoreSendMessageService storeMessage: 持久化单聊消息体失败 - message: {}", message);
            return;
        }
        log.info("NepStoreSendMessageService storeMessage: 持久化单聊消息成功 - message: {}", message);
    }

    /**
     * <h3>根据消息生成消息头</h3>
     */
    private List<NepChatP2PMessageHeader> generateMessageHeader(NepChatP2PMessage message){
        // 1. 生成发送者的消息头
        NepChatP2PMessageHeader sender = new NepChatP2PMessageHeader()
                                                 .setMessageKey(IdUtil.getSnowflakeNextId()).setMessageOwnerId(message.getSenderId())
                                                 .setMessageSenderId(message.getSenderId()).setMessageReceiverId(message.getReceiverId())
                                                 .setMessageSendTime(message.getMessageSendTime()).setMessageCreateTime(System.currentTimeMillis());
        // 2. 生成接收者的消息头
        NepChatP2PMessageHeader receiver = new NepChatP2PMessageHeader()
                                                 .setMessageKey(IdUtil.getSnowflakeNextId()).setMessageOwnerId(message.getReceiverId())
                                                 .setMessageSenderId(message.getSenderId()).setMessageReceiverId(message.getReceiverId())
                                                 .setMessageSendTime(message.getMessageSendTime()).setMessageCreateTime(System.currentTimeMillis());

        return Arrays.asList(sender, receiver);
    }

    /**
     * <h3>根据消息生成消息体</h3>
     */
    private NepChatMessageBody generateMessageBody(NepChatP2PMessage message){
        return new NepChatMessageBody().setDelete(false)
                .setMessageKey(IdUtil.getSnowflakeNextId()).setMessageBody(message.getMessageBody())
                .setMessageSendTime(message.getMessageSendTime()).setMessageCreateTime(System.currentTimeMillis());
    }

    /**
     * <h3>存储群聊消息</h3>
     */
    @Transactional
    public void storeMessage(Long messageKey, NepChatGroupMessage message){
        // 1. 根据消息生成消息头
        NepChatGroupMessageHeader messageHeader = generateMessageHeader(messageKey, message);
        // 2. 根据内容生成消息体
        NepChatMessageBody messageBody = generateMessageBody(messageKey, message);
        // 3. 持久化保存消息头
        int isStoreGroupMessageHeader = chatGroupMessageHeaderMapper.storeChatGroupMessageHeader(message.getAppId(), messageHeader);
        if (isStoreGroupMessageHeader <= 0){
            log.error("NepStoreSendMessageService storeMessage: 持久化群聊消息头失败 - message: {}", message);
            return;
        }
        // 4. 持久胡保存消息体
        int isStoreMessageBody = chatMessageBodyMapper.storeChatMessageBody(message.getAppId(), messageBody);
        if (isStoreMessageBody <= 0){
            log.error("NepStoreSendMessageService storeMessage: 持久化群聊消息头失败 - message: {}", message);
            return;
        }
        log.info("NepStoreSendMessageService storeMessage: 持久化群聊消息成功 - message: {}", message);
    }

    /**
     * <h3>根据消息生成消息头</h3>
     */
    private NepChatGroupMessageHeader generateMessageHeader(Long messageKey, NepChatGroupMessage message){
        return new NepChatGroupMessageHeader().setMessageKey(messageKey)
                       .setMessageSenderId(message.getSenderId()).setMessageReceiverGroupId(message.getGroupId())
                       .setMessageSendTime(message.getMessageSendTime()).setMessageCreateTime(System.currentTimeMillis());
    }

    /**
     * <h3>根据消息生成消息体</h3>
     */
    private NepChatMessageBody generateMessageBody(Long messageKey, NepChatGroupMessage message){
        return new NepChatMessageBody().setDelete(false)
                       .setMessageKey(messageKey).setMessageBody(message.getMessageBody())
                       .setMessageSendTime(message.getMessageSendTime()).setMessageCreateTime(System.currentTimeMillis());
    }


}
