package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.message.friendship.NepAddFriendMessage;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.message.NepFriendshipMessageType;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipDealService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Transactional(rollbackFor = Exception.class)
    public NepFriend doAddFriendshipDirectly(NepRequestHeader header, NepFriendship friendship){
        // 0. 获取变量
        Integer appId = header.getAppId();
        Integer friendshipSenderId = friendship.getFriendFromId();
        Integer friendshipReceiverId = friendship.getFriendToId();
        // 1. 查询两个用户之间的关系
        NepFriendship friendshipSender = friendshipMapper.queryFriendshipById(appId, friendshipSenderId, friendshipReceiverId);
        NepFriendship friendshipReceiver = friendshipMapper.queryFriendshipById(appId, friendshipReceiverId, friendshipSenderId);
        // 2. 判断两个用户此前是否添加过好友
        if (Objects.isNull(friendshipSender) && Objects.isNull(friendshipReceiver)){
            // 2.1 直接添加好友关系
            int isAddFriendship = friendshipMapper.addFriendship(appId, friendship.setCreateTime(System.currentTimeMillis())
                                                                                .setUpdateTime(System.currentTimeMillis()));
            if (isAddFriendship <= 0){
                log.error("NepFriendshipDealService doAddFriendshipDirectly: 添加好友关系失败 - fromId: {}, toId: {}", friendshipSenderId, friendshipReceiverId);
                return null;
            }
        }else if (Objects.nonNull(friendshipSender) && Objects.nonNull(friendshipReceiver)
                    && friendshipSender.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_RELEASE.getStatus()
                    && friendshipReceiver.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_RELEASE.getStatus()){
            // 2.2 更新好友关系
            int isEditFriendship = friendshipMapper.editFriendship(appId,
                    friendship.setFriendshipStatus(NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus())
                            .setUpdateTime(System.currentTimeMillis()));
            if (isEditFriendship <= 0){
                log.error("NepFriendshipDealService addFriendship: 更新好友关系失败 - fromId: {}, toId: {}", friendshipSenderId, friendshipReceiverId);
                return null;
            }
        }else{
            log.error("NepFriendshipDealService doAddFriendshipDirectly: 用户双方已经是好友或者为单边好友关系 - friendshipSenderId: {}, friendshipReceiverId: {}", friendshipSenderId, friendshipReceiverId);
            return null;
        }
        // 3. 查询被添加好友的用户
        NepUser receiver = userMapper.queryUserById(appId, friendshipReceiverId);
        // 4. 拼装返回结果
        NepFriend newFriend = BeanUtil.copyProperties(receiver, NepFriend.class).setFriendRemark(friendship.getFriendRemark());
        // 5. 查询发起添加好友的用户
        NepUser sender = userMapper.queryUserById(appId, friendshipSenderId);
        // 5. 拼装需要发送的消息
        NepFriend synFriend = BeanUtil.copyProperties(sender, NepFriend.class);
        // 5. 给对方用户的所有客户端发送新增的好友
        int messageType = NepFriendshipMessageType.FRIEND_ADD.getMessageType();
        NepMessageBody messageBody = BeanUtil.copyProperties(synFriend, NepAddFriendMessage.class)
                                                .setMessageType(messageType);
        messageSender.sendMessage(appId, friendshipReceiverId, messageType, messageBody);
        // TODO 6. 同步给用户的其他客户端

        // 7. 返回结果
        return newFriend;
    }

}
