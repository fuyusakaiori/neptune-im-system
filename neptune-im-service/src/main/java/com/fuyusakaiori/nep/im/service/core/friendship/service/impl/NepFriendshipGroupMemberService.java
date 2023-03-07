package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipGroupMemberResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipGroupResponseCode;
import com.example.neptune.im.common.enums.code.NepUserResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupMemberService;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendGroupMemberParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepFriendshipGroupMemberService implements INepFriendshipGroupMemberService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Override
    public NepModifyFriendshipGroupMemberResponse addFriendshipGroupMember(NepAddFriendshipGroupMemberRequest request) {
        // 0. 准备返回结果
        NepModifyFriendshipGroupMemberResponse response = new NepModifyFriendshipGroupMemberResponse();
        // 1. 参数校验
        if (!NepCheckFriendGroupMemberParamUtil.checkAddFriendshipGroupMemberRequestParam(request)){
            log.error("NepFriendshipApplicationService addFriendshipGroupMember: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer groupId = request.getGroupId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 3. 查询分组
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(header.getAppId(), groupId);
        // 4. 校验分组是否存在
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipApplicationService addFriendshipGroupMember: 好友分组不存在 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 5. 查询好友分组中的成员
        List<Integer> groupJoinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(header.getAppId(), groupMemberIdList);
        // 6. 计算没有加入任何分组的成员
        List<Integer> groupDisjointMemberIdList = CollectionUtil.subtractToList(groupMemberIdList, groupJoinedMemberIdList);
        // 7. 首先向好友分组中添加没有加入任何分组的成员
        if (CollectionUtil.isNotEmpty(groupDisjointMemberIdList)){
            // 7.1 查询这些没有加入分组的用户是否存在
            List<NepUser> userList = userMapper.querySimpleUserByIdList(header.getAppId(), groupDisjointMemberIdList);
            if (CollectionUtil.isEmpty(userList) || userList.size() != groupDisjointMemberIdList.size()){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: 移入好友分组中的好友不存在或者部分不存在 - request: {}", request);
                return response.setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                               .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
            }
            // 7.2 向好友分组中添加好友
            int result = friendshipGroupMemberMapper.addFriendshipGroupMember(header.getAppId(), groupId, groupDisjointMemberIdList, System.currentTimeMillis(), System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: 移入好友分组失败 - request: {}", request);
                return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_ADD_FAIL.getCode())
                               .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_ADD_FAIL.getMessage());
            }
        }
        // 8. 然后变更已经在好友分组的成员
        if (CollectionUtil.isNotEmpty(groupJoinedMemberIdList)){
            int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(header.getAppId(), groupId, groupJoinedMemberIdList, System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: 变更好友分组失败 - request: {}", request);
                return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getCode())
                               .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getMessage());
            }
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepModifyFriendshipGroupMemberResponse moveFriendshipGroupMember(NepMoveFriendshipGroupMemberRequest request) {
        // 0. 准备返回结果
        NepModifyFriendshipGroupMemberResponse response = new NepModifyFriendshipGroupMemberResponse();
        // 1. 参数校验
        if (!NepCheckFriendGroupMemberParamUtil.checkMoveFriendshipGroupMemberRequestParam(request)){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer groupId = request.getGroupId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 3. 查询分组
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(header.getAppId(), groupId);
        // 4. 校验分组是否存在
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 好友分组不存在 - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 5. 查询用户所在的分组
        List<Integer> groupJoinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(header.getAppId(), groupMemberIdList);
        // 6. 校验用户是否已有分组
        if (CollectionUtil.isEmpty(groupJoinedMemberIdList) || groupJoinedMemberIdList.size() != groupMemberIdList.size()){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 好友此前没有任何分组, 无法变更 - request: {}", request);
            return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_NOT_EXIST.getMessage());
        }
        // 7. 变更分组
        int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(header.getAppId(), groupId, groupMemberIdList, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 好友所在分组变更失败 - request: {}", request);
            return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getCode())
                           .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }
}
