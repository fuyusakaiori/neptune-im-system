package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipGroupMemberResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipGroupResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepCreateFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupService;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendGroupParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipGroupService implements INepFriendshipGroupService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Override
    public NepModifyFriendshipGroupResponse createFriendshipGroup(NepCreateFriendshipGroupRequest request) {
        // 0. 准备返回结果
        NepModifyFriendshipGroupResponse response = new NepModifyFriendshipGroupResponse();
        // 1. 校验参数
        if (!NepCheckFriendGroupParamUtil.checkCreateFriendshipGroupRequestParam(request)){
            log.error("NepFriendshipGroupService createFriendshipGroup: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer groupOwnerId = request.getGroupOwnerId();
        String groupName = request.getGroupName();
        // 3. 查询用户
        NepUser user = userMapper.querySimpleUserById(header.getAppId(), groupOwnerId);
        // 4. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupService createFriendshipGroup: 创建分组的用户不存在 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_OWNER_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_OWNER_NOT_EXIST.getMessage());
        }
        // 5. 直接创建分组
        int result = friendshipGroupMapper.createFriendshipGroup(header.getAppId(), transferToFriendshipGroup(groupOwnerId, groupName), System.currentTimeMillis(), System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipGroupService createFriendshipGroup: 分组创建失败 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_CREATE_FAIL.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_CREATE_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepModifyFriendshipGroupResponse deleteFriendshipGroup(NepDeleteFriendshipGroupRequest request) {
        // 0. 准备返回结果
        NepModifyFriendshipGroupResponse response = new NepModifyFriendshipGroupResponse();
        // 1. 校验参数
        if (!NepCheckFriendGroupParamUtil.checkDeleteFriendshipGroupRequestParam(request)){
            log.error("NepFriendshipGroupService deleteFriendshipGroup: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer groupId = request.getGroupId();
        // 3. 查询好友分组
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(header.getAppId(), groupId);
        // 4. 校验好友分组是否存在
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipGroupService deleteFriendshipGroup: 好友分组不存在 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 5. 先删除好友分组
        int result = friendshipGroupMapper.deleteFriendshipGroup(header.getAppId(), groupId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipGroupService deleteFriendshipGroup: 好友分组删除失败 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_DELETE_FAIL.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_DELETE_FAIL.getMessage());
        }
        // 6. 再删除该好友分组下的成员
        result = friendshipGroupMemberMapper.clearFriendshipGroupMember(header.getAppId(), groupId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipGroupService deleteFriendshipGroup: 好友分组下的好友成员删除失败 - request: {}", request);
            return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_CLEAR_FAIL.getCode())
                           .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_CLEAR_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryFriendshipGroupResponse queryAllFriendshipGroup(NepQueryAllFriendshipGroupRequest request) {
        // 0. 准备返回结果
        NepQueryFriendshipGroupResponse response = new NepQueryFriendshipGroupResponse();
        // 1. 校验参数
        if (!NepCheckFriendGroupParamUtil.checkQueryAllFriendshipGroupRequestParam(request)){
            log.error("NepFriendshipGroupService queryAllFriendshipGroup: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer groupOwnerId = request.getGroupOwnerId();
        // 3. 查询用户
        NepUser user = userMapper.querySimpleUserById(header.getAppId(), groupOwnerId);
        // 4. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendshipGroupService queryAllFriendshipGroup: 该用户不存在 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_OWNER_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_OWNER_NOT_EXIST.getMessage());
        }
        // 5. 查询所有好友分组
        List<NepFriendshipGroup> friendshipGroupList = friendshipGroupMapper.queryAllFriendshipGroup(header.getAppId(), groupOwnerId);
        // 6. 校验好友分组集合是否为空
        if (CollectionUtil.isEmpty(friendshipGroupList)){
            log.error("NepFriendshipGroupService queryAllFriendshipGroup: 该用户没有创建任何好友分组 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        return response.setGroupList(friendshipGroupList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    private NepCreateFriendshipGroup transferToFriendshipGroup(Integer groupOwnerId, String groupName){
        return new NepCreateFriendshipGroup()
                       .setGroupOwnerId(groupOwnerId)
                       .setGroupName(groupName);
    }
}
