package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Transactional(rollbackFor = Exception.class)
    public List<NepFriendshipGroup> doCreateFriendshipGroup(NepCreateFriendshipGroupRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupOwnerId = request.getGroupOwnerId();
        // 2. 查询用户
        NepUser user = userMapper.queryUserById(appId, groupOwnerId);
        // 3. 校验用户是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepFriendshipGroupService doCreateFriendshipGroup: 创建好友分组的用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 4. 直接创建分组
        int result = friendshipGroupMapper.createFriendshipGroup(appId, BeanUtil.copyProperties(request, NepFriendshipGroup.class));
        if (result <= 0){
            log.error("NepFriendshipGroupService doCreateFriendshipGroup: 好友分组创建失败 - request: {}", request);
            return Collections.emptyList();
        }
        // 5. 全部查询出来: 因为好友分组主键是自增的, 而好友分组的名称是可以重复的, 所以导致新增的时候没有办法直接获取到刚才新增的, 后面考虑下有没有什么更好的解决方法
        List<NepFriendshipGroup> friendshipGroupList = friendshipGroupMapper.queryAllFriendshipGroup(appId, groupOwnerId);
        // 6. 检查集合是否为空
        if (CollectionUtil.isEmpty(friendshipGroupList)){
            return Collections.emptyList();
        }
        return friendshipGroupList;
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
        int isDeleteGroup = friendshipGroupMapper.deleteFriendshipGroup(appId, groupId, System.currentTimeMillis());
        if (isDeleteGroup <= 0){
            log.error("NepFriendshipGroupService doDeleteFriendshipGroup: 好友分组删除失败 - request: {}", request);
            return isDeleteGroup;
        }
        // 5. 查询该好友分组下的成员
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryAllFriendshipGroupMemberInGroup(appId, groupId);
        // 6. 判断好友成员是否为空
        if (CollectionUtil.isEmpty(friendshipGroupMemberList)){
            log.info("NepFriendshipGroupService doDeleteFriendshipGroup: 该好友分组下没有任何成员 - request: {}", request);
            return isDeleteGroup;
        }
        // 7. 清空好友分组下的所有成员
        int isClearGroupMember = friendshipGroupMemberMapper.clearFriendshipGroupMember(appId, groupId);
        if (isClearGroupMember <= 0){
            log.error("NepFriendshipGroupService doDeleteFriendshipGroup: 好友分组下的好友成员删除失败 - request: {}", request);
            return isClearGroupMember;
        }
        // TODO 6. 通知该用户的其他客户端
        return isClearGroupMember;
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
