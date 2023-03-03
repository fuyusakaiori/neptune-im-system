package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.code.NepUserResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryFriendUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepFriendUserMapper;
import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendUserService;
import com.fuyusakaiori.nep.im.service.util.NepCheckUserParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepFriendUserService implements INepFriendUserService {

    @Autowired
    private INepFriendUserMapper friendUserMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;


    @Override
    public NepQueryFriendUserResponse queryAllFriendUser(NepQueryAllFriendUserRequest request) {
        // 0. 准备响应结果
        NepQueryFriendUserResponse response = new NepQueryFriendUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryAllFriendUserRequestParam(request)){
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
        List<NepUser> friendList = friendUserMapper.queryFriendUserByIdList(header.getAppId(), friendIdList);
        // 4. 检查结果
        if (CollectionUtil.isEmpty(friendList)){
            log.info("NepFriendUserService queryAllFriendUser: 没有根据用户的好友关系查询到相应的用户 - request: {}", request);
            return response.setFriendUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.QUERY_USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.QUERY_USER_LIST_EMPTY.getMessage());
        }
        // 5. 填充响应结果
        return response.setFriendUserList(transferNepUserToNepFriendUser(friendList))
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryFriendUserResponse queryFriendUser(NepQueryFriendUserRequest request) {
        // 0. 准备响应结果
        NepQueryFriendUserResponse response = new NepQueryFriendUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryFriendUserRequestParam(request)){
            log.error("NepFriendUserService queryFriendUser: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        String friendName = request.getFriendName();
        // 3. 判断请求携带的查询条件
        if (Objects.nonNull(friendToId)){
            // 3.1 根据用户 ID 查询好友
            List<NepFriendUser> userList = queryFriendUserById(request, header, friendFromId, friendToId);
            return response.setFriendUserList(userList)
                           .setCode(NepBaseResponseCode.SUCCESS.getCode())
                           .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }else if (Objects.nonNull(friendName)){
            // 3.2 根据用户昵称或者备注查询好友
            List<NepFriendUser> userList = queryFriendUserByName(header, friendFromId, friendName);
            return response.setFriendUserList(userList)
                           .setCode(NepBaseResponseCode.SUCCESS.getCode())
                           .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }
        // 4. 返回异常
        return response.setFriendUserList(Collections.emptyList())
                       .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                       .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
    }

    private List<NepFriendUser> queryFriendUserByName(NepRequestHeader header, Integer friendFromId, String friendName) {
        // 1. 传入的可能是用户名或者备注, 所以分别查询两次后合并
        // 2. 根据备注查询
        Set<Integer> friendIdSetByRemark = friendshipMapper.queryFriendshipByRemark(header.getAppId(), friendFromId, friendName)
                                                   .stream().map(NepFriendship::getFriendshipToId)
                                                   .collect(Collectors.toSet());
        // 3. 根据昵称查询
        Set<Integer> friendIdSetByNickName = friendUserMapper.queryFriendUserByNickName(header.getAppId(), friendName)
                                                     .stream().map(NepUser::getUserId).collect(Collectors.toSet());
        // 4 合并查询结果
        friendIdSetByRemark.addAll(friendIdSetByNickName);
        // 5 校验查询结果
        if (CollectionUtil.isEmpty(friendIdSetByRemark)){
            log.error("NepFriendUserService queryFriendUserByName: 没有查询到任何好友 - fromId: {}, name: {}", friendFromId, friendName);
            return Collections.emptyList();
        }
        // 3.2.5 查询结果集
        List<NepUser> userList = friendUserMapper.queryFriendUserByIdList(header.getAppId(), new ArrayList<>(friendIdSetByRemark));
        if (CollectionUtil.isEmpty(userList)){
            log.error("NepFriendUserService queryFriendUserByName: 没有查询到任何符合条件的用户 - fromId: {}, name: {}", friendFromId, friendName);
            return Collections.emptyList();
        }
        return transferNepUserToNepFriendUser(userList);
    }

    private List<NepFriendUser> queryFriendUserById(NepQueryFriendUserRequest request, NepRequestHeader header, Integer friendFromId, Integer friendToId) {
        // 3.1 如果好友 ID 不为空, 那么使用好友 ID 查询
        // 3.1.1 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 3.1.2 判断好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendUserService queryFriendUserById: 好友关系不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 3.1.2 查询用户信息
        NepUser user = friendUserMapper.queryFriendUserById(header.getAppId(), friendToId);
        // 3.1.3 判断用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendUserService queryFriendUserById: 用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        return transferNepUserToNepFriendUser(Collections.singletonList(user));
    }

    private List<NepFriendUser> transferNepUserToNepFriendUser(List<NepUser> userList){
        return userList.stream().map(user -> new NepFriendUser().setUserId(user.getUserId())
                                              .setNickName(user.getUserNickName())
                                              .setAvatarAddress(user.getUserAvatarAddress())
                                              .setSelfSignature(user.getUserSelfSignature()))
                                              .collect(Collectors.toList());
    }
}
