package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
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
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
    private NepFriendshipApplicationServiceImpl friendshipApplicationServiceImpl;

    @Autowired
    private NepServiceToGateWayMessageProducer messageProducer;

    @Transactional(rollbackFor = Exception.class)
    public int doAddFriendship(NepAddFriendshipRequest request){
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // TODO 2. 添加好友前执行回调

        // 3. 查询用户是否存在: 没有直接查关系表是因为需要判断用户是否可以添加好友
        NepUser fromUser = userMapper.queryUserById(appId, friendFromId);
        NepUser toUser = userMapper.queryUserById(appId, friendToId);
        if (Objects.isNull(fromUser) || fromUser.isDelete() || Objects.isNull(toUser) || toUser.isDelete()){
            log.error("NeptuneFriendshipService doAddFriendship: 新增的好友关系中有一方用户是不存在的 - fromUser: {}, toUser: {}", fromUser, toUser);
            return 0;
        }
        // 4. 检查用户是否可以添加好友
        Integer type = toUser.getFriendshipAllowType();
        if (Objects.isNull(type)){
            log.error("NeptuneFriendshipService doAddFriendship: 用户添加好友类型为空 - fromUser: {},  toUser: {}", fromUser, toUser);
            return 0;
        }
        // 4.1 如果对方不允许添加好友, 那么直接返回
        if (type == NepFriendshipAllowType.BAN.getType()){
            log.info("NeptuneFriendshipService doAddFriendship: 对方不允许添加好友 - fromUser: {}, toUser: {}", fromUser, toUser);
            return NepFriendshipAllowType.BAN.getType();
        }
        // 4.2 如果对方不需要验证就能添加好友, 那么直接添加
        if (type == NepFriendshipAllowType.ANY.getType()){
            int result = friendshipDealService.doAddFriendshipDirectly(appId, BeanUtil.copyProperties(request, NepFriendship.class, "header"));
            if (result <= 0){
                log.error("NeptuneFriendshipService doAddFriendship - ANY: 好友添加失败 - fromUser: {},  toUser: {}", fromUser, toUser);
                return 0;
            }
            return NepFriendshipAllowType.ANY.getType();
        }
        // 4.3 如果对方需要验证才能添加, 那么走验证后添加的逻辑: 1. 发送好友申请后就结束 2. 对方审批好友申请后执行添加方法
        if (type == NepFriendshipAllowType.VALIDATION.getType()){
            int result = friendshipApplicationServiceImpl.doSendFriendshipApplication(request);
            if (result <= 0){
                log.error("NeptuneFriendshipService doAddFriendship - VALIDATION: 好友添加失败 - fromUser: {},  toUser: {}", fromUser, toUser);
                return 0;
            }
            return NepFriendshipAllowType.VALIDATION.getType();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int doEditFriendshipRemark(NepEditFriendshipRemarkRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        String friendRemark = request.getFriendRemark();
        // 2. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 3. 检查好友关系是否存在
        if (Objects.nonNull(friendship) && friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            // 3.1.1 如果好友关系双向存在, 那么直接更新
            int result = friendshipMapper.editFriendshipRemark(header.getAppId(), friendFromId, friendToId, friendRemark, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService doEditFriendshipRemark: 更新好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
            }
            // TODO 3.1.2 通知用户的其他客户端

            // TODO 3.1.3 执行回调
            return result;
        }else{
            // 3.2 如果好友关系不存在, 那么返回不存在
            log.error("NeptuneFriendshipService doEditFriendshipRemark: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int doReleaseFriendship(NepReleaseFriendshipRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 2. 查询好友关系: 双向查询 - 如果用户表中不存在用户, 那么关系表理论上是查不出来的
        NepFriendship friendship = friendshipMapper.queryFriendshipById(appId, friendFromId, friendToId);
        // 3. 判断好友关系
        if (Objects.nonNull(friendship) && friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            // 3.1.1 删除好友关系
            int result = friendshipMapper.releaseFriendship(appId, friendFromId, friendToId, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService doReleaseFriendship: 好友关系删除失败 - fromId: {}, toId: {}", friendFromId, friendToId);
            }
            // 3.1.2 查询好友所在的分组
            NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupByMemberIdAndOwnerId(appId, friendToId, friendFromId);
            // 3.1.3 删除好友所在的分组
            if (Objects.isNull(friendshipGroup)){
                return result;
            }
            result = friendshipGroupMemberMapper.removeFriendshipGroupMember(appId, friendshipGroup.getGroupId(), friendToId);
            if (result <= 0){
                log.error("NeptuneFriendshipService doReleaseFriendship: 移除好友所在分组失败 - fromId: {}, toId: {}", friendFromId, friendToId);
            }
            // TODO 通知用户的其他客户端和被删除的用户的所有客户端

            // TODO 执行回调
            return result;
        }else{
            // 3.2 好友关系不存在
            log.error("NeptuneFriendshipService releaseFriendship: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }

    }
    @Transactional(rollbackFor = Exception.class)
    public int doReleaseAllFriendship(NepReleaseAllFriendshipRequest request) {
        // 1. 获取变量
        Integer fromId = request.getFriendFromId();
        Integer appId = request.getHeader().getAppId();
        // 2. 查询用户的所有好友关系
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(appId, fromId);
        // 3. 检查好友关系是否为空
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NeptuneFriendshipService doReleaseAllFriendship: 用户不存在好友关系或者用户不存在 - request: {}", request);
            return 0;
        }
        // 4. 清空用户的所有好友关系
        int result = friendshipMapper.releaseAllFriendship(appId, fromId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NeptuneFriendshipService doReleaseAllFriendship: 删除所有好友关系失败 - request: {}", request);
            return result;
        }
        // TODO 5. 通知用户的其他客户端和被删除用户的所有客户端

        // TODO 6.执行回调

        return result;
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
