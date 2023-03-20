package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipGroupMemberServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    public int doMoveFriendshipGroupMember(NepMoveFriendshipGroupMemberRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer oldGroupId = request.getOldGroupId();
        Integer newGroupId = request.getNewGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        // 2. 查询分组
        NepFriendshipGroup oldFriendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(appId, oldGroupId);
        NepFriendshipGroup newFriendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(appId, newGroupId);
        // 3. 校验分组是否存在:
        if (Objects.isNull(newFriendshipGroup) || Objects.isNull(oldFriendshipGroup)){
            log.error("NepFriendshipApplicationService doMoveFriendshipGroupMember: 好友分组不存在 - request: {}", request);
            return 0;
        }
        // 4. 查询用户是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupMemberServiceImpl doMoveFriendshipGroupMember: 用户不存在! - request: {}", request);
            return 0;
        }
        // 5. 如果用户已经在分组中, 那么就更新到其他分组
        int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(appId, oldGroupId, newGroupId, groupMemberId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipApplicationService doMoveFriendshipGroupMember: 将好友移入新的分组失败 - request: {}", request);
            return result;
        }

        // TODO 7. 通知用户的其他客户端
        return result;
    }

    public int doAddFriendshipGroupMember(NepAddFriendshipGroupMemberRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        // 2. 查询用户是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupMemberServiceImpl doAddFriendshipGroupMember: 用户不存在! - request: {}", request);
            return 0;
        }
        // 3. 检查分组是否存在
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(appId, groupId);
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipGroupMemberServiceImpl doAddFriendshipGroupMember: 用户分组不存在! - request: {}", request);
            return 0;
        }
        // 4. 添加用户到分组中
        int result = friendshipGroupMemberMapper.addFriendshipGroupMember(appId, groupId, groupMemberId, System.currentTimeMillis(), System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipApplicationService doAddFriendshipGroupMember: 将好友添加到分组中失败 - request: {}", request);
        }
        return result;
    }

    public int doDeleteFriendshipGroupMember(NepDeleteFriendshipGroupMemberRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        // 2. 查询用户是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupMemberServiceImpl doDeleteFriendshipGroupMember: 用户不存在! - request: {}", request);
            return 0;
        }
        // 3. 检查分组是否存在
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(appId, groupId);
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipGroupMemberServiceImpl doDeleteFriendshipGroupMember: 用户分组不存在! - request: {}", request);
            return 0;
        }
        // 4. 添加用户到分组中
        int result = friendshipGroupMemberMapper.removeFriendshipGroupMember(appId, groupId, groupMemberId);
        if (result <= 0){
            log.error("NepFriendshipApplicationService doDeleteFriendshipGroupMember: 从好友分组中移除好友失败 - request: {}", request);
        }
        return result;
    }
}
