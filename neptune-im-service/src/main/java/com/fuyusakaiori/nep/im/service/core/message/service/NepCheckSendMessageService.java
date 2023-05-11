package com.fuyusakaiori.nep.im.service.core.message.service;

import com.example.nep.im.common.enums.status.*;
import com.fuyusakaiori.nep.im.service.config.NepApplicationConfig;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class NepCheckSendMessageService {


    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;

    @Autowired
    private INepGroupMapper groupMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private NepApplicationConfig applicationConfig;

    /**
     * <h3>单聊消息的前置校验</h3>
     */
    public boolean checkChatP2PMessageSend(int appId, int senderId, int receiverId){
        // 1. 校验双方是否为好友
        if (applicationConfig.isSendMessageCheckFriendship()){
            // 1.1 校验好友关系的状态: 0 表示双方都是好友, 1 表示双方彼此不是好友, 2 表示左侧不是右侧的好友, 3 表示右侧不是左侧的好友
            int checkFriendshipStatus = friendshipMapper.checkBiFriendshipStatus(appId, senderId, receiverId);
            // 1.2 根据好友关系状态决定是否继续
            if (checkFriendshipStatus != NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                log.error("NepCheckSendMessageService checkChatP2PMessageSend: 双方并非好友关系不可以进行聊天 - senderId: {}, receiverId: {}", senderId, receiverId);
                return false;
            }
        }
        // 2. 校验双方彼此是否在黑名单中
        if (applicationConfig.isSendMessageCheckFriendshipBlack()){
            // 2.1 校验黑名单关系
            int checkBlackStatus = friendshipBlackMapper.checkBiFriendInBlackList(appId, senderId, receiverId);
            // 2.2 根据拉黑关系决定是否继续: 先简单化, 只要有一方拉黑就不允许发送消息
            if (checkBlackStatus != NepFriendshipBlackStatus.WHITE.getStatus())   {
                log.error("NepChatP2PMessageService handleMessage: 双方存在拉黑关系不可以进行聊天 - senderId: {}, receiverId: {}", senderId, receiverId);
                return false;
            }
        }
        return true;
    }


    /**
     * <h3>群聊消息的前置校验</h3>
     */
    public boolean checkChatGroupMessageSend(int appId, int senderId, int groupId){
        // 1. 查询消息发送者
        NepUser user = userMapper.queryUserById(appId, senderId);
        // 2. 校验消息发送者是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepCheckSendMessageService checkChatGroupMessageSend: 群聊消息的发送者不存在 - appId: {}, senderId: {}, groupId: {}", appId, senderId, groupId);
            return false;
        }
        // 3. 查询群聊
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 4. 校验群聊是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepCheckSendMessageService checkChatGroupMessageSend: 消息发送的群聊是不存在的 - appId: {}, senderId: {}, groupId: {}", appId, senderId, groupId);
            return false;
        }
        // 5. 查询用户是否在群聊中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, senderId);
        // 6. 校验群聊成员是否存在
        if (Objects.isNull(groupMember)
                    || Objects.nonNull(groupMember.getGroupMemberExitTime())
                    || Objects.nonNull(groupMember.getGroupMemberExitType())){
            log.error("NepCheckSendMessageService checkChatGroupMessageSend: 发送消息的用户不在群聊中或者已经退出群聊 - appId: {}, senderId: {}, groupId: {}", appId, senderId, groupId);
            return false;
        }
        // 7. 校验用户是否被禁言
        Long muteEndTime = groupMember.getGroupMemberMuteEndTime();
        if (Objects.nonNull(muteEndTime) && muteEndTime > System.currentTimeMillis()){
            log.error("NepCheckSendMessageService checkChatGroupMessageSend: 发送消息的用户现在正在被禁言 - appId: {}, senderId: {}, groupId: {}", appId, senderId, groupId);
            return false;
        }
        // 8. 校验群聊是否开启全局禁言: 仅允许群主和管理员发言
        if (group.isMute() && groupMember.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepCheckSendMessageService checkChatGroupMessageSend: 现在群聊正在开启全局禁言, 仅允许管理员和群主发言 - appId: {}, senderId: {}, groupId: {}", appId, senderId, groupId);
            return false;
        }
        // 9. 其余情况都允许发言
        return true;
    }


}
