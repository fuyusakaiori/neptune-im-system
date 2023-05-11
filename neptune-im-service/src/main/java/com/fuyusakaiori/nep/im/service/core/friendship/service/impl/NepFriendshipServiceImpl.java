package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.message.friendship.NepRemoveAllFriendMessage;
import com.example.nep.im.common.entity.proto.message.friendship.NepRemoveFriendMessage;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.message.NepFriendshipMessageType;
import com.example.nep.im.common.enums.status.NepFriendshipAllowType;
import com.example.nep.im.common.enums.status.NepFriendshipCheckType;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepAddFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepEditFriendshipRemarkRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepReleaseAllFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepReleaseFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class NepFriendshipServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Autowired
    private NepFriendshipDealService friendshipDealService;


    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doAddFriendship(NepAddFriendshipRequest request){
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendshipSenderId = request.getFriendFromId();
        Integer friendshipReceiverId = request.getFriendToId();
        // 2. 查询用户是否存在
        NepUser friendshipSender = userMapper.queryUserById(appId, friendshipSenderId);
        NepUser friendshipReceiver = userMapper.queryUserById(appId, friendshipReceiverId);
        if (Objects.isNull(friendshipSender) || friendshipSender.isDelete()
                    || Objects.isNull(friendshipReceiver) || friendshipReceiver.isDelete()){
            log.error("NepFriendshipServiceImpl doAddFriendship: 新增的好友关系中有一方用户是不存在的 - " +
                              "friendshipSender: {}, friendshipReceiver: {}, request: {}", friendshipSender, friendshipReceiver, request);
            return Collections.emptyMap();
        }
        // 3. 判断对方选择的添加好友的方式
        Integer friendshipAllowType = friendshipReceiver.getFriendshipAllowType();
        // 4. 如果对方拒绝添加好友, 直接返回好友添加方式和空的好友对象
        if (NepFriendshipAllowType.BAN.getType() == friendshipAllowType){
            log.info("NepFriendshipServiceImpl doAddFriendship: 对方用户禁止添加好友 - request: {}", request);
            return new HashMap<String, Object>(){{put("friendshipAllowType", NepFriendshipAllowType.BAN.getType());}};
        }
        // 5. 如果对方验证后添加好友, 直接返回好友添加
        if (NepFriendshipAllowType.VALIDATION.getType() == friendshipAllowType){
            log.info("NepFriendshipServiceImpl doAddFriendship: 对方选择验证后添加好友, 需要发送好友申请 - request: {}", request);
            return new HashMap<String, Object>(){{put("friendshipAllowType", NepFriendshipAllowType.ANY.getType());}};
        }
        // 6. 如果对方允许任何添加好友, 那么直接好友添加并返回
        if (NepFriendshipAllowType.ANY.getType() == friendshipAllowType){
            log.info("NepFriendshipServiceImpl doAddFriendship: 对方允许直接添加好友 - request: {}", request);
            // 6.1 直接添加好友
            NepFriend newFriend = friendshipDealService.doAddFriendshipDirectly(request.getHeader(),
                    BeanUtil.copyProperties(request, NepFriendship.class));
            // 6.2 判断返回的新增好友是否为空
            if (Objects.isNull(newFriend)){
                log.error("NepFriendshipServiceImpl doAddFriendship - 直接添加好友失败 - request: {}", request);
                return Collections.emptyMap();
            }
            return new HashMap<String, Object>(){{put("friendshipAllowType", NepFriendshipAllowType.ANY.getType());put("newFriend", newFriend);}};
        }
        // 7. 如果是其他类型的添加好友方式, 就是无效的
        return Collections.emptyMap();
    }

    @Transactional(rollbackFor = Exception.class)
    public int doEditFriendshipRemark(NepEditFriendshipRemarkRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendshipSenderId = request.getFriendFromId();
        Integer friendshipReceiverId = request.getFriendToId();
        String friendshipSenderRemark = request.getFriendRemark();
        // 2. 查询好友关系
        NepFriendship friendshipSender = friendshipMapper.queryFriendshipById(appId, friendshipSenderId, friendshipReceiverId);
        NepFriendship friendshipReceiver = friendshipMapper.queryFriendshipById(appId, friendshipReceiverId, friendshipSenderId);
        // 3. 检查好友关系是否存在
        if (Objects.nonNull(friendshipSender) && friendshipSender.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()
                    && Objects.nonNull(friendshipReceiver) && friendshipReceiver.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            int isEditFriendshipRemark = friendshipMapper.editFriendshipRemark(appId, friendshipSenderId, friendshipReceiverId, friendshipSenderRemark, System.currentTimeMillis());
            if (isEditFriendshipRemark <= 0){
                log.error("NepFriendshipServiceImpl doEditFriendshipRemark: 更新好友关系失败 - request: {}", request);
            }
            return isEditFriendshipRemark;
        }else{
            // 3.2 如果好友关系不存在, 那么返回不存在
            log.error("NepFriendshipServiceImpl doEditFriendshipRemark: 好友关系不存在 - request: {}", request);
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int doReleaseFriendship(NepReleaseFriendshipRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendshipSenderId = request.getFriendFromId();
        Integer friendshipReceiverId = request.getFriendToId();
        // 2. 查询好友关系: 双向查询 - 如果用户表中不存在用户, 那么关系表理论上是查不出来的
        NepFriendship friendshipSender = friendshipMapper.queryFriendshipById(appId, friendshipSenderId, friendshipReceiverId);
        NepFriendship friendshipReceiver = friendshipMapper.queryFriendshipById(appId, friendshipReceiverId, friendshipSenderId);
        // 3. 判断好友关系
        if (Objects.nonNull(friendshipSender) && friendshipSender.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()
                    && Objects.nonNull(friendshipReceiver) && friendshipReceiver.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            // 3.1.1 删除好友关系
            int isReleaseFriendship = friendshipMapper.releaseFriendship(appId, friendshipSenderId, friendshipReceiverId, System.currentTimeMillis());
            if (isReleaseFriendship <= 0){
                log.error("NepFriendshipServiceImpl doReleaseFriendship: 好友关系删除失败 - request: {}", request);
                return isReleaseFriendship;
            }
            // 3.1.2 查询好友所在的分组
            NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupByMemberIdAndOwnerId(appId, friendshipReceiverId, friendshipSenderId);
            // 3.1.3 删除好友所在的分组
            if (Objects.nonNull(friendshipGroup) && !friendshipGroup.isDelete()){
                int isRemoveFriendshipGroupMember = friendshipGroupMemberMapper.removeFriendshipGroupMember(appId, friendshipGroup.getGroupId(), friendshipReceiverId);
                if (isRemoveFriendshipGroupMember <= 0){
                    log.error("NepFriendshipServiceImpl doReleaseFriendship: 移除好友所在分组失败 - request: {}", request);
                    return isRemoveFriendshipGroupMember;
                }
            }
            // 3.1.4 通知对方客户端, 好友关系被解除
            int messageType = NepFriendshipMessageType.FRIEND_REMOVE.getMessageType();
            NepMessageBody messageBody = BeanUtil.copyProperties(request, NepRemoveFriendMessage.class)
                                                 .setMessageType(messageType);
            messageSender.sendMessage(appId, friendshipReceiverId, messageType, messageBody);
            return isReleaseFriendship;
        }else{
            // 3.2 好友关系不存在
            log.error("NepFriendshipServiceImpl doReleaseFriendship: 好友关系不存在 - fromId: {}, toId: {}", friendshipSenderId, friendshipReceiverId);
            return 0;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public int doReleaseAllFriendship(NepReleaseAllFriendshipRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendshipSenderId = request.getFriendFromId();
        // 2. 查询用户的所有好友关系
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(appId, friendshipSenderId);
        // 3. 检查好友关系是否为空
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NepFriendshipServiceImpl doReleaseAllFriendship: 用户不存在好友关系或者用户不存在 - request: {}", request);
            return 0;
        }
        // 4. 清空用户的所有好友关系
        int isReleaseAllFriendship = friendshipMapper.releaseAllFriendship(appId, friendshipSenderId, System.currentTimeMillis());
        if (isReleaseAllFriendship <= 0){
            log.error("NepFriendshipServiceImpl doReleaseAllFriendship: 删除所有好友关系失败 - request: {}", request);
            return isReleaseAllFriendship;
        }
        // 5. 通知被删除好友关系的所有用户
        int messageType = NepFriendshipMessageType.FRIEND_ALL_REMOVE.getMessageType();
        for (NepFriendship friendship : friendshipList) {
            NepMessageBody messageBody = BeanUtil.copyProperties(request, NepRemoveAllFriendMessage.class)
                                                 .setMessageType(messageType);
            messageSender.sendMessage(appId, friendship.getFriendToId(), messageType, messageBody);
        }
        return isReleaseAllFriendship;
    }

    public int doCheckFriendship(int appId, int friendFromId, int friendToId, int checkType) {
        // 1. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(appId, friendFromId, friendToId);
        // 2. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            // 注: 如果没有查出好友关系, 那么双方肯定不是好友
            return NepFriendshipStatus.FRIENDSHIP_RELEASE.getStatus();
        }
        // 3. 校验好友关系
        if (checkType == NepFriendshipCheckType.SINGLE.getType()){
            return friendshipMapper.checkFriendshipStatus(appId, friendFromId, friendToId);
        }else {
            return friendshipMapper.checkBiFriendshipStatus(appId, friendFromId, friendToId);
        }
    }
}
