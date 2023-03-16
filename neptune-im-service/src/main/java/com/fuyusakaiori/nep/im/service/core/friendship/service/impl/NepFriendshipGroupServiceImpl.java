package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipGroupMemberResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipGroupResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipGroupServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Transactional(rollbackFor = Exception.class)
    public int doCreateFriendshipGroup(NepCreateFriendshipGroupRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer groupOwnerId = request.getGroupOwnerId();
        // 2. 查询用户
        NepUser user = userMapper.queryUserById(header.getAppId(), groupOwnerId);
        // 3. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupService doCreateFriendshipGroup: 创建分组的用户不存在 - request: {}", request);
            return 0;
        }
        // 4. 直接创建分组
        int result = friendshipGroupMapper.createFriendshipGroup(header.getAppId(),
                BeanUtil.copyProperties(request, NepFriendshipGroup.class, "header"));
        if (result <= 0){
            log.error("NepFriendshipGroupService doCreateFriendshipGroup: 分组创建失败 - request: {}", request);
            return result;
        }
        // TODO 5. 通知用户的其他客户端
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int doDeleteFriendshipGroup(NepDeleteFriendshipGroupRequest request) {
        // 1. 获取变量
        Integer groupId = request.getGroupId();
        Integer appId = request.getHeader().getAppId();
        // 2. 查询好友分组
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(appId, groupId);
        // 3. 校验好友分组是否存在
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipGroupService doDeleteFriendshipGroup: 好友分组不存在 - request: {}", request);
            return 0;
        }
        // 4. 先删除好友分组
        int result = friendshipGroupMapper.deleteFriendshipGroup(appId, groupId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipGroupService doDeleteFriendshipGroup: 好友分组删除失败 - request: {}", request);
            return result;
        }
        // 5. 再删除该好友分组下的成员
        result = friendshipGroupMemberMapper.clearFriendshipGroupMember(appId, groupId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipGroupService doDeleteFriendshipGroup: 好友分组下的好友成员删除失败 - request: {}", request);
            return result;
        }
        // TODO 6. 通知该用户的其他客户端
        return result;
    }

    public List<NepFriendshipGroup> doQueryAllFriendshipGroup(NepQueryAllFriendshipGroupRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupOwnerId = request.getGroupOwnerId();
        // 2. 查询用户
        NepUser user = userMapper.queryUserById(appId, groupOwnerId);
        // 3. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupService doQueryAllFriendshipGroup: 该用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 4. 查询所有好友分组
        List<NepFriendshipGroup> friendshipGroupList = friendshipGroupMapper.queryAllFriendshipGroup(appId, groupOwnerId);
        // 5. 校验好友分组集合是否为空
        if (CollectionUtil.isEmpty(friendshipGroupList)){
            log.error("NepFriendshipGroupService doQueryAllFriendshipGroup: 该用户没有创建任何好友分组 - request: {}", request);
            return Collections.emptyList();
        }
        return friendshipGroupList;
    }
}
