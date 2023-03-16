package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
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

    public int doAddFriendshipGroupMember(NepAddFriendshipGroupMemberRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 2. 查询分组
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(header.getAppId(), groupId);
        // 3. 校验分组是否存在
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipApplicationService doAddFriendshipGroupMember: 好友分组不存在 - request: {}", request);
            return 0;
        }
        // 4. 查询好友分组中的成员
        List<Integer> groupJoinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(header.getAppId(), groupMemberIdList);
        // 5. 计算没有加入任何分组的成员
        List<Integer> groupDisjointMemberIdList = CollectionUtil.subtractToList(groupMemberIdList, groupJoinedMemberIdList);
        // 6. 首先向好友分组中添加没有加入任何分组的成员
        if (CollectionUtil.isNotEmpty(groupDisjointMemberIdList)){
            // 6.1 查询这些没有加入分组的用户是否存在
            List<NepUser> userList = userMapper.queryUserByIdList(header.getAppId(), groupDisjointMemberIdList);
            if (CollectionUtil.isEmpty(userList) || userList.size() != groupDisjointMemberIdList.size()){
                log.error("NepFriendshipApplicationService doAddFriendshipGroupMember: 移入好友分组中的好友不存在或者部分不存在 - request: {}", request);
                return 0;
            }
            // 6.2 向好友分组中添加好友
            int result = friendshipGroupMemberMapper.addFriendshipGroupMember(header.getAppId(), groupId, groupDisjointMemberIdList, System.currentTimeMillis(), System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: 移入好友分组失败 - request: {}", request);
                return result;
            }
            return result;
        }
        // 7. 然后变更已经在好友分组的成员
        if (CollectionUtil.isNotEmpty(groupJoinedMemberIdList)){
            int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(header.getAppId(), groupId, groupJoinedMemberIdList, System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationService doAddFriendshipGroupMember: 变更好友分组失败 - request: {}", request);
                return result;
            }
            return result;
        }
        // TODO 8. 通知用户的其他客户端
        return 0;
    }

    public int doMoveFriendshipGroupMember(NepMoveFriendshipGroupMemberRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 2. 查询分组
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(appId, groupId);
        // 3. 校验分组是否存在
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipApplicationService doMoveFriendshipGroupMember: 好友分组不存在 - request: {}", request);
            return 0;
        }
        // 4. 查询用户所在的分组
        List<Integer> groupJoinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(appId, groupMemberIdList);
        // 5. 校验用户是否已有分组
        if (CollectionUtil.isEmpty(groupJoinedMemberIdList) || groupJoinedMemberIdList.size() != groupMemberIdList.size()){
            log.error("NepFriendshipApplicationService doMoveFriendshipGroupMember: 存在好友此前没有任何分组, 无法变更 - request: {}", request);
            return 0;
        }
        // 6. 变更分组
        int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(appId, groupId, groupMemberIdList, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 好友所在分组变更失败 - request: {}", request);
            return result;
        }
        // TODO 7. 通知用户的其他客户端
        return result;
    }
}
