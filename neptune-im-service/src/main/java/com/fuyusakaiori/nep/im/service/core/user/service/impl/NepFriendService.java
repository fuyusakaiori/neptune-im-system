package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.*;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendGroup;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepQueryFriendApplication;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendParamUtil;
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
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Override
    public NepQueryUserResponse queryAllFriend(NepQueryAllFriendRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckFriendParamUtil.checkNepQueryAllFriendRequestParam(request)){
            log.error("NepFriendUserService queryAllFriendUser: 请求头中的参数检查失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
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
        List<NepUser> friendList = userMapper.queryUserByIdList(header.getAppId(), friendIdList);
        // 4. 检查结果
        if (CollectionUtil.isEmpty(friendList)){
            log.info("NepFriendUserService queryAllFriendUser: 没有根据用户的好友关系查询到相应的用户 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
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
        if (!NepCheckFriendParamUtil.checkNepQueryFriendByAccountRequestParam(request)){
            log.error("NepFriendUserService queryFriendByAccount: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String userAccount = request.getUserAccount();
        // 注: 因为无法直接通过账号查询好友关系, 所以先查询用户, 然后再判断是否是好友
        // 3. 查询用户
        NepUser user = userMapper.queryUserByUserName(header.getAppId(), userAccount);
        // 4. 判断用户是否存在
        if (Objects.isNull(user)){
            log.error("NepFriendUserService queryFriendByAccount: 参数校验失败 - request: {}", request);
            return response.setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                           .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
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
        if (!NepCheckFriendParamUtil.checkNepQueryFriendByNameRequestParam(request)){
            log.error("NepFriendUserService queryFriendByName: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
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
        List<NepUser> userList = userMapper.queryUserByNickName(header.getAppId(), friendName);
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
        List<NepUser> friendByRemark = userMapper.queryUserByIdList(header.getAppId(), friendIdListByRemark);
        if (CollectionUtil.isEmpty(friendByRemark)){
            return Collections.emptyList();
        }
        return friendByRemark;
    }

    @Override
    public NepQueryUserResponse queryAllFriendBlackList(NepQueryAllFriendBlackRequest request) {
        // 0. 响应结果准备
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (NepCheckFriendParamUtil.checkNepQueryAllFriendBlackListRequestParam(request)){
            log.error("NepFriendService queryAllFriendBlackList: 参数校验失败 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        // 3. 获取该用户拉黑的所用好友 ID
        List<Integer> friendToIdList = friendshipBlackMapper.queryAllFriendInBlackList(header.getAppId(), friendFromId);
        if (CollectionUtil.isEmpty(friendToIdList)){
            log.error("NepFriendService queryAllFriendBlackList: 用户没有拉黑的好友 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepFriendshipBlackResponseCode.FRIEND_BLACK_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_BLACK_NOT_EXIST.getMessage());
        }
        // 4. 查询被拉黑好友的简易信息
        List<NepUser> blackUserList = userMapper.queryUserByIdList(header.getAppId(), friendToIdList);
        if(CollectionUtil.isEmpty(blackUserList)){
            log.error("NepFriendService queryAllFriendBlackList: 用户拉黑的好友不存在 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
        }
        return response.setUserList(blackUserList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryFriendApplicationResponse queryAllFriendApplication(NepQueryAllFriendApplicationRequest request) {
        // 0. 响应结果准备
        NepQueryFriendApplicationResponse response = new NepQueryFriendApplicationResponse();
        // 1. 参数校验
        if (NepCheckFriendParamUtil.checkNepQueryAllFriendApplicationRequestParam(request)){
            log.error("NepFriendService queryAllFriendApplication: 参数校验失败 - request: {}", request);
            return response.setApplicationList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 3. 获取向该用户发出的所有好友申请
        List<NepFriendshipApplication> applicationList = friendshipApplicationMapper.queryAllFriendshipApplication(header.getAppId(), userId);
        if (CollectionUtil.isEmpty(applicationList)){
            log.error("NepFriendService queryAllFriendApplication: 用户没有接收到任何好友申请 - request: {}", request);
            return response.setApplicationList(Collections.emptyList())
                           .setCode(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getMessage());
        }
        // TODO 将所有未读的好友申请变为已读

        // 4. 查询发出申请的用户信息
        List<NepUser> userList = userMapper.queryUserByIdList(header.getAppId(), applicationList.stream()
                                                                                               .map(NepFriendshipApplication::getFriendshipFromId)
                                                                                               .collect(Collectors.toList()));
        if(CollectionUtil.isEmpty(userList)){
            log.error("NepFriendService queryAllFriendApplication: 发出好友申请的用户不存在 - request: {}", request);
            return response.setApplicationList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
        }
        // 5. 拼装返回集合
        List<NepQueryFriendApplication> friendApplicationList = transferFriendApplicationList(userId, userList, applicationList);
        // 6. 设置响应信息
        return response.setApplicationList(friendApplicationList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    /**
     * <h3>将好友信息和好友申请中的信息合并到一起</h3>
     */
    private List<NepQueryFriendApplication> transferFriendApplicationList(int friendToId, List<NepUser> userList, List<NepFriendshipApplication> applicationList) {
        // 1. 将发出好友申请的用户信息填充到返回结果中
        Map<Integer, NepQueryFriendApplication> map = userList.stream()
                                                              .map(user -> new NepQueryFriendApplication()
                                                                                   .setFriendFromId(user.getUserId())
                                                                                   .setFriendAccount(user.getUsername())
                                                                                   .setFriendNickName(user.getNickname())
                                                                                   .setFriendAvatarAddress(user.getAvatarAddress()))
                                                              .collect(Collectors.toMap(NepQueryFriendApplication::getFriendFromId, NepQueryFriendApplication -> NepQueryFriendApplication));
        // 2. 将好友申请中的信息填充到返回结果中
        for (NepFriendshipApplication application : applicationList) {
            if (map.containsKey(application.getFriendshipFromId())){
                NepQueryFriendApplication friendApplication = map.get(application.getFriendshipFromId());
                friendApplication.setFriendApplicationId(application.getFriendshipApplyId())
                         .setFriendToId(friendToId)
                         .setFriendSource(application.getApplySource())
                         .setAdditionalInfo(application.getApplyAdditionalInfo())
                         .setReadStatus(application.getApplyReadStatus())
                         .setApproveStatus(application.getApplyApproveStatus());
            }
        }
        // 3. 返回集合
        return new ArrayList<>(map.values());
    }


    @Override
    public NepQueryFriendGroupMemberResponse queryAllFriendGroupMember(NepQueryAllFriendGroupMemberRequest request) {
        // 0. 响应结果准备
        NepQueryFriendGroupMemberResponse response = new NepQueryFriendGroupMemberResponse();
        // 1. 参数校验
        if (NepCheckFriendParamUtil.checkNepQueryAllFriendGroupMemberRequestParam(request)){
            log.error("NepFriendService queryAllFriendGroupMember: 参数校验失败 - request: {}", request);
            return response.setFriendGroupMemberMap(Collections.emptyMap())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 3. 查询自己创建的所有好友分组
        List<NepFriendshipGroup> groupList = friendshipGroupMapper.queryAllFriendshipGroup(header.getAppId(), userId);
        if (CollectionUtil.isEmpty(groupList)){
            log.error("NepFriendService queryAllFriendGroupMember: 该用户没有创建任何好友分组 - request: {}", request);
            return response.setFriendGroupMemberMap(Collections.emptyMap())
                           .setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 4. 查询每个分组下的成员
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryAllFriendshipGroupMember(header.getAppId(),
                groupList.stream().map(NepFriendshipGroup::getGroupId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(friendshipGroupMemberList)){
            // TODO
        }
        Map<Integer, List<Integer>> groupIdAndGroupMemberIdList = new HashMap<>();
        for (NepFriendshipGroupMember friendshipGroupMember : friendshipGroupMemberList) {
            List<Integer> groupMemberIdList = groupIdAndGroupMemberIdList.getOrDefault(friendshipGroupMember.getFriendshipGroupId(), new ArrayList<>());
            groupMemberIdList.add(friendshipGroupMember.getFriendshipGroupMemberId());
            groupIdAndGroupMemberIdList.put(friendshipGroupMember.getFriendshipGroupId(), groupMemberIdList);
        }
        // 5. 查询每个分组中的好友信息
        Map<NepFriendGroup, List<NepFriend>> groupAndGroupMember = new HashMap<>();
        for(Map.Entry<Integer, List<Integer>> entry : groupIdAndGroupMemberIdList.entrySet()){
            Integer groupId = entry.getKey();
            List<Integer> groupMemberIdList = entry.getValue();
            List<NepUser> userList = userMapper.queryUserByIdList(header.getAppId(), groupMemberIdList);
            NepFriendshipGroup friendshipGroup = groupList.get(groupId);
            NepFriendGroup friendGroup = transferFriendshipGroupToFriendGroup(friendshipGroup);
            List<NepFriend> friendGroupMemberList = userList.stream().map(this::transferUserToFriend).collect(Collectors.toList());
            groupAndGroupMember.put(friendGroup, friendGroupMemberList);
        }
        return response.setFriendGroupMemberMap(groupAndGroupMember)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    private NepFriendGroup transferFriendshipGroupToFriendGroup(NepFriendshipGroup friendshipGroup) {
        return new NepFriendGroup().setGroupId(friendshipGroup.getGroupId())
                       .setGroupOwnerId(friendshipGroup.getGroupOwnerId())
                       .setGroupName(friendshipGroup.getGroupName());
    }

    private NepFriend transferUserToFriend(NepUser user) {
        return new NepFriend()
                       .setUserId(user.getUserId())
                       .setUserAccount(user.getUsername())
                       .setUserNickName(user.getNickname())
                       .setUserAvatarAddress(user.getAvatarAddress())
                       .setUserSelfSignature(user.getSelfSignature());
    }
}
