package com.fuyusakaiori.nep.im.service.core.message.service;

import com.example.nep.im.common.entity.proto.message.NepChatAckMessage;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.example.nep.im.common.enums.status.NepFriendshipBlackCheckType;
import com.example.nep.im.common.enums.status.NepFriendshipBlackStatus;
import com.example.nep.im.common.enums.status.NepFriendshipCheckType;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.config.NepApplicationConfig;
import com.fuyusakaiori.nep.im.service.core.friendship.service.impl.NepFriendshipBlackServiceImpl;
import com.fuyusakaiori.nep.im.service.core.friendship.service.impl.NepFriendshipServiceImpl;
import com.fuyusakaiori.nep.im.service.util.mq.publish.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepChatP2PMessageService {

    @Autowired
    private NepApplicationConfig applicationConfig;

    @Autowired
    private NepServiceToGateWayMessageProducer messageProducer;

    @Autowired
    private NepFriendshipServiceImpl friendshipServiceImpl;

    @Autowired
    private NepFriendshipBlackServiceImpl friendshipBlackServiceImpl;

    /**
     * <h3>处理消息</h3>
     */
    public void handleMessage(NepChatP2PMessage message){
        // 0. 获取消息中的变量
        int appId = message.getAppId();
        int fromUserId = message.getFromUserId();
        int toUserId = message.getToUserId();
        // 1. 校验参数是否合理
        if (appId <= 0 || fromUserId <= 0 || toUserId <= 0){
            log.error("NepChatP2PMessageService handleMessage: 用户或者应用 ID 违法 - appId: {}, fromId: {}, toId: {}", appId, fromUserId, toUserId);
            return;
        }
        // 2. 校验消息是否可以发送: 校验双方是否为好友, 校验双方彼此是否在黑名单中
        if (applicationConfig.isSendMessageCheckFriendship()){
            // 2.1 校验好友关系的状态: 0 表示双方都是好友, 1 表示双方彼此不是好友, 2 表示左侧不是右侧的好友, 3 表示右侧不是左侧的好友
            int checkFriendshipStatus = friendshipServiceImpl.doCheckFriendship(appId, fromUserId, toUserId, NepFriendshipCheckType.DOUBLE.getType());
            // 2.2 根据好友关系状态决定是否继续
            if (checkFriendshipStatus != NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                log.error("NepChatP2PMessageService handleMessage: fromId: {} 和 toId: {} 不是互为好友的关系, 不可以发送消息", fromUserId, toUserId);
                return;
            }
        }
        if (applicationConfig.isSendMessageCheckFriendshipBlack()){
            // 2.3 校验黑名单关系
            int checkBlackStatus = friendshipBlackServiceImpl.doCheckFriendInBlackList(appId, fromUserId, toUserId, NepFriendshipBlackCheckType.DOUBLE.getType());
            // 2.4 根据拉黑关系决定是否继续: 先简单化, 只要有一方拉黑就不允许发送消息
            if (checkBlackStatus != NepFriendshipBlackStatus.WHITE.getStatus())   {
                log.error("NepChatP2PMessageService handleMessage: fromId: {} 和 toId: {} 存在拉黑关系, 不可以发送消息", fromUserId, toUserId);
                return;
            }
        }
        // 3. 响应 ACK 消息给客户端
        messageProducer.sendMessage(appId, message.getFromUserId(), message.getClientType(), message.getImei(),
                NepChatMessageType.MESSAGE_ACK.getMessageType(), new NepChatAckMessage().setMessageId(message.getMessageId()));
        // 4. 同步消息给自己的所有客户端
        messageProducer.sendMessage(appId, message.getFromUserId(), message.getMessageType(), message);
        // 5. 发送消息给对方的所有客户端
        messageProducer.sendMessage(appId, message.getToUserId(), message.getMessageType(), message);
    }


}
