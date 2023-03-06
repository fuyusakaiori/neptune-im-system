package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.code.NepUserResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendService;
import com.fuyusakaiori.nep.im.service.util.NepCheckUserParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepFriendService implements INepFriendService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Override
    public NepQueryUserResponse queryAllFriend(NepQueryAllFriendRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryAllFriendRequestParam(request)){
            log.error("NepFriendUserService queryAllFriendUser: 请求头中的参数检查失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        // 3. 查询用户的好友
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(header.getAppId(), friendFromId);
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NepFriendUserService queryAllFriendUser: 用户没有任何好友 - request: {}", request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 4. 根据用户的好友关系查询每个好友的简易信息
        List<Integer> friendIdList = friendshipList.stream()
                                             .map(NepFriendship::getFriendshipToId)
                                             .collect(Collectors.toList());
        List<NepUser> friendList = userMapper.querySimpleUserByIdList(header.getAppId(), friendIdList);
        // 4. 检查结果
        if (CollectionUtil.isEmpty(friendList)){
            log.info("NepFriendUserService queryAllFriendUser: 没有根据用户的好友关系查询到相应的用户 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.QUERY_USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.QUERY_USER_LIST_EMPTY.getMessage());
        }
        // 5. 填充响应结果
        return response.setUserList(friendList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryUserResponse queryFriendByAccount(NepQueryFriendByAccountRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryFriendByAccountRequestParam(request)){
            log.error("NepFriendUserService queryFriendByAccount: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String userAccount = request.getUserAccount();
        // 注: 因为无法直接通过账号查询好友关系, 所以先查询用户, 然后再判断是否是好友
        // 3. 查询用户
        NepUser user = userMapper.querySimpleUserByAccount(header.getAppId(), userAccount);
        // 4. 判断用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendUserService queryFriendByAccount: 参数校验失败 - request: {}", request);
            return response.setCode(NepUserResponseCode.QUERY_USER_NOT_EXIST.getCode())
                           .setMessage(NepUserResponseCode.QUERY_USER_NOT_EXIST.getMessage());
        }
        // 5. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, user.getUserId());
        // 6. 判断好友关系是否存在
        if (Objects.isNull(friendship) || friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_RELEASE.getStatus()){
            log.error("NepFriendUserService queryFriendByAccount: 好友关系不存在 - from: {}, to: {}, request: {}", friendFromId, user.getUserId(), request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 7. 如果存在, 那么直接返回该用户
        return response.setUserList(Collections.singletonList(user))
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryUserResponse queryFriendByName(NepQueryFriendByNameRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryFriendByNameRequestParam(request)){
            log.error("NepFriendUserService queryFriendByName: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String friendName = request.getFriendName();
        // 注: 传入的可能是用户名或者备注, 所以分别查询两次后合并
        // 3. 根据备注查询好友关系
        List<NepUser> friendByRemark = getFriendByRemark(header, friendFromId, friendName);
        // 注: 因为没有办法直接通过昵称查询好友关系, 所以还是先查询用户 (其实可以用联表查询直接找到用户)
        // 5. 根据昵称查询用户
        List<NepUser> friendByNickName = getFriendByNickName(header, friendFromId, friendName);
        // 6. 合并查询结果
        List<NepUser> friends = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(friendByRemark)){
            friends.addAll(friendByRemark);
        }
        if (CollectionUtil.isNotEmpty(friendByNickName)){
            friends.addAll(friendByNickName);
        }
        // 7. 校验查询结果
        if (CollectionUtil.isEmpty(friends)){
            log.error("NepFriendUserService queryFriendByName: 没有查询到任何好友 - fromId: {}, name: {}", friendFromId, friendName);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        return response.setUserList(friends)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    private List<NepUser> getFriendByNickName(NepRequestHeader header, Integer friendFromId, String friendName) {
        // 1. 根据昵称查询用户
        List<NepUser> userList = userMapper.querySimpleUserByNickName(header.getAppId(), friendName);
        if (CollectionUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        // 2. 查询好友关系
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByIdList(header.getAppId(), friendFromId,
                userList.stream().map(NepUser::getUserId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(friendshipList)){
            return Collections.emptyList();
        }
        // 3. 确认好友
        List<NepUser> friendByNickName = new ArrayList<>();
        Set<Integer> friendIdSet = friendshipList.stream()
                                           .map(NepFriendship::getFriendshipToId)
                                           .collect(Collectors.toSet());
        for (NepUser user : userList) {
            if (friendIdSet.contains(user.getUserId())){
                friendByNickName.add(user);
            }
        }
        return friendByNickName;
    }

    private List<NepUser> getFriendByRemark(NepRequestHeader header, Integer friendFromId, String friendName) {
        // 1. 根据备注查询好友关系
        List<Integer> friendIdListByRemark = friendshipMapper.queryFriendshipByRemark(header.getAppId(), friendFromId, friendName)
                                                   .stream().map(NepFriendship::getFriendshipToId)
                                                   .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(friendIdListByRemark)){
            return Collections.emptyList();
        }
        // 2. 通过好友关系查询对应的用户
        List<NepUser> friendByRemark = userMapper.querySimpleUserByIdList(header.getAppId(), friendIdListByRemark);
        if (CollectionUtil.isEmpty(friendByRemark)){
            return Collections.emptyList();
        }
        return friendByRemark;
    }

    @Override
    public NepQueryUserResponse queryAllFriendBlackList(NepQueryAllFriendBlackRequest request) {
        return null;
    }

    @Override
    public NepQueryUserResponse queryAllFriendApplication(NepQueryAllFriendApplicationRequest request) {
        return null;
    }

    @Override
    public NepQueryUserResponse queryAllFriendGroupMember(NepQueryAllFriendGroupMemberRequest request) {
        return null;
    }
}
